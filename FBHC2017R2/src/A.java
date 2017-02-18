import java.io.*;
import java.util.*;

public class A {
	FastScanner in;
	PrintWriter out;

	class Dsu {
		int[] p;

		Dsu(int n) {
			p = new int[n];
			for (int i = 0; i < n; i++) {
				p[i] = i;
			}
		}

		int get(int x) {
			return p[x] == x ? x : (p[x] = get(p[x]));
		}

		void unite(int x, int y) {
			p[get(x)] = get(y);
		}
	}

	HashSet<Long> was = new HashSet<Long>();

	// -1 can't pass
	// 0 - can
	// 1 - ok!
	int isOk(boolean[][] f) {
		int n = f.length;
		int m = f[0].length;
		boolean[][] g = new boolean[n][m];
		g[0][0] = !f[0][0];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (i > 0 && g[i - 1][j]) {
					g[i][j] = true;
				}
				if (j > 0 && g[i][j - 1]) {
					g[i][j] = true;
				}
				g[i][j] &= !f[i][j];
			}
		}
		if (g[n - 1][m - 1]) {
			return 0;
		}
		Dsu dsu = new Dsu(n * m);
		int[] dx = new int[] { -1, 0, 0, 1 };
		int[] dy = new int[] { 0, -1, 1, 0 };
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (!f[i][j]) {
					for (int it = 0; it < dx.length; it++) {
						int ni = i + dx[it], nj = j + dy[it];
						if (ni >= 0 && ni < n && nj >= 0 && nj < m
								&& !f[ni][nj]) {
							dsu.unite(i * m + j, ni * m + nj);
						}
					}
				}
			}
		}
		if (dsu.get(0) == dsu.get(n * m - 1)) {
			return 1;
		}
		return -1;
	}

	int go(boolean[][] f, int alr, int k) {
		int val = isOk(f);
		if (val == -1) {
			return Integer.MAX_VALUE;
		}
		if (val == 1) {
			return alr;
		}
		long value = 0;
		int it = 0;
		for (int i = 0; i < f.length; i++) {
			for (int j = 0; j < f[i].length; j++) {
				if (f[i][j]) {
					value |= 1L << it;
				}
				it++;
			}
		}
		if (was.contains(value)) {
			return Integer.MAX_VALUE;
		}
		was.add(value);
		int res = Integer.MAX_VALUE;
		for (int i = 0; i + k <= f.length; i++) {
			for (int j = 0; j + k <= f[0].length; j++) {
				boolean ok = true;
				for (int ni = i; ni < i + k; ni++) {
					for (int nj = j; nj < j + k; nj++) {
						ok &= !f[ni][nj];
					}
				}
				if (ok) {
					for (int ni = i; ni < i + k; ni++) {
						for (int nj = j; nj < j + k; nj++) {
							f[ni][nj] = true;
						}
					}
					res = Math.min(res, go(f, alr + 1, k));
					for (int ni = i; ni < i + k; ni++) {
						for (int nj = j; nj < j + k; nj++) {
							f[ni][nj] = false;
						}
					}
				}
			}
		}
		return res;
	}

	int solve(int n, int m, int k) {
		System.err.println(n + " " + m + " " + k);
		was.clear();
		boolean[][] f = new boolean[n][m];
		return go(f, 0, k);
	}

	int solveOneFast2(int n, int m, int k) {
		if (1 + k + 1 + k + 1 > m) {
			return Integer.MAX_VALUE;
		}
		if (1 + k > n) {
			return Integer.MAX_VALUE;
		}
		return 1 + (n - 1) / k;
	}

	int solveOneFast3(int n, int m, int k) {
		if (k == 1) {
			if (m >= 5 && n >= 3) {
				return 5;
			}
			return Integer.MAX_VALUE;
		} else {
			if (m >= k * 2 + 3 && n >= k * 2 + 1) {
				return 4;
			}
			return Integer.MAX_VALUE;
		}
	}

	int solveOneFast(int n, int m, int k) {
		return Math.min(solveOneFast2(n, m, k), solveOneFast3(n, m, k));
	}

	int solveFast(int n, int m, int k) {
		return Math.min(solveOneFast(n, m, k), solveOneFast(m, n, k));
	}

	void solveOneTest() {
		int res = (solveFast(in.nextInt(), in.nextInt(), in.nextInt()));
		out.println(res == Integer.MAX_VALUE ? -1 : res);
	}

	void solve123() {
		for (int sum = 1;; sum++) {
			for (int n = 1; n < sum; n++) {
				int m = sum - n;
				for (int k = 1; k <= n || k <= m; k++) {
					if (n * m < 2) {
						continue;
					}
					int my = solveFast(n, m, k);
					int correct = solve(n, m, k);
					System.err.println(n + " " + m + " " + k + " " + my + " " + correct);
					if (my != correct) {
						throw new AssertionError();
					}
				}
			}
		}
	}

	void solve() {
		int tc = in.nextInt();
		for (int t = 0; t < tc; t++) {
			System.err.println("test " + t);
			out.print("Case #" + (t + 1) + ": ");
			solveOneTest();
		}
	}

	void run() {
		try {
			in = new FastScanner(new File("A.in"));
			out = new PrintWriter(new File("A.out"));

			solve();

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
		new A().run();
	}
}