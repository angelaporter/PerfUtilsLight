package perflab;

import java.util.Random;

import junit.framework.TestCase;

public class PerfUtilsLightTest extends TestCase {

	PerformanceTransactions transactions;

	protected void setUp() throws Exception {
		super.setUp();
		transactions = PerformanceTransactions.getInstance();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testFullCycle() {
		Random randomGenerator = null;
		randomGenerator = new Random();
		int randomInt  = 0;
		for (int iteration = 0; iteration < 4; iteration++) {

			PerfUtilsLight.PerformanceTransactionStart(transactions, "trans_1",
					iteration);
			// replace with a transaction
			try {
				
				randomInt = randomGenerator.nextInt(20);
				Thread.sleep(10 + randomInt);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PerfUtilsLight.PerformanceTransactionEnd(transactions, "trans_1",
					iteration);

			PerfUtilsLight.PerformanceTransactionStart(transactions, "trans_2",
					iteration);

			try {
				randomInt = randomGenerator.nextInt(20);
				Thread.sleep(15 + randomInt);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PerfUtilsLight.PerformanceTransactionEnd(transactions, "trans_2",
					iteration);

		}

		String summary = PerfUtilsLight
				.PerformanceTransactionsSummary(transactions);
		System.out.println(summary);
		
		summary = PerfUtilsLight.PerformanceTransactionsAverageCsv(transactions);
		
		PerfUtilsLight.WriteCSV(summary , "C:/Jenkins/workspace/metrics.csv");
		
		System.out.println(summary);

	}
}
