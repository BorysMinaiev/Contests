import java.io.*;
import java.util.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class SolveNew2 {
	FastScanner in;
	PrintWriter out;

	int r;
	char[][] a;

	final static boolean printPicture = true;

	final int OUTER_ITER = 1000;
	final int INNER_ITER = 30;

	void solve(int addRouters) {
		Random rnd = new Random(222);
		n = in.nextInt();
		m = in.nextInt();
		r = in.nextInt();
		System.err.println(n + " " + m + " " + r);
		int backbone = in.nextInt();
		int router = in.nextInt();
		System.err.println(backbone + " " + router);
		int budget = in.nextInt();
		System.err.println("money " + budget);
		int stX = in.nextInt(), stY = in.nextInt();
		out.println(stX + " " + stY);
		a = new char[n][];
		for (int i = 0; i < n; i++) {
			a[i] = in.next().toCharArray();
			for (int j = 0; j < m; j++) {
				if (a[i][j] == '-') {
					a[i][j] = '#';
				}
			}
		}

		int totalPoints = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (a[i][j] == '.') {
					totalPoints++;
				}
			}
		}
		System.err.println("total Points = " + totalPoints);
		int routersNumMax = budget / (r * 2 * backbone + router) + addRouters;
		System.err.println("routers num ~" + routersNumMax);
		System.err.println("want to cover " + (totalPoints / routersNumMax)
				+ " by each router (r = " + r + ", max = " + (2 * r + 1)
				* (2 * r + 1) + ")");
		System.err.println();

		routersUsed = 0;
		coveredByRouters = 0;
		calcCoverLeftRight();

		ArrayList<Answer> answers = new ArrayList<SolveNew2.Answer>();
		for (int ITER = 0; ITER < OUTER_ITER; ITER++) {
			System.err.println("I = " + ITER);
			canPut = routersNumMax;
			for (int want = (2 * r + 1) * (2 * r + 1); want > 0; want--) {
				// System.err.println("want " + want);
				for (int i = 0; i < n; i++) {
					if (rnd.nextBoolean()) {
						for (int j = 0; j < m; j++) {
							do_it(i, j, want);
						}
					} else {
						for (int j = m - 1; j >= 0; j--) {
							do_it(i, j, want);
						}
					}
				}
			}
			Answer curAnswer = genAnswer();
			answers.add(curAnswer);

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (routerHere[i][j]) {
						notUseXY(i, j);
					}
				}
			}
		}
		Collections.sort(answers);
		int max = 0;
		Answer best = null;
		for (int ITER = 0; ITER < answers.size() && ITER < INNER_ITER; ITER++) {
			Answer curAnswer = answers.get(ITER);
			System.err.println("iter = " + ITER);
			for (Point p : curAnswer.points) {
				useXY(p.x, p.y);
			}
			while (firstLocalOpt() || secondLocalOpt()) {
			}

			max = Math.max(max, coveredByRouters);
			System.err.println("!!!!!!!!!!!              score = "
					+ coveredByRouters + ", max = " + max);
			if (coveredByRouters == max) {
				best = genAnswer();
			}

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (routerHere[i][j]) {
						notUseXY(i, j);
					}
				}
			}
		}

		for (Point p : best.points) {
			useXY(p.x, p.y);
		}

		out.println(budget - routersUsed * router);
		out.println(routersUsed);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (routerHere[i][j]) {
					out.println(i + " " + j);
				}
			}
		}

		if (printPicture) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (routerHere[i][j]) {
						out.print('*');
					} else {
						out.print(a[i][j]);
					}
				}
				out.println();
			}
		}
		out.close();
		System.err.println("tot = " + totalPoints + ", covered = "
				+ coveredByRouters);
		System.err.println("used routers " + routersUsed);
		System.err.println("used money " + (routersUsed * router)
				+ ", budget = " + budget);

		System.err.println("max score: " + (totalPoints * 1000 + budget));

		if (routersUsed * router > budget) {
			System.err
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			// throw new AssertionError();
		}

	}

	Answer genAnswer() {
		Answer curAnswer = new Answer(coveredByRouters);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (routerHere[i][j]) {
					curAnswer.points.add(new Point(i, j));
				}
			}
		}
		return curAnswer;
	}

	class Point {
		int x, y;

		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

	}

	class Answer implements Comparable<Answer> {
		int score;
		ArrayList<Point> points;

		public Answer(int score) {
			super();
			this.score = score;
			this.points = new ArrayList<SolveNew2.Point>();

		}

		public int compareTo(Answer o) {
			return Integer.compare(o.score, score);
		}

	}

	int canPut;

	void do_it(int i, int j, int want) {
		int willCover = cntCover[i][j];
		if (willCover >= want && canPut > 0) {
			// System.err.println("put " + i + " " + j + "-> " + want);
			canPut--;
			useXY(i, j);
		}
	}

	int n, m;

	boolean firstLocalOpt() {
		boolean allCh = false;
		while (true) {
			System.err.println("now " + coveredByRouters);
			boolean changed = false;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (routerHere[i][j]) {
						int best = 0;
						int bDX = 0, bDY = 0;
						int wasCovered = coveredByRouters;
						notUseXY(i, j);
						for (int di = -1; di < 2; di++) {
							for (int dj = -1; dj < 2; dj++) {
								useXY(i + di, j + dj);
								int add = coveredByRouters - wasCovered;
								if (add > best) {
									best = add;
									bDX = di;
									bDY = dj;
								}
								notUseXY(i + di, j + dj);
							}
						}
						if (best > 0) {
							useXY(i + bDX, j + bDY);
							// System.err.println("-> " + coveredByRouters);
							changed = true;
						} else {
							useXY(i, j);
						}
					}
				}
			}
			if (!changed) {
				break;
			} else {
				allCh = true;
			}
		}
		return allCh;
	}

	class P implements Comparable<P> {
		int x, y, score;

		public P(int x, int y, int score) {
			super();
			this.x = x;
			this.y = y;
			this.score = score;
		}

		public int compareTo(P o) {
			return Integer.compare(score, o.score);
		}

	}

	boolean secondLocalOpt() {
		boolean allCh = false;
		while (true) {
			ArrayList<P> check = new ArrayList<SolveNew2.P>();
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (routerHere[i][j]) {
						notUseXY(i, j);
						check.add(new P(i, j, cntCover[i][j]));
						useXY(i, j);
					}
				}
			}
			Collections.sort(check);
			boolean ch = false;
			for (int id = 0; id < 10 && id < check.size(); id++) {
				int min = check.get(id).score;
				System.err.println("id = " + id + ", min = " + min);
				int bI = check.get(id).x;
				int bJ = check.get(id).y;
				notUseXY(bI, bJ);
				int putBest = 0;
				int putI = -1, putJ = -1;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						if (cntCover[i][j] > putBest) {
							putBest = cntCover[i][j];
							putI = i;
							putJ = j;
						}
					}
				}
				if (putBest > min) {
					// System.err.println(min + "----> " + putBest + "  id = "
					// + id);
					ch = true;
					useXY(putI, putJ);
					break;
				} else {
					useXY(bI, bJ);
				}
			}
			if (!ch) {
				break;
			}
			allCh = true;
			System.err.println("optimized -> " + coveredByRouters);
		}
		return allCh;
	}

	Router[][] routersXY;

	void updateNearbyRouters(int i, int j) {
		for (int nx = i - r; nx <= i + r; nx++) {
			for (int ny = j - coverLeft[i][j][r + nx - i] + 1; ny < j
					+ coverRight[i][j][r + nx - i]; ny++) {
				if (routersXY[nx][ny] != null) {
					routersXY[nx][ny].updateCover();
				}
			}
		}
	}

	class Router implements Comparable<Router> {
		int i, j;
		int cover;

		public Router(int x, int y) {
			super();
			this.i = x;
			this.j = y;
		}

		public int compareTo(Router o) {
			return Integer.compare(cover, o.cover);
		}

		void removeRouter() {
			routerHere[i][j] = false;
			routersUsed--;
			for (int nx = i - r; nx <= i + r; nx++) {
				for (int ny = j - coverLeft[i][j][r + nx - i] + 1; ny < j
						+ coverRight[i][j][r + nx - i]; ny++) {
					coveredByXRoutes[nx][ny]--;
					if (coveredByXRoutes[nx][ny] == 1) {
						updateNearbyRouters(nx, ny);
					}
					if (coveredByXRoutes[nx][ny] == 0) {
						coveredByRouters--;
					}
				}
			}
		}

		void updateCover() {
			cover = 0;
			for (int nx = i - r; nx <= i + r; nx++) {
				for (int ny = j - coverLeft[i][j][r + nx - i] + 1; ny < j
						+ coverRight[i][j][r + nx - i]; ny++) {
					if (coveredByXRoutes[nx][ny] == 1) {
						cover++;
					}
				}
			}
		}

	}

	int routersUsed = 0;
	int coveredByRouters = 0;

	boolean[][] routerHere;
	int[][] cntCover;
	int[][][] coverLeft;
	int[][][] coverRight;
	int[][] coveredByXRoutes;

	void calcCoverLeftRight() {
		coverLeft = new int[n][m][2 * r + 1];
		coverRight = new int[n][m][2 * r + 1];
		routerHere = new boolean[n][m];
		coveredByXRoutes = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				coverLeft[i][j][r] = 1 + (j == 0 ? 0 : coverLeft[i][j - 1][r]);
				if (i != 0) {
					for (int dx = 1; dx <= r; dx++) {
						coverLeft[i][j][r - dx] = Math.min(coverLeft[i][j][r],
								coverLeft[i - 1][j][r - dx + 1]);
					}
				}
				for (int dx = 0; dx <= 2 * r; dx++) {
					if (a[i][j] == '#') {
						coverLeft[i][j][dx] = 0;
					} else {
						coverLeft[i][j][dx] = Math.min(coverLeft[i][j][dx],
								r + 1);
					}
				}

			}
			for (int j = m - 1; j >= 0; j--) {
				coverRight[i][j][r] = 1 + (j == m - 1 ? 0
						: coverRight[i][j + 1][r]);
				if (i != 0) {
					for (int dx = 1; dx <= r; dx++) {
						coverRight[i][j][r - dx] = Math.min(
								coverRight[i][j][r], coverRight[i - 1][j][r
										- dx + 1]);
					}
				}
				for (int dx = 0; dx <= 2 * r; dx++) {
					if (a[i][j] == '#') {
						coverRight[i][j][dx] = 0;
					} else {
						coverRight[i][j][dx] = Math.min(coverRight[i][j][dx],
								r + 1);
					}
				}
			}
		}
		for (int i = n - 1; i >= 0; i--) {
			for (int j = 0; j < m; j++) {
				if (i != n - 1) {
					for (int dx = 1; dx <= r; dx++) {
						coverLeft[i][j][r + dx] = Math.min(coverLeft[i][j][r],
								coverLeft[i + 1][j][r + dx - 1]);
						coverRight[i][j][r + dx] = Math.min(
								coverRight[i][j][r], coverRight[i + 1][j][r
										+ dx - 1]);
					}
				}
				for (int dx = 0; dx <= 2 * r; dx++) {
					if (a[i][j] == '#') {
						coverLeft[i][j][dx] = 0;
						coverRight[i][j][dx] = 0;
					} else {
						coverLeft[i][j][dx] = Math.min(coverLeft[i][j][dx],
								r + 1);
						coverRight[i][j][dx] = Math.min(coverRight[i][j][dx],
								r + 1);
					}
				}
			}
		}
		cntCover = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				for (int dx = -r; dx <= r; dx++) {
					int sz = Math.max(0, coverLeft[i][j][r + dx]
							+ coverRight[i][j][r + dx] - 1);
					cntCover[i][j] += sz;
				}
			}
		}
	}

	void wasCovered(int i, int j) {
		if (a[i][j] != '.' && a[i][j] != '+') {
			return;
		}
		coveredByXRoutes[i][j]++;
		if (a[i][j] == '+') {
			return;
		}
		coveredByRouters++;
		// System.err.println("color " + i + " " + j);
		a[i][j] = '+';
		for (int nx = i - r; nx <= i + r; nx++) {
			for (int ny = j - coverLeft[i][j][r + nx - i] + 1; ny < j
					+ coverRight[i][j][r + nx - i]; ny++) {
				cntCover[nx][ny]--;
			}
		}
	}

	void wasNotCovered(int i, int j) {
		if (a[i][j] != '.' && a[i][j] != '+') {
			return;
		}
		coveredByXRoutes[i][j]--;
		if (coveredByXRoutes[i][j] != 0) {
			return;
		}
		coveredByRouters--;
		a[i][j] = '.';
		for (int nx = i - r; nx <= i + r; nx++) {
			for (int ny = j - coverLeft[i][j][r + nx - i] + 1; ny < j
					+ coverRight[i][j][r + nx - i]; ny++) {
				cntCover[nx][ny]++;
			}
		}
	}

	void useXY(int i, int j) {
		// System.err
		// .println("use " + i + ", " + j + " covered " + cntCover[i][j]);
		routerHere[i][j] = true;
		routersUsed++;
		for (int nx = i - r; nx <= i + r; nx++) {
			for (int ny = j - coverLeft[i][j][r + nx - i] + 1; ny < j
					+ coverRight[i][j][r + nx - i]; ny++) {
				wasCovered(nx, ny);
			}
		}
	}

	void notUseXY(int i, int j) {
		routerHere[i][j] = false;
		routersUsed--;
		for (int nx = i - r; nx <= i + r; nx++) {
			for (int ny = j - coverLeft[i][j][r + nx - i] + 1; ny < j
					+ coverRight[i][j][r + nx - i]; ny++) {
				wasNotCovered(nx, ny);
			}
		}
	}

	int getAddRouters(String name) {
		if (name.equals("rue_de_londres")) {
			return 10;
		}
		if (name.equals("opera")) {
			return 23;
		}
		return 0;
	}

	void run() {
		// String[] tests = new String[] { "charleston_road", "lets_go_higher",
		// "opera", "rue_de_londres" };
		// String[] tests = new String[] { "opera", "rue_de_londres" };
		String[] tests = new String[] { "rue_de_londres" };
		try {
			for (String t : tests) {
				System.err.println("do " + t);
				in = new FastScanner(new File("tests/" + t + ".in"));
				out = new PrintWriter(new File("tests/answers/" + t + ".out"));

				solve(getAddRouters(t));

				out.close();
				System.err.println("---------------------------------");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	class FastScanner {
		BufferedReader br;
		StringTokenizer st;

		public FastScanner(File f) {
			try {
				br = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		public FastScanner(InputStream f) {
			br = new BufferedReader(new InputStreamReader(f));
		}

		String next() {
			while (st == null || !st.hasMoreTokens()) {
				String s = null;
				try {
					s = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (s == null)
					return null;
				st = new StringTokenizer(s);
			}
			return st.nextToken();
		}

		boolean hasMoreTokens() {
			while (st == null || !st.hasMoreTokens()) {
				String s = null;
				try {
					s = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (s == null)
					return false;
				st = new StringTokenizer(s);
			}
			return true;
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		long nextLong() {
			return Long.parseLong(next());
		}

		double nextDouble() {
			return Double.parseDouble(next());
		}
	}

	public static void main(String[] args) {
		new SolveNew2().run();
	}
}