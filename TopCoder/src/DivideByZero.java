public class DivideByZero {
	public static int CountNumbers(int[] numbers) {
		int len = numbers.length;
		for (int i = 0; i < len; i++) {
			System.err.println(numbers[i]);
		}
		System.err.println("hi!\n");
		return 1;
	}

	public static void main(String[] args) {
		System.err.println("!!!");
		System.err.println(CountNumbers(new int[] { 1, 2, 3, 4 }));
	}
}