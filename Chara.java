

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Chara implements Common {

	private Thread threadAnime;

	Image image;

	WorldMap wmap;

	private static final int SPEED = 4;
	public static final double PROB_MOVE = 0.02;
	private int py;
	private int px;
	private int x;
	private int y;
	private int nextY;
	private int nextX;
	private int direction;
	private int count;
	public boolean isMoving;
	private int moveLength;

	private int charaNo;

	private int moveType;

	private String message;

	public Chara(int x, int y, int charaNo, int direction, int moveType, WorldMap map) {
		this.x = x;
		this.y = y;

		px = x * CS;
		py = y * CS;

		this.charaNo = charaNo;
		this.direction = direction;
		count = 0;
		this.moveType = moveType;
		this.wmap = map;

		if (image == null) {
			loadImage();
		}

		threadAnime = new Thread(new AnimationThread());
		threadAnime.start();
	}

	public void draw(Graphics g, int offsetX, int offsetY) {
		int cx = charaNo * CS * 3;

		g.drawImage(image, px + offsetX, py + offsetY, px + offsetX + CS, py + offsetY + CS, cx + count * CS,
				direction * CS, cx + (count + 1) * CS, (direction * CS) + 31, null);
	}

	private class AnimationThread extends Thread {
		public void run() {
			while (true) {
				if (count == 2) {
					count = 0;
				} else {
					count++;
				}

				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setDirection(int dir) {
		this.direction = dir;
	}

	public void setPx(int x) {
		this.px = x;
	}

	public void setPy(int y) {
		this.py = y;
	}

	public int getPy() {
		return py;
	}

	public int getPx() {
		return px;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean b) {
		isMoving = b;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
			return message;
	}

	public int getMoveType() {
		return moveType;
	}

	public boolean move() {
		switch (direction) {
		case LEFT:
			if (LeftMove()) {

				return true;
			}
			break;
		case RIGHT:
			if (RightMove()) {

				return true;
			}
			break;
		case UP:
			if (UpMove()) {

				return true;
			}
			break;
		case DOWN:
			if (DownMove()) {

				return true;
			}
			break;
		}

		return false;
	}

	public boolean RightMove() {
		nextY = y;
		nextX = x + 1;
		if (nextX > MAPX - 1)
			nextX = MAPX - 1;
		if (wmap.isHit(nextY, nextX)) {
			if (isMoving) {
				if (moveLength >= CS) {
					x++;
					if (px > WorldMap.WIDTH - CS)
						px = WorldMap.WIDTH - CS;
					px = x * CS;
					isMoving = false;
					moveLength = 0;
					return true;
				} else {
					px += SPEED;
					moveLength += SPEED;
				}
			}
		} else {
			isMoving = false;
		}
		return false;
	}

	public boolean LeftMove() {
		nextY = y;
		nextX = x - 1;
		if (nextX < 0)
			nextX = 0;
		if (wmap.isHit(nextY, nextX)) {
			if (isMoving) {
				if (moveLength >= CS) {
					x--;
					if (x < 0)
						x = 0;
					px = x * CS;
					isMoving = false;
					moveLength = 0;
					return true;
				} else {
					px -= SPEED;
					moveLength += SPEED;
				}
			}
		} else {
			isMoving = false;
		}
		return false;
	}

	public boolean UpMove() {
		nextY = y - 1;
		nextX = x;
		if (nextY < 0)
			nextY = 0;
		if (wmap.isHit(nextY, nextX)) {
			if (isMoving) {
				if (moveLength >= CS) {
					y--;
					if (y < 0)
						y = 0;
					py = y * CS;
					isMoving = false;
					moveLength = 0;
					return true;
				} else {
					py -= SPEED;
					moveLength += SPEED;
				}
			}
		} else {
			isMoving = false;
		}
		return false;
	}

	public boolean DownMove() {
		nextY = y + 1;
		nextX = x;
		if (nextY > MAPY - 1)
			nextY = MAPY - 1;
		if (wmap.isHit(nextY, nextX)) {
			if (isMoving) {
				if (moveLength >= CS) {
					y++;
					if (py > WorldMap.WIDTH - CS)
						py = WorldMap.WIDTH - CS;
					py = y * CS;
					isMoving = false;
					moveLength = 0;
					return true;
				} else {
					py += SPEED;
					moveLength += SPEED;
				}
			}
		} else {
			isMoving = false;
		}
		return false;
	}

	public void loadImage() {
		String Filename = "Chara.png";
		ImageIcon icon = new ImageIcon(getClass().getResource(Filename));
		image = icon.getImage();
	}

	public Chara talkWith() {
        int nextX = 0;
        int nextY = 0;
        switch (direction) {
            case LEFT:
                nextX = x - 1;
                nextY = y;
                break;
            case RIGHT:
                nextX = x + 1;
                nextY = y;
                break;
            case UP:
                nextX = x;
                nextY = y - 1;
                break;
            case DOWN:
                nextX = x;
                nextY = y + 1;
                break;
        }
        Chara chara;
        chara = wmap.charaCheck(nextX, nextY);
        if (chara != null) {
            switch (direction) {
                case LEFT:
                    chara.setDirection(RIGHT);
                    break;
                case RIGHT:
                    chara.setDirection(LEFT);
                    break;
                case UP:
                    chara.setDirection(DOWN);
                    break;
                case DOWN:
                    chara.setDirection(UP);
                    break;
            }
        }

        return chara;
    }
}
