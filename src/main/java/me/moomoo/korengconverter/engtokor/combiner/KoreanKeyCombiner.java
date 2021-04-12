package me.moomoo.korengconverter.engtokor.combiner;

import me.moomoo.korengconverter.engtokor.parser.HangulParser;
import me.moomoo.korengconverter.engtokor.parser.HangulParserException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 한글의 경우, 모음과 모음, 자음과 모음 등 음절 단위의 조합이 필요함
 * 해당 클래스는 조합을 담당
 */
public class KoreanKeyCombiner {

    // 초성 세트
    private static final Set<String> CHOSUNG_SET = new HashSet<>(List.of("ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ",
            "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"));
    // 중성 세트
    private static final Set<String> JUNGSUNG_SET = new HashSet<>(List.of("ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ",
            "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"));
    // 종성 세트
    private static final Set<String> JONGSUNG_SET = new HashSet<>(List.of("ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ",
            "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ",
            "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"));
    // 복합 종성의 첫글자 세트
    // ㄱ으로 시작하는 복합 종성 세트
    private static final Set<String> COMP_JONGSUNG_SET_1 = new HashSet<>(List.of("ㅅ"));
    // ㄴ으로 시작하는 복합 종성 세트
    private static final Set<String> COMP_JONGSUNG_SET_2 = new HashSet<>(List.of("ㅈ", "ㅎ"));
    // ㄹ으로 시작하는 복합 종성 세트
    private static final Set<String> COMP_JONGSUNG_SET_3 = new HashSet<>(List.of("ㄱ", "ㅁ", "ㅂ", "ㅅ", "ㅌ", "ㅍ", "ㅎ"));
    // ㅂ으로 시작하는 복합 종성 세트
    private static final Set<String> COMP_JONGSUNG_SET_4 = new HashSet<>(List.of("ㅅ"));
    // 복합 중성의 첫글자 세트
    private static final Set<String> COMP_JUNGSUNG_SET = new HashSet<>(List.of("ㅗ", "ㅜ", "ㅡ"));
    // ㅗ으로 시작하는 복합 중성 세트
    private static final Set<String> COMP_JUNGSUNG_SET_1 = new HashSet<>(List.of("ㅏ", "ㅐ", "ㅣ"));
    // ㅜ으로 시작하는 복합 중성 세트
    private static final Set<String> COMP_JUNGSUNG_SET_2 = new HashSet<>(List.of("ㅓ", "ㅔ", "ㅣ"));
    // ㅡ으로 시작하는 복합 중성 세트
    private static final Set<String> COMP_JUNGSUNG_SET_3 = new HashSet<>(List.of("ㅣ"));

