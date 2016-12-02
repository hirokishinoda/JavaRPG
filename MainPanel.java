

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

public class MainPanel extends JPanel implements Common, KeyListener, Runnable {

	public static final int WIDTH = 512;
	public static final int HEIGHT = 512;

	private static Rectangle WND_RECT = new Rectangle(25, 355, 455, 140);

	WorldMap wmap = new WorldMap();
	private Window messageWindow;
	private Chara hero;

	private ActionKey leftKey;
	private ActionKey rightKey;
	private ActionKey upKey;
	private ActionKey downKey;
	private ActionKey spaceKey;

	private Thread gameLoop;
	Random rand = new Random();

	public MainPanel() {
		leftKey = new ActionKey();
		rightKey = new ActionKey();
		upKey = new ActionKey();
		downKey = new ActionKey();
		spaceKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);

		hero = new Chara(6, 6, 0, DOWN, 0, wmap);
		wmap.addChara(hero);

		messageWindow = new Window(WND_RECT);

		setFocusable(true);
		addKeyListener(this);

		gameLoop = new Thread(this);
		gameLoop.start();
	}

	public void paint(Graphics g) {

		int offsetX = MainPanel.WIDTH / 2 - hero.getPx();
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, MainPanel.WIDTH - WorldMap.WIDTH);

		int offsetY = MainPanel.HEIGHT / 2 - hero.getPy();
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, MainPanel.HEIGHT - WorldMap.HEIGHT);

		wmap.draw(g, offsetX, offsetY);
		messageWindow.draw(g);
	}


	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			leftKey.press();
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			rightKey.press();
		}
		if (keyCode == KeyEvent.VK_UP) {
			upKey.press();
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			downKey.press();
		}
		if (keyCode == KeyEvent.VK_SPACE) {
			spaceKey.press();
		}
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			leftKey.release();
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			rightKey.release();
		}
		if (keyCode == KeyEvent.VK_UP) {
			upKey.release();
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			downKey.release();
		}
		if (keyCode == KeyEvent.VK_SPACE) {
			spaceKey.release();
		}
	}


	private void mainWindowCheckInput() {
		if (leftKey.isPressed()) {
			if (!hero.isMoving()) {
				hero.setDirection(LEFT);
				hero.setMoving(true);
			}
		}
		if (rightKey.isPressed()) {
			if (!hero.isMoving()) {
				hero.setDirection(RIGHT);
				hero.setMoving(true);
			}
		}
		if (upKey.isPressed()) {
			if (!hero.isMoving()) {
				hero.setDirection(UP);
				hero.setMoving(true);
			}
		}
		if (downKey.isPressed()) {
			if (!hero.isMoving()) {
				hero.setDirection(DOWN);
				hero.setMoving(true);
			}
		}
		if (spaceKey.isPressed()) { 
				if (hero.isMoving()) return;
				if (!messageWindow.isVisible()) {
						Chara chara = hero.talkWith();
						if (chara != null) {
								messageWindow.setMessage(chara.getMessage());
								messageWindow.show();
						} else {
								messageWindow.setMessage("ああああああ");
								messageWindow.show();
						}
				}
		}
	}

	private void messageWindowCheckInput() {
		if (spaceKey.isPressed()) {
			messageWindow.hide();
		}
	}


	public void run() {

		while (true) {
			if (messageWindow.isVisible()) {
				messageWindowCheckInput();
			} else {
				mainWindowCheckInput();
			}

			if (!messageWindow.isVisible()) {
				heroMove();
				charaMove();
			}
			repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void heroMove() {
		if (hero.isMoving()) {
			if (hero.move()) {
			}
		}
	}

	private void charaMove() {
		Vector<Chara> charas = wmap.getCharas();
		for (int i = 0; i < charas.size(); i++) {
			Chara chara = (Chara) charas.get(i);
			if (chara.getMoveType() == 1) {
				if (chara.isMoving()) {
					chara.move();
				} else if (rand.nextDouble() < Chara.PROB_MOVE) {
					chara.setDirection(rand.nextInt(4));
					chara.setMoving(true);
				}
			}
		}
	}
}
