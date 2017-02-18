import java.io.*;
import java.util.*;

public class CopyOfE {
	FastScanner in;
	PrintWriter out;

	final int SQRT = 333;

	int[] blockId;
	int[] blockPos;
	byte[][][] bits;
	int[][] sum;
	int[] totSum;

	int k;

	int getSum(int fr, int to) {
		int res = 0;
		int bId = blockId[fr];
		int it = fr % SQRT;
		while (fr <= to && blockId[fr] == bId) {
			for (int z = 1; z < k; z++) {
				res += bits[bId][z][it] * z;
			}
			it++;
			fr++;
		}
		while (fr <= to && blockId[fr] != blockId[to]) {
			res += totSum[blockId[fr]];
			fr += SQRT;
		}
		if (fr <= to) {
			it = 0;
			bId = blockId[fr];
			while (fr <= to) {
				for (int z = 1; z < k; z++) {
					res += bits[bId][z][it] * z;
				}
				fr++;
				it++;
			}
		}
		return res;
	}

	byte[][] tmpB = new byte[10][];
	int[] tmpI = new int[10];

	void changeBlock(int bId, int d) {
		for (int z = 1; z < k; z++) {
			totSum[bId] -= z * sum[bId][z];
		}
		for (int z = 0; z < k; z++) {
			tmpB[z] = bits[bId][z];
			tmpI[z] = sum[bId][z];
		}
		int n = d;
		for (int z = 0; z < k; z++) {
			bits[bId][n] = tmpB[z];
			sum[bId][n] = tmpI[z];
			totSum[bId] += n * tmpI[z];
			n++;
			if (n == k) {
				n = 0;
			}
		}
	}

	int n;

	void print() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < k; j++) {
				if (bits[blockId[i]][j][i % SQRT] == 1) {
					System.err.print(j + " ");
				}
			}
		}
		System.err.println();
	}

	void change(int fr, int to, int d) {
		if (d == 0) {
			return;
		}
		int bId = blockId[fr];
		int it = fr % SQRT;
		while (fr <= to && blockId[fr] == bId) {
			for (int z = 0; z < k; z++) {
				if (bits[bId][z][it] == 1) {
					bits[bId][z][it] = 0;
					totSum[bId] -= z;
					sum[bId][z]--;
					int n = (z + d) % k;
					totSum[bId] += n;
					sum[bId][n]++;
					bits[bId][n][it] = 1;
					break;
				}
			}
			it++;
			fr++;
		}
		while (fr <= to && blockId[fr] != blockId[to]) {
			changeBlock(blockId[fr], d);
			fr += SQRT;
		}
		if (fr <= to) {
			it = 0;
			bId = blockId[fr];
			while (fr <= to) {
				for (int z = 0; z < k; z++) {
					if (bits[bId][z][it] == 1) {
						bits[bId][z][it] = 0;
						totSum[bId] -= z;
						sum[bId][z]--;
						int n = (z + d) % k;
						totSum[bId] += n;
						sum[bId][n]++;
						bits[bId][n][it] = 1;
						break;
					}
				}
				it++;
				fr++;
			}
		}
	}

	void solve() {
		long START = System.currentTimeMillis();
		long xor = 0;
		n = 100000;
		k = 10;
		int q = 100000;
		Random rnd = new Random(123);
		int blocks = 1 + (n - 1) / SQRT;
		blockId = new int[n];
		for (int i = 0; i < n; i++) {
			blockId[i] = i / SQRT;
		}
		bits = new byte[blocks][k][SQRT];
		sum = new int[blocks][k];
		totSum = new int[blocks];
		blockPos = new int[n];
		for (int i = 0; i < n; i++) {
			blockPos[i] = i % SQRT;
		}
		for (int i = 0; i < n; i++) {
			int val = rnd.nextInt(k);
			bits[blockId[i]][val][blockPos[i]] = 1;
			sum[blockId[i]][val]++;
		}
		for (int i = 0; i < blocks; i++) {
			for (int j = 0; j < k; j++) {
				totSum[i] += j * sum[i][j];
			}
		}
		for (int i = 0; i < q; i++) {
			if (rnd.nextDouble() < 0.9) {
				int fr = rnd.nextInt(n);
				int to = rnd.nextInt(n);
				if (fr > to) {
					int t = fr;
					fr = to;
					to = t;
				}
				int diff = rnd.nextInt(k);
				change(fr, to, diff);
			} else {
				int fr = 1;
				int to = n - 1;
				if (fr > to) {
					int t = fr;
					fr = to;
					to = t;
				}
				xor ^= getSum(fr, to);
			}
		}
		System.err.println("xr = " + xor);
		System.err.println(System.currentTimeMillis() - START);
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
		new CopyOfE().runIO();
	}
}