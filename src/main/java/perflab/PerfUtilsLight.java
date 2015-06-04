package perflab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

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
	
	public static String PerformanceTransactionsAverageCsv(PerformanceTransactions transactions){
		return transactions.getAveragesCsv();		
	}
	
	public static void WriteCSV (String csv , String path){
		try {
			Files.write(Paths.get(path), csv.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
