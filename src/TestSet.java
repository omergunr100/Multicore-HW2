// Simple testbed for concurrent Set implementations
// the entry-point for the testbed is the static runTest() method

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

class TestSet extends Thread
{
    public volatile AtomicLong operations = new AtomicLong(0); 
	private final Set	set;
	private final int	keyRange;
	private final int	percentContains;
	private final int	percentAdd;

	public TestSet(Set set, int keyRange, int percentContains, int percentAdd)
	{
		this.set = set;
		this.keyRange = keyRange;
		this.percentContains = percentContains;
		this.percentAdd = percentAdd;
	}

    @Override
	public void run()
	{
        Random rand = new Random();
        int percentileRemove = percentContains + percentAdd;
        while (!interrupted()) {
            // perform a random operation
            int rand_int = rand.nextInt(1, 100);
            if (rand_int <= percentContains) {
                set.contains(rand.nextInt(0, keyRange));
            } else if (rand_int < percentileRemove) {
                set.add(rand.nextInt(0, keyRange));
            } else {
                set.remove(rand.nextInt(0, keyRange));
            }
            operations.incrementAndGet();
        }
	}

	/** This is the base method for the TestSet class, which executes a single test.
	 * @param set the set to test
	 * @param numThreads number of threads for the current set
	 * @param keyRange range of keys to test [0,keyRange)
	 * @param perCon percentage of contains(x) operations
	 * @param perAdd percentage of add(x) operations
	 * @param ms length of test (in milliseconds)
	 */
	public static void runTest(Set set, int numThreads, int keyRange, int perCon, int perAdd, int ms)
	{
		// create all threads for the test
		TestSet[] threads = new TestSet[numThreads];
		for (int i = 0; i < numThreads; ++i)
			threads[i] = new TestSet(set, keyRange, perCon, perAdd);

		// start threads
		for (int i = 0; i < numThreads; ++i) {
			threads[i].start();
		}

		// wait
		try {
			Thread.sleep(ms);
		} catch (InterruptedException iex) {}

		// stop threads
		for (int i = 0; i < numThreads; ++i) {
			threads[i].interrupt();
		}
        
        // wait a bit
        try {
            Thread.sleep(100);
        } catch (InterruptedException iex) {}
        
        // wait for threads to finish
        for (int i = 0; i < numThreads; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException iex) {}
        }
        
        // sum up and print the total number of operations
        System.out.println("Total operations: " + Arrays.stream(threads).mapToLong(t -> t.operations.get()).sum());
	}
}
