package me.moomoo.korengconverter.engtokor.parser;

import java.util.List;

public class HangulParser {
    private static final String TAG = HangulParser.class.getSimpleName();

    // First '가' : 0xAC00(44032), 끝 '힟' : 0xD79F(55199)
    private static final int FIRST_HANGUL = 44032;

    // 19 initial consonants
    private static final char[] CHOSUNG_LIST = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    private static int JUNGSUNG_COUNT = 21;

    // 21 vowels
    private static final char[] JUNGSUNG_LIST = {
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
            'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ',
            'ㅣ'
    };

    private static int JONGSUNG_COUNT = 28;

    // 28 consonants placed under a vowel(plus one empty character)
    private static final char[] JONGSUNG_LIST = {
            ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ',
            'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ',
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    public static String assemble(List<String> jasoList, final int startIdx, final int assembleSize) throws HangulParserException {
        int unicode = FIRST_HANGUL;

        final int chosungIndex = new String(CHOSUNG_LIST).indexOf(jasoList.get(startIdx));

        if (chosungIndex >= 0) {
            unicode += JONGSUNG_COUNT * JUNGSUNG_COUNT * chosungIndex;
        } else {
            throw new HangulParserException((startIdx + 1) + "번째 자소가 한글 초성이 아닙니다");
        }

        final int jungsungIndex = new String(JUNGSUNG_LIST).indexOf(jasoList.get(startIdx + 1));

        if(jungsungIndex >= 0) {
            unicode += JONGSUNG_COUNT * jungsungIndex;
        } else {
            throw new HangulParserException((startIdx + 2) + "번째 자소가 한글 중성이 아닙니다");
        }

        if (assembleSize > 2) {
            final int jongsungIndex = new String(JONGSUNG_LIST).indexOf(jasoList.get(startIdx + 2));

            if (jongsungIndex >= 0) {
                unicode += jongsungIndex;
            } else {
                throw new HangulParserException((startIdx + 3) + "번째 자소가 한글 종성이 아닙니다");
            }
        }

        return Character.toString((char) unicode);
    }

    private static int getNextAssembleSize(List<String> jasoList, final int startIdx) throws HangulParserException {
        final int remainJasoLength = jasoList.size() - startIdx;
        final int assembleSize;

        if (remainJasoLength > 3) {
            if (new String(JUNGSUNG_LIST).contains(jasoList.get(startIdx + 3))) {
                assembleSize = 2;
            } else {
                assembleSize = 3;
            }
        } else if(remainJasoLength == 3 || remainJasoLength == 2) {
            assembleSize = remainJasoLength;
        } else {
            throw new HangulParserException("한글을 구성할 자소가 부족하거나 한글이 아닌 문자가 있습니다");
        }

        return assembleSize;
    }
}
