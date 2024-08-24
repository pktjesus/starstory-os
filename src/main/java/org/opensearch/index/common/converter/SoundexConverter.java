package org.opensearch.index.common.converter;

import com.google.common.primitives.Chars;
import org.apache.commons.lang3.StringUtils;
import org.opensearch.index.common.character.JamoConstant;
import org.opensearch.index.common.character.KeyboardUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SoundexConverter {
    public String convert(String token) {

        if (StringUtils.isBlank(token)) {
            return "";
        }

        return  soundex(token.trim());
    }

    /***
     * 한글 자모 분석된 단어를 동의어처럼 처리하여 soundex 단어를 생성한다.
     * @param word
     * @return
     */
    private String soundex(String word) {
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < word.length(); index++) {
            char originalChar = word.charAt(index);

            String singleWord = word.substring(index, index + 1);
            String regex = "[가-힣]";
            // 처리 불가능한 글자는 그냥 넘긴다.
            if (KeyboardUtil.IGNORE_CHAR.contains(singleWord) || !singleWord.matches(regex)) {
                result.append(singleWord);
                continue;
            }

            // 처리 할 char의 유니코드 인덱스를 구한다.
            char unicodeIndex = (char)(originalChar - JamoConstant.START_KOREA_UNICODE);

            // 초성 유니코드
            int idxChoSung = unicodeIndex / (28 * 21);
            char chosung = JamoConstant.UNICODE_CHO_SUNG[idxChoSung];
            result.append(chosung);

            // 중성 유니코드
            int idxJungSung = unicodeIndex % (28 * 21) / 28;
            char jungsung = JamoConstant.UNICODE_JUNG_SUNG[idxJungSung];
            result.append(checkJungsung(jungsung));

            // 종성 유니코드
            int idxJongSung = unicodeIndex % (28 * 21) % 28;
            char jongsung = JamoConstant.UNICODE_JONG_SUNG[idxJongSung];
            if(jongsung != JamoConstant.UNICODE_JONG_SUNG_EMPTY) {
                result.append(checkJongsung(jongsung));
            }
        }


        return result.toString();
    }

    private boolean isReplacementJungsung = false;
    private boolean isReplacementJongsung = false;
    private char checkJungsung(char jungsung) {
        isReplacementJungsung = false;

        char[] eData = {'ㅐ','ㅒ','ㅔ','ㅖ'};
        char[] aData = {'ㅏ','ㅑ'};
        char[] eoData = {'ㅓ','ㅕ'};
        char[] oData = {'ㅗ','ㅛ'};
        char[] uData = {'ㅜ','ㅠ'};
        char[] euData = {'ㅢ','ㅣ','ㅟ'};
        char[] waeData = {'ㅚ','ㅙ','ㅞ'};
        String type = "jungsung";

        char replaceChar = replaceArr(jungsung, eData,'ㅔ',type);
        if(!isReplacementJungsung) replaceChar = replaceArr(jungsung,aData,'ㅏ', type);
        if(!isReplacementJungsung) replaceChar = replaceArr(jungsung,eoData,'ㅓ', type);
        if(!isReplacementJungsung) replaceChar = replaceArr(jungsung,oData,'ㅗ', type);
        if(!isReplacementJungsung) replaceChar = replaceArr(jungsung,uData,'ㅜ', type);
        if(!isReplacementJungsung) replaceChar = replaceArr(jungsung,euData,'ㅣ', type);
        if(!isReplacementJungsung) replaceChar = replaceArr(jungsung,waeData,'ㅚ', type);
        if(!isReplacementJungsung) replaceChar = jungsung;

        return replaceChar;

    }

    private char checkJongsung(char jongsung) {
        isReplacementJongsung = false;

        char[] giyeongData = {'ㄱ','ㄲ','ㅋ'};
        char[] digeudData = {'ㄷ','ㄸ','ㅌ','ㅅ','ㅆ','ㅎ','ㅈ','ㅉ','ㅊ'};
        char[] bieubData = {'ㅂ','ㅃ','ㅍ'};
        String type = "jongsung";

        char replaceChar = replaceArr(jongsung, giyeongData,'ㄱ', type);
        if(!isReplacementJongsung) replaceChar = replaceArr(jongsung,digeudData,'ㄷ',type);
        if(!isReplacementJongsung) replaceChar = replaceArr(jongsung,bieubData,'ㅂ',type);
        if(!isReplacementJongsung) replaceChar = jongsung;
        return replaceChar;
    }

    /***
     *
     * @param word 오리지널 단어
     * @param candidateWords 변경될 단어의 후보군
     * @param replaceWord 변경될 단어
     * @param type 자소타입
     * @return
     */
    private char replaceArr(char word, char[] candidateWords, char replaceWord, String type ) {

        boolean isReplacement = Chars.indexOf(candidateWords,word)>-1;
        if("jungsung".equals(type)) isReplacementJungsung = isReplacement;
        else if("jongsung".equals(type)) isReplacementJongsung = isReplacement;

        return isReplacement ? replaceWord : word;
    }

    /***
     * 정규표현식으로 단어가 한글인지 영문인지를 판단한다.
     * @param regex 정규표현식
     * @param word 단어
     * @return
     */
    private Matcher matchRegex(String regex, String word) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(word);
    }
}

