package SocketGame;

import static SocketGame.Connections.expectOK;
import static SocketGame.Connections.nextDouble;
import static SocketGame.Connections.nextInt;
import static SocketGame.Connections.sendToServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Strategy {

	public static HashSet<Integer> packetsHave = new HashSet<>();
	public static HashSet<Integer> packetsOnWay = new HashSet<>();
	public static Point mother = new Point(0, 0);
	static HashMap<Integer, Point> destinations = new HashMap<>();
	static ArrayList<Satelite> satelites = new ArrayList<>();
	static ArrayList<Integer> packetsSubmitedOnPrevTurn = new ArrayList<>();
	static HashMap<Integer, Planet> planets = new HashMap<>();
	static ArrayList<Planet> allPlanets = new ArrayList<>();
	static HashSet<Integer> allPacketsIds = new HashSet<>();
	static int points, money;
	static int sateliteCost;
	static int motherSpeed;

	static void getPlanets() {
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

	static void getMother() {
		sendToServer("GET_MOTHERSHIP");
		expectOK();
		int x = nextInt();
		int y = nextInt();
		// System.err.println("!!! " + x + " " +y);
		mother.x = x;
		mother.y = y;
	}

	static Point getMovePoint(int frX, int frY, int toX, int toY, int speed) {
		int dx = toX - frX, dy = toY - frY;
		double dist = Math.sqrt(dx * dx + dy * dy);
		double coef = Math.min(1, speed / dist);
		int realDX = (int) (dx * coef), realDY = (int) (dy * coef);
		return new Point(frX + realDX, frY + realDY);
	}

	static void moveMother() {
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

	static void moveSatellite(Satelite s, int x, int y) {
		Point next = getMovePoint(s.x, s.y, x, y, s.speed);
		sendToServer("MOVE_SATELLITE " + (s.id) + " " + next.x + " " + next.y);
		expectOK();
	}

	static void getConstants() {
		sendToServer("GET_CONSTANTS");
		expectOK();
		sateliteCost = nextInt();
		motherSpeed = nextInt();
		nextInt();
		nextInt();
		nextInt();
		System.err.println("??? " + sateliteCost + "  " + motherSpeed);
	}

	static void getPoints() {
		sendToServer("GET_POINTS");
		expectOK();
		points = nextInt();
		money = nextInt();
		System.err.println("points: " + points + ", " + money);
	}

	static void getTasks() {
		packetsHave.clear();
		allPacketsIds.clear();
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
				allPacketsIds.add(packetId);
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

	static void buySatelites() {
		if (money >= sateliteCost) {
			sendToServer("BUY_SATELLITE");
			expectOK();
		}
	}

	static void getSatelites() {
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

	static boolean okPlanet(Planet p) {
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

	static void moveSatelites() {
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

	static void getPackets(int satId) {
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

	static int dist(int x1, int y1, int x2, int y2) {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
	}

	static boolean savePacket(Satelite s, int packetId) {
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

	static void getPacketsFromPlanets() {
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

	static void getPacketsMain() {
		for (int id : packetsSubmitedOnPrevTurn) {
			sendToServer("DELIVER_PACKET " + id);
			expectOK();
		}
	}

	static void sendPacketsToMain() {
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

	public static void move() {
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
	}
}
