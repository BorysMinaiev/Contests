import java.io.*;
import java.util.*;

public class G {
	FastScanner in;
	PrintWriter out;

	class DSU {
		int[] p, minR, minC, maxR, maxC, size;
		int n, m;

		public DSU(int n, int m) {
			int N = n * m;
			p = new int[N];
			for (int i = 0; i < N; i++) {
				p[i] = i;
			}
			minR = new int[N];
			minC = new int[N];
			maxR = new int[N];
			maxC = new int[N];
			size = new int[N];
			this.n = n;
			this.m = m;
		}

		int get(int v) {
			if (p[v] == v) {
				return v;
			}
			return p[v] = get(p[v]);
		}

		boolean unite(int a, int b) {
			a = get(a);
			b = get(b);
			if (a == b) {
				return false;
			}

			minR[a] = Math.min(minR[a], minR[b]);
			minC[a] = Math.min(minC[a], minC[b]);
			maxR[a] = Math.max(maxR[a], maxR[b]);
			maxC[a] = Math.max(maxC[a], maxC[b]);
			size[a] += size[b];
			return true;
		}
		
		boolean good(int comp) {
			return size[comp] == (maxR[comp] - minR[comp] + 1) * (maxC[comp] - minC[comp] + 1);
		}
	}
	

	char[][] field;

	List<Integer> result = new ArrayList<Integer>();

	int[] dx = new int[] { -1, 0, 1, 0 };
	int[] dy = new int[] { 0, 1, 0, -1 };

	void solve() {
		int n = in.nextInt() + 2, m = in.nextInt() + 2;
		field = new char[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				field[n][m] = '.';
			}
		}
		for (int i = 1; i < n - 1; i++) {
			String s = in.next();
			for (int j = 0; j < s.length(); j++) {
				field[i][j + 1] = s.charAt(j);
			}
		}

		DSU dsu = new DSU(n, m);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				int id = i * m + j;
				dsu.minR[id] = dsu.maxR[id] = i;
				dsu.minC[id] = dsu.maxC[id] = j;
				dsu.size[id] = field[i][j] == '.' ? 1 : 0;
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (field[i][j] == '.') {
					for (int d = 0; d < 4; d++) {
						int ni = i + dx[d], nj = j + dy[d];
						if (0 <= ni && ni < n && 0 <= nj && nj < m && field[ni][nj] == '.') {
							dsu.unite(i * m + j, ni * m + nj);
						}
					}
				}
			}
		}
		
		TreeSet<Integer> rects = new TreeSet<Integer>();
		boolean[] inSet = new boolean[n * m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (field[i][j] == '.') {
					int pos = dsu.get(i * m + j);
					if (dsu.good(pos) && !inSet[pos]) {
						rects.add(pos);
						inSet[pos] = true;
					}
				}
			}
		}

		while (rects.size() > 0) {
			int cur = rects.pollFirst();
			if (dsu.size[cur] == n * m) {
				break;
			}
			
			int minR = dsu.minR[cur] - 1, minC = dsu.minC[cur] - 1;
			int maxR = dsu.maxR[cur] + 1, maxC = dsu.maxC[cur] + 1;
			
			for (int c = minC; c <= maxC; c++) {
				field[minR][c] = field[maxR][c] = '.';
				dsu.size[minR * m + c] = dsu.size[maxR * m + c] = 1;
			}
			for (int r = minR + 1; r < maxR; r++) {
				field[r][minC] = field[r][maxC] = '.';
				dsu.size[r * m + minC] = dsu.size[r * m + maxC] = 1;
			}
			
			for (int c = minC; c <= maxC; c++) {
				for (int d = 0; d < 4; d++) {
					int ni = minR + dx[d], nj = c + dy[d];
					if (0 <= ni && ni < n && 0 <= nj && nj < m && field[ni][nj] == '.') {
						dsu.uniteWithSet(minR, c, ni, nj);
					}
				}
				for (int d = 0; d < 4; d++) {
					int ni = maxR + dx[d], nj = c + dy[d];
					if (0 <= ni && ni < n && 0 <= nj && nj < m && field[ni][nj] == '.') {
						dsu.uniteWithSet(maxR, c, ni, nj);
					}
				}
			}
			for (int r = minR + 1; r < maxR; r++) {
				for (int d = 0; d < 4; d++) {
					int ni = minR + dx[d], nj = c + dy[d];
					if (0 <= ni && ni < n && 0 <= nj && nj < m && field[ni][nj] == '.') {
						dsu.uniteWithSet(minR, c, ni, nj);
					}
				}
				field[r][minC] = field[r][maxC] = '.';
				dsu.size[r * m + minC] = dsu.size[r * m + maxC] = 1;
			}
			
			
		}
		
		out.println(result.size() / 4);
		for (int i = 0; i < result.size(); i += 4) {
			out.println((result.get(i) + 1) + " " + (result.get(i + 1) + 1)
					+ " " + (result.get(i + 2) + 1) + " "
					+ (result.get(i + 3) + 1));
		}
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
		new G().run();
	}
}