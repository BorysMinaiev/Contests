import java.io.*;
import java.util.*;

public class I {
	FastScanner in;
	PrintWriter out;

	class State {
		int[] color;
		int posA, posB;
		int moveId;

		public State(int[] color, int posA, int posB, int moveId) {
			super();
			this.color = color;
			this.posA = posA;
			this.posB = posB;
			this.moveId = moveId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + Arrays.hashCode(color);
			result = prime * result + moveId;
			result = prime * result + posA;
			result = prime * result + posB;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			State other = (State) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (!Arrays.equals(color, other.color))
				return false;
			if (moveId != other.moveId)
				return false;
			if (posA != other.posA)
				return false;
			if (posB != other.posB)
				return false;
			return true;
		}

		private I getOuterType() {
			return I.this;
		}

	}

	HashMap<State, Integer> answers = new HashMap<I.State, Integer>();

	int k;
	ArrayList<Integer>[] g;

	int solve(ArrayList<Integer>[] g, int a, int b, int k) {
		answers.clear();
		this.k = k;
		this.g = g;
		int[] color = new int[g.length];
		color[a] = 1;
		color[b] = -1;
		State s = new State(color, a, b, 0);
		return getAnswer(s);
	}

	int getAnswer(State s) {
		if (answers.containsKey(s)) {
			return answers.get(s);
		}
		if (s.moveId == k) {
			int res = 0;
			for (int i = 0; i < s.color.length; i++) {
				res += s.color[i];
			}
			answers.put(s, res);
			return res;
		}
		int res = 0;
		if (s.moveId % 2 == 0) {
			res = Integer.MIN_VALUE;
			for (int to : g[s.posA]) {
				int[] next = s.color.clone();
				next[to] = 1;
				res = Math.max(res, getAnswer(new State(next, to, s.posB,
						s.moveId + 1)));
			}
			if (res == Integer.MIN_VALUE) {
				System.err.println(s.posA + " " + Arrays.toString(g));
				throw new AssertionError();
			}
		} else {
			res = Integer.MAX_VALUE;
			for (int to : g[s.posB]) {
				int[] next = s.color.clone();
				next[to] = -1;
				res = Math.min(res, getAnswer(new State(next, s.posA, to,
						s.moveId + 1)));
			}
		}
		answers.put(s, res);
		return res;
	}

	int[] p;

	int get(int x) {
		return p[x] == x ? x : (p[x] = get(p[x]));
	}

	void unite(int x, int y) {
		p[get(x)] = get(y);
	}

	int getDist(int v, int p, int need, ArrayList<Integer>[] g) {
		if (v == need) {
			return 0;
		}
		for (int to : g[v]) {
			if (to != p) {
				int res = getDist(to, v, need, g);
				if (res != Integer.MAX_VALUE) {
					return res + 1;
				}
			}
		}
		return Integer.MAX_VALUE;
	}

	void dfs(int v, int p, boolean[] used, int h, ArrayList<Integer>[] g) {
		used[v] = true;
		if (h != 0) {
			for (int to : g[v]) {
				if (to == p) {
					continue;
				}
				dfs(to, v, used, h - 1, g);
			}
		}
	}

	int dfsMax(int v, int p, boolean[] used, ArrayList<Integer>[] g) {
		int res = 0;
		for (int to : g[v]) {
			if (to == p || used[to]) {
				continue;
			}
			res = Math.max(res, 1 + dfsMax(to, v, used, g));
		}
		return res;
	}

	int getMaxPath(ArrayList<Integer>[] g, int a, int b, int canMoveB) {
		boolean[] used = new boolean[g.length];
		dfs(b, b, used, canMoveB, g);
		return dfsMax(a, a, used, g);
	}

	int[] h;
	int time;
	int n;
	int[] tin, tout;

	final int MAX = 20;

	int[][] up;

	void go(int v, int p, int curH) {
		maxGoDown[v] = 0;
		tin[v] = time++;
		h[v] = curH;
		up[0][v] = p;
		for (int i = 1; i < MAX; i++) {
			up[i][v] = up[i - 1][up[i - 1][v]];
		}
		for (int i = 0; i < g[v].size(); i++) {
			int to = g[v].get(i);
			if (to == p) {
				continue;
			}
			go(to, v, curH + 1);
			maxGoDown[v] = Math.max(maxGoDown[v], maxGoDown[to] + 1);
		}
		tout[v] = time - 1;
	}

	boolean inside(int x, int y) {
		return tin[y] >= tin[x] && tin[y] <= tout[x];
	}

	int lca(int x, int y) {
		for (int i = MAX - 1; i >= 0; i--) {
			if (!inside(up[i][x], y)) {
				x = up[i][x];
			}
		}
		return inside(x, y) ? x : up[0][x];
	}

	int getDistFast(int a, int b) {
		int lca = lca(a, b);
		return h[a] + h[b] - 2 * h[lca];
	}

	int goUp(int v, int h) {
		for (int i = 0; i < MAX; i++) {
			if (((1 << i) & h) != 0) {
				v = up[i][v];
			}
		}
		return v;
	}

