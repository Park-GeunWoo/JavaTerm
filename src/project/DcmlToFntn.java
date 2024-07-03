package project;

public class DcmlToFntn {
    public DcmlToFntn() {
    }

    public String dtf(double num) {
        if (num == (int) num) { // 정수인 경우
            return String.valueOf((int)num);
        }
        String nStr = String.valueOf(num);
        if (isRepeatingDeciaml(nStr)) {//순환소수 판별
            return RepeatingDecimalToFraction(nStr);
        } else {
            return DecimalToFraction(nStr);
        }
    }

    public static boolean isRepeatingDeciaml(String num) {//순환소수 판별
        int pointIndex = num.indexOf('.');//소수점 위치
        if (pointIndex == -1) {//정수일때
            return false;
        }

        String frcPart = num.substring(pointIndex + 1);//소수부준
        int length = frcPart.length();//소수길이

        for (int i = 1; i <= length / 2; i++) {//순환패턴 길이
            String pattern = frcPart.substring(0, i);//패턴
            boolean repeating = true;//반복유무

            for (int j = i; j < length; j += i) {//탐색
                if (!frcPart.startsWith(pattern, j)) {//부분패털이 일치하지 않으면 false
                    repeating = false;
                    break;
                }
            }

            if (repeating) {//반복적일때
                return true;
            }
        }

        return false;
    }

    public static String RepeatingDecimalToFraction(String num) {//순환소수>분수
        int pointIndex = num.indexOf('.');
        String intPart = num.substring(0, pointIndex);//정수부분만 잘라내기
        String frcPart = num.substring(pointIndex + 1);//소수부분만

        int length = frcPart.length();//소수부분 길이

        for (int i = 1; i <= length / 2; i++) {//순환패턴 길이

            String pattern = frcPart.substring(0, i);//패턴 후보
            boolean repeating = true;//패턴 반복유무

            for (int j = i; j < length; j += i) {//j:확인중인 위치
                if (!frcPart.startsWith(pattern, j)) {//부분패턴이 일치하지 않으면 false
                    repeating = false;
                    break;
                }
            }

            if (repeating) {//패턴을 찾으면 true
                try {
                    int numerator = Integer.parseInt(intPart + pattern); // 분자
                    int denominator = (int) Math.pow(10, i) - 1; // 분모
                    int gcd = gcd(numerator, denominator); // 약분
                    double tmp1 = (double) numerator / gcd;
                    double tmp2 = (double) denominator / gcd;
                    return String.valueOf(tmp1) + "/" + String.valueOf(tmp2);
                } catch (NumberFormatException e) {
                    return "-1";
                }
            }
        }
        return "RepeatingDecimalToFraction Error";
    }

    public static String DecimalToFraction(String num) {//그외 소수>분수
        int pointIndex = num.indexOf('.');//'.'위치

        if (pointIndex == -1) {//정수일때
            return num;
        }

        String intPart = num.substring(0, pointIndex);//정부부분
        String frcPart = num.substring(pointIndex + 1);//소수부분

        int frcLength = frcPart.length();//소수길이
        try {
            int numerator = Integer.parseInt(intPart + frcPart); // 분자
            int denominator = (int) Math.pow(10, frcLength); // 분모
            int gcd = gcd(numerator, denominator); // 약분
            return (numerator / gcd) + "/" + (denominator / gcd);
        } catch (NumberFormatException e) {
            return "-1";
        }
    }


    private static int gcd(int a, int b) { //유클리드 호제법
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }
}
