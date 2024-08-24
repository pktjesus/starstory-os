package org.opensearch.index.common.converter;

import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.common.Strings;
import org.opensearch.core.common.Strings;
import org.opensearch.index.common.character.JamoConstant;
import org.opensearch.index.common.type.MorphemeEnum;
import org.opensearch.index.common.util.KeyboardUtil;

/**
 * 한영 오타 변환기 (Kor -> Eng)
 *
 */
public class KorToEngConverter {


    /**
     * 토큰을 한글 키보드 기준으로 변환한다.
     *
     * @param token
     * @return
     */
    public String convert(String token) {
        StringBuilder sb = new StringBuilder();

        // 문자열을 한글자씩 잘라서 처리한다.
        String word = token.trim();
        word = changeDoubleMoum(word);


        for (int index = 0; index < word.length(); index++) {
            String singleWord = word.substring(index, index + 1);
            String regex = "[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]";

            // 처리 불가능한 글자는 그냥 넘긴다.
            if (!singleWord.matches(regex) || org.opensearch.index.common.character.KeyboardUtil.IGNORE_CHAR.contains(singleWord)) {
                sb.append(singleWord);
                continue;
            }
            if (index >= word.length()) {
                break;
            }

            try {
                int init = word.charAt(index);
                int initUnicode = init - JamoConstant.START_KOREA_UNICODE;

                if (initUnicode > 0) {
                    /**
                     * 1글자로 조합형 한글이 들어올 경우 처리
                     */
                    int cho  = initUnicode / 21 / 28;   // 0 ~ 18
                    String strCho = getSameEngChar(MorphemeEnum.CHOSUNG, cho);
                    if (StringUtils.isNotEmpty(strCho)) {
                        sb.append(strCho);
                    }


                    int jung = initUnicode / 28 % 21;   // 0 ~ 20
                    String strJung = getSameEngChar(MorphemeEnum.JUNGSUNG, jung);
                    if (StringUtils.isNotEmpty(strJung)) {
                        sb.append(strJung);
                    }

                    int jong = initUnicode % 28;        // 0 ~ 27
                    String strJong = getSameEngChar(MorphemeEnum.JONGSUNG, jong);
                    if (StringUtils.isNotEmpty(strJong)) {
                        sb.append(strJong);
                    }

                } else {
                    /**
                     * 1글자로 자모가 들어올 경우 처리
                     */
                    String subStr = String.valueOf((char) init);
                    sb.append(getSameEngCharForJamo(subStr));
                }
            } catch(Exception e) {
                e.fillInStackTrace();
            }
        }
        return sb.toString();
    }

    private String changeDoubleMoum(String word) {
        if(Strings.isEmpty(word))  return "";

        word = replaceAttr(word,"ㅙ","ㅗㅐ");
        word = replaceAttr(word,"ㅚ","ㅗㅣ");
        word = replaceAttr(word,"ㅘ","ㅗㅏ");
        word = replaceAttr(word,"ㅟ","ㅜㅣ");
        word = replaceAttr(word,"ㅞ","ㅜㅔ");
        word = replaceAttr(word,"ㅝ","ㅜㅓ");
        word = replaceAttr(word,"ㅢ","ㅡㅣ");
        return word;
    }

    private String replaceAttr(String originalWord, String checkWord, String replaceWord) {
        return originalWord.replace(checkWord, replaceWord);
    }


    private String getSameEngChar(MorphemeEnum type, int pos) {
        switch (type) {
            case CHOSUNG:
                return KeyboardUtil.KEYBOARD_CHO_SUNG[pos];

            case JUNGSUNG:
                return KeyboardUtil.KEYBOARD_JUNG_SUNG[pos];

            case JONGSUNG:
                if ((pos - 1) > -1) {
                    return KeyboardUtil.KEYBOARD_JONG_SUNG[pos - 1];
                }
                return "";
        }

        return "";
    }


    private String getSameEngCharForJamo(String key) {
        for (int i=0; i<KeyboardUtil.KEYBOARD_KEY_KOR.length; i++) {
            if (KeyboardUtil.KEYBOARD_KEY_KOR[i].equals(key)) {
                return KeyboardUtil.KEYBOARD_KEY_ENG[i];
            }
        }

        return "";
    }

}
