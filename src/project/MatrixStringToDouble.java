package project;

class MatrixStringToDouble {//text area에서 받은 문자열을 행렬에 사용할수있게 변환ㅍ
    //textarea의 숫자를 행렬크기에 맞게 동적으로 저장하는 클래스
    public static double[][] convert(String text) {
        String[] rows = text.split("\n");//행 단위로 분리
        double[][] matrix = new double[rows.length][];//행에 맞는 2차원 배열


        for (int i = 0; i < rows.length; i++) {
            //공백 기준으로 열 단위로 분리
            String[] cols = rows[i].trim().split("\\s+");

            matrix[i] = new double[cols.length];//행에 맞는 크기로 열분리
            for (int j = 0; j < cols.length; j++) {
                //문자열을 double로 저장
                matrix[i][j] = Double.parseDouble(cols[j]);
            }
        }
        return matrix;
    }
}