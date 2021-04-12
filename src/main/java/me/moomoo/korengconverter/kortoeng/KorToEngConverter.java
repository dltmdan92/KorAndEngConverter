package me.moomoo.korengconverter.kortoeng;

import me.moomoo.korengconverter.Converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.Map.*;

public class KorToEngConverter implements Converter {
    /* 모음 */
    private final int VOWEL = 21;
    /* 표음문자 */
    private final int PHONOGRAM = 28;
    /* 초성 키 맵 */
    private final Map<Integer, String> initialConsonantList;
    /* 중성 키 맵 */
    private final Map<Integer, String> syllableNucleusList;
    /* 종성 키 맵 */
    private final Map<Integer, String> finalConsonantList;
    /* 단일 글자 키 맵 */
    private final Map<Integer, String> compatibilityList;

    /**
     * 한글을 입력 받아 해당 영문 키 값으로 변환한다.
     * @param kor 변환할 내용
     * @return 변환된 키 내용.
     */
    @Override
    public String parse(String kor) {
        final List<_CharZone> lst = this.divideKoreanTypo(kor);

        final StringBuilder sb = new StringBuilder();

        for( _CharZone item : lst ){
            sb.append( item );
        }

        return sb.toString();
    }

    public KorToEngConverter() {
        // 초성 Key : 한글 자음 유니코드, Value : 대응되는 알파벳
        initialConsonantList = Map.ofEntries(
                entry(0x1100, "r"),entry(0x1101, "R"),entry(0x1102, "s"),entry(0x1103, "e"),
                entry(0x1104, "E"),entry(0x1105, "f"),entry(0x1106, "a"),entry(0x1107, "q"),
                entry(0x1108, "Q"),entry(0x1109, "t"),entry(0x110a, "T"),entry(0x110b, "d"),
                entry(0x110c, "w"),entry(0x110d, "W"),entry(0x110e, "c"),entry(0x110f, "z"),
                entry(0x1110, "x"),entry(0x1111, "v"),entry(0x1112, "g"),entry(0x1113, "sr"),
                entry(0x1114, "ss"),entry(0x1115, "se"),entry(0x1116, "sq"),entry(0x1117, "er"),
                entry(0x1118, "fs"),entry(0x1119, "ff"),entry(0x111a, "fg"),//entry(0x111b, ""),
                entry(0x111c, "aq"),//entry(0x111d, ""),entry(0x111e, "qr"),entry(0x111f, "qs"),
                entry(0x1120, "qe"),entry(0x1121, "qt"),entry(0x1122, "qtr"),entry(0x1123, "qte"),
                entry(0x1124, "qtq"),entry(0x1125, "qtt"),entry(0x1126, "qtw"),entry(0x1127, "qw"),
                entry(0x1128, "qc"),entry(0x1129, "qx"),entry(0x112a, "qv"),//entry(0x112b, ""),
                //entry(0x112c, ""),entry(0x112d, "tr"),entry(0x112e, "ts"),entry(0x112f, "te"),
                entry(0x1130, "tf"),entry(0x1131, "ta"),entry(0x1132, "tq"),entry(0x1133, "tqr"),
                entry(0x1134, "ttt"),entry(0x1135, "td"),entry(0x1136, "tw"),entry(0x1137, "tc"),
                entry(0x1138, "tz"),entry(0x1139, "tx"),entry(0x113a, "tv"),entry(0x113b, "tg"),
                entry(0x113c, "t"),entry(0x113d, "T"),entry(0x113e, "t"),entry(0x113f, "T"),
                //entry(0x1140, ""),entry(0x1141, "dr"),entry(0x1142, "de"),entry(0x1143, "da"),
                entry(0x1144, "dq"),entry(0x1145, "dt"),//entry(0x1146, ""),entry(0x1147, "dd"),
                entry(0x1148, "dw"),entry(0x1149, "dc"),entry(0x114a, "dx"),entry(0x114b, "dv"),
                entry(0x114c, "d"),entry(0x114d, "wd"),entry(0x114e, "w"),entry(0x114f, "W"),
                entry(0x1150, "w"),entry(0x1151, "W"),entry(0x1152, "cz"),entry(0x1153, "cg"),
                entry(0x1154, "c"),entry(0x1155, "vq"),entry(0x1156, "vg"),//entry(0x1157, ""),
                entry(0x1158, "gg"),//entry(0x1159, ""),entry(0x115a, "re"),entry(0x115b, "st"),
                entry(0x115c, "sw"),entry(0x115d, "sg"),entry(0x115e, "ef")//entry(0x115f, "")
        );

        // 중성
        syllableNucleusList = Map.ofEntries(
                //		entry(0x1160, ""),
                entry(0x1161, "k"),entry(0x1162, "o"),entry(0x1163, "i"),entry(0x1164, "O"),
                entry(0x1165, "j"),entry(0x1166, "p"),entry(0x1167, "u"),entry(0x1168, "P"),
                entry(0x1169, "h"),entry(0x116a, "hk"),entry(0x116b, "ho"),entry(0x116c, "hl"),
                entry(0x116d, "y"),entry(0x116e, "n"),entry(0x116f, "nj"),entry(0x1170, "np"),
                entry(0x1171, "nl"),entry(0x1172, "b"),entry(0x1173, "m"),entry(0x1174, "ml"),
                entry(0x1175, "l"),entry(0x1176, "hk"),entry(0x1177, "nk"),entry(0x1178, "hi"),
                entry(0x1179, "yi"),entry(0x117a, "hj"),entry(0x117b, "nj"),entry(0x117c, "mj"),
                entry(0x117d, "hu"),entry(0x117e, "nu"),entry(0x117f, "hj"),entry(0x1180, "nP"),
                entry(0x1181, "hP"),entry(0x1182, "hh"),entry(0x1183, "hn"),entry(0x1184, "yi"),
                entry(0x1185, "yO"),entry(0x1186, "yu"),entry(0x1187, "yh"),entry(0x1188, "yl"),
                entry(0x1189, "nk"),entry(0x118a, "no"),entry(0x118b, "njm"),entry(0x118c, "nP"),
                entry(0x118d, "nn"),entry(0x118e, "bk"),entry(0x118f, "bj"),entry(0x1190, "bp"),
                entry(0x1191, "bu"),entry(0x1192, "bP"),entry(0x1193, "bn"),entry(0x1194, "bl"),
                entry(0x1195, "mn"),entry(0x1196, "mm"),entry(0x1197, "mln"),entry(0x1198, "lk"),
                entry(0x1199, "li"),entry(0x119a, "hl"),entry(0x119b, "nl"),entry(0x119c, "ml"),
                //		entry(0x119d, ""),//		entry(0x119e, ""),//		entry(0x119f, ""),//		entry(0x11a0, ""),
                //		entry(0x11a1, ""),//		entry(0x11a2, ""),
                entry(0x11a3, "mk"),entry(0x11a4, "ni"),entry(0x11a5, "ui"),entry(0x11a6, "hi"),
                entry(0x11a7, "hO")
        );


        // 종성
        finalConsonantList = Map.ofEntries(
                entry(0x11a8, "r"),entry(0x11a9, "R"),entry(0x11aa, "rt"),entry(0x11ab, "s"),
                entry(0x11ac, "sw"),entry(0x11ad, "sg"),entry(0x11ae, "e"),entry(0x11af, "f"),
                entry(0x11b0, "fr"),entry(0x11b1, "fa"),entry(0x11b2, "fq"),entry(0x11b3, "ft"),
                entry(0x11b4, "fx"),entry(0x11b5, "fv"),entry(0x11b6, "fg"),entry(0x11b7, "a"),
                entry(0x11b8, "q"),entry(0x11b9, "qt"),entry(0x11ba, "t"),entry(0x11bb, "T"),
                entry(0x11bc, "d"),entry(0x11bd, "w"),entry(0x11be, "c"),entry(0x11bf, "z"),
                entry(0x11c0, "x"),entry(0x11c1, "v"),entry(0x11c2, "g"),entry(0x11c3, "rf"),
                entry(0x11c4, "rtr"),entry(0x11c5, "sr"),entry(0x11c6, "se"),entry(0x11c7, "st"),
                //entry()(0x11c8, ""),
                entry(0x11c9, "sx"),entry(0x11ca, "er"),entry(0x11cb, "ef"),entry(0x11cc, "frt"),
                entry(0x11cd, "fs"),entry(0x11ce, "fe"),entry(0x11cf, "feg"),entry(0x11d0, "ff"),
                entry(0x11d1, "far"),entry(0x11d2, "fat"),entry(0x11d3, "fqt"),entry(0x11d4, "fqg"),
                //		entry()(0x11d5, ""),
                entry(0x11d6, "fT"),
                //		entry()(0x11d7, ""),
                entry(0x11d8, "fz"),
                //		entry()(0x11d9, ""),
                entry(0x11da, "ar"),entry(0x11db, "af"),entry(0x11dc, "aq"),entry(0x11dd, "at"),
                entry(0x11de, "aT"),
                //		entry()(0x11df, ""),
                entry(0x11e0, "ac"),entry(0x11e1, "ag"),
                //		entry()(0x11e2, ""),
                entry(0x11e3, "qf"),entry(0x11e4, "qv"),entry(0x11e5, "qg"),
                //		entry()(0x11e6, ""),
                entry(0x11e7, "tr"),entry(0x11e8, "te"),entry(0x11e9, "tf"),entry(0x11ea, "tq"),
                //		entry()(0x11eb, ""),
                //		entry()(0x11ec, ""),
                //		entry()(0x11ed, ""),
                //		entry()(0x11ee, ""),
                //		entry()(0x11ef, ""),
                //		entry()(0x11f0, ""),
                //		entry()(0x11f1, ""),
                //		entry()(0x11f2, ""),
                entry(0x11f3, "vq"),
                //		entry()(0x11f4, ""),
                entry(0x11f5, "gs"),entry(0x11f6, "gf"),entry(0x11f7, "ga"),entry(0x11f8, "gq"),
                //		entry()(0x11f9, ""),
                entry(0x11fa, "rs"),entry(0x11fb, "rq"),entry(0x11fc, "rc"),entry(0x11fd, "rz"),
                entry(0x11fe, "rg"),entry(0x11ff, "ss")
        );



        // 자음만
        compatibilityList = Map.ofEntries(
                //entry(0x3130, ""),
                entry(0x3131, "r"),entry(0x3132, "R"),entry(0x3133, "rt"),entry(0x3134, "s"),
                entry(0x3135, "sw"),entry(0x3136, "sg"),entry(0x3137, "e"),entry(0x3138, "E"),
                entry(0x3139, "f"),entry(0x313a, "fr"),entry(0x313b, "fa"),entry(0x313c, "fq"),
                entry(0x313d, "ft"),entry(0x313e, "fx"),entry(0x313f, "fv"),entry(0x3140, "fg"),
                entry(0x3141, "a"),entry(0x3142, "q"),entry(0x3143, "Q"),entry(0x3144, "qt"),
                entry(0x3145, "t"),entry(0x3146, "T"),entry(0x3147, "d"),entry(0x3148, "w"),
                entry(0x3149, "W"),entry(0x314a, "c"),entry(0x314b, "z"),entry(0x314c, "x"),
                entry(0x314d, "v"),entry(0x314e, "g"),entry(0x314f, "k"),entry(0x3150, "o"),
                entry(0x3151, "i"),entry(0x3152, "O"),entry(0x3153, "j"),entry(0x3154, "p"),
                entry(0x3155, "u"),entry(0x3156, "P"),entry(0x3157, "h"),entry(0x3158, "hk"),
                entry(0x3159, "ho"),entry(0x315a, "hl"),entry(0x315b, "y"),entry(0x315c, "n"),
                entry(0x315d, "nj"),entry(0x315e, "np"),entry(0x315f, "nl"),entry(0x3160, "b"),
                entry(0x3161, "m"),entry(0x3162, "ml"),entry(0x3163, "l"),//entry(0x3164, ""),
                entry(0x3165, "ss"),entry(0x3166, "se"),entry(0x3167, "st"),//entry(0x3168, ""),
                entry(0x3169, "frt"),entry(0x316a, "fe"),entry(0x316b, "fqt"),//entry(0x316c, ""),
                //entry(0x316d, ""),
                entry(0x316e, "aq"),
                entry(0x316f, "at"),
                //entry(0x3170, ""),//entry(0x3171, ""),//entry(0x3172, ""),//entry(0x3173, ""),//entry(0x3174, ""),
                //entry(0x3175, ""),//entry(0x3176, ""),//entry(0x3177, ""),//entry(0x3178, ""),//entry(0x3179, ""),
                //entry(0x317a, ""),//entry(0x317b, ""),//entry(0x317c, ""),//entry(0x317d, ""),//entry(0x317e, ""),
                //entry(0x317f, ""),//entry(0x3180, ""),//entry(0x3181, ""),//entry(0x3182, ""),//entry(0x3183, ""),
                //entry(0x3184, ""),
                entry(0x3185, "gg"),
                //entry(0x3186, ""),
                entry(0x3187, "yi"),entry(0x3188, "yO"),entry(0x3189, "yl"),entry(0x318a, "bu"),
                entry(0x318b, "bP"),entry(0x318c, "bl")
                //entry(0x318d, ""),//entry(0x318e, "")
        );

    }

