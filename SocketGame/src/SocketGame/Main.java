package SocketGame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import static SocketGame.Connections.*;

@SuppressWarnings("serial")
public class Main extends JFrame {

	void printLogin() {
		sendToServer(Constants.LOGIN);
	}

	void printPass() {
		sendToServer(Constants.PASSWORD);
	}

	JPanel myCanvas;

	void drawLine(MyTokenizer in) {
		if (in.nextToken().equals("line")) {
			int x1 = in.nextInt();
			System.err.println("x1 = " + x1);
			int y1 = in.nextInt();
			System.err.println("y1 = " + y1);
			int x2 = in.nextInt();
			int y2 = in.nextInt();
			shapes.add(new Line2D.Double(x1, y1, x2, y2));
			myCanvas.repaint();
		}

	}

	void mainCycle() {
		colors[rnd.nextInt(colors.length)][rnd.nextInt(colors[0].length)] = Color.BLUE;
		myCanvas.repaint();
	}

	void realSolve() throws IOException {
		Connections.expect("LOGIN");
		printLogin();
		Connections.expect("PASS");
		printPass();
		Connections.expect("OK");
		boolean needMainCycle = true;
		while (true) {
			try {
				if (needMainCycle) {
					mainCycle();
					Connections.sendToServer("WAIT");
					Connections.expect("OK");
				}
				Connections.Event e = Connections.getNextEvent();
				if (e.fromStdIn) {
					needMainCycle = false;
					if (e.line.startsWith("line")) {
						drawLine(e.tokenizer);
					} else {
						// Connections.sendToServer(e.line);
					}
				} else {
					needMainCycle = true;
					if (e.line.equals("OK")) {
						continue;
					}
					System.err.println("WTF? Got something strange: " + e.line);
				}
			} catch (Exception e) {
				System.err.println("some exception : " + e.getMessage() + ", "
						+ Arrays.toString(e.getStackTrace()));
			}
		}
	}

	ArrayList<Shape> shapes = new ArrayList<>();

	void solve() {
		myCanvas = new MyCanvas();
		myCanvas.setDoubleBuffered(true);
		add("Center", myCanvas);
		for (int i = 0; i < colors.length; i++) {
			for (int j = 0; j < colors[i].length; j++) {
				colors[i][j] = Color.GREEN;
			}
		}
		setSize(700, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setAlwaysOnTop(true);

		myCanvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.err.println("mouse clicked: " + e.getX() + " "
						+ e.getY());
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});
		myCanvas.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				myCanvas.repaint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
		try {
			Connections.createConnections();
			realSolve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Main().solve();
	}

	int mouseX, mouseY;

	Random rnd = new Random(123);
	long lastRepaintTime = 0;

	void drawText(Graphics g, String s, int x, int y) {
		Color textColor = Color.BLACK;
		Color bgColor = Color.WHITE;

		FontMetrics fm = g.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(s, g);

		g.setColor(bgColor);
		g.fillRect(x, y - fm.getAscent(), (int) rect.getWidth(),
				(int) rect.getHeight());

		g.setColor(textColor);
		g.drawString(s, x, y);
	}

	Color[][] colors = new Color[100][200];

	class MyCanvas extends JPanel {

		public void paint(Graphics graphics) {
			Graphics2D g = (Graphics2D) graphics;

			int width = myCanvas.getWidth(), height = myCanvas.getHeight();
			int[] pixels = new int[width * height];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int rI = i * colors.length / height;
					int rJ = j * colors[0].length / width;
					g.setBackground(colors[rI][rJ]);
					pixels[i * width + j] = colors[rI][rJ].getRGB();
				}
			}
			BufferedImage pixelImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			pixelImage.setRGB(0, 0, width, height, pixels, 0, width);
			g.drawImage(pixelImage, 0, 0, null);
			drawText(graphics, "mouse " + mouseX + ", mouseY = " + mouseY, 100,
					100);
		}
	}
}