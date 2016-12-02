import java.awt.Container;

import javax.swing.JApplet;

/*
<applet code="Main.class" width="512" height="512">
</applet>
*/

public class Main extends JApplet {

	public void init() {
		MainPanel panel = new MainPanel();
		Container contentPane = getContentPane();
		contentPane.add(panel);
	}

}
