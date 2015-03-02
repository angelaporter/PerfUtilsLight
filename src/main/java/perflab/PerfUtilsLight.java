package perflab;

public class PerfUtilsLight {

	public static PerformanceTransactions getPerformanceTransactions(){
		PerformanceTransactions transactions = PerformanceTransactions.getInstance();
		return transactions;
	}
	
	public static void PerformanceTransactionStart(PerformanceTransactions transactions, String transactionName, int iteration){
		PerformanceTransaction tr = new PerformanceTransaction(transactionName);
		tr.setBeginTimestamp();
		transactions.addPerformanceTransaction(tr, iteration,false);
	}

	public static void PerformanceTransactionEnd(PerformanceTransactions transactions, String transactionName, int iteration){
		PerformanceTransaction tr = transactions.getPerformanceTransactionByName(transactionName, iteration);
		tr.setEndTimestamp();
	}
	
	public static String PerformanceTransactionsSummary(PerformanceTransactions transactions){
		return transactions.getSummary();		
	}

}
