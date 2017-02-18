import java.io.*;
import java.util.*;

public class D {
	FastScanner in;
	PrintWriter out;

	void gen(int[] a, Random rnd, int more) {
		while (more > 0) {
			int pos = rnd.nextInt(a.length);
			if (a[pos] == 0) {
				a[pos] = 1;
				more--;
			}
		}
	}

	long getValue(int[] a) {
		long res = 0;
		for (int i : a) {
			res = res * 2 + i;
		}
		return res;
	}

	long getAns(int n, int k, int[] a) {
		int su = 0;
		for (int i : a) {
			su += i;
		}
		if (su == k) {
			return 0;
		}
		Random rnd = new Random(123);
		ArrayList<Integer> freePos = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			if (a[i] == 0) {
				freePos.add(i);
			}
		}
		int more = k - su;
		int[] b = new int[n];
		int[] c = new int[n];
		long result = 0;
		for (int it = 0; it < 10000; it++) {
			for (int i = 0; i < n; i++) {
				b[i] = c[i] = a[i];
			}
			gen(b, rnd, more);
			gen(c, rnd, more);
			boolean same = true;
			for (int i = 0; i < n; i++) {
				if (b[i] != c[i]) {
					same = false;
					break;
				}
			}
			if (same) {
				continue;
			}
			long v1 = getValue(b);
			long v2 = getValue(c);
			long canDiff = Long.MAX_VALUE;
			for (int z = 0; z < n; z++) {
				if (a[z] == 0) {
					continue;
				}
				long mask = (1L << (n - z)) - 1;
				long cost1 = (mask + 1) - (v1 & mask);
				long cost2 = (mask + 1) - (v2 & mask);
				if (cost1 == cost2) {
					continue;
				}
				canDiff = Math.min(canDiff, Math.min(cost1, cost2));
			}
			result = Math.max(result, canDiff);
		}
		return result;
	}

	long getAnsFast(int n, int k, int[] a) {
		int su = 0;
		for (int i : a) {
			su += i;
		}
		if (su == k) {
			return 0;
		}
		if (k == n) {
			return 0;
		}
		ArrayList<Integer> freePos = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			if (a[i] == 0) {
				freePos.add(i);
			}
		}
		int more = k - su;

		long result = 0;
		for (int pos1 = 0; pos1 < n; pos1++) {
			if (a[pos1] == 0) {
				for (int pos2 = pos1 + 1; pos2 < n; pos2++) {
					if (a[pos2] == 0) {
						int id = pos2;
						while (id >= 0 && a[id] == 0) {
							id--;
						}
						if (id == -1) {
							return Long.MAX_VALUE;
						}
						int[] b = a.clone();
						int[] c = a.clone();
						b[pos1] = 1;
						c[pos2] = 1;
						int have = more - 1;
						for (int i = 0; i < id; i++) {
							if (i != pos1 && i != pos2 && a[i] == 0) {
								if (have > 0) {
									have--;
									b[i] = c[i] = 1;
								}
							}
						}
						for (int i = n - 1; i > pos2; i--) {
							if (a[i] == 0 && have > 0) {
								have--;
								b[i] = c[i] = 1;
							}
						}
						if (have == 0) {
							long v1 = getValue(b);
							long v2 = getValue(c);
							long canDiff = Long.MAX_VALUE;
							for (int z = 0; z < n; z++) {
								if (a[z] == 0) {
									continue;
								}
								long mask = (1L << (n - z)) - 1;
								long cost1 = (mask + 1) - (v1 & mask);
								long cost2 = (mask + 1) - (v2 & mask);
								if (cost1 == cost2) {
									continue;
								}
								canDiff = Math.min(canDiff,
										Math.min(cost1, cost2));
							}
							result = Math.max(result, canDiff);
						}
					}
				}
			}
		}

		return result;
	}

	void solveOneTest() {
		int n = in.nextInt();
		int k = in.nextInt();
		int[] a = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = in.nextInt();
		}
		long res = getAnsFast(n, k, a);
		long res2 = getAns(n, k, a);
		if (res < res2) {
			throw new AssertionError();
		}
		out.println(res == Long.MAX_VALUE ? -1 : res);
	}

	void solve123() {
		Random rnd = new Random(12);
		for (int it = 0; it < 123123; it++) {
			System.err.println("oit " + it);
			int n = 1 + rnd.nextInt(10);
			int[] a = new int[n];
			double p = rnd.nextDouble();
			int su = 0;
			for (int i = 0; i < n; i++) {
				a[i] = rnd.nextDouble() < p ? 1 : 0;
				su += a[i];
			}
			int k = su + rnd.nextInt(n - su + 1);
			long my = getAnsFast(n, k, a);
			long correct = getAns(n, k, a);
			System.err.println(my + " " + correct);
			if (correct > my) {
				throw new AssertionError();
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
			in = new FastScanner(new File("D.in"));
			out = new PrintWriter(new File("D.out"));

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
		new D().run();
	}
}