	int getMaxPathFast(int a, int deny) {
		if (a == deny) {
			throw new AssertionError();
		}
		if (inside(a, deny)) {
			int res = maxGoUp[a];
			int dh = h[deny] - h[a];
			for (int i = 0; i < MAX; i++) {
				if (((1 << i) & dh) != 0) {
					res = Math.max(res, dh + upSub[i][deny]);
					deny = up[i][deny];
					dh -= (1 << i);
				}
			}
			return res;
		} else {
			int res = maxGoDown[a];
			int lca = lca(a, deny);
			if (deny != lca) {
				int dh = h[deny] - h[lca] - 1;
				for (int i = 0; i < MAX; i++) {
					if (((1 << i) & dh) != 0) {
						res = Math.max(res, 1 + dh + upSub[i][deny] + h[a]
								- h[lca]);
						deny = up[i][deny];
						dh -= 1 << i;
					}
				}
			}
			int na = a;
			if (a != lca) {
				int dh = h[a] - h[lca] - 1;
				for (int i = 0; i < MAX; i++) {
					if (((1 << i) & dh) != 0) {
						res = Math.max(res, upAdd[i][na] + h[a] - h[na]);
						na = up[i][na];
					}
				}
			}
			if (deny == lca) {
				return res;
			}
			if (a != lca) {
				res = Math.max(
						res,
						h[a]
								- h[lca]
								+ Math.max(maxGoUp[lca],
										max3[lca].getMax(na, deny)));
			} else {
				res = Math.max(res,
						Math.max(maxGoUp[lca], max3[lca].getMax(deny, -1)));
			}
			return res;
		}
	}

	Max3[] max3;

	class Max3 {
		int[] id;
		int[] val;
		int sz;

		Max3() {
			id = new int[4];
			val = new int[4];
		}

		void add(int pos, int value) {
			id[sz] = pos;
			val[sz] = value;
			sz++;
			for (int i = sz - 1; i > 0; i--) {
				if (val[i - 1] < val[i]) {
					int tmp = val[i];
					val[i] = val[i - 1];
					val[i - 1] = tmp;
					tmp = id[i];
					id[i] = id[i - 1];
					id[i - 1] = tmp;
				}
			}
			sz = Math.min(sz, 3);
		}

		int getMax(int notX, int notY) {
			for (int i = 0; i < sz; i++) {
				if (id[i] != notX && id[i] != notY) {
					return val[i];
				}
			}
			return 0;
		}
	}

	int getMaxPathFast(int a, int b, int canMoveB) {
		int lca = lca(a, b);
		int distToLCA = h[b] - h[lca];
		int deny = -1;
		if (distToLCA >= canMoveB) {
			deny = goUp(b, canMoveB);
		} else {
			canMoveB -= distToLCA;
			int more = h[a] - h[lca];
			if (more <= canMoveB) {
				return -1;
			}
			deny = goUp(a, more - canMoveB);
		}
		int res = getMaxPathFast(a, deny);
//		int correct = getMaxPath(g, a, b, prCanMove);
//		if (res != correct) {
//			System.err.println("my = " + res);
//			System.err.println("correct = " + correct);
//			System.err.println("g = " + Arrays.toString(g));
//			System.err.println("A = " + a + " , b = " + b + ", canMove =  "
//					+ prCanMove);
//			System.err.println("Deny =" + deny);
//			throw new AssertionError();
//		}
		return res;
	}

	int solveFast(ArrayList<Integer>[] g, int a, int b, int k) {
		int dist = getDistFast(a, b);
		if (k < dist) {
			return k % 2;
		}
		if ((k % 2) != (dist % 2)) {
			return k % 2;
		}
		if (dist % 2 == 0) {
			if (k >= dist * 2) {
				return -1;
			}
			int maxPath = getMaxPathFast(a, b, k / 2);
			if (maxPath >= k / 2) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (k >= 2 * dist - 1) {
				return 2;
			}
			int maxPath = getMaxPathFast(b, a, (k + 1) / 2);
			if (maxPath >= k / 2) {
				return 1;
			} else {
				return 2;
			}
		}
	}

	int[] maxGoDown;
	int[] maxGoUp;
	int[] maxGoUpOnce;
	int[][] upAdd;
	int[][] upSub;

