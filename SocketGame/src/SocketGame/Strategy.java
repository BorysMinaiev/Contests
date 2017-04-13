package SocketGame;

import static SocketGame.Connections.expect;
import static SocketGame.Connections.expectOK;
import static SocketGame.Connections.nextDouble;
import static SocketGame.Connections.nextInt;
import static SocketGame.Connections.sendToServer;

import java.util.ArrayList;
import java.util.Collections;
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
	static ArrayList<Point> others = new ArrayList<>();
	static int scanerTime;
	static int turnsLeft;
	static int commandsSend;

	static void getPlanets() {
		sendToServer("GET_PLANETS");
		if (!expectOK()) {
			return;
		}
		int n = nextInt();
		System.err.println("planets " + n);
		allPlanets.clear();
		for (int i = 0; i < n; i++) {
			int id = nextInt();
			int x = nextInt();
			int y = nextInt();
			int prom = nextInt();
			Planet planet = new Planet(id, x, y, prom);
			allPlanets.add(planet);
			planets.put(id, planet);
		}
	}

	static void getMother() {
		sendToServer("GET_MOTHERSHIP");
		if (!expectOK()) {
			return;
		}
		int x = nextInt();
		int y = nextInt();
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
		if (!expectOK()) {
			return;
		}
	}

	static void moveSatellite(Satelite s, int x, int y) {
		Point next = getMovePoint(s.x, s.y, x, y, s.speed);
		sendToServer("MOVE_SATELLITE " + (s.id) + " " + next.x + " " + next.y);
		if (!expectOK()) {
			return;
		}
	}

	static void getConstants() {
		sendToServer("GET_CONSTANTS");
		if (!expectOK()) {
			return;
		}
		sateliteCost = nextInt();
		motherSpeed = nextInt();
		nextInt();
		scanerTime = nextInt();
		nextInt();
		System.err.println("??? " + sateliteCost + "  " + motherSpeed);
	}

	static void getPoints() {
		sendToServer("GET_POINTS");
		if (!expectOK()) {
			return;
		}
		points = nextInt();
		money = nextInt();
		System.err.println("points: " + points + ", " + money);
	}

	public static int tasksTotal, tasksDone;

	static HashMap<Integer, Integer> tasksCost = new HashMap<>();

	static void getTasks() {
		sendToServer("GET_TASKS");
		if (!expectOK()) {
			return;
		}
		tasksCost.clear();
		Connections.roundInfo.println("GET TASKS:");
		packetsHave.clear();
		allPacketsIds.clear();
		int n = nextInt();
		tasksTotal = n;
		tasksDone = 0;
		System.err.println(n + " tasks we have!");
		for (int i = 0; i < n; i++) {
			int k = nextInt();
			int perPacketCost = nextInt();
			int perTaskCost = nextInt();
			Connections.roundInfo.print("packet cost = " + perPacketCost
					+ ", task = " + perTaskCost);
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
			int sum = 0;
			boolean[] done = new boolean[k];
			for (int j = 0; j < k; j++) {
				done[j] = nextInt() == 1;
				if (done[j]) {
					packetsHave.add(ids[j]);
					sum++;
				}
			}
			if (sum == k) {
				tasksDone++;
			}
			for (int j = 0; j < k; j++) {
				if (!done[j]) {
					int addCost = perPacketCost;
					if (satelites.size() > 10) addCost += 5 * perTaskCost / (k - sum) ;
					Integer now = tasksCost.get(ids[j]);
					tasksCost.put(ids[j], addCost + (now == null ? 0 : now));
					// Connections.roundInfo.println("TASK COST " + ids[j] + " "
					// + tasksCost.get(ids[j]));
				}
			}
			Connections.roundInfo.println(" : " + sum + "/" + k);
			System.err.println("task done: " + (sum) + "/" + k);
			// System.err.println();
		}
		Connections.roundInfo.flush();
	}

	static void buySatelites() {
		if (money >= sateliteCost) {
			sendToServer("BUY_SATELLITE");
			if (!expectOK()) {
				return;
			}
		}
	}

	static void turnsLeft() {
		sendToServer("TURNS_LEFT");
		expect("OK");
		turnsLeft = nextInt();
		int a = nextInt();
		int b = nextInt();
		// System.err.println(tot + " " + a + " " + b);

	}

	static void getOthers() {
		if (turnsLeft % scanerTime == 0) {
			others.clear();
			sendToServer("SCAN");
			expectOK();
			int n = nextInt();
			for (int i = 0; i < n; i++) {
				int x = nextInt();
				int y = nextInt();
				others.add(new Point(x, y));
			}
		}
	}

	static void scan() {
		turnsLeft();
		getOthers();
	}

	static void getSatelites() {
		sendToServer("GET_SATELLITES");
		if (!expectOK()) {
			return;
		}
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

	static double getPlanetScore(Planet p, Satelite s) {
		ArrayList<Integer> costs = new ArrayList<>();
		for (int packetId : p.packetsHere) {
			Integer c = tasksCost.get(packetId);
			if (c != null) {
				costs.add(c);
			}
		}
		Collections.sort(costs);
		double totalCost = 0;
		for (int i = 0; i < costs.size() && i < s.memory; i++) {
			totalCost += costs.get(costs.size() - i - 1);
		}
		double dist = 2
				* Math.floor(Math.sqrt(dist(p.x, p.y, s.x, s.y)) / s.speed + 1)
				+ Math.min(costs.size(), s.memory);
		totalCost /= dist;
		return totalCost;
	}

	static void moveSatelites() {
		if (commandsSend > 90) {
			return;
		}
		for (Satelite s : satelites) {
			Point dest = destinations.get(s.id);
			if (dest != null) {
				for (Planet p : allPlanets) {
					if (dist(p.x, p.y, dest.x, dest.y) == 0) {
						if (s.hasMemory() && p.isInteresting()) {
							p.used = true;
						} else {
							destinations.remove(s.id);
							break;
						}
					}
				}
			}
		}

		for (Satelite s : satelites) {
			Point dest = destinations.get(s.id);
			if (dest != null) {
				boolean moveToMother = dist(dest.x, dest.y, mother.x, mother.y) <= 50;
				if (moveToMother && !s.needToMother()) {
					destinations.remove(s.id);
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
				double bestDist = 0;
				Planet bestP = null;
				for (Planet p : allPlanets) {
					if (okPlanet(p)) {
						double dist = getPlanetScore(p, s);
						if (dist > bestDist) {
							bestDist = dist;
							bestP = p;
						}
					}
				}
				if (bestP != null) {
					Connections.roundInfo.println("best score " + bestDist);
					dest = new Point(bestP.x, bestP.y);
					bestP.used = true;
				}
			}
			if (dest != null) {
				destinations.put(s.id, dest);
				moveSatellite(s, dest.x, dest.y);
			}
		}
	}

	static void getPackets(int satId) {
		sendToServer("GET_TRANSMISSIONS " + satId);
		if (!expectOK()) {
			return;
		}
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
				if (!expectOK()) {
					return false;
				}
				return true;
			}
			if (s.packets[i] == -2) {
				return false;
			}
		}
		return false;
	}

	static class Get implements Comparable<Get> {
		int packetId;
		double cost;

		public Get(int packetId, double cost) {
			super();
			this.packetId = packetId;
			this.cost = cost;
		}

		@Override
		public int compareTo(Get o) {
			return Double.compare(o.cost, cost);
		}

	}

	static void getPacketsFromPlanets() {
		for (Satelite s : satelites) {
			ArrayList<Get> all = new ArrayList<>();
			for (Planet p : allPlanets) {
				if (dist(s.x, s.y, p.x, p.y) <= p.prom * p.prom) {
					for (int packetId : p.packetsHere) {
						if (packetsOnWay.contains(packetId)) {
							continue;
						}
						if (packetsHave.contains(packetId)) {
							continue;
						}
						Integer co = tasksCost.get(packetId);
						if (co != null) {
							all.add(new Get(packetId, co));
						}
					}
				}
			}
			Collections.sort(all);
			if (all.size() != 0) {
				if (savePacket(s, all.get(0).packetId)) {
					packetsOnWay.add(all.get(0).packetId);
				}
			}
		}
	}

	static void getPacketsMain() {
		for (int id : packetsSubmitedOnPrevTurn) {
			if (packetsHave.contains(id)) {
				continue;
			}
			sendToServer("DELIVER_PACKET " + id);
			if (!expectOK()) {
				return;
			}
		}
	}

	static void sendPacketsToMain() {
		packetsSubmitedOnPrevTurn.clear();
		for (Satelite s : satelites) {
			if (dist(s.x, s.y, mother.x, mother.y) <= 200) {
				for (int i = 0; i < s.packets.length; i++) {
					if (s.packets[i] >= 0
							&& !packetsHave.contains(s.packets[i])) {
						sendToServer("TRANSMIT_PACKET " + s.id + " "
								+ s.packets[i] + " " + s.maxSygnal);
						if (!expectOK()) {
							return;
						}
						packetsSubmitedOnPrevTurn.add(s.packets[i]);
						break;
					}
				}

			}
		}
	}

	static int updateMemoryCost = -1;

	static void getUpdates() {
		sendToServer("GET_UPGRADES");
		expectOK();
		int n = 4;
		for (int i = 0; i < n; i++) {
			String s = Connections.nextToken();
			Connections.roundInfo.print(s + " ");
			int k = nextInt();
			for (int j = 0; j < k + 1; j++) {
				double value = nextDouble();
				Connections.roundInfo.print(value + " ");
			}
			for (int j = 0; j < k; j++) {
				int cost = nextInt();
				if (i == 1 && j == 1) {
					updateMemoryCost = cost;
				}
				Connections.roundInfo.print(cost + " ");
			}
			Connections.roundInfo.println();
		}
		Connections.roundInfo.flush();
	}

	public static void updateSatelites() {
		for (Satelite s : satelites) {
			if (s.lvlPam == 0 && money >= updateMemoryCost) {
				money -= updateMemoryCost;
				sendToServer("UPGRADE_SATELLITE " + s.id + " memory " + 1);
				expectOK();
			}
			// Connections.roundInfo.println("LVL = " + s.lvlPam);
		}
	}

	static long last = 0;

	public static void move() {
		long cur = System.currentTimeMillis();
		if (cur - last < 100) {
			return;
		}
		last = cur;
		commandsSend = 0;
		getPlanets();
		getMother();
		getConstants();
		getUpdates();
		scan();
		getTasks();
		getPoints();
		getSatelites();
		updateSatelites();
		getPacketsMain();
		sendPacketsToMain();
		// for (Satelite s : satelites) {
		// getPackets(s.id);
		// }
		getPacketsFromPlanets();
		buySatelites();
		moveSatelites();
		moveMother();
		Connections.roundInfo.println("sent commands: " + commandsSend);
	}
}
