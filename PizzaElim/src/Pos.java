import java.io.*;
import java.util.*;

public class Pos {
	FastScanner in;
	PrintWriter out;

	void solve() {
		int tc = in.nextInt();
		System.err.println("tests " + tc);
		for (int t = 0; t < tc; t++) {
			int n = in.nextInt();
			int m = in.nextInt();
			System.err.println(n + " " + m);
			V[] a = new V[m];
			for (int i = 0; i < m; i++) {
				a[i] = new V(in.nextInt(), in.nextInt());
				a[i].pos--;
			}
			Arrays.sort(a);
			ArrayList<Segment> segms = new ArrayList<Pos.Segment>();
			segms.add(new Segment(0, 0));
			int from = 0;
			for (int ct = 0;; ) {
				if (segms.size() == 0) {
					throw new AssertionError();
				}
				boolean found = false;
				for (Segment s : segms) {
					if (s.right >= n - 1) {
						found = true;
						out.println(ct);
						out.flush();
						break;
					}
				}
				if (found) {
					break;
				}
				int nextEvent = Integer.MAX_VALUE;
				for (Segment s : segms) {
					nextEvent = Math.min(nextEvent, n - 1 - s.right);
				}
				while (from != m && a[from].time <= ct) {
					from++;
				}
				if (from != m) {
					nextEvent = Math.min(nextEvent, a[from].time - ct);
				}
				if (nextEvent <= 0) {
					throw new AssertionError();
				}
				ct += nextEvent;
				for (Segment s : segms) {
					s.left = Math.max(0, s.left - nextEvent);
					s.right = Math.min(n - 1, s.right + nextEvent);
				}
				ArrayList<Segment> next = new ArrayList<Pos.Segment>();
				for (Segment s : segms) {
					if (next.size() == 0
							|| next.get(next.size() - 1).right < s.left - 1) {
						next.add(s);
					} else {
						Segment last = next.get(next.size() - 1);
						last.right = Math.max(last.right, s.right);
					}
				}
				segms = next;
				next = new ArrayList<Pos.Segment>();
				for (Segment s : segms) {
					while (from < m && a[from].time < ct) {
						from++;
					}
					while (from != m && a[from].time == ct
							&& a[from].pos <= s.right) {
						if (s.left < a[from].pos) {
							next.add(new Segment(s.left, a[from].pos - 1));
						}
						s.left = Math.max(s.left, a[from].pos + 1);
						from++;
					}
					if (s.left <= s.right) {
						next.add(s);
					}
				}
				segms = next;
			}
		}
	}

	class V implements Comparable<V> {
		int pos, time;

		public V(int pos, int time) {
			super();
			this.pos = pos;
			this.time = time;
		}

		public int compareTo(V o) {
			if (time == o.time) {
				return Integer.compare(pos, o.pos);
			}
			return Integer.compare(time, o.time);
		}

	}

	class Segment {
		int left, right;

		public Segment(int left, int right) {
			super();
			this.left = left;
			this.right = right;
		}

		@Override
		public String toString() {
			return "Segment [left=" + left + ", right=" + right + "]";
		}

	}

	void run() {
		for (int test = 7; test <= 10; test++) {
			String tN = Integer.toString(test);
			while (tN.length() < 2) {
				tN = "0" + tN;
			}
			System.err.println("do " + tN);
			try {
				in = new FastScanner(new File("testy/pos" + tN + ".in"));
				out = new PrintWriter(new File("testy/pos" + tN + ".out"));

				solve();

				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
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
		new Pos().run();
	}
}