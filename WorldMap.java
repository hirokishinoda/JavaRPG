
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;

public class WorldMap implements Common {

	public static final int WIDTH = MAPX * CS;
	public static final int HEIGHT = MAPY * CS;

	int mapBase[][] = new int[MAPY][MAPX];
	int mapArrangement[][] = new int[MAPY][MAPX];

	MainPanel panel;
	Chara chara;

	Image ground;
	Image sea;
	Image storn;
	Image bridge;
	public int moveLengthX;
	public int moveLengthY;
	private Vector<Chara> charas = new Vector<Chara>();

	public WorldMap() {
		ImageIcon icon = new ImageIcon(getClass().getResource("./pipoya_mcset1_base0001.png"));
		ground = icon.getImage();
		storn = icon.getImage();
		icon = new ImageIcon(getClass().getResource("./pipoya_mcset1_at_water4b.png"));
		sea = icon.getImage();
		icon = new ImageIcon(getClass().getResource("./chip12e_map_01.png"));
		bridge = icon.getImage();

		loadMaptxt(mapBase, "WorldMapBase.txt");
		loadMaptxt(mapArrangement, "WorldMapArrangement.txt");
		loadEvent("./chara.dat");
	}

	public void draw(Graphics g, int offsetX, int offsetY) {

		int firstTileX = pixelsToTiles(-offsetX);
		int lastTileX = firstTileX + pixelsToTiles(MainPanel.WIDTH) + 1;
		lastTileX = Math.min(lastTileX, MAPX);

		int firstTileY = pixelsToTiles(-offsetY);
		int lastTileY = firstTileY + pixelsToTiles(MainPanel.HEIGHT) + 1;
		lastTileY = Math.min(lastTileY, MAPY);

		int i = firstTileY;
		for (int y = firstTileY; y < lastTileY; y++) {
			int j = firstTileX;
			for (int x = firstTileX; x < lastTileX; x++) {

				switch (mapBase[y][x]) {
				case 0:
					g.drawImage(ground, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY,
							tilesToPixels(j) + offsetX + CS, tilesToPixels(i) + offsetY + CS, 0, 0, 31, 31, panel);
					break;
				case 1:
					g.drawImage(sea, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY,
							tilesToPixels(j) + offsetX + CS, tilesToPixels(i) + offsetY + CS, 0, 128, 32, 159, panel);
					break;
				}

				switch (mapArrangement[y][x]) {
				case 1:
					g.drawImage(storn, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY,
							tilesToPixels(j) + offsetX + CS, tilesToPixels(i) + offsetY + CS, 97, 1255, 125, 1277,
							panel);
					break;
				case 3:
					g.drawImage(bridge, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY,
							tilesToPixels(j) + offsetX + CS, tilesToPixels(i) + offsetY + CS, 368, 225, 383, 243,
							panel);
					break;
				case 4:
					g.drawImage(bridge, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY,
							tilesToPixels(j) + offsetX + CS, tilesToPixels(i) + offsetY + CS, 352, 240, 367, 255,
							panel);
					break;
				}
				j++;
			}
			i++;
		}

		for (int n = 0; n < charas.size(); n++) {
			Chara chara = (Chara) charas.get(n);
			chara.draw(g, offsetX, offsetY);
		}
	}

	private void loadMaptxt(int map[][], String fileName) {
		try {
			String str;
			String address = "./" + fileName;
			int y = 0;

			File file = new File(address);
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((str = br.readLine()) != null) {

				String[] str2 = str.split(" ", 0);

				for (int x = 0; x < MAPX; x++) {

					map[y][x] = Integer.valueOf(str2[x]);

				}
				y++;
			}

			br.close();
		} catch (NumberFormatException e) {
			System.out.println(e);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private void loadEvent(String filename) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    getClass().getResourceAsStream(filename), "Shift_JIS"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("")) continue;
                if (line.startsWith("#")) continue;
                StringTokenizer st = new StringTokenizer(line, ",");
                String eventType = st.nextToken();
                if (eventType.equals("CHARA")) {
                    makeCharacter(st);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public boolean isHit(int y, int x) {
		if (mapArrangement[y][x] == 1) {
			return false;
		}

		for (int i = 0; i < charas.size(); i++) {
			Chara chara = (Chara) charas.get(i);
			if (chara.getX() == x && chara.getY() == y) {
				return false;
			}
		}

		return true;
	}

	private void makeCharacter(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        int charaNo = Integer.parseInt(st.nextToken());
        int dir = Integer.parseInt(st.nextToken());
        int moveType = Integer.parseInt(st.nextToken());
        String message = st.nextToken();
        Chara c = new Chara(x, y, charaNo, dir, moveType, this);
        c.setMessage(message);
        charas.add(c);
    }

		public Chara charaCheck(int x, int y) {
        for (int i=0; i<charas.size(); i++) {
            Chara chara = (Chara)charas.get(i);
            if (chara.getX() == x && chara.getY() == y) {
                return chara;
            }
        }

        return null;
    }

	public void addChara(Chara chara) {
		charas.add(chara);
	}

	public static int pixelsToTiles(double pixels) {
		return (int) Math.floor(pixels / CS);
	}

	public static int tilesToPixels(int tiles) {
		return tiles * CS;
	}

	public Vector<Chara> getCharas() {
        return charas;
    }

}
