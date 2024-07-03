package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class LUDecomposition extends JPanel {
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private JFrame parentFrame;

    //private JButton calButton,backButton;
    private JTextArea inputTextArea,resultArea;
    private double[][] matrix,result,L,U;
    private int n,size,matrixRows,matrixCols;

    LUDecomposition(CardLayout cardLayout, JPanel parentPanel, JFrame parentFrame) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());

        //텍스트박스 초기화
        inputTextArea = new JTextArea("Enter matrix",10, 20);
        resultArea = new JTextArea("result Area",20, 40);
        inputTextArea.setFont(new Font("Arial", Font.PLAIN, 24));
        resultArea.setFont(new Font("Monospaced", Font.BOLD, 24));
        resultArea.setEditable(false);

        //버튼생성
        RoundedButton calButton=new RoundedButton("계산", new Color(255, 153, 0),Color.black,Color.black);
        RoundedButton backButton = new RoundedButton("뒤로가기", new Color(255, 153, 0),Color.black,Color.black);
        //calButton = new JButton("계산");
        //backButton = new JButton("뒤로 가기");
        JPanel buttonPanel = new JPanel(new FlowLayout());//버튼 패널
        buttonPanel.add(calButton);
        buttonPanel.add(backButton);

        //계산버튼 이벤트
        calButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLUDecomposition();
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

        JLabel titleLabel=new JLabel(" LU분해");//타이틀 레이블
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

    //LU분해
    void performLUDecomposition() {
        try {

            //입력받은 텍스트를 배열에 저장
            matrix = MatrixStringToDouble.convert(inputTextArea.getText());
            matrixRows = matrix.length;
            matrixCols = matrix[0].length;
            size=matrix.length;

            //System.out.printf("%d %d %d %d",matrix1Rows,matrix1Cols,matrix2Rows,matrix2Cols);
            if (matrixCols != matrixRows) {//정방행렬 확인
                throw new IllegalArgumentException("행과 열의 크기가 같지 않음");
            }

            result = new double[size][size];

            //행렬초기화
            L=new double[size][size];
            U=new double[size][size];


            if (!decompose(matrix, size)) {//LU분해 가능여부
                JOptionPane.showMessageDialog(this, "LU 분해가 불가능합니다. (특이행렬이거나 0으로 나누는 경우 발생)", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            L=getL();
            U=getU();

//            System.out.println("L Matrix:");
//            printMatrix(L);
//
//            System.out.println("U Matrix:");
//            printMatrix(U);

            StringBuilder detailedResult = new StringBuilder();
            DecimalFormat df = new DecimalFormat("#.####"); //소수점 4자리까지 불필요한 0제거

            //결과출력
            //L형식 마추기
            int maxLengthL = 0;
            for (double[] row : L) {//행 반복
                for (double value : row) {
                    int length = df.format(value).length();//길이
                    if (length > maxLengthL) {
                        maxLengthL = length;
                    }
                }
            }

            //L저장
            detailedResult.append("L :\n");
            for (double[] row : L) {
                for (double value : row) {
                    String formattedValue = df.format(value);
                    detailedResult.append(String.format("%-" + (maxLengthL + 2) + "s", formattedValue));
                }
                detailedResult.append("\n");
            }


            //U형식
            int maxLengthU = 0;
            for (double[] row : U) {//u행 반복
                for (double value : row) {
                    int length = df.format(value).length();
                    if (length > maxLengthU) {
                        maxLengthU = length;
                    }
                }
            }
            //u저장
            detailedResult.append("\nU :\n");
            for (double[] row : U) {
                for (double value : row) {
                    String formattedValue = df.format(value);
                    detailedResult.append(String.format("%-" + (maxLengthU + 2) + "s", formattedValue));
                }
                detailedResult.append("\n");
            }

            resultArea.setText(detailedResult.toString());//출력

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "입력 값이 올바르지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    double[][] getL() {
        return L;
    }

    double[][] getU() {
        return U;
    }

    //LU계산
    private boolean decompose(double[][] matrix, int size) {
        for (int i = 0; i < size; i++) {//행반복
            for (int k = i; k < size; k++) {//u 상삼각행렬 계산
                double sum = 0;
                for (int j = 0; j < i; j++) {
                    sum += (L[i][j] * U[j][k]);//L*U 합
                }
                U[i][k] = matrix[i][k] - sum; //현재값-합
            }

            for (int k = i; k < size; k++) {//L 하삼삭행렬
                if (U[i][i] == 0) {//U행렬 대각선이 0인경우 분해 안됌
                    return false; //피봇팅이 필요
                }

                if (i == k) {//대각선 1로 지정
                    L[i][i] = 1;
                } else {
                    double sum = 0;
                    for (int j = 0; j < i; j++) {
                        sum += (L[k][j] * U[j][i]);//L*U의 합
                    }
                    L[k][i] = (matrix[k][i] - sum) / U[i][i]; //(현재값-합)/L의 대각선 값을 L에 함
                }
            }
        }
        return true;
    }

}
