package project;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;


//동근버튼 만드는 클래스
class RoundedButton extends JButton {
    private static final long serialVersionUID = 1L;
    private Color backgroundColor;
    private Color foregroundColor;
    private Color borderColor;

    public RoundedButton(String text, Color backgroundColor, Color foregroundColor, Color borderColor) {
        super(text);
        this.backgroundColor = backgroundColor; //버튼 배경
        this.foregroundColor = foregroundColor;//텍스트 색
        this.borderColor = borderColor;//테두리 색
        setOpaque(false); // 기본배경제거
        setContentAreaFilled(false);//버튼 내용 없애기
        setBorderPainted(false);//테두리 그리기 없애기
        setFocusPainted(false);//포커스 테두리 제거
    }

    @Override
    protected void paintComponent(Graphics g) {//버튼그리기
        super.paintComponent(g); // 기본 컴포넌트

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();//버튼폭 (크기 변경 안됨..
        int height = getHeight();//버튼높이 (크기 변경 안됨/..
        int arc = 30; // 둥근 모서리의 크기

        // 버튼 배경 그리기
        g2.setColor(backgroundColor);//배경색
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));

        // 버튼 테두리 그리기
        g2.setColor(borderColor);//테두리 색
        g2.draw(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, arc, arc));

        // 텍스트 그리기
        g2.setColor(foregroundColor);//텍스트 색
        g2.setFont(getFont());//폰트
        int textWidth = g2.getFontMetrics().stringWidth(getText());//폭
        int textHeight = g2.getFontMetrics().getHeight();//높이
        int textX = (width - textWidth) / 2;//x
        int textY = (height + textHeight) / 2 - g2.getFontMetrics().getDescent();//y
        g2.drawString(getText(), textX, textY);//드로우

        g2.dispose();//Graphice2D 해제
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(130, 30); // 기본 크기 반환
    }
}