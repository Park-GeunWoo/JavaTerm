package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

class MatrixMultiplicationCal extends JPanel {
    private JTextArea matrix1_TextArea, matrix2_TextArea, resultArea;
    //private JButton calButton, backButton;
    private double[][] matrix1, matrix2, result;
    private int matrix1Rows, matrix1Cols, matrix2Rows, matrix2Cols;
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private JFrame parentFrame;


    MatrixMultiplicationCal(CardLayout cardLayout, JPanel parentPanel,JFrame parentFrame) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());

        //텍스트박스 초기화
        matrix1_TextArea = new JTextArea("Enter matrix 1",10, 20);
        matrix2_TextArea = new JTextArea("Enter matrix 2",10, 20);
        resultArea = new JTextArea("result Area",20, 40);
        resultArea.setEditable(false);//결과창은 입력불가

        matrix1_TextArea.setFont(new Font("Arial", Font.ITALIC, 24));
        matrix2_TextArea.setFont(new Font("Arial", Font.ITALIC, 24));
        resultArea.setFont(new Font("Monospaced", Font.BOLD, 24));

        //버튼들
        RoundedButton calButton=new RoundedButton("계산", new Color(255, 153, 0),Color.black,Color.black);
        RoundedButton backButton = new RoundedButton("뒤로가기", new Color(255, 153, 0),Color.black,Color.black);
        //calButton = new JButton("계산");
        //backButton = new JButton("뒤로 가기");
        JPanel buttonPanel = new JPanel(new FlowLayout());//버튼 패널
        buttonPanel.add(calButton);
        buttonPanel.add(backButton);

        //계산버튼
        calButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performMatrixMultiplication();
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

        JLabel titleLabel=new JLabel(" 행렬곱셈 계산기");//타이틀 안내
        titleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 24));//폰트
        add(titleLabel,BorderLayout.NORTH);


        JPanel inputPanel = new JPanel(new GridLayout(1, 3,5,0));//행렬입력받는패널
        JScrollPane matrix1Area_scroll=new JScrollPane(matrix1_TextArea);//matrix area들의 스크롤패인
        JScrollPane matrix2Area_scroll=new JScrollPane(matrix2_TextArea);
        matrix1_TextArea.setBackground(Color.WHITE); //색변경
        matrix2_TextArea.setBackground(Color.WHITE);

        matrix1Area_scroll.setBorder(BorderFactory.createEmptyBorder()); //기본테두리 제거
        matrix2Area_scroll.setBorder(BorderFactory.createEmptyBorder());

        inputPanel.add(matrix1Area_scroll, BorderLayout.EAST);
        inputPanel.add(matrix2Area_scroll, BorderLayout.WEST);

        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));//패널태두리
        inputPanel.setBackground(Color.lightGray);
        inputPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 5));//테두리 추가

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(new JScrollPane(resultArea), BorderLayout.EAST);
    }

    private void performMatrixMultiplication() {
        try {
            //입력받은 텍스트를 배열에 저장
            matrix1 = MatrixStringToDouble.convert(matrix1_TextArea.getText());
            matrix2 = MatrixStringToDouble.convert(matrix2_TextArea.getText());
            matrix1Rows = matrix1.length;
            matrix1Cols = matrix1[0].length;
            matrix2Rows = matrix2.length;
            matrix2Cols = matrix2[0].length;

            //System.out.printf("%d %d %d %d",matrix1Rows,matrix1Cols,matrix2Rows,matrix2Cols);
            if (matrix1Cols != matrix2Rows) {//행렬곱 가능여부
                throw new IllegalArgumentException("행과 열의 크기가 같지 않음");
            }

            result = new double[matrix1Rows][matrix2Cols];
            StringBuilder result = new StringBuilder();//출력버퍼
            DecimalFormat df = new DecimalFormat("#.####"); //소수점 4자리까지 0제거

            //곱셈시작
            for (int i = 0; i < matrix1Rows; i++) {
                for (int j = 0; j < matrix2Cols; j++) {
                    result.append(String.format("a%db%d : ", i, j));//위치 출력 a_0 b_0
                    for (int k = 0; k < matrix1Cols; k++) {
                        this.result[i][j] += matrix1[i][k] * matrix2[k][j];
                        //System.out.println(result[i][j]);
                        result.append(String.format("(%s×%s) + ", df.format(matrix1[i][k]), df.format(matrix2[k][j])));//과정 저장
                    }
                    result.setLength(result.length() - 2); //마지막 +제거
                    result.append(String.format(" = %s\n", df.format(this.result[i][j])));//마지막에 답 저장
                }
            }

            result.append("\n답 :\n");
            for (double[] row : this.result) {
                for (double value : row) {
                    result.append(df.format(value) + " ");
                }
                result.append("\n");
            }

            resultArea.setText(result.toString());

        } catch (Exception ex) {//matrix 1의 마지막 행의 요소만 더 많을때는 오류를 출력하지못함.,.
            JOptionPane.showMessageDialog(this, "입력값이 올바르지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}