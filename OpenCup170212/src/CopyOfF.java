import java.io.*;
import java.util.*;

public class CopyOfF {
	FastScanner in;
	PrintWriter out;

	class Rectangle implements Comparable<Rectangle> {
		int type;
		int a, b;
		int l;
		int w;

		public Rectangle(int type, int l, int a, int b, int w) {
			super();
			this.type = type;
			this.a = a;
			this.b = b;
			this.l = l;
			this.w = w;
		}

		public int compareTo(Rectangle o) {
			return Integer.compare(a, o.a);
		}

		@Override
		public String toString() {
			return "Rectangle [type=" + type + ", a=" + a + ", b=" + b + ", l="
					+ l + ", w=" + w + "]";
		}

	}

	int solveSlow(Rectangle[] a, int w, int n) {
		boolean[][] can = new boolean[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (a[i].a >= a[j].b || a[i].b <= a[j].a
						|| (a[i].type != a[j].type && a[i].l + a[j].l <= w)) {
					can[i][j] = true;
				}
			}
		}
		int res = 0;
		for (int mask = 0; mask < 1 << n; mask++) {
			boolean ok = true;
			for (int i = 0; i < n; i++) {
				if (((1 << i) & mask) != 0) {
					for (int j = i + 1; j < n; j++) {
						if (((1 << j) & mask) != 0) {
							if (!can[i][j]) {
								ok = false;
							}
						}
					}
				}
			}
			if (ok) {
				int su = 0;
				for (int i = 0; i < n; i++) {
					if (((1 << i) & mask) != 0) {
						su += a[i].w;
					}
				}
				res = Math.max(res, su);
			}
		}
		return res;
	}

	void solve() {
		Random rnd = new Random(123);
		final int MAX = 20;
		for (int iter = 0; iter < 123123; iter++) {
			System.err.println("it = " + iter);
			int n = 1 + rnd.nextInt(10);
			int w = 1 + rnd.nextInt(MAX);
			;
			final Rectangle[] a = new Rectangle[n + 2];
			for (int i = 0; i < n; i++) {
				int l = rnd.nextInt(MAX);
				int r = rnd.nextInt(MAX);
				if (r < l) {
					int tmp = l;
					l = r;
					r = tmp;
				}
				if (l == r) {
					r++;
				}
				a[i] = new Rectangle(rnd.nextInt(2), rnd.nextInt(w) + 1, l, r,
						1 + rnd.nextInt(MAX));
			}
			int correct = solveSlow(a, w, n);
			n += 2;
			a[n - 2] = new Rectangle(0, 0, -10, -10, 0);
			a[n - 1] = new Rectangle(1, 0, -10, -10, 0);
			Arrays.sort(a);
			int[] it = new int[n];
			int[] dp = new int[n];
			int[][] dp2 = new int[n][n];
			int res = 0;
			for (int i = 0; i < n; i++) {
				dp[i] = a[i].w;
				res = Math.max(res, dp[i]);
			}
			Integer[] ids = new Integer[n];
			for (int i = 0; i < n; i++) {
				ids[i] = i;
			}
			Arrays.sort(ids, new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
					return Integer.compare(a[o1].b, a[o2].b);
				}
			});
			for (int sum = 0; sum < 2 * n; sum++) {
				for (int i = 0; i < n && i <= sum; i++) {
					int j = sum - i;
					if (j >= i && j < n) {
						if (iter == 88 && i == 1 && j == 3) {
							System.err.println("!!!");
						}
						if (a[i].type != a[j].type) {
							boolean ok = a[i].a >= a[j].b || a[i].b <= a[j].a
									|| a[i].l + a[j].l <= w;
							if (!ok) {
								continue;
							}
							{
								int updX = a[j].a;
								while (it[i] < n && a[ids[it[i]]].b <= updX) {
									int check = ids[it[i]];
									if (a[i].type != a[check].type) {
										if (a[i].a >= a[check].b
												|| a[i].b <= a[check].a
												|| (a[i].l + a[check].l <= w)) {
											dp[i] = Math.max(dp[i],
													dp2[i][check]);
										}
									}
									it[i]++;
								}
							}
							// {
							// int updX = a[i].a;
							// while (it[j] < n && a[ids[it[j]]].b <= updX) {
							// int check = ids[it[j]];
							// if (a[j].type != a[check].type) {
							// if (a[j].a >= a[check].b
							// || a[j].b <= a[check].a
							// || (a[j].l + a[check].l <= w)) {
							// dp[j] = Math.max(dp[j],
							// dp2[j][check]);
							// }
							// }
							// it[j]++;
							// }
							// }
							dp2[i][j] = Math.max(dp2[i][j], dp[i] + a[j].w);
							dp2[j][i] = dp2[i][j];
							// dp2[i][j] = Math.max(dp2[i][j], dp[j] + a[i].w);
							res = Math.max(res, dp2[i][j]);
						}
					}
				}
			}
			if (res != correct) {
				System.err.println("w = " + w);
				for (int i = 0; i < n; i++) {
					System.err.println(a[i]);
				}
				System.err.println("my = " + res + ", corr = " + correct);
				for (int i = 0; i < n; i++) {
					System.err.println(Arrays.toString(dp2[i]));
				}
				throw new AssertionError();
			}
			out.println(res);
		}
	}

	void run() {
		try {
			in = new FastScanner(new File("F.in"));
			out = new PrintWriter(new File("F.out"));

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
		new CopyOfF().run();
	}
}