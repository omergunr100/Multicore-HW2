public class MppRunner {
    public static void main(String[] args) {
        for (int i = 1; i <= 64; i *= 2) {
            System.out.println("Threads (" + i + ") operations");
            TestSet.runTest(new BSTOptSet(), i, 2^17, 90, 9, 10_000);
        }
    }
}
