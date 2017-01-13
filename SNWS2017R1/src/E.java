import java.io.*;
import java.util.*;

public class E {
	FastScanner in;
	PrintWriter out;

	class State implements Comparable<State> {
		int x, y, pref, cost;

		public State(int x, int y, int pref, int cost) {
			super();
			this.x = x;
			this.y = y;
			this.pref = pref;
			this.cost = cost;
		}

		public int compareTo(State o) {
			return Integer.compare(cost, o.cost);
		}

	}

	void solve() {
		int n = in.nextInt();
		int m = in.nextInt();
		char[][] a = new char[n][];
		for (int i = 0; i < n; i++) {
			a[i] = in.next().toCharArray();
		}
		char[] cmd = in.next().toCharArray();
		PriorityQueue<State> pq = new PriorityQueue<E.State>();
		boolean[][][] was = new boolean[n][m][cmd.length + 1];
		int[][][] dist = new int[n][m][cmd.length + 1];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				Arrays.fill(dist[i][j], Integer.MAX_VALUE);
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (a[i][j] == 'P') {
					dist[i][j][0] = 0;
					pq.add(new State(i, j, 0, 0));
				}
			}
		}
		String ss = "NSEW";
		int[] dx = new int[] { -1, 1, 0, 0 };
		int[] dy = new int[] { 0, 0, 1, -1 };
		while (pq.size() > 0) {
			State s = pq.poll();
			if (was[s.x][s.y][s.pref]) {
				continue;
			}
			if (a[s.x][s.y] == 'E') {
				out.println(s.cost);
				return;
			}
			was[s.x][s.y][s.pref] = true;
			if (s.pref != cmd.length) {
				int dir = ss.indexOf(cmd[s.pref]);
				int nx = s.x + dx[dir];
				int ny = s.y + dy[dir];
				if (nx < 0 || nx >= n || ny < 0 || ny >= m || a[nx][ny] == 'X') {
					nx = s.x;
					ny = s.y;
				}
				if (dist[nx][ny][s.pref + 1] > s.cost) {
					dist[nx][ny][s.pref + 1] = s.cost;
					pq.add(new State(nx, ny, s.pref + 1, s.cost));
				}
				if (dist[s.x][s.y][s.pref + 1] > s.cost + 1) {
					dist[s.x][s.y][s.pref + 1] = s.cost + 1;
					pq.add(new State(s.x, s.y, s.pref + 1, s.cost + 1));
				}
			}
			for (int dir = 0; dir < dx.length; dir++) {
				int nx = s.x + dx[dir];
				int ny = s.y + dy[dir];
				if (nx < 0 || nx >= n || ny < 0 || ny >= m || a[nx][ny] == 'X') {
					nx = s.x;
					ny = s.y;
				}
				if (dist[nx][ny][s.pref] > s.cost + 1) {
					dist[nx][ny][s.pref] = s.cost + 1;
					pq.add(new State(nx, ny, s.pref, s.cost + 1));
				}
			}
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
		new E().runIO();
	}
}