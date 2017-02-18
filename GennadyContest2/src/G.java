import java.io.*;
import java.util.*;

public class G {
	FastScanner in;
	PrintWriter out;

	void solve() {
		int t = in.nextInt();
		// 20, 3, 20
		Random rnd = new Random(123);
		final int MAX = 5;
		for (int it = 0; it < 123123; it++) {
			int n = 3 + rnd.nextInt(9);
			ArrayList<Integer> masks = new ArrayList<Integer>();
			for (int mask = 0; mask < 1 << n; mask++) {
				if (Integer.bitCount(mask) == 3) {
					masks.add(mask);
				}
			}
			int trains = 1 + rnd.nextInt(MAX);
			int[] who = new int[trains];
			for (int i = 0; i < trains; i++) {
				who[i] = masks.get(rnd.nextInt(masks.size()));
			}
			int[] strategy1 = new int[trains];
			int[] strategy2 = new int[trains];
			for (int i = 0; i < trains; i++) {
				int id = 0;
				while (true) {
					boolean ok = true;
					for (int j = 0; j < i; j++) {
						if (strategy1[j] == id && ((who[j] & who[i]) != 0)) {
							ok = false;
						}
					}
					if (ok) {
						break;
					}
					id++;
				}
				strategy1[i] = id;
			}
			for (int i = 0; i < trains; i++) {
				int max = -1;
				for (int j = 0; j < i; j++) {
					max = Math.max(strategy2[j], max);
				}
				O[] a = new O[max + 2];
				for (int j = 0; j < a.length; j++) {
					a[j] = new O(0, j);
				}
				for (int j = 0; j < i; j++) {
					a[strategy2[j]].cnt++;
				}
				Arrays.sort(a);
				for (int j = 0; j < a.length; j++) {
					int id = a[j].id;
					boolean ok = true;
					for (int k = 0; k < i; k++) {
						if (strategy2[k] == id && ((who[k] & who[i]) != 0)) {
							ok = false;
						}
					}
					if (ok) {
						strategy2[i] = id;
						break;
					}
				}
			}
			int max1 = 0;
			int max2 = 0;
			for (int i = 0; i < trains; i++) {
				max1 = Math.max(max1, strategy1[i]);
				max2 = Math.max(max2, strategy2[i]);
				if ((t <= 1 && max1 < max2) || (t == 2 && max1 > max2)) {
					// System.err.println(Arrays.toString(strategy1));
					// System.err.println(Arrays.toString(strategy2));
					out.println((1 +Math.min(max1, max2)) + " " + (i + 1));
					for (int j = 0; j <= i; j++) {
						for (int k = 0; k < n; k++) {
							if (((1 << k) & who[j]) != 0) {
								out.print((char) ('a' + k));
								out.print(" ");
							}
						}
						out.println();
					}
					return;
				}
			}
		}
	}

	class O implements Comparable<O> {
		int cnt;
		int id;

		public O(int cnt, int id) {
			super();
			this.cnt = cnt;
			this.id = id;
		}

		public int compareTo(O o) {
			if (cnt != o.cnt) {
				return -Integer.compare(cnt, o.cnt);
			}
			return Integer.compare(id, o.id);
		}

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
		new G().runIO();
	}
}