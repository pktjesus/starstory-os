package org.opensearch.index.analysis.jamo;

import org.apache.lucene.analysis.charfilter.BaseCharFilter;
import org.opensearch.index.common.parser.KoreanJamoParser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * 한글 자모 분석 필터
 *
 */
public final class JamoCharFilter extends BaseCharFilter {
    private KoreanJamoParser parser;
    private Reader transformedInput;

    public JamoCharFilter(Reader in) {
        super(in);
        this.parser = new KoreanJamoParser();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (transformedInput == null) {
            fill();
        }

        return transformedInput.read(cbuf, off, len);
    }

    private void fill() throws IOException {
        StringBuilder buffered = new StringBuilder();
        char [] temp = new char [1024];
        for (int cnt = input.read(temp); cnt > 0; cnt = input.read(temp)) {
            buffered.append(temp, 0, cnt);
        }
        transformedInput = new StringReader(parse(buffered).toString());
    }

    private CharSequence parse(CharSequence input) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] charArray = input.toString().toCharArray();

        for(char ch : charArray) {
            stringBuilder.append(ch);
        }

        return parser.parse(stringBuilder.toString());
    }
}

