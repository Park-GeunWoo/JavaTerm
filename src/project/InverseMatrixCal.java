package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

class InverseMatrixCal extends JPanel {
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private JFrame parentFrame;
    private JTextArea inputTextArea,resultArea;
    //private JButton calButton,backButton;
    private double[][] matrix,result,inverseMatrix;
    private int matrixRows, matrixCols;
    private int n;
    private RoundedButton backButton,calButton;

    InverseMatrixCal(CardLayout cardLayout, JPanel parentPanel, JFrame parentFrame) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        //초기화

        //텍스트박스 초기화
        inputTextArea =new JTextArea("Enter matrix");
        resultArea=new JTextArea("RESULT AREA");
        resultArea.setEditable(false);//resultArea 입력불가
        inputTextArea.setFont(new Font("Arial", Font.PLAIN, 24));
        resultArea.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        inputTextArea.setBackground(Color.LIGHT_GRAY);

        //버튼들
        calButton=new RoundedButton("계산", new Color(255, 153, 0),Color.black,Color.black);
        backButton = new RoundedButton("뒤로가기", new Color(255, 153, 0),Color.black,Color.black);
        //calButton = new JButton("계산");
        //backButton = new JButton("뒤로 가기");
        JPanel buttonPanel = new JPanel(new FlowLayout());//버튼 패널
        buttonPanel.add(calButton);
        buttonPanel.add(backButton);


        //계산버튼
        calButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performInverseMatrix();
            }
        });

        //뒤로가기
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(parentPanel, "Main Panel");
                parentFrame.setSize(500, 500);
            }
        });

        JLabel titleLabel=new JLabel(" 역행렬");//타이틀 레이블
        titleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 24));//폰트
        add(titleLabel,BorderLayout.NORTH);


        JPanel panel = new JPanel(new GridLayout(1, 3,5,0));//textArea의 패널
        JScrollPane matrixArea_scroll=new JScrollPane(inputTextArea);
        JScrollPane resultArea_scroll=new JScrollPane(resultArea);
        inputTextArea.setBackground(Color.WHITE); //색 변경
        resultArea.setBackground(Color.WHITE);

        matrixArea_scroll.setBorder(BorderFactory.createEmptyBorder()); //기본테두리 제거
        resultArea_scroll.setBorder(BorderFactory.createEmptyBorder());

        panel.add(matrixArea_scroll, BorderLayout.EAST);
        panel.add(resultArea_scroll, BorderLayout.WEST);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));//패널태두리
        panel.setBackground(Color.lightGray);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 5));//테두리 추가

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    private void performInverseMatrix() { //역행렬 연산
        try {
            //입력받은 텍스트를 배열에 저장
            matrix = MatrixStringToDouble.convert(inputTextArea.getText());
            matrixRows = matrix.length;//가로
            matrixCols = matrix[0].length;//세로

            //System.out.printf("%d %d %d %d",matrix1Rows,matrix1Cols,matrix2Rows,matrix2Cols);
            if (matrixCols != matrixRows) {//catch부분ㅇ; 먼저 실행됨
                throw new IllegalArgumentException("행과 열의 크기가 같지 않음");
            }

            //result = new double[matrixRows][matrixCols];

            double det=Determinant(matrix);//행렬식 구하기
            inverseMatrix = invert(matrix);//역행려 계산

            //결과출력
            StringBuilder result = new StringBuilder();
            DecimalFormat df = new DecimalFormat("#.####"); //소수점 4자리까지 불필요한 0제거

            if (det == 0) { //행렬식이 0일떄
                result.append("Det<A> :  ").append(String.format("%s",det)).append("\n");
                result.append("역행렬이 없음.\n\n");
            }
            else { //행렬식이 존재할 때
                result.append("Det<A> :  ").append(String.format("%s",det)).append("\n\n");
            }

            //DcmlToFntn cvt = new DcmlToFntn(); //실패
            result.append("\n결과 :\n");
            for (int i = 0; i < matrixRows; i++) {
                for (int j = 0; j < matrixRows; j++) {
                    //System.out.println(cvt.dtf(inverseMatrix[i][j]));
                    //result.append(cvt.dtf(inverseMatrix[i][j])).append("  "); //소수>분수로
                    result.append(String.format("%s  ", df.format(inverseMatrix[i][j])));
                }
                result.append("\n");
            }


            resultArea.setText(result.toString());//출력
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "입력값이 올바르지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double[][] invert(double a[][]) {//역핼렬 계산
        int n = a.length;
        double[][] augmentedMatrix = new double[n][2 * n];//첨가행렬

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {//외쪽에 원본절반 복사
                augmentedMatrix[i][j] = a[i][j];
            }
            augmentedMatrix[i][i + n] = 1;//오른쪽에 단위행렬
        }

        for (int i = 0; i < n; i++) {//가우스소거법
            double pivot = augmentedMatrix[i][i];
            for (int j = 0; j < 2 * n; j++) {
                augmentedMatrix[i][j] /= pivot;//피봇행/피봇값 1로 만들기
            }
            for (int k = 0; k < n; k++) {//다른행/피봇값 0만들기
                if (k != i) {
                    double factor = augmentedMatrix[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                    }
                }
            }
        }

        double[][] inverseMatrix = new double[n][n];//역행렬

        for (int i = 0; i < n; i++) {//첨가행렬 오른쪽 절반 복사
            for (int j = 0; j < n; j++) {
                inverseMatrix[i][j] = augmentedMatrix[i][j + n];
            }
        }

        return inverseMatrix;
    }


    private double Determinant(double matrix[][]) {//행렬식 구하기
        int n = matrix.length; //크기
        double det = 0;//행렬식 값

        if (n == 1) {
            det = matrix[0][0];
        }
        else if (n == 2) {
            det = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }
        else {
            det = 0;
            int sign = 1;//부호

            for (int i = 0; i < n; i++) {
                double[][] subMatrix = new double[n - 1][n - 1];//소행렬

                for (int j = 1; j < n; j++) {//i열은 계산하면 안됌
                    int subMatrixCol = 0;//소행렬 요소 초기화
                    for (int k = 0; k < n; k++) {
                        if (k == i) {
                            continue;//현재열 제외
                        }
                        subMatrix[j - 1][subMatrixCol] = matrix[j][k];
                        subMatrixCol++;//다음열 이동
                    }
                }

                det += sign * matrix[0][i] * Determinant(subMatrix);//부호*현재인덱스*소행렬
                sign = -sign;//부호 변경. +,-,+
            }
        }
        return det;
    }
}