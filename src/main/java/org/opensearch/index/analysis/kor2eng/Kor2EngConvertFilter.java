package org.opensearch.index.analysis.kor2eng;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.opensearch.index.common.converter.KorToEngConverter;

/**
 * 한영 변환 필터
 *
 */
public final class Kor2EngConvertFilter extends TokenFilter {

    private KorToEngConverter converter;
    private CharTermAttribute termAtt;

    public Kor2EngConvertFilter(TokenStream stream) {
        super(stream);
        this.converter = new KorToEngConverter();
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