    private List<_CharZone> divideKoreanTypo( String typo ){
        // 리스트로 받아오기 위한 변수
        final List<_CharZone> result = new ArrayList<>();

        // typo 스트링의 글자수 만큼 lst에 담아둡니다.
        // 한글을 검색한다.
        IntStream.range(0, typo.length()).forEach(i -> {
            final _CharZone cz = new _CharZone();
            result.add(cz);
            final int comVal = typo.charAt(i);
            if ((comVal >= 0xAC00) && (comVal <= 0xD79F)) {
                final int uniVal = comVal - 0xAC00;
                final int uniIndex = uniVal % PHONOGRAM;

                // 유니코드표에 맞추어 초성 중성 종성을 분리한다.
                final int cho = (((uniVal - uniIndex) / PHONOGRAM) / VOWEL) + 0x1100;
                final int jung = (((uniVal - uniIndex) / PHONOGRAM) % VOWEL) + 0x1161;
                final int jong = uniIndex + 0x11A7;

                cz.initialConsonant = cho;
                cz.syllableNucleus = jung;
                cz.finalConsonant = (uniIndex != 0) ? jong : 0;

            } else if ((comVal >= 0x3130) && (comVal < 0x318F)) {
                // 초성 또는 중성만 있을 경우
                cz.compatibility = comVal;
            } else {
                // 한글이 아닌 것들...
                cz.notKorean = comVal;
            }
        });

        return result;
    }

    /**
     * 글자 표기
     */
    private class _CharZone{
        /** 초성 */
        private int initialConsonant;
        /** 중성 */
        private int syllableNucleus;
        /** 종성 */
        private int finalConsonant;
        /** Single 초성 */
        private int compatibility;
        /** 한글 아님 */
        private int notKorean;

        /**
         * @Override
         * 해당 키 값으로 변환 하도록 한다.
         */
        @Override
        public String toString(){

            if( notKorean > 0 ){
                return String.valueOf( (char)notKorean );
            }

            String result;

            try {
                if( compatibility > 0 ) {
                    return compatibilityList.get( compatibility );
                }

                result  = initialConsonantList.get( initialConsonant );
                result += syllableNucleusList.get( syllableNucleus );

                if( finalConsonant > 0 ) {
                    result += finalConsonantList.get( finalConsonant );
                }
            } catch (Exception e) {
                System.err.println("Convert Error : " + e.getMessage() );
                result = "";
            }

            return result;
        }

    }

}
