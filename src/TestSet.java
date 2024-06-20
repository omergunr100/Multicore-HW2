// Simple testbed for concurrent Set implementations
// the entry-point for the testbed is the static runTest() method

class TestSet extends Thread
{
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

	public void run()
	{
		try {
			while (true) {
				/* TODO: execute a random op (and collect details) */
			}
		}
		catch (InterruptedException iex) { /* Done */ }
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

		/* TODO: print details */
	}
}
