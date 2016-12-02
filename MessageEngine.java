import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class MessageEngine {

public static final int FONT_WIDTH = 16;
public static final int FONT_HEIGHT = 22;

public static final int WHITE = 0;
public static final int RED = 160;
public static final int GREEN = 320;
public static final int BLUE = 480;


private Image fontImage;

private HashMap kana2Pos;

private int color;

public MessageEngine() {

        ImageIcon icon = new ImageIcon(getClass().getResource("misaki_gothic.png"));
        fontImage = icon.getImage();

        color = WHITE;

        kana2Pos = new HashMap();
        createHash();
}

public void setColor(int c) {
        this.color = c;

        if (color != WHITE && color != RED && color != GREEN && color != BLUE) {
                this.color = WHITE;
        }
}

public void drawMessage(int x, int y, String message, Graphics g) {
        for (int i=0; i<message.length(); i++) {
                char c = message.charAt(i);
                int dx = x + FONT_WIDTH * i;
                drawCharacter(dx, y, c, g);
        }
}

public void drawCharacter(int x, int y, char c, Graphics g) {
        Point pos = (Point)kana2Pos.get(new Character(c));
        g.drawImage(fontImage, x, y, x + FONT_WIDTH, y + FONT_HEIGHT,
                    pos.x + color, pos.y, pos.x + color + FONT_WIDTH, pos.y + FONT_HEIGHT, null);
}

private void createHash() {
        kana2Pos.put(new Character('あ'), new Point(8,24));
}
}
