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
		
		for (int iteration = 0; iteration <= 10 ; iteration++)
		{
		
			PerfUtilsLight.PerformanceTransactionStart(transactions, "trans_1", iteration);
			
			try {
				Thread.sleep(1000 + iteration );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PerfUtilsLight.PerformanceTransactionEnd(transactions, "trans_1", iteration);
	
			PerfUtilsLight.PerformanceTransactionStart(transactions, "trans_2", iteration);
			
			try {
				Thread.sleep( 123 + iteration );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PerfUtilsLight.PerformanceTransactionEnd(transactions, "trans_2", iteration);

		}
		
		String summary = PerfUtilsLight.PerformanceTransactionsSummary(transactions);
		
		System.out.println(summary);
		
	}	
}
