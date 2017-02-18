import java.io.*;
import java.util.*;

public class GStupid {
	FastScanner in;
	PrintWriter out;

	void check() {
		// if (p[p.length - 1] != 0) {
		// return;
		// }
		/*int ans = 0;
		int[] p = this.p.clone();
		while (p[0] != 0) {
			int x = p[0];
			for (int i = 0; i < x; i++) {
				p[i] = p[i + 1];
			}
			p[x] = x;
			ans++;
		}*/

		int ans2 = 0;
		p = this.p.clone();
		while (p[0] != 0) {
			int max = 0;
			int z = 0;
			for (int i = 0; i < p.length; i++) {
				max = Math.max(max, p[i]);
				if (p[i] == 0) {
					break;
				}
				if (max == i + 1) {
					z = i + 1;
				} else {
					break;
				}
			}

			if (z == 0) {
				int x = p[0];
				for (int i = 0; i < x; i++) {
					p[i] = p[i + 1];
				}
				p[x] = x;
				ans2++;
			} else {
				ans2 += (1 << z) - 1;
				for (int x = 0; x < z; x++) {
					for (int y = x + 1; y < z; y++) {
						if (p[x] > p[y]) {
							ans2 -= (1 << (p[y] - 1));
						}
					}
				}
				Arrays.sort(p, 0, z);
				int x = p[z];
				for (int y = z; y > 0; y--) {
					p[y] = p[y - 1];
				}
				p[0] = x;	
			}
		}
		//if (ans != ans2)
		out.println(ans2);

	}

	void go(int pos) {
		if (pos == p.length) {
			check();
			return;
		}
		for (int i = 0; i < p.length; i++) {
			if (!used[i]) {
				p[pos] = i;
				used[i] = true;
				go(pos + 1);
				used[i] = false;
			}
		}
	}

	int[] p;
	boolean[] used;

	void solve() {
		int n = in.nextInt();
		p = new int[n];
		used = new boolean[n];
		//go(0);
		for (int i = 0; i < n; i++) {
			p[i] = in.nextInt();
		}
		check();
	}

	void run() {

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
		new GStupid().run();
	}
}