package SocketGame;
import java.util.StringTokenizer;

public class MyTokenizer {
	private StringTokenizer st;

	public String nextWord;

	void updateNextWord() {
		if (st.hasMoreTokens()) {
			nextWord = st.nextToken();
		} else {
			nextWord = null;
		}

	}

	boolean hasMoreElements() {
		return nextWord != null;
	}

	MyTokenizer(String s) {
		st = new StringTokenizer(s);
		updateNextWord();
	}

	public String nextToken() {
		String res = nextWord;
		updateNextWord();
		return res;
	}

	public int nextInt() {
		int res = Integer.parseInt(nextWord);
		updateNextWord();
		return res;
	}

	public long nextLong() {
		long res = Long.parseLong(nextWord);
		updateNextWord();
		return res;
	}
}
