import java.io.*;
import java.util.*;

public class CopyOfMain {
	FastScanner in;
	PrintWriter out;

	class O {

		char[][] a;

		public O(char[][] a) {
			super();
			this.a = a;
		}

		public int hashCode() {
			int res = 0;
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[i].length; j++) {
					res = res * 2390017 + a[i][j];
				}
			}
			return res;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			O other = (O) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[i].length; j++) {
					if (a[i][j] != other.a[i][j]) {
						return false;
					}
				}
			}
			return true;
		}

		private CopyOfMain getOuterType() {
			return CopyOfMain.this;
		}

	}

	HashMap<O, Integer> res = new HashMap<CopyOfMain.O, Integer>();

	void prec(int n) {
		char[][] a = new char[n][n];
		for (int i = 0; i < n; i++) {
			Arrays.fill(a[i], '#');
		}
		O o = new O(a);
		res.put(o, 0);
		for (int st = 0; st < 1 << (n * n); st++) {
			char[][] b = new char[n][n];
			int it = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (((1 << it) & st) != 0) {
						b[i][j] = '.';
					} else {
						b[i][j] = '#';
					}
					it++;
				}
			}
			O oo = new O(b);
			if (!res.containsKey(oo)) {
				res.put(oo, Integer.MAX_VALUE / 2);
			}
		}
		while (true) {
			boolean ch = false;
			for (java.util.Map.Entry<O, Integer> curRes : res.entrySet()) {
				for (int row = 0; row < n; row++) {
					for (int col = 0; col < n; col++) {
						char[][] f = new char[n][n];
						for (int i = 0; i < n; i++) {
							for (int j = 0; j < n; j++) {
								f[i][j] = curRes.getKey().a[i][j];
							}
						}
						char[] tmp = f[row].clone();
						for (int i = 0; i < n; i++) {
							f[i][col] = tmp[i];
						}
						Integer newAns = res.get(new O(f));
						if (newAns + 1 < curRes.getValue()) {
							ch = true;
							res.put(curRes.getKey(), newAns + 1);
						}
					}
				}
			}
			if (!ch) {
				break;
			}
		}
	}

	void solve() {
		int n = 4;
		Random rnd = new Random(123);
		prec(n);
		for (int it = 0; it < 123123; it++) {
			char[][] a = new char[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					a[i][j] = rnd.nextBoolean() ? '.' : '#';
				}
			}
			int my = solve(a);
			int cor = res.get(new O(a));
			if (cor >= Integer.MAX_VALUE / 2) {
				cor = Integer.MAX_VALUE;
			}
			if (cor != my) {
				System.err.println(Arrays.deepToString(a));
				System.err.println("my " + my);
				System.err.println("corr " + cor);
				throw new AssertionError();
			}
		}

	}

	int solve(char[][] a) {
		int n = a.length;
		int res = Integer.MAX_VALUE;
		boolean smth = false;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (a[i][j] == '#') {
					smth = true;
				}
			}
		}
		if (!smth) {
			return Integer.MAX_VALUE;
		}
		for (int r = 0; r < n; r++) {
			boolean ok = false;
			for (int i = 0; i < n; i++) {
				if (a[i][r] == '#') {
					ok = true;
				}
			}
			int cnt = ok ? 0 : 1;
			for (int i = 0; i < n; i++) {
				if (a[r][i] == '.') {
					cnt++;
				}
			}
			for (int c = 0; c < n; c++) {
				boolean ok2 = true;
				for (int i = 0; i < n; i++) {
					if (a[i][c] == '.') {
						ok2 = false;
					}
				}
				if (!ok2) {
					cnt++;
				}
			}
			res = Math.min(res, cnt);

		}

		return res;
	}

	void solve123() {
		int n = in.nextInt();
		char[][] a = new char[n][];
		for (int i = 0; i < n; i++) {
			a[i] = in.next().toCharArray();
		}
		boolean full = true;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (a[i][j] == '.') {
					full = false;
				}
			}
		}
		if (full) {
			out.println(0);
			return;
		}
		int res = Integer.MAX_VALUE;
		for (int r = 0; r < n; r++) {
			boolean ok = false;
			for (int i = 0; i < n; i++) {
				if (a[i][r] == '#') {
					ok = true;
				}
			}
			if (ok) {
				int cnt = 0;
				for (int i = 0; i < n; i++) {
					if (a[r][i] == '.') {
						cnt++;
					}
				}
				for (int c = 0; c < n; c++) {
					boolean ok2 = true;
					for (int i = 0; i < n; i++) {
						if (a[i][c] == '.') {
							ok2 = false;
						}
					}
					if (!ok2) {
						cnt++;
					}
				}
				res = Math.min(res, cnt);
			}
		}
		out.println(res == Integer.MAX_VALUE ? -1 : res);
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
		new CopyOfMain().runIO();
	}
}