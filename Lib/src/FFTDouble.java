import java.io.*;
import java.util.*;

public class FFTDouble {

	void FFT(double[] re, double[] im, boolean invert) {
		int n = re.length;
		if (im.length != n)
			throw new AssertionError("Sizes of arrays differ");
		if (Integer.bitCount(n) != 1)
			throw new AssertionError("N is not power of 2");
		if (n != 1) {
			int m = n / 2;
			double[] re1 = new double[m];
			double[] im1 = new double[m];
			double[] re2 = new double[m];
			double[] im2 = new double[m];
			for (int i = 0; i < n; i += 2) {
				re1[i / 2] = re[i];
				im1[i / 2] = im[i];
				re2[i / 2] = re[i + 1];
				im2[i / 2] = im[i + 1];
			}
			FFT(re1, im1, invert);
			FFT(re2, im2, invert);
			double angle = (invert ? -1 : 1) * Math.PI * 2 / n;
			double epsR = Math.cos(angle);
			double epsI = Math.sin(angle);
			double curR = 1;
			double curI = 0;
			for (int i = 0; i < m; i++) {
				double real = curR * re2[i] - curI * im2[i];
				double imag = curI * re2[i] + curR * im2[i];
				re[i] = re1[i] + real;
				im[i] = im1[i] + imag;
				real = -real;
				imag = -imag;
				re[i + m] = re1[i] + real;
				im[i + m] = im1[i] + imag;
				double nR = curR * epsR - curI * epsI;
				double nI = curR * epsI + curI * epsR;
				curR = nR;
				curI = nI;
				if (invert) {
					re[i] /= 2.;
					im[i] /= 2;
					re[i + m] /= 2;
					im[i + m] /= 2;
				}
			}
		}
	}

	long[] mul(long[] a, long[] b) {
		int len = Math.max(a.length, b.length) * 2;
		int mLen = 1;
		while (mLen < len)
			mLen *= 2;
		double[] r1 = new double[mLen];
		double[] i1 = new double[mLen];
		for (int i = 0; i < a.length; i++)
			r1[i] = a[i];
		double[] r2 = new double[mLen];
		double[] i2 = new double[mLen];
		for (int i = 0; i < b.length; i++)
			r2[i] = b[i];
		FFT(r1, i1, false);
		FFT(r2, i2, false);
		double[] rNew = new double[mLen];
		double[] iNew = new double[mLen];
		for (int i = 0; i < mLen; i++) {
			rNew[i] = r1[i] * r2[i] - i1[i] * i2[i];
			iNew[i] = r1[i] * i2[i] + r2[i] * i1[i];
		}
		FFT(rNew, iNew, true);
		long[] res = new long[mLen];
		for (int i = 0; i < mLen; i++)
			res[i] = (long) Math.round(rNew[i]);
		return res;
	}

	long[] slowMul(long[] a, long[] b) {
		long[] res = new long[a.length + b.length + 2];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < b.length; j++)
				res[i + j] += a[i] * b[j];
		return res;
	}

	boolean same(long[] a, long[] b) {
		for (int i = 0; i < Math.max(a.length, b.length); i++) {
			if (i < a.length && i < b.length) {
				if (a[i] != b[i])
					return false;
			} else {
				if (i < a.length) {
					if (a[i] != 0)
						return false;
				} else {
					if (b[i] != 0)
						return false;
				}
			}
		}
		return true;
	}

	void ACTest() {
		for (int test = 0;; test++) {
			System.err.println(test);
			int n = 1;
			long[] a = new long[n];
			long[] b = new long[n];
			for (int i = 0; i < n; i++) {
				a[i] = rnd.nextInt(100);
				b[i] = rnd.nextInt(100);
			}
			long[] r1 = mul(a, b);
			long[] r2 = slowMul(a, b);
			if (!same(r1, r2)) {
				throw new AssertionError();
			}
		}
	}

	void TLTest() {
		for (int test = 0;; test++) {
			System.err.println(test);
			long st = System.currentTimeMillis();
			int n = 100000;
			long[] a = new long[n];
			long[] b = new long[n];
			for (int i = 0; i < n; i++) {
				a[i] = rnd.nextInt(10000);
				b[i] = rnd.nextInt(10000);
			}
			long[] r1 = mul(a, b);
			System.err.println(System.currentTimeMillis() - st + " ms");
		}
	}

	Random rnd = new Random(77);

	void realSolve() throws IOException {
		TLTest();
	}

	private class InputReader {
		StringTokenizer st;
		BufferedReader br;

		public InputReader(File f) {
			try {
				br = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		public InputReader(InputStream f) {
			br = new BufferedReader(new InputStreamReader(f));
		}

		String next() {
			while (st == null || !st.hasMoreElements()) {
				String s;
				try {
					s = br.readLine();
				} catch (IOException e) {
					return null;
				}
				if (s == null)
					return null;
				st = new StringTokenizer(s);
			}
			return st.nextToken();
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		double nextDouble() {
			return Double.parseDouble(next());
		}

		boolean hasMoreElements() {
			while (st == null || !st.hasMoreElements()) {
				String s;
				try {
					s = br.readLine();
				} catch (IOException e) {
					return false;
				}
				st = new StringTokenizer(s);
			}
			return st.hasMoreElements();
		}

		long nextLong() {
			return Long.parseLong(next());
		}
	}

	InputReader in;
	PrintWriter out;

	void solveIO() throws IOException {
		in = new InputReader(System.in);
		out = new PrintWriter(System.out);

		realSolve();

		out.close();

	}

	public static void main(String[] args) throws IOException {
		new FFTDouble().solveIO();
	}
}