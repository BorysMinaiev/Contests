import java.io.*;
import java.util.*;

public class L {
	FastScanner in;
	PrintWriter out;

	int[] p;
	int[] sz;

	int get(int x, int y) {
		return x * m + y;
	}

	int n, m;

	int get(int x) {
		return p[x] == x ? x : (p[x] = get(p[x]));
	}

	void unite(int x, int y) {
		x = get(x);
		y = get(y);
		if (x != y) {
			sz[y] += sz[x];
			p[get(x)] = get(y);
		}
	}

	void solve() {
		n = in.nextInt();
		m = in.nextInt();
		char[][] a = new char[n][];
		for (int i = 0; i < n; i++) {
			a[i] = in.next().toCharArray();
		}
		p = new int[n * m];
		sz = new int[n * m];
		for (int i = 0; i < p.length; i++) {
			sz[i] = 1;
			p[i] = i;
		}
		int[][] left = new int[n][m];
		int[][] right = new int[n][m];
		int[][] up = new int[n][m];
		int[][] down = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (j == 0) {
					left[i][j] = -1;
				} else {
					if (left[i][j - 1] != -1 && a[i][j] == '+') {
						unite(get(i, j), get(i, left[i][j - 1]));
					}
					left[i][j] = left[i][j - 1];
				}
				if (a[i][j] == '+') {
					left[i][j] = j;
				}
				if (i == 0) {
					up[i][j] = -1;
				} else {
					if (up[i - 1][j] != -1 && a[i][j] == '+') {
						unite(get(i, j), get(up[i - 1][j], j));
					}
					up[i][j] = up[i - 1][j];
				}
				if (a[i][j] == '+') {
					up[i][j] = i;
				}
			}
		}
		for (int i = n - 1; i >= 0; i--) {
			for (int j = m - 1; j >= 0; j--) {
				if (i == n - 1) {
					down[i][j] = -1;
				} else {
					if (down[i + 1][j] != -1 && a[i][j] == '+') {
						unite(get(i, j), get(down[i + 1][j], j));
					}
					down[i][j] = down[i + 1][j];
				}
				if (a[i][j] == '+') {
					down[i][j] = i;
				}
				if (j == m - 1) {
					right[i][j] = -1;
				} else {
					if (right[i][j + 1] != -1 && a[i][j] == '+') {
						unite(get(i, j), get(i, right[i][j + 1]));
					}
					right[i][j] = right[i][j + 1];
				}
				if (a[i][j] == '+') {
					right[i][j] = j;
				}
			}
		}
		int res = 0, resX = -1, resY = -1;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (a[i][j] == '+') {
					continue;
				}
				useIt = 0;
				totSz = 0;
				if (left[i][j] != -1) {
					add(get(i, left[i][j]));
				}
				if (right[i][j] != -1) {
					add(get(i, right[i][j]));
				}
				if (up[i][j] != -1) {
					add(get(up[i][j], j));
				}
				if (down[i][j] != -1) {
					add(get(down[i][j], j));
				}
				if (totSz > res) {
					res = totSz;
					resX = i;
					resY = j;
				}
			}
		}
		out.println(res);
		if (res != 0) {
			out.println((1 + resX) + " " + (1 + resY));
		}
	}

	int[] use = new int[4];
	int useIt;
	int totSz;

	void add(int id) {
		for (int i = 0; i < useIt; i++) {
			if (get(use[i]) == get(id)) {
				return;
			}
		}
		use[useIt++] = id;
		totSz += sz[get(id)];
	}

	void run() {
		try {
			in = new FastScanner(new File("object.in"));
			out = new PrintWriter(new File("object.out"));

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
		new L().runIO();
	}
}