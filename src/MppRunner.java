public class MppRunner {
    public static void main(String[] args) {
        for (int i = 1; i <= 32; i *= 2) {
            System.out.println("Threads (" + i + ") operations");
            TestSet.runTest(new BSTFineSet(), i, 255, 90, 9, 10_000);
        }
    }
}
