import java.io.*;
import java.util.*;

public class F {
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
		while (in.hasMoreTokens()) {
			int n = in.nextInt();
			int w = in.nextInt();
			final Rectangle[] a = new Rectangle[n + 2];
			for (int i = 0; i < n; i++) {
				a[i] = new Rectangle(in.nextInt(), in.nextInt(), in.nextInt(),
						in.nextInt(), in.nextInt());
			}
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
		new F().runIO();
	}
}