package org.opensearch.index.common.parser;

import com.google.common.primitives.Chars;
import org.opensearch.index.common.character.JamoConstant;

/**
 * 한글 자모 Parser
 *
 */
public class KoreanJamoParser extends AbstractKoreanParser {

    private char[] originalJongsungs = {'ㄲ','ㄳ','ㄵ','ㄶ','ㄸ','ㄺ','ㄻ','ㄼ','ㄽ','ㄾ','ㄿ','ㅀ','ㅃ','ㅄ','ㅆ','ㅉ'};
    private String[] changeJongsungs={"ㄱㄱ","ㄱㅅ","ㄴㅈ","ㄴㅎ","ㄷㄷ","ㄹㄱ","ㄹㅁ","ㄹㅂ","ㄹㅅ","ㄹㅌ","ㄹㅍ","ㄹㅎ","ㅂㅂ","ㅂㅅ","ㅅㅅ","ㅈㅈ"};


    @Override
    protected void processForKoreanChar(StringBuilder sb, char chosung, char jungsung, char jongsung) {
        sb.append(chosung).append(jungsung);

        if(jongsung != JamoConstant.UNICODE_JONG_SUNG_EMPTY) {
            sb.append(separateDoubleConsonant(jongsung));
        }
    }

    @Override
    protected void processForOther(StringBuilder sb, char eachToken) {
        sb.append(eachToken);
    }

    /***
     * 복자음에 대한 종성을 분리한다.
     * @param jongsung 종성
     * @return 분리된 복자음
     */
    private String separateDoubleConsonant(char jongsung) {
        return isDoubleConsonant(jongsung) ? separateConsonant(jongsung) : String.valueOf(jongsung);
    }

    private boolean isDoubleConsonant(char jongsung) {
        return Chars.indexOf(originalJongsungs, jongsung) > -1;
    }

    private String separateConsonant(char jongsung) {
        int index = Chars.indexOf(originalJongsungs,jongsung);
        if(index == -1) return String.valueOf(jongsung);
        return changeJongsungs[index];
    }

}



