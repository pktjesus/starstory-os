package org.opensearch.index.common.converter;

import java.util.Map;

import org.opensearch.index.common.character.JamoConstant;
import org.opensearch.index.common.character.KeyboardUtil;
import org.opensearch.index.common.util.JamoUtil;

/**
 * 영한 오타 변환기 (Eng -> Kor)
 *
 */
public class EngToKorConverter {


    /**
     * 토큰을 영문 키보드 기준으로 변환한다.
     *
     * @param token
     * @return
     */
    public String convert(String token) {
        StringBuilder sb = new StringBuilder();

        // 문자열을 한글자씩 잘라서 처리한다.
        String word = token.trim();
        for (int index = 0; index < word.length(); index++) {
            String singleWord = word.substring(index, index + 1);
            String regex = "[A-Z|a-z\\s]*$";
            // 처리 불가능한 글자는 그냥 넘긴다.
            if (org.opensearch.index.common.character.KeyboardUtil.IGNORE_CHAR.contains(singleWord) || !singleWord.matches(regex)) {
                sb.append(singleWord);
                continue;
            }

            if (index >= word.length()) {
                break;
            }

            try {
                // 초성 정보를 구한다.
                Map<String, Integer> mChoSung = org.opensearch.index.common.character.KeyboardUtil.getInfoForChoSung(index, word);
                int cho = mChoSung.get("code");
                index = mChoSung.get("idx");

                // 중성 정보를 구한다.
                Map<String, Integer> mJungSung = org.opensearch.index.common.character.KeyboardUtil.getInfoForJungSung(index, word);
                int jung = mJungSung.get("code");
                index = mJungSung.get("idx");

                // 종성 정보를 구한다.
                Map<String, Integer> mJongSung = KeyboardUtil.getInfoForJongSung(index, word);
                int jong = mJongSung.get("code");
                index = mJongSung.get("idx");



                // 한글 유니코드를 생성한다.
                sb.append((char) (JamoConstant.START_KOREA_UNICODE + cho + jung + jong));
            } catch(Exception e) {
                e.fillInStackTrace();
            }
        }

        return sb.toString();
    }

}

