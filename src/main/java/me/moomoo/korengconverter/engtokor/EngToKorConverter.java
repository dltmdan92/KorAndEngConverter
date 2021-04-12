package me.moomoo.korengconverter.engtokor;

import me.moomoo.korengconverter.Converter;
import me.moomoo.korengconverter.engtokor.combiner.KoreanKeyCombiner;

import java.util.*;

import static java.util.Map.entry;

/**
 * 영문입력 타자를 한글 키보드 타자(분리된 상태)로 변환해주는 클래스
 * 해당 클래스에서 리턴한 분리된 한글 스트링을 KoreanKeyCombiner class에서 음절 조합 처리한다.
 *
 */
public class EngToKorConverter implements Converter {

    /* 모음 */
    private static final int VOWEL = 21;
    /* 표음문자 */
    private static final int PHONOGRAM = 28;

    /* 초성 키 맵 */
    //private final Map<String, Integer> initialConsonantList = new HashMap<>();
    /* 중성 키 맵 */
    //private final Map<String, Integer> syllableNucleusList = new HashMap<>();
    /* 종성 키 맵 */
    //private final Map<String, Integer> finalConsonantList = new HashMap<>();
    /* 단일 글자 키 맵 */
    private final Map<String, Integer> compatibilityList;

    public EngToKorConverter() {
        compatibilityList = Map.ofEntries(
                // 자음만
                //entry("", 0x3130),
                entry("r", 0x3131), entry("R", 0x3132), entry("rt", 0x3133), entry("s", 0x3134),
                entry("S", 0x3134), entry("sw", 0x3135), entry("sg", 0x3136), entry("e", 0x3137),
                entry("E", 0x3138), entry("f", 0x3139), entry("F", 0x3139), entry("fr", 0x313a),
                entry("fa", 0x313b), entry("fq", 0x313c), entry("ft", 0x313d), entry("fx", 0x313e),
                entry("fv", 0x313f), entry("fg", 0x3140), entry("a", 0x3141), entry("A", 0x3141),
                entry("q", 0x3142), entry("Q", 0x3143), entry("qt", 0x3144), entry("t", 0x3145),
                entry("T", 0x3146), entry("d", 0x3147), entry("D", 0x3147), entry("w", 0x3148),
                entry("W", 0x3149), entry("c", 0x314a),entry("C", 0x314a),entry("z", 0x314b),
                entry("Z", 0x314b),entry("x", 0x314c),entry("X", 0x314c),entry("v", 0x314d),
                entry("V", 0x314d),entry("g", 0x314e),entry("G", 0x314e),
                // 모음만
                entry("k", 0x314f),entry("K", 0x314f),entry("o", 0x3150),entry("i", 0x3151),
                entry("I", 0x3151),entry("O", 0x3152),entry("j", 0x3153),entry("J", 0x3153),
                entry("p", 0x3154),entry("u", 0x3155),entry("U", 0x3155),entry("P", 0x3156),
                entry("h", 0x3157),entry("H", 0x3157),entry("hk", 0x3158),entry("ho", 0x3159),
                entry("hl", 0x315a),entry("y", 0x315b),entry("Y", 0x315b),entry("n", 0x315c),
                entry("N", 0x315c),entry("nj", 0x315d),entry("np", 0x315e),entry("nl", 0x315f),
                entry("b", 0x3160),entry("B", 0x3160),entry("m", 0x3161),entry("M", 0x3161),
                entry("ml", 0x3162),entry("l", 0x3163),entry("L", 0x3163)
        );
    }

    /**
     * 영문을 받아서 한글 오타어로 변형 및 리턴해주는 메소드
     * @param eng 영문
     * @return 한글로 변형된 스트링
     */
    @Override
    public String parse(String eng) {
        // getDividedKorKey를 통해 생성된 한글 스트링을 KoreanKeyCombiner로 음절 결합 처리 후 스트링 리턴
        return KoreanKeyCombiner.getEngKeyToKorKey(getDividedKorKey(eng));
    }

    // 한글 글자 추출
    public Character getType(String sHan) {
        if(compatibilityList.containsKey(sHan)) {
            return (char) (compatibilityList.get(sHan)).intValue();
        } else {
            return 0;
        }
    }

    public String getDividedKorKey(String sHan) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < sHan.length(); i++) {
            if(getType(sHan.substring(i, i + 1)) == 0) {
                sb.append(sHan.charAt(i));
                continue;
            }
            sb.append(getType(sHan.substring(i, i + 1)));
        }

        return sb.toString();
    }
}
