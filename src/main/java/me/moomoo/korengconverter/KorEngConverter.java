package me.moomoo.korengconverter;

import me.moomoo.korengconverter.engtokor.EngToKorConverter;
import me.moomoo.korengconverter.kortoeng.KorToEngConverter;

/**
 * 해당 프로젝트는
 * KOR -> ENG
 * ENG -> KOR
 * 문자열 Converting을 수행합니다.
 *
 * ex)  korToEng : 자바프로그래밍 -> wkqkvmfhrmfoald
 *      engToKor : wkqkvmfhrmfoald -> 자바프로그래밍
 */
public class KorEngConverter {

    private static final KorToEngConverter korToEng = new KorToEngConverter();
    private static final EngToKorConverter engToKor = new EngToKorConverter();

    public static String korToEng(String kor) {
        return korToEng.parse(kor);
    }

    public static String engToKor(String eng) {
        return engToKor.parse(eng);
    }
}