    public static String getEngKeyToKorKey(String keyword) {

        // eng to han 에서 리턴 받은 국문글자들에 대해서 음절 조합 수행
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < keyword.length(); i++) {
            String firstkey = keyword.substring(i, i + 1);
            if (i < keyword.length() - 1) {
                String secondKey = keyword.substring(i + 1, i + 2);

                if (CHOSUNG_SET.contains(firstkey) && JUNGSUNG_SET.contains(secondKey)) {
                    List<String> jasoList = new ArrayList<>();
                    jasoList.add(firstkey); // 음절 결합 리스트에 초성(자음) 추가
                    jasoList.add(secondKey); // 음절 결합 리스트에 중성(모음) 추가
                    // 종성 or 복합중성(복합모음) 체크 로직 시작
                    if (i < keyword.length() - 2) {
                        // 세번째 키워드가 종성(자음)에 해당되는 케이스
                        if (JONGSUNG_SET.contains(keyword.substring(i + 2, i + 3))) {

                            String jongsung = keyword.substring(i + 2, i + 3);
                            String secondJongsung;

                            if (i < keyword.length() - 3) {

                                // 종성 뒤에 모음(중성)이 올 경우 앞의 중성까지만 음절에 결합하고 continue처리
                                if (JUNGSUNG_SET.contains(keyword.substring(i + 3, i + 4))) {
                                    try {
                                        // 초성(자음), 중성(모음)만 결합 처리
                                        sb.append(HangulParser.assemble(jasoList, 0, jasoList.size()));
                                        i++;
                                    } catch (HangulParserException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    continue;
                                }
                                // 복합 종성 처리 부분
                                switch (keyword.substring(i + 2, i + 3)) {
                                    case "ㄱ":
                                        if (COMP_JONGSUNG_SET_1.contains(keyword.substring(i + 3, i + 4))) {
                                            secondJongsung = keyword.substring(i + 3, i + 4);
                                            switch (secondJongsung) {
                                                case "ㅅ":
                                                    jongsung = "ㄳ";
                                                    break;
                                            }
                                            i++;
                                        }
                                        break;
                                    case "ㄴ":
                                        if (COMP_JONGSUNG_SET_2.contains(keyword.substring(i + 3, i + 4))) {
                                            secondJongsung = keyword.substring(i + 3, i + 4);
                                            switch (secondJongsung) {
                                                case "ㅈ":
                                                    jongsung = "ㄵ";
                                                    break;
                                                case "ㅎ":
                                                    jongsung = "ㄶ";
                                                    break;
                                            }
                                            i++;
                                        }
                                        break;
                                    case "ㄹ":
                                        if (COMP_JONGSUNG_SET_3.contains(keyword.substring(i + 3, i + 4))) {
                                            secondJongsung = keyword.substring(i + 3, i + 4);
                                            switch (secondJongsung) {
                                                case "ㄱ":
                                                    jongsung = "ㄺ";
                                                    break;
                                                case "ㅁ":
                                                    jongsung = "ㄻ";
                                                    break;
                                                case "ㅂ":
                                                    jongsung = "ㄼ";
                                                    break;
                                                case "ㅅ":
                                                    jongsung = "ㄽ";
                                                    break;
                                                case "ㅌ":
                                                    jongsung = "ㄾ";
                                                    break;
                                                case "ㅍ":
                                                    jongsung = "ㄿ";
                                                    break;
                                                case "ㅎ":
                                                    jongsung = "ㅀ";
                                                    break;
                                            }
                                            i++;
                                        }
                                        break;
                                    case "ㅂ":
                                        if (COMP_JONGSUNG_SET_4.contains(keyword.substring(i + 3, i + 4))) {
                                            secondJongsung = keyword.substring(i + 3, i + 4);
                                            if ("ㅅ".equals(secondJongsung)) {
                                                jongsung = "ㅄ";
                                            }
                                            i++;
                                        }
                                        break;
                                }
                            }
                            jasoList.add(jongsung);
                            i++;
                        } else if (COMP_JUNGSUNG_SET.contains(secondKey)) {
                            // 두번째 글자(중성)가 복합 중성의 시작부분에 속할 경우

                            String secondJungsung = keyword.substring(i + 2, i + 3);
                            System.out.println(secondJungsung);

                            switch (secondKey) {
                                case "ㅗ":
                                    if (COMP_JUNGSUNG_SET_1.contains(secondJungsung)) {
                                        switch (secondJungsung) {
                                            case "ㅏ":
                                                secondKey = "ㅘ";
                                                break;
                                            case "ㅐ":
                                                secondKey = "ㅙ";
                                                break;
                                            case "ㅣ":
                                                secondKey = "ㅚ";
                                                break;
                                        }
                                    }
                                    break;
                                case "ㅜ":
                                    if (COMP_JUNGSUNG_SET_2.contains(secondJungsung)) {
                                        switch (secondJungsung) {
                                            case "ㅓ":
                                                secondKey = "ㅝ";
                                                break;
                                            case "ㅔ":
                                                secondKey = "ㅞ";
                                                break;
                                            case "ㅣ":
                                                secondKey = "ㅟ";
                                                break;
                                        }
                                    }
                                    break;
                                case "ㅡ":
                                    if (COMP_JUNGSUNG_SET_3.contains(secondJungsung)) {
                                        if ("ㅣ".equals(secondJungsung)) {
                                            secondKey = "ㅢ";
                                        }
                                    }
                                    break;
                            }
                            jasoList.remove(jasoList.size() - 1);
                            jasoList.add(secondKey); // 음절 결합 리스트에 중성(모음) 추가

                            // 종성 or 복합중성(복합모음) 체크 로직 시작
                            if (i < keyword.length() - 3) {
                                // 세번째 키워드가 종성(자음)에 해당되는 케이스
                                if (JONGSUNG_SET.contains(keyword.substring(i + 3, i + 4))) {
                                    String jongsung = keyword.substring(i + 3, i + 4);
                                    String secondJongsung = "";

                                    if (i < keyword.length() - 4) {

                                        // 종성 뒤에 모음(중성)이 올 경우 앞의 중성까지만 음절에 결합하고 continue처리
                                        if (JUNGSUNG_SET.contains(keyword.substring(i + 4, i + 5))) {
                                            try {
                                                // 초성(자음), 중성(모음)만 결합 처리
                                                sb.append(HangulParser.assemble(jasoList, 0, jasoList.size()));
                                                i++;
                                            } catch (HangulParserException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            continue;
                                        }
                                        // 복합 종성 처리 부분
                                        switch (keyword.substring(i + 3, i + 4)) {
                                            case "ㄱ":
                                                if (COMP_JONGSUNG_SET_1.contains(keyword.substring(i + 4, i + 5))) {
                                                    secondJongsung = keyword.substring(i + 4, i + 5);
                                                    if ("ㅅ".equals(secondJongsung)) {
                                                        jongsung = "ㄳ";
                                                    }
                                                    i++;
                                                }
                                                break;
                                            case "ㄴ":
                                                if (COMP_JONGSUNG_SET_2.contains(keyword.substring(i + 4, i + 5))) {
                                                    secondJongsung = keyword.substring(i + 4, i + 5);
                                                    switch (secondJongsung) {
                                                        case "ㅈ":
                                                            jongsung = "ㄵ";
                                                            break;
                                                        case "ㅎ":
                                                            jongsung = "ㄶ";
                                                            break;
                                                    }
                                                    i++;
                                                }
                                                break;
                                            case "ㄹ":
                                                if (COMP_JONGSUNG_SET_3.contains(keyword.substring(i + 4, i + 5))) {
                                                    secondJongsung = keyword.substring(i + 4, i + 5);
                                                    switch (secondJongsung) {
                                                        case "ㄱ":
                                                            jongsung = "ㄺ";
                                                            break;
                                                        case "ㅁ":
                                                            jongsung = "ㄻ";
                                                            break;
                                                        case "ㅂ":
                                                            jongsung = "ㄼ";
                                                            break;
                                                        case "ㅅ":
                                                            jongsung = "ㄽ";
                                                            break;
                                                        case "ㅌ":
                                                            jongsung = "ㄾ";
                                                            break;
                                                        case "ㅍ":
                                                            jongsung = "ㄿ";
                                                            break;
                                                        case "ㅎ":
                                                            jongsung = "ㅀ";
                                                            break;
                                                    }
                                                    i++;
                                                }
                                                break;
                                            case "ㅂ":
                                                if (COMP_JONGSUNG_SET_4.contains(keyword.substring(i + 4, i + 5))) {
                                                    secondJongsung = keyword.substring(i + 4, i + 5);
                                                    if ("ㅅ".equals(secondJongsung)) {
                                                        jongsung = "ㅄ";
                                                    }
                                                    i++;
                                                }
                                                break;
                                        }
                                    }
                                    System.out.println(jongsung);
                                    jasoList.add(jongsung);
                                    i++;
                                }
                            }
                        }
                    }

                    try {
                        sb.append(HangulParser.assemble(jasoList, 0, jasoList.size()));
                        i++;
                        continue;
                    } catch (HangulParserException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (COMP_JUNGSUNG_SET.contains(firstkey)) {
                    List<String> jasoList = new ArrayList<>();
                    boolean combineYn = false;
                    switch (firstkey) {
                        case "ㅗ":
                            if (COMP_JUNGSUNG_SET_1.contains(secondKey)) {
                                combineYn = true;
                                switch (secondKey) {
                                    case "ㅏ":
                                        secondKey = "ㅘ";
                                        break;
                                    case "ㅐ":
                                        secondKey = "ㅙ";
                                        break;
                                    case "ㅣ":
                                        secondKey = "ㅚ";
                                        break;
                                }
                            }
                            break;
                        case "ㅜ":
                            if (COMP_JUNGSUNG_SET_2.contains(secondKey)) {
                                combineYn = true;
                                switch (secondKey) {
                                    case "ㅓ":
                                        secondKey = "ㅝ";
                                        break;
                                    case "ㅔ":
                                        secondKey = "ㅞ";
                                        break;
                                    case "ㅣ":
                                        secondKey = "ㅟ";
                                        break;
                                }
                            }
                            break;
                        case "ㅡ":
                            if (COMP_JUNGSUNG_SET_3.contains(secondKey)) {
                                combineYn = true;
                                if ("ㅣ".equals(secondKey)) {
                                    secondKey = "ㅢ";
                                }
                            }
                            break;
                    }
                    if (combineYn) {
                        sb.append(secondKey);
                        i++;
                        continue;
                    }


                }
            }
            sb.append(keyword.charAt(i));
        }

        return sb.toString();
    }
}
