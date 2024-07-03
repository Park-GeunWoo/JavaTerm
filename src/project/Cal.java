package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Cal extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JButton btn1, btn2, btn3;
    private RoundedButton bt1, bt2, bt3;
    private JPanel pn0, pn2, pn3, buttonPanel, imagepanel;
    private MatrixMultiplicationCal pn1;
    private ImageIcon backImage;
    private Color backgroundColor;

    public Cal() {
        setTitle("행렬계산기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setSize(500, 500);
        setVisible(true);

        //메뉴생성
        createMenu();

        mainPanel = new JPanel();
        cardLayout = new CardLayout();//추가 패널에서 사용하기위함
        mainPanel.setLayout(cardLayout);

        // 메인 패널
        pn0 = new JPanel();
        backImage = new ImageIcon("src\\project\\eclipselogo.jpg");//메인페널 이미지

        imagepanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        pn0.setLayout(new BorderLayout());

        // 추가 패널들
        pn1 = new MatrixMultiplicationCal(cardLayout, mainPanel, this);
        pn2 = new InverseMatrixCal(cardLayout, mainPanel, this);
        pn3 = new LUDecomposition(cardLayout, mainPanel, this);

        mainPanel.add(pn0, "Main Panel");
        mainPanel.add(pn1, "multiplication");
        mainPanel.add(pn2, "inverse matrix");
        mainPanel.add(pn3, "LU decomposition");

        // 버튼 생성
        buttonPanel = new JPanel();//버튼 패널
        bt1 = new RoundedButton("행렬곱 계산", new Color(255, 153, 0),Color.black,Color.black);

        //btn1 = new JButton("행렬곱 계산");
        bt2=new RoundedButton("역행렬 계산", new Color(255, 153, 0),Color.black,Color.black);

        //btn2 = new JButton("역행렬 계산");
        bt3=new RoundedButton("LU 분해", new Color(255, 153, 0),Color.black,Color.black);
        //btn3 = new JButton("LU분해");

        bt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "multiplication");
                setSize(1000, 500);
            }
        });

        bt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "inverse matrix");
                setSize(1000, 500);
            }
        });

        bt3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "LU decomposition");
                setSize(1300, 500);
            }
        });

        //btn1.setBackground(new Color(255,153, 0));

        //btn1.setPreferredSize(new Dimension(100, 30));
        //btn1.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        //btn1.setBorder(BorderFactory.createRaisedBevelBorder());

        buttonPanel.add(bt1);
        buttonPanel.add(bt2);
        buttonPanel.add(bt3);

        pn0.add(buttonPanel, BorderLayout.SOUTH);
        pn0.add(imagepanel, BorderLayout.CENTER);

        imagepanel.setBackground(Color.white);
        buttonPanel.setBackground(Color.white);

        add(mainPanel, BorderLayout.CENTER);
    }

    //메뉴
    void createMenu() {
        JMenuBar mb=new JMenuBar();
        JMenu m1=new JMenu("메뉴");

        JMenuItem item1=new JMenuItem("eclipselogo");
        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //배경 이미지 변경
                changeImage("src\\project\\eclipselogo.jpg");
            }
        });

        JMenuItem item2 = new JMenuItem("Java1");
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeImage("src\\project\\JavaLogo.jpg");
            }
        });

        JMenuItem item3 = new JMenuItem("Java2");
        item3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeImage("src\\project\\logo1.jpg");
            }
        });

        m1.add(item1);
        m1.add(item2);
        m1.add(item3);

        mb.add(m1);
        setJMenuBar(mb);
    }

    void changeImage(String imagePath) {//이미지 변경클래스
        backImage = new ImageIcon(imagePath);
        imagepanel.repaint();
    }


    public static void main(String[] args) {
        // 이벤트 디스패치 스레드에서 GUI 생성
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Cal();
            }
        });
    }
}
