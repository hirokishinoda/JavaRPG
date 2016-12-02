

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Window {

private static final int EDGE_WIDTH = 2;

protected static final int LINE_HEIGHT = 8;

private static final int MAX_CHAR_IN_LINE = 20;

private static final int MAX_LINES = 3;

private static final int MAX_CHAR_IN_PAGE = MAX_CHAR_IN_LINE * MAX_LINES;

private Rectangle rect;

private Rectangle innerRect;

private boolean isVisible = false;

private char[] text = new char[128 * MAX_CHAR_IN_LINE];

private int maxPage;

private int curPage = 0;

public Window(Rectangle rect) {
								this.rect = rect;

								innerRect = new Rectangle(rect.x + EDGE_WIDTH, rect.y + EDGE_WIDTH, rect.width - EDGE_WIDTH * 2,
																																		rect.height - EDGE_WIDTH * 2);

								textRect = new Rectangle(
																innerRect.x + 16,
																innerRect.y + 16,
																320,
																120);

								messageEngine = new MessageEngine();

								ImageIcon icon = new ImageIcon(getClass().getResource("cursor.gif"));
								cursorImage = icon.getImage();
}

public void setMessage(String msg) {
								curPage = 0;

								for (int i=0; i<text.length; i++) {
																text[i] = ' ';
								}

								int p = 0;
								for (int i=0; i<msg.length(); i++) {
																char c = msg.charAt(i);
																if (c == '\\') {
																								i++;
																								if (msg.charAt(i) == 'n') {
																																p += MAX_CHAR_IN_LINE;
																																p = (p / MAX_CHAR_IN_LINE) * MAX_CHAR_IN_LINE;
																								} else if (msg.charAt(i) == 'f') {
																																p += MAX_CHAR_IN_PAGE;
																																p = (p / MAX_CHAR_IN_PAGE) * MAX_CHAR_IN_PAGE;
																								}
																} else {
																								text[p++] = c;
																}
								}

								maxPage = p / MAX_CHAR_IN_PAGE;
}

public void draw(Graphics g) {
								if (isVisible == false)
																return;

								g.setColor(Color.WHITE);
								g.fillRect(rect.x, rect.y, rect.width, rect.height);

								g.setColor(Color.BLACK);
								g.fillRect(innerRect.x, innerRect.y, innerRect.width, innerRect.height);

								for (int i=0; i<MAX_CHAR_IN_PAGE; i++) {
																char c = text[curPage * MAX_CHAR_IN_PAGE + i];
																int dx = textRect.x + MessageEngine.FONT_WIDTH * (i % MAX_CHAR_IN_LINE);
																int dy = textRect.y + (LINE_HEIGHT + MessageEngine.FONT_HEIGHT) * (i / MAX_CHAR_IN_LINE);
																messageEngine.drawCharacter(dx, dy, c, g);
								}

								if (curPage < maxPage) {
																int dx = textRect.x + (MAX_CHAR_IN_LINE / 2) * MessageEngine.FONT_WIDTH - 8;
																int dy = textRect.y + (LINE_HEIGHT + MessageEngine.FONT_HEIGHT) * 3;
																g.drawImage(cursorImage, dx, dy, null);
								}
}

public boolean nextMessage() {
								if (curPage == maxPage) {
																return true;
								}
								curPage++;

								return false;
}

public void show() {
								isVisible = true;
}

public void hide() {
								isVisible = false;
}

public boolean isVisible() {
								return isVisible;
}
}
