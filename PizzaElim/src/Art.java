import java.io.*;
import java.util.*;

public class Art {
	FastScanner in;
	PrintWriter out;

	char[][] rotate(char[][] a) {
		int n = a.length;
		int m = a[0].length;
		char[][] res = new char[m][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				res[m - 1 - j][i] = a[i][j];
			}
		}
		return res;
	}

	int cntSame = -1;

	char[][] add(char[][] a, char[][] b, int shX, int shY) {
		for (int x = Math.max(0, -shX); x < b.length && x + shX < a.length; x++) {
			for (int y = Math.max(0, -shY); y < b[0].length && y + shY < a[0].length; y++) {
				if (b[x][y] != '.' && a[x + shX][y + shY] != '.') {
					if (b[x][y] != a[x + shX][y + shY]) {
						return null;
					}
				}
			}
		}
		
		int minX = Math.min(0, shX), maxX = Math.max(a.length - 1, b.length - 1
				+ shX);
		int minY = Math.min(0, shY), maxY = Math.max(a[0].length - 1,
				b[0].length - 1 + shY);
		char[][] res = new char[maxX - minX + 1][maxY - minY + 1];
		for (int i = 0; i < res.length; i++) {
			Arrays.fill(res[i], '.');
		}
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				int posX = i - minX, posY = j - minY;
				res[posX][posY] = a[i][j];
			}
		}
		cntSame = 0;
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[i].length; j++) {
				int posX = i + shX - minX, posY = j + shY - minY;
				if (b[i][j] == '.') {
					continue;
				}
				if (res[posX][posY] == '.' || res[posX][posY] == b[i][j]) {
					if (res[posX][posY] != '.') {
						cntSame++;
					}
					res[posX][posY] = b[i][j];
				} else {
					return null;
				}
			}
		}
		return res;
	}

	int bestSame;

	char[][] tryAdd(char[][] current, char[][] add) {
		char[][] best = null;
		bestSame = -1;
		for (int shiftX = -add.length; shiftX <= current.length; shiftX++) {
			for (int shiftY = -add[0].length; shiftY <= current[0].length; shiftY++) {
				char[][] get = add(current, add, shiftX, shiftY);
				if (get == null) {
					continue;
				}
				// System.err.println(cntSame);
				// if (bestSame == -1
				// || get.length * get[0].length < best.length
				// * best[0].length) {
				// best = get;
				// bestSame = cntSame;
				// }
				if (cntSame > bestSame
						|| (cntSame == bestSame && get.length * get[0].length < best.length
								* best[0].length)) {
					bestSame = cntSame;
					best = get;
				}
			}
		}
		return best;
	}

	char[][] tryAdd2(char[][] current, char[][] add) {
		char[][] best = null;
		bestSame = -1;
		for (int shiftX = -add.length; shiftX <= current.length; shiftX++) {
			for (int shiftY = -add[0].length; shiftY <= current[0].length; shiftY++) {
				char[][] get = add(current, add, shiftX, shiftY);
				if (get == null) {
					continue;
				}
				System.err.println(cntSame);
				if (bestSame == -1
						|| get.length * get[0].length < best.length
								* best[0].length) {
					best = get;
					bestSame = cntSame;
				}
			}
		}
		return best;
	}

	int iter = 0;

	class O {
		char[][] a;
		int id = iter++;
		int count;

		public O(char[][] a, int count) {
			super();
			this.a = a;
			this.count = count;
			// if (a.length * a[0].length > count * x * x) {
			// System.err.println("count = " + count);
			// System.err.println("x = " + x);
			// System.err.println(Arrays.deepToString(a));
			// throw new AssertionError();
			// }
		}

	}

	HashMap<Long, O> saved = new HashMap<Long, Art.O>();
	HashMap<Long, Integer> saved2 = new HashMap<Long, Integer>();

	int bSame;

	O getBest(O first, O second) {
		long id = first.id * (1L << 32) + second.id;
		if (saved.containsKey(id)) {
			bSame = saved2.get(id);
			return saved.get(id);
		}
		char[][] add = second.a;
		char[][] best = null;
		bSame = -1;
		for (int rot = 0; rot < 4; rot++) {
			char[][] next = tryAdd(first.a, add);
			if (best == null || bestSame > bSame) {
				best = next;
				bSame = bestSame;
			}
//			if (best == null
//					|| best.length * best[0].length > next.length
//							* next[0].length) {
//				best = next;
//			}
			add = rotate(add);
		}
		O result = new O(best, first.count + second.count);
		saved.put(id, result);
		saved2.put(id, bSame);
		return result;
	}

	O getBest2(O first, O second) {
		long id = first.id * (1L << 32) + second.id;
		if (saved.containsKey(id)) {
			bSame = saved2.get(id);
			return saved.get(id);
		}
		char[][] add = second.a;
		char[][] best = null;
		bSame = -1;
		for (int rot = 0; rot < 4; rot++) {
			char[][] next = tryAdd(first.a, add);
			if (best == null
					|| best.length * best[0].length > next.length
							* next[0].length) {
				best = next;
			}
			add = rotate(add);
		}
		O result = new O(best, first.count + second.count);
		saved.put(id, result);
		saved2.put(id, bSame);
		return result;
	}

	int x;

	void solve() {
		int tc = in.nextInt();
		System.err.println("tests " + tc);
		for (int t = 0; t < tc; t++) {
			if (t == 3) {
				// break;
			}
			System.err.println("do " + t);
			int cnt = in.nextInt();
			char[][][][] a = new char[cnt][4][][];
			for (int ii = 0; ii < cnt; ii++) {
				int n = in.nextInt();
				int m = in.nextInt();
				a[ii][0] = new char[n][m];
				for (int i = 0; i < n; i++) {
					a[ii][0][i] = in.next().toCharArray();
				}
				for (int i = 1; i < 4; i++) {
					a[ii][i] = rotate(a[ii][i - 1]);
				}
			}
			x = 0;
			for (int i = 0; i < cnt; i++) {
				x = Math.max(x, Math.max(a[i][0].length, a[i][0][0].length));
			}

			/*
			 * for (int i = 0; i < cnt; i++) { for (int j = 0; j < i; j++) { int
			 * mx = 0; for (int it = 0; it < 4; it++) { char[][] next =
			 * tryAdd(a[i][0], a[j][it]); mx = Math.max(mx, bestSame); } double
			 * f = Math.min(a[i][0].length * a[i][0][0].length, a[j][0].length *
			 * a[j][0][0].length); System.err.printf("%.3f ", mx / f); }
			 * System.err.println(); }
			 */

			ArrayList<O> current = new ArrayList<Art.O>();
			for (int i = 0; i < cnt; i++) {
				current.add(new O(a[i][0], 1));
			}
			while (current.size() > 1) {
				double bestScore = -1;
				int bestF = -1, bestS = -1;
				O last = null;
				for (int f = 0; f < current.size(); f++) {
					for (int s = f + 1; s < current.size(); s++) {
						O next = getBest(current.get(f), current.get(s));
						double minW = Math.min(current.get(f).a.length
								* current.get(f).a[0].length,
								current.get(s).a.length
										* current.get(s).a[0].length);
						double score = bSame / minW;
						if (score > bestScore && score > 0.2) {
							bestScore = score;
							bestF = f;
							bestS = s;
							last = next;
						}
					}
				}
				if (last == null) {
					break;
				}
				System.err.println(bestScore + "!! " + last.a.length + " "
						+ last.a[0].length);
				ArrayList<O> next = new ArrayList<Art.O>();
				next.add(getBest(current.get(bestF), current.get(bestS)));
				for (int i = 0; i < current.size(); i++) {
					if (i != bestF && i != bestS) {
						next.add(current.get(i));
					}
				}
				current = next;
			}
			saved.clear();
			saved2.clear();
			while (current.size() > 1) {
				double bestScore = Double.MAX_VALUE;
				int bestF = -1, bestS = -1;
				O last = null;
				for (int f = 0; f < current.size(); f++) {
					for (int s = f + 1; s < current.size(); s++) {
						O next = getBest2(current.get(f), current.get(s));
						double score = next.a.length * next.a[0].length;
						if (score < bestScore) {
							bestScore = score;
							bestF = f;
							bestS = s;
							last = next;
						}
					}
				}
				if (last == null) {
					break;
				}
				System.err.println(bestScore + "?? " + last.a.length + " "
						+ last.a[0].length);
				ArrayList<O> next = new ArrayList<Art.O>();
				next.add(getBest(current.get(bestF), current.get(bestS)));
				for (int i = 0; i < current.size(); i++) {
					if (i != bestF && i != bestS) {
						next.add(current.get(i));
					}
				}
				current = next;
			}
			char[][] res = current.get(0).a;
			if (res.length * res[0].length > x * x * cnt) {
				System.err.println("got " + res.length + ", " + res[0].length);
				System.err.println("x = " + x + ", cnt = " + cnt);
				for (int ii = 0; ii < cnt; ii++) {
					System.err.println();
					for (int i = 0; i < a[ii][0].length; i++) {
						for (int j = 0; j < a[ii][0][i].length; j++) {
							System.err.print(a[ii][0][i][j]);
						}
						System.err.println();
					}
					System.err.println();
				}
				throw new AssertionError();
			}
			out.println(res.length + " " + res[0].length);
			for (int i = 0; i < res.length; i++) {
				for (int j = 0; j < res[i].length; j++) {
					char c = res[i][j];
					out.print(c == '.' ? 'a' : c);
				}
				out.println();
			}
			out.flush();

		}
	}

	void run() {
		for (int test = 5; test <= 5; test++) {
			String tN = Integer.toString(test);
			while (tN.length() < 2) {
				tN = "0" + tN;
			}
			System.err.println("do " + tN);
			try {
				in = new FastScanner(new File("testy/art" + tN + ".in"));
				out = new PrintWriter(new File("testy/art" + tN + ".out"));

				solve();

				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	void runIO() {

		in = new FastScanner(System.in);
		out = new PrintWriter(System.out);

		solve();

		out.close();
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
		new Art().run();
	}
}