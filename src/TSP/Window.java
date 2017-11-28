package TSP;

import java.awt.Graphics;

import javax.swing.JFrame;

public class Window extends JFrame{
	private static final long serialVersionUID = 1L;
	private Display display;
	public Window(int width, int height, City[] cities) {
		setSize(width,height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.display = new Display(cities);
		add(display);
		setVisible(true);
	}
	public Display getDisplay() {
		return display;
	}
	public void updateRoute(City[] path) {
		display.updateRoute(path);
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

}
