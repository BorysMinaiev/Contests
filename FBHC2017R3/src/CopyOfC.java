import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class CopyOfC {
	FastScanner in;
	PrintWriter out;

	final long mod = (long) 1e9 + 7;

	int[] genArray(int first, int n, int a, int b, int c, int d) {
		int[] res = new int[n];
		res[0] = first;
		for (int i = 1; i < n; i++) {
			res[i] = (int) ((res[i - 1] * 1L * a + b) % c + d);
		}
		return res;
	}

	class Edge implements Comparable<Edge> {
		int to;
		long cost;

		public Edge(int to, long cost) {
			super();
			this.to = to;
			this.cost = cost;
		}

		@Override
		public int compareTo(Edge o) {
			return Long.compare(cost, o.cost);
		}

	}

	long[] getDist(int from, ArrayList<Edge>[] g) {
		int n = g.length;
		long[] dist = new long[n];
		Arrays.fill(dist, Long.MAX_VALUE);
		dist[from] = 0;
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(from, 0));
		boolean[] was = new boolean[n];
		while (pq.size() > 0) {
			Edge cur = pq.poll();
			if (was[cur.to]) {
				continue;
			}
			was[cur.to] = true;
			for (Edge e : g[cur.to]) {
				if (dist[e.to] > dist[cur.to] + e.cost) {
					dist[e.to] = dist[cur.to] + e.cost;
					pq.add(new Edge(e.to, dist[e.to]));
				}
			}
		}
		return dist;
	}

	long getDistClockwise(long[] pref, int from, int to) {
		if (from == to) {
			return 0;
		}
		if (to == 0) {
			return pref[pref.length - 1] - pref[from];
		}
		if (to < from) {
			return getDistClockwise(pref, from, 0)
					+ getDistClockwise(pref, 0, to);
		}
		return pref[to] - pref[from];
	}

	long getDistCounterClockwise(long[] pref, int from, int to) {
		return pref[pref.length - 1] - getDistClockwise(pref, from, to);
	}

	long getSum(long[] pref, int from, int to) {
		if (from < 0 || to < 0 || from > pref.length - 1
				|| to > pref.length - 1) {
			throw new AssertionError();
		}
		if (to < from) {
			return getSum(pref, from, pref.length - 2) + getSum(pref, 0, to);
		}
		return pref[to + 1] - pref[from];
	}

	Random rnd = new Random(123);

	void solveOneTest() {
		long START = System.currentTimeMillis();
		final int MAX = 1000000;
		int n = 1000000;
		int o1 = 1 + rnd.nextInt(MAX);
		int a0 = rnd.nextInt(MAX);
		int b0 = rnd.nextInt(MAX);
		int c0 = 1 + rnd.nextInt(MAX);
		int d0 = 1 + rnd.nextInt(MAX);
		int r1 = 1 + rnd.nextInt(MAX);
		int ar = rnd.nextInt(MAX);
		int br = rnd.nextInt(MAX);
		int cr = 1 + rnd.nextInt(MAX);
		int dr = 1 + rnd.nextInt(MAX);
		int[] o = genArray(o1, n, a0, b0, c0, d0);
		int[] r = genArray(r1, n, ar, br, cr, dr);
		ArrayList<Edge>[] g = new ArrayList[n + 1];
		for (int i = 0; i < g.length; i++) {
			g[i] = new ArrayList<>();
		}
		for (int i = 0; i < n; i++) {
			g[n].add(new Edge(i, r[i]));
			g[i].add(new Edge(n, r[i]));
			g[i].add(new Edge((i + 1) % n, o[i]));
			g[(i + 1) % n].add(new Edge(i, o[i]));
		}
		long[] dist = getDist(n, g);
		long result = 0;
		for (int i = 0; i < n; i++) {
			result = (result + dist[i]) % mod;
		}
		result = (result + result) % mod;
		long[] pref = new long[n + 1];
		for (int i = 0; i < n; i++) {
			pref[i + 1] = pref[i] + o[i];
		}
		long[] prefSum = new long[n + 1];
		for (int i = 0; i < n; i++) {
			prefSum[i + 1] = (prefSum[i] + dist[i]) % mod;
		}
		long[] fAdd = new long[n];
		long[] fDiff = new long[n * 2];
		long[] fAddBack = new long[n];
		long[] fDiffBack = new long[n * 2];
		for (int start = 0; start < n; start++) {
			int left = 0, right = n;
			while (right - left > 1) {
				int mid = (left + right) >> 1;
				int to = (start + mid) % n;
				if (getDistClockwise(pref, start, to) <= getDistCounterClockwise(
						pref, start, to)) {
					left = mid;
				} else {
					right = mid;
				}
			}
			int cntClock = left;
			{
				// clockwise
				left = 0;
				right = cntClock + 1;
				while (right - left > 1) {
					int mid = (left + right) >> 1;
					int to = (start + mid) % n;
					if (getDistClockwise(pref, start, to) <= dist[start]
							+ dist[to]) {
						left = mid;
					} else {
						right = mid;
					}
				}
				int cntCenter = cntClock - left;
				if (cntCenter != 0) {
					result = (result + cntCenter * (dist[start] % mod)) % mod;
					result = (result + getSum(prefSum, (start + left + 1) % n,
							(start + cntClock) % n)) % mod;
				}
				fAdd[start] = left;
				fDiff[start]--;
				fDiff[start + left]++;
			}
			{
				// counter
				int cntCounter = n - 1 - cntClock;
				left = 0;
				right = cntCounter + 1;
				while (right - left > 1) {
					int mid = (left + right) >> 1;
					int to = (start - mid + n) % n;
					if (getDistCounterClockwise(pref, start, to) <= dist[start]
							+ dist[to]) {
						left = mid;
					} else {
						right = mid;
					}
				}
				int cntCenter = cntCounter - left;
				if (cntCenter != 0) {
					result = (result + cntCenter * (dist[start] % mod)) % mod;
					result = (result + getSum(prefSum, (start - cntCounter + n)
							% n, (start - left - 1 + n) % n));
				}
				fAddBack[start] = left;
				fDiffBack[start + n]--;
				fDiffBack[start + n - left]++;
			}
		}
		long cur = 0;
		long curD = 0;
		for (int i = 0; i < n || cur > 0; i++) {
			cur += i < n ? fAdd[i] : 0;
			curD += fDiff[i];
			result = (result + cur % mod * (o[(i % n)] % mod)) % mod;
			cur += curD;
		}
		cur = 0;
		curD = 0;
		for (int i = n - 1; i >= 0 || cur > 0; i--) {
			cur += i >= 0 ? fAddBack[i] : 0;
			curD += fDiffBack[i + n];
			result = (result + cur % mod * (o[(i - 1 + n) % n] % mod)) % mod;
			cur += curD;
		}
		out.println(result
				* BigInteger.valueOf(2).modInverse(BigInteger.valueOf(mod))
						.intValue() % mod);
		System.err.println(System.currentTimeMillis() - START);
	}

	void solve() {
		int tc = 123123;
		for (int t = 0; t < tc; t++) {
			System.err.println("test " + t);
			out.print("Case #" + (t + 1) + ": ");
			solveOneTest();
		}
	}

	void run() {
		try {
			in = new FastScanner(new File("C.in"));
			out = new PrintWriter(new File("C.out"));

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
		new CopyOfC().run();
	}
}