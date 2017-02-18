import java.io.*;
import java.util.*;

public class C {
	FastScanner in;
	PrintWriter out;

	final int INF = (int) 1e9;
	final int MAX = 750000;
	final int ALPH = 27;

	int[] s = new int[MAX];
	int[][] to = new int[MAX][ALPH];
	int[] len = new int[MAX], pos = new int[MAX];
	int[][] link = new int[MAX][ALPH];
	int[] par = new int[MAX];
	int sz = 1, n = 0;

	void attach(int child, int parent, int c, int child_len) {
		to[parent][c] = child;
		len[child] = child_len;
		par[child] = parent;
	}

	List<Integer> path = new ArrayList<Integer>();

	void extend(int i) {
		int v, vlen = n - i, old = sz - 1;
		for (v = old; link[v][s[i]] == 0; v = par[v]) {
			vlen -= len[v];
			path.add(v);
		}
		int w = link[v][s[i]];
		if (to[w][s[i + vlen]] != 0) {
			int u = to[w][s[i + vlen]];
			for (pos[sz] = pos[u] - len[u]; s[pos[sz]] == s[i + vlen]; pos[sz] += len[v]) {
				v = path.remove(path.size() - 1);
				vlen += len[v];
			}
			attach(sz, w, s[pos[u] - len[u]], len[u] - (pos[u] - pos[sz]));
			attach(u, sz, s[pos[sz]], pos[u] - pos[sz]);
			w = link[v][s[i]] = sz++;
		}
		link[old][s[i]] = sz;
		attach(sz, w, s[i + vlen], n - (i + vlen));
		pos[sz++] = n;
	}

	void init() {
		len[1] = 1;
		pos[1] = -1;
		par[1] = 0;
		sz = 2;
		for (int c = 0; c < ALPH; c++)
			link[0][c] = 1;
	}

	int[] sufEnd = new int[MAX];

	List<Integer> topSort = new ArrayList<Integer>();

	void dfs(int u) {
		topSort.add(u);
		for (int i = 0; i < ALPH; i++) {
			if (to[u][i] != 0) {
				dfs(to[u][i]);
			}
		}
	}

	String str = "";

	void solve() {
		init();
		str = in.next();
		this.s = new int[str.length() + 1];
		this.n = s.length;
		for (int i = 0; i < str.length(); i++) {
			s[i] = str.charAt(i) - 'a' + 1;
		}
		for (int i = s.length - 1; i >= 0; i--) {
			extend(i);
		}
		System.err.println(sz);
		dfs(1);
		for (int i = 1; i <= str.length(); i++) {
			// link[sufEnd[i]] = sufEnd[i - 1];
		}
		for (int i = 0; i < sz; i++) {
			for (int ch = 0; ch < ALPH; ch++) {
				if (to[i][ch] != 0) {
					System.err.println(i + " -> " + to[i][ch] + " by "
							+ ((char) ('a' + ch - 1)));
				}
			}
		}

		int[][] sum = new int[sz][ALPH];
		int[] first = new int[sz];

		for (int i : topSort) {
			for (int ch = 1; ch < ALPH; ch++) {
				if (to[i][ch] != 0) {
					System.err.println(i + " " + to[i][ch]);
					sum[i][ch]++;
					first[to[i][ch]] = i == 0 ? ch : first[i];
					for (int t = 1; t < ALPH; t++) {
						sum[to[i][ch]][t] += sum[i][t];
					}
				}
			}
		}
		long ans = 0;
		System.err.println(topSort);
		for (int i : topSort) {
			if (i == 0) {
				for (int ch = 1; ch < ALPH; ch++) {
					if (to[i][ch] != 0) {
						ans++;
					}
				}
			} else {
				for (int ch = 1; ch < ALPH; ch++) {
					if (to[i][ch] != 0) {
						int top = link[i][first[i]], bottom = link[to[i][ch]][first[i]];
						System.err.println(top + " " + bottom);
						ans += sum[bottom][first[i]] - sum[top][first[i]];
					}
				}
			}
		}
		out.println(ans);
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
		new C().run();
	}
}