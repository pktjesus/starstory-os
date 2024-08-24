package org.opensearch.index.analysis.soundex;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.opensearch.index.common.converter.SoundexConverter;

import java.io.IOException;

/**
 * 한영 변환 필터
 *
 */
public final class SoundexConvertFilter extends TokenFilter {

    private SoundexConverter converter;
    private CharTermAttribute termAtt;

    public SoundexConvertFilter(TokenStream stream) {
        super(stream);
        this.converter = new SoundexConverter();
        this.termAtt = addAttribute(CharTermAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {

        if (input.incrementToken()) {
            CharSequence parserdData = converter.convert(termAtt.toString());
            termAtt.setEmpty();
            termAtt.resizeBuffer(parserdData.length());
            termAtt.append(parserdData);
            termAtt.setLength(parserdData.length());

            return true;
        }

        return false;
    }

}