	void go2(int v, int p, int maxUp, int maxOnce) {
		maxGoUp[v] = maxUp;
		maxGoUpOnce[v] = maxOnce;
		upAdd[0][v] = maxGoUpOnce[v];
		upSub[0][v] = maxGoUpOnce[v] - 2;
		for (int i = 1; i < MAX; i++) {
			upAdd[i][v] = upAdd[i - 1][v];
			upSub[i][v] = upSub[i - 1][v];
			if (h[v] >= 1 << (i - 1)) {
				upAdd[i][v] = Math.max(upAdd[i][v], upAdd[i - 1][up[i - 1][v]]
						+ (1 << (i - 1)));
				upSub[i][v] = Math.max(upSub[i][v], upSub[i - 1][up[i - 1][v]]
						- (1 << (i - 1)));
			}
		}
		int[] prefDown = new int[g[v].size() + 1];
		prefDown[0] = 0;
		for (int i = 0; i < g[v].size(); i++) {
			int to = g[v].get(i);
			prefDown[i + 1] = prefDown[i];
			if (to == p) {
				continue;
			}
			prefDown[i + 1] = Math.max(prefDown[i + 1], maxGoDown[to] + 1);
		}
		int rightMax = 0;
		for (int i = g[v].size() - 1; i >= 0; i--) {
			int to = g[v].get(i);
			if (to == p) {
				continue;
			}
			max3[v].add(to, 1 + maxGoDown[to]);
			go2(to, v, 1 + Math.max(maxUp, Math.max(rightMax, prefDown[i])),
					1 + Math.max(rightMax, prefDown[i]));
			rightMax = Math.max(rightMax, maxGoDown[to] + 1);
		}
	}

	void precalc(ArrayList<Integer>[] g) {
		this.g = g;
		this.n = g.length;
		time = 0;
		tin = new int[n];
		tout = new int[n];
		h = new int[n];
		up = new int[MAX][n];
		maxGoDown = new int[n];
		go(0, 0, 0);
		maxGoUpOnce = new int[n];
		maxGoUp = new int[n];
		upAdd = new int[MAX][n];
		upSub = new int[MAX][n];
		max3 = new Max3[n];
		for (int i = 0; i < n; i++) {
			max3[i] = new Max3();
		}
		go2(0, 0, 0, 0);
	}

	int solveOk(ArrayList<Integer>[] g, int a, int b, int k) {
		int dist = getDist(a, a, b, g);
		if (k < dist) {
			return k % 2;
		}
		if ((k % 2) != (dist % 2)) {
			return k % 2;
		}
		if (dist % 2 == 0) {
			if (k >= dist * 2) {
				return -1;
			}
			int maxPath = getMaxPath(g, a, b, k / 2);
			if (maxPath >= k / 2) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (k >= 2 * dist - 1) {
				return 2;
			}
			int maxPath = getMaxPath(g, b, a, (k + 1) / 2);
			if (maxPath >= k / 2) {
				return 1;
			} else {
				return 2;
			}
		}
	}

	void solve123() {
		Random rnd = new Random(123);
		HashSet<Integer> all = new HashSet<Integer>();

		HashSet<Integer>[][] answers = new HashSet[12][24];
		for (int i = 0; i < answers.length; i++) {
			for (int j = 0; j < answers[i].length; j++) {
				answers[i][j] = new HashSet<Integer>();
			}
		}

		final int M = 200000;

		for (int it = 0; it < 123123; it++) {
			System.err.println(it);
			int n = M;//2 + rnd.nextInt(M);
			ArrayList<Integer>[] g = new ArrayList[n];
			for (int i = 0; i < n; i++) {
				g[i] = new ArrayList<Integer>();
			}
			p = new int[n];
			for (int i = 0; i < n; i++) {
				p[i] = i;
			}
			for (int i = 0; i + 1 < n; i++) {
				int fr = rnd.nextInt(n);
				int to = rnd.nextInt(n);
				if (get(fr) == get(to)) {
					i--;
					continue;
				}
				g[fr].add(to);
				g[to].add(fr);
				unite(fr, to);
			}
			int a = 0, b = 0;
			while (a == b) {
				a = rnd.nextInt(n);
				b = rnd.nextInt(n);
			}
			int k = 1 + rnd.nextInt(2 * n);
//			all.add(solve(g, a, b, k));
			int dist = getDist(a, a, b, g);
			// System.err.println("all size = " + all.size());
//			int res = solveOk(g, a, b, k);
			int my = solveFast(g, a, b, k);
//			if (res != my) {
//				System.err.println("my =" + my);
//				System.err.println("correct = " + res);
//				throw new AssertionError();
//			}
			// answers[dist][k].add(res);
			// System.err.println(n + " " + k + " " + solve(g, a, b, k));
		}
		for (int i = 0; i < answers.length; i++) {
			for (int j = 0; j < answers[i].length; j++) {
				if (answers[i][j].size() != 0)
					System.err.println(i + "  " + j + " " + answers[i][j]);
			}
		}
	}

	void solve() {
		while (in.hasMoreTokens()) {
			int n = in.nextInt();
			int q = in.nextInt();
			ArrayList<Integer>[] g = new ArrayList[n];
			for (int i = 0; i < n; i++) {
				g[i] = new ArrayList<Integer>();
			}
			for (int i = 0; i + 1 < n; i++) {
				int fr = in.nextInt() - 1;
				int to = in.nextInt() - 1;
				g[fr].add(to);
				g[to].add(fr);
			}
			precalc(g);
			for (int i = 0; i < q; i++) {
				int fr = in.nextInt() - 1;
				int to = in.nextInt() - 1;
				int k = in.nextInt();
				out.println(solveFast(g, fr, to, k));
			}
		}
	}

	void run() {
		try {
			in = new FastScanner(new File("I.in"));
			out = new PrintWriter(new File("I.out"));

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
		new I().run();
	}
}