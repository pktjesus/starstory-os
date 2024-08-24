package org.opensearch.index.analysis.lowercase;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.charfilter.BaseCharFilter;

public class LowercaseCharFilter extends BaseCharFilter {

    private Reader transformedInput;

    public LowercaseCharFilter(Reader reader) {
        super(reader);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (transformedInput == null) {
            fill();
        }

        return transformedInput.read(cbuf, off, len);
    }

    public String fill() throws IOException {
        StringBuilder buffered  = readInputKeyword();
        String result  = buffered.toString().toLowerCase();
        transformedInput = new StringReader(result);
        return result;
    }

    private StringBuilder readInputKeyword() throws IOException {
        StringBuilder buffered = new StringBuilder();
        char[] temp = new char[1024];
        for (int cnt = input.read(temp); cnt > 0; cnt = input.read(temp)) {
            buffered.append(temp, 0, cnt);
        }

        return buffered;
    }
}
