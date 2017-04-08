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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import javax.jws.Oneway;
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

	void turnsLeft() {
		sendToServer("TURNS_LEFT");
		expect("OK");
		turnsLeft = nextInt();
		int a = nextInt();
		int b = nextInt();
		// System.err.println(tot + " " + a + " " + b);

	}

	void mainCycle() {
		turnsLeft();
		Strategy.move();
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
				motherDest = getPointFromRealCoord(mouseX, mouseY);
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
				mouseX = e.getY();
				mouseY = e.getX();
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

	void drawText(Graphics g, String s, int x, int y, boolean needGround) {
		Color textColor = Color.BLACK;
		Color bgColor = Color.WHITE;

		FontMetrics fm = g.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(s, g);

		g.setColor(bgColor);
		if (needGround) {
			g.fillRect(x, y - fm.getAscent(), (int) rect.getWidth(),
					(int) rect.getHeight());
		}
		g.setColor(textColor);
		g.drawString(s, x, y);
	}

	Color[][] colors = new Color[100][200];

	int width, height;
	int[] pixels;
	double coef;

	Point convertPoint(int rx, int ry) {
		return new Point((int) ((rx - minX) * coef), (int) ((ry - minY) * coef));
	}

	void drawCircle(int rx, int ry, int r, int color) {
		Point p = convertPoint(rx, ry);
		int x = p.x, y = p.y;
		for (int nx = x - r; nx <= x + r; nx++) {
			for (int ny = y - r; ny <= y + r; ny++) {
				if (nx >= 0 && nx < height && ny >= 0 && ny < width) {
					if ((x - nx) * (x - nx) + (y - ny) * (y - ny) <= r * r) {
						pixels[nx * width + ny] = color;
					}
				}
			}
		}
	}

	int minX, maxX, minY, maxY;

	Point motherDest = new Point(0, 0);

	final int ADD_SIZE = 600;

	void updateScreenSize(int x, int y) {
		minX = Math.min(minX, x - ADD_SIZE);
		maxX = Math.max(maxX, x + ADD_SIZE);
		minY = Math.min(minY, y - ADD_SIZE);
		maxY = Math.max(maxY, y + ADD_SIZE);
	}

	Point getPointFromRealCoord(int x, int y) {
		return new Point(x / coef + minX, y / coef + minY);
	}

	int turnsLeft;

	class MyCanvas extends JPanel {
		public void paint(Graphics graphics) {
			Graphics2D g = (Graphics2D) graphics;

			width = myCanvas.getWidth();
			height = myCanvas.getHeight();
			pixels = new int[width * height];
			Arrays.fill(pixels, Color.WHITE.getRGB());
			minX = Integer.MAX_VALUE;
			minY = Integer.MAX_VALUE;
			maxX = Integer.MIN_VALUE;
			maxY = Integer.MIN_VALUE;
			for (Planet p : Strategy.allPlanets) {
				// updateScreenSize(p.x, p.y);
			}
			updateScreenSize(Strategy.mother.x, Strategy.mother.y);
			for (Satelite s : Strategy.satelites) {
				updateScreenSize(s.x, s.y);
			}
			coef = Math.min(height * 1. / (maxX - minX), width * 1.
					/ (maxY - minY));
			for (Planet p : Strategy.allPlanets) {
				drawCircle(p.x, p.y, 5, p.isEmpty() ? Color.YELLOW.getRGB()
						: Color.BLACK.getRGB());
			}
			for (Satelite s : Strategy.satelites) {
				drawCircle(s.x, s.y, 5, Color.BLUE.getRGB());
			}
			drawCircle(Strategy.mother.x, Strategy.mother.y, 5,
					Color.RED.getRGB());
			BufferedImage pixelImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			pixelImage.setRGB(0, 0, width, height, pixels, 0, width);
			g.drawImage(pixelImage, 0, 0, null);

			for (Satelite s : Strategy.satelites) {
				Point p = convertPoint(s.x, s.y);
				// System.err.println("(" + s.x + ", " + s.y + ") -> (" + p.x
				// + ", " + p.y + ")");
				drawText(graphics, s.id + "", p.y, p.x, false);
			}

			Point mouse = getPointFromRealCoord(mouseX, mouseY);
			drawText(graphics,
					"money = " + Strategy.money + ", pts = " + Strategy.points
							+ ", have packets = " + Strategy.packetsHave.size()
							+ "/" + Strategy.allPacketsIds.size()
							+ ", satellites = " + Strategy.satelites.size()
							+ ", turns left = " + turnsLeft, 100, 100, false);
		}
	}
}