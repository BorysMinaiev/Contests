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

	class Planet {
		int id, x, y, prom;
		HashSet<Integer> packetsHere;
		boolean used;

		public Planet(int id, int x, int y, int prom) {
			super();
			this.id = id;
			this.x = x;
			this.y = y;
			this.prom = prom;
			packetsHere = new HashSet<>();
		}

		boolean isEmpty() {
			for (int packetId : packetsHere) {
				if (!packetsHave.contains(packetId)) {
					return false;
				}
			}
			return true;
		}

		boolean isInteresting() {
			for (int packetId : packetsHere) {
				if (!packetsHave.contains(packetId)
						&& !packetsOnWay.contains(packetId)) {
					return true;
				}
			}
			return false;
		}
	}

	ArrayList<Planet> allPlanets = new ArrayList<>();

	void getPlanets() {
		sendToServer("GET_PLANETS");
		expectOK();
		int n = nextInt();
		System.err.println("planets " + n);
		allPlanets.clear();
		for (int i = 0; i < n; i++) {
			int id = nextInt();
			int x = nextInt();
			int y = nextInt();
			int prom = nextInt();
			Planet planet = new Planet(id, x, y, prom);
			// System.err.println("r = " + prom);
			// System.err.println(id + " " + x + " " + y + " " + prom);
			allPlanets.add(planet);
			planets.put(id, planet);
		}
	}

	void getMother() {
		sendToServer("GET_MOTHERSHIP");
		expectOK();
		int x = nextInt();
		int y = nextInt();
		// System.err.println("!!! " + x + " " +y);
		mother.x = x;
		mother.y = y;
	}

	Point getMovePoint(int frX, int frY, int toX, int toY, int speed) {
		int dx = toX - frX, dy = toY - frY;
		double dist = Math.sqrt(dx * dx + dy * dy);
		double coef = Math.min(1, speed / dist);
		int realDX = (int) (dx * coef), realDY = (int) (dy * coef);
		return new Point(frX + realDX, frY + realDY);
	}

	void moveMother() {
		int x = 0, y = 0;
		for (Satelite s : satelites) {
			x += s.x;
			y += s.y;
		}
		if (satelites.size() == 0) {
			return;
		}
		x /= satelites.size();
		y /= satelites.size();
		Point next = getMovePoint(mother.x, mother.y, x, y, motherSpeed);
		sendToServer("MOVE_MOTHERSHIP " + next.x + " " + next.y);
		expectOK();
	}

	void moveSatellite(Satelite s, int x, int y) {
		Point next = getMovePoint(s.x, s.y, x, y, s.speed);
		sendToServer("MOVE_SATELLITE " + (s.id) + " " + next.x + " " + next.y);
		expectOK();
	}

	int points, money;

	int sateliteCost, motherSpeed;

	void getConstants() {
		sendToServer("GET_CONSTANTS");
		expectOK();
		sateliteCost = nextInt();
		motherSpeed = nextInt();
		nextInt();
		nextInt();
		nextInt();
		System.err.println("??? " + sateliteCost + "  " + motherSpeed);
	}

	void getPoints() {
		sendToServer("GET_POINTS");
		expectOK();
		points = nextInt();
		money = nextInt();
		System.err.println("points: " + points + ", " + money);
	}

	void getTasks() {
		packetsHave.clear();
		sendToServer("GET_TASKS");
		expectOK();
		int n = nextInt();
		System.err.println(n + " tasks we have!");
		for (int i = 0; i < n; i++) {
			int k = nextInt();
			int perPacketCost = nextInt();
			int perTaskCost = nextInt();
			// System.err.println(k + " packets " + perPacketCost + ", "
			// + perTaskCost);
			int[] ids = new int[k];
			for (int j = 0; j < k; j++) {
				int packetId = nextInt();
				ids[j] = packetId;
				int planetId = nextInt();
				Planet pl = planets.get(planetId);
				pl.packetsHere.add(packetId);
				// System.err.print(packetId + " " + planetId + " ");
			}
			// System.err.println();
			for (int j = 0; j < k; j++) {
				if (nextInt() == 1) {
					packetsHave.add(ids[j]);
				}
			}
			// System.err.println();
		}
	}

	void buySatelites() {
		if (money >= sateliteCost) {
			sendToServer("BUY_SATELLITE");
			expectOK();
		}
	}

	class Satelite implements Comparable<Satelite> {
		int id;
		int x, y;
		int speed;
		int memory;
		int maxSygnal;
		double minSINR;
		int lvlSil, lvlPam, lvlNad, lvlOdb;
		int[] packets;

		public Satelite(int id, int x, int y, int speed, int memory,
				int maxSygnal, double minSINR, int lvlSil, int lvlPam,
				int lvlNad, int lvlOdb) {
			super();
			this.id = id;
			this.x = x;
			this.y = y;
			this.speed = speed;
			this.memory = memory;
			this.maxSygnal = maxSygnal;
			this.minSINR = minSINR;
			this.lvlSil = lvlSil;
			this.lvlPam = lvlPam;
			this.lvlNad = lvlNad;
			this.lvlOdb = lvlOdb;
			packets = new int[memory];
		}

		@Override
		public int compareTo(Satelite o) {
			return Integer.compare(id, o.id);
		}

		boolean hasMemory() {
			for (int id : packets) {
				if (id < 0) {
					return true;
				}
				if (packetsHave.contains(id)) {
					return true;
				}
			}
			return false;
		}

	}

	HashMap<Integer, Point> destinations = new HashMap<>();

	ArrayList<Satelite> satelites = new ArrayList<>();

	void getSatelites() {
		sendToServer("GET_SATELLITES");
		expectOK();
		satelites.clear();
		int n = nextInt();
		for (int i = 0; i < n; i++) {
			Satelite current = new Satelite(nextInt(), nextInt(), nextInt(),
					nextInt(), nextInt(), nextInt(), nextDouble(), nextInt(),
					nextInt(), nextInt(), nextInt());
			for (int j = 0; j < current.memory; j++) {
				current.packets[j] = nextInt();
			}
			satelites.add(current);
		}
	}

	HashMap<Integer, Planet> planets = new HashMap<>();

	boolean okPlanet(Planet p) {
		if (p.used) {
			return false;
		}
		for (int packetId : p.packetsHere) {
			if (packetsHave.contains(packetId)
					|| packetsOnWay.contains(packetId)) {
				continue;
			}
			return true;
		}
		return false;
	}

	void moveSatelites() {
		for (Satelite s : satelites) {
			Point dest = destinations.get(s.id);
			if (dest != null) {
				for (Planet p : allPlanets) {
					if (!p.isInteresting()) {
						continue;
					}
					if (dist(p.x, p.y, dest.x, dest.y) == 0) {
						if (s.hasMemory()) {
							p.used = true;
						} else {
							destinations.remove(s.id);
						}
					}
				}
			}
		}

		for (Satelite s : satelites) {
			Point next = destinations.get(s.id);
			if (next == null) {
				for (int packet : s.packets) {
					if (packet >= 0 && !packetsHave.contains(packet)) {
						destinations.put(s.id, mother);
					}
				}
			}
		}

		for (Satelite s : satelites) {
			Point dest = destinations.get(s.id);
			if (dest == null) {
				int bestDist = Integer.MAX_VALUE;
				Planet bestP = null;
				for (Planet p : allPlanets) {
					if (okPlanet(p)) {
						int dist = dist(p.x, p.y, s.x, s.y);
						if (dist < bestDist) {
							bestDist = dist;
							bestP = p;
						}
					}
				}
				if (bestP != null) {
					dest = new Point(bestP.x, bestP.y);
					bestP.used = true;
				}
			}
			destinations.put(s.id, dest);
			moveSatellite(s, dest.x, dest.y);
		}
	}

	void getPackets(int satId) {
		sendToServer("GET_TRANSMISSIONS " + satId);
		expectOK();
		int n = nextInt();
		for (int i = 0; i < n; i++) {
			int packetId = nextInt();
			int satelliteId = nextInt();
			int someId = nextInt();
			int x = nextInt();
			int y = nextInt();
			double power = nextDouble();
			double SINR = nextDouble();
			System.err.println("----> " + packetId + " " + satelliteId + " "
					+ someId);
		}
	}

	int dist(int x1, int y1, int x2, int y2) {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
	}

	boolean savePacket(Satelite s, int packetId) {
		for (int i = 0; i < s.packets.length; i++) {
			if (s.packets[i] == -1 || packetsHave.contains(s.packets[i])) {
				sendToServer("RECEIVE_PACKET " + s.id + " " + packetId + " "
						+ i);
				s.packets[i] = -2;
				expectOK();
				return true;
			}
			if (s.packets[i] == -2) {
				return false;
			}
		}
		return false;
	}

	void getPacketsFromPlanets() {
		for (Satelite s : satelites) {
			for (Planet p : allPlanets) {
				if (dist(s.x, s.y, p.x, p.y) <= p.prom * p.prom) {
					for (int packetId : p.packetsHere) {
						if (packetsOnWay.contains(packetId)) {
							continue;
						}
						if (packetsHave.contains(packetId)) {
							continue;
						}
						if (savePacket(s, packetId)) {
							packetsOnWay.add(packetId);
							System.err.println("total siz = "
									+ packetsOnWay.size());
						}
					}
				}
			}
		}
	}

	ArrayList<Integer> packetsSubmitedOnPrevTurn = new ArrayList<>();

	void getPacketsMain() {
		for (int id : packetsSubmitedOnPrevTurn) {
			sendToServer("DELIVER_PACKET " + id);
			expectOK();
		}
	}

	void sendPacketsToMain() {
		packetsSubmitedOnPrevTurn.clear();
		for (Satelite s : satelites) {
			if (dist(s.x, s.y, mother.x, mother.y) <= 100) {
				{
					System.err.println("send to main!");
					for (int i = 0; i < s.packets.length; i++) {
						if (s.packets[i] >= 0
								&& !packetsHave.contains(s.packets[i])) {
							System.err.println(" ID = " + s.packets[i]);
							sendToServer("TRANSMIT_PACKET " + s.id + " "
									+ s.packets[i] + " " + s.maxSygnal);
							expectOK();
							packetsSubmitedOnPrevTurn.add(s.packets[i]);
							break;
						}
					}
				}
			}
		}
	}

	HashSet<Integer> packetsHave = new HashSet<>();
	HashSet<Integer> packetsOnWay = new HashSet<>();

	void mainCycle() {
		turnsLeft();
		getPlanets();
		getMother();
		getConstants();
		getTasks();
		getPoints();
		getSatelites();
		getPacketsMain();
		sendPacketsToMain();
		for (Satelite s : satelites) {
			getPackets(s.id);
		}
		getPacketsFromPlanets();
		buySatelites();
		moveSatelites();
		moveMother();
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

	class Point {
		int x, y;

		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		public Point(double x, double y) {
			this.x = (int) x;
			this.y = (int) y;
		}
	}

	Point mother = new Point(0, 0);
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
			for (Planet p : allPlanets) {
				// updateScreenSize(p.x, p.y);
			}
			updateScreenSize(mother.x, mother.y);
			for (Satelite s : satelites) {
				updateScreenSize(s.x, s.y);
			}
			coef = Math.min(height * 1. / (maxX - minX), width * 1.
					/ (maxY - minY));
			for (Planet p : allPlanets) {
				drawCircle(p.x, p.y, 5, p.isEmpty() ? Color.YELLOW.getRGB()
						: Color.BLACK.getRGB());
			}
			for (Satelite s : satelites) {
				drawCircle(s.x, s.y, 5, Color.BLUE.getRGB());
			}
			drawCircle(mother.x, mother.y, 5, Color.RED.getRGB());
			BufferedImage pixelImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			pixelImage.setRGB(0, 0, width, height, pixels, 0, width);
			g.drawImage(pixelImage, 0, 0, null);

			for (Satelite s : satelites) {
				Point p = convertPoint(s.x, s.y);
				// System.err.println("(" + s.x + ", " + s.y + ") -> (" + p.x
				// + ", " + p.y + ")");
				drawText(graphics, s.id + "", p.y, p.x, false);
			}

			Point mouse = getPointFromRealCoord(mouseX, mouseY);
			drawText(graphics, "money = " + money + ", pts = " + points
					+ ", have packets = " + packetsHave.size()
					+ ", satellites = " + satelites.size() + ", turns left = "
					+ turnsLeft, 100, 100, false);
		}
	}
}