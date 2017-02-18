import java.io.*;
import java.util.*;

public class G {
	FastScanner in;
	PrintWriter out;

	class Item {
		int[] p;

		public Item(int[] p) {
			this.p = p.clone();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + Arrays.hashCode(p);
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
			Item other = (Item) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (!Arrays.equals(p, other.p))
				return false;
			return true;
		}

		private G getOuterType() {
			return G.this;
		}
		
	}
	
	Map<Item, Long> length = new HashMap<Item, Long>();
	Map<Item, int[]> end = new HashMap<Item, int[]>();
	
	void go(int[] p) {
		Item resultItem = new Item(p);
		if (end.containsKey(resultItem)) {
			return;
		}
		if (p.length == 1) {
			Item item = new Item(p);
			length.put(item, 0L);
			end.put(item, p);
			return;
		}
		int fst = 0;
		while (p[fst] < p.length - 1) {
			fst++;
		}
		long result = 0;
		int[] newP = Arrays.copyOf(p, fst + 1);
		go(newP);
		Item item = new Item(newP);
		result += length.get(item);
		int[] newPrefix = end.get(item);
		
		int[] nextP = p.clone();
		for (int i = 0; i < newPrefix.length; i++) {
			nextP[i] = newPrefix[i];
		}
		result++;
		nextP = Arrays.copyOfRange(nextP, 1, nextP.length);
		
		go(nextP);
		item = new Item(nextP);
		result += length.get(item);
		newPrefix = end.get(item);
		
		int[] end = new int[p[fst] + 1];
		for (int i = 0; i < newPrefix.length; i++) {
			end[i] = newPrefix[i];
		}
		end[end.length - 1] = p[fst];
		

		length.put(resultItem, result);
		this.end.put(resultItem, end);
	}
	
	void solve() {
		int n = in.nextInt();
		int[] p = new int[n];
		int pos0 = -1;
		for (int i = 0; i < n; i++) {
			p[i] = in.nextInt();
			if (p[i] == 0) {
				pos0 = i;
			}
		}
		p = Arrays.copyOf(p, pos0 + 1);
		go(p);
		out.println(length.get(new Item(p)));
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