public class ConsecutiveOnes {
	public static long get(long val, int k) {
		if ((1L << k) > val) {
			return (1L << k) - 1;
		}
		String s = Long.toBinaryString(val);
		String need = "";
		for (int i = 0; i < k; i++) {
			need += "1";
		}
		if (s.contains(need)) {
			return val;
		}
		long best = Long.MAX_VALUE;
		for (int start = 0; start + k <= s.length(); start++) {
			char[] next = s.toCharArray();
			for (int i = 0; i < k; i++) {
				next[i + start] = '1';
			}
			for (int i = start + k; i < s.length(); i++) {
				next[i] = '0';
			}
			long v = Long.parseLong(new String(next), 2);
			best = Math.min(best, v);
		}
		return best;
	}
}
