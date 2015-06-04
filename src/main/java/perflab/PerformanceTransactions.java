package perflab;

import java.util.ArrayList;
import java.util.HashMap;

public class PerformanceTransactions {
	
	//Collection of performance transactions
	//private ArrayList<PerformanceTransaction> transactions = null;
	//private ArrayList<ArrayList<PerformanceTransaction>> iterative_transactions = null;
	private HashMap<Integer, ArrayList<PerformanceTransaction>> transactions = null;
	
	/* Singletone implementation start */
	public static PerformanceTransactions instance = null;
	
	public static PerformanceTransactions getInstance() {
		   if(instance == null) {
			   instance = new PerformanceTransactions ();
		   }
		   return instance;
	}
	
	protected PerformanceTransactions() {
		//this.transactions = new ArrayList<PerformanceTransaction>();
		this.transactions = new HashMap<Integer, ArrayList<PerformanceTransaction>>();
	}
	/* Singletone implementation end */
	
	public HashMap<Integer, ArrayList<PerformanceTransaction>> getTransactions() {
		//return this.transactions;
		return this.transactions;
	}
	
	public PerformanceTransaction getPerformanceTransactionByName(String transactionName, int iteration){
		PerformanceTransaction tr = null;
		
		if(transactions.containsKey(Integer.valueOf(iteration))){
			if(transactions.get(Integer.valueOf(iteration)) != null){
				for (PerformanceTransaction p : transactions.get(Integer.valueOf(iteration)) ){
					if (p.getName().equals(transactionName)){
						tr = p;
						break;
					}
				}
			}
		}
		
		return tr;
	}

	public boolean removePerformanceTransactionbyName(String transactionName, int iteration){
		boolean removed = false;
		for (PerformanceTransaction tr : transactions.get(Integer.valueOf(iteration))){
		    if (tr.getName().equals(transactionName)){
		    	transactions.get(Integer.valueOf(iteration)).remove(tr);
		    	removed = true;
		    	break;
		    }
		}
		return removed;
	}

	
	public boolean addPerformanceTransaction(PerformanceTransaction tr, int iteration, boolean overwrite){
		//Reject transactions without name
		if(tr.getName().isEmpty()){ 
			return false;
		}

		if(!overwrite){
			//Reject transactions with same name as already exists
			if(this.getPerformanceTransactionByName(tr.getName(), iteration) != null) {	
				return false;
			}				
		}else{
			this.removePerformanceTransactionbyName(tr.getName(), iteration);
		}
		
		if(!transactions.containsKey(Integer.valueOf(iteration))){
			this.transactions.put(Integer.valueOf(iteration), new ArrayList<PerformanceTransaction>());
		}
		transactions.get(Integer.valueOf(iteration)).add(tr);
				
		return true;
	}


	public String getSummary() {
		String summary = "";
	
		HashMap<String, Long> averages = new HashMap<String, Long>(); 
		
		/*Detailed - per iteration*/
		int iterationNumber = 0;
		
		
		for (Integer iterationKey : this.transactions.keySet()){
			iterationNumber++;
			for (PerformanceTransaction tr : this.transactions.get(iterationKey)){
				String name = tr.getName();
				long duration = (tr.getEndTimestamp() - tr.getBeginTimestamp());
				
				summary += iterationNumber + " : " + name + " : " + duration + "\n";
				
				if( averages.containsKey(name) ){
					averages.put(name, averages.get(name) + duration);
				}else{
					averages.put(name, duration);
				}
				
			}
		}
		
		
		/*Averages*/
		for (String key : averages.keySet()) {
			summary += "Avg" + " : " + key + " : " + ( averages.get(key)/iterationNumber ) + "\n";		    
		}
		
		return summary;
	}	
	
	public String getAveragesCsv() {
		String headers = "";
		String body = "";
	
		HashMap<String, Long> averages = new HashMap<String, Long>(); 
		
		/*Detailed - per iteration*/
		int iterationNumber = 0;
		
		
		for (Integer iterationKey : this.transactions.keySet()){
			iterationNumber++;
			for (PerformanceTransaction tr : this.transactions.get(iterationKey)){
				String name = tr.getName();
				long duration = (tr.getEndTimestamp() - tr.getBeginTimestamp());					
				
				if( averages.containsKey(name) ){
					averages.put(name, averages.get(name) + duration);
				}else{
					averages.put(name, duration);
				}
				
			}
		}
		
		
		/*Averages headers*/
		int i = 0;
		for (String key : averages.keySet()) {
			i++;
			headers += key;
			if (i < averages.size()){
				headers +=	"," ;	
			}	    
		}
		headers += "\n";
		
		/*Averages csv*/
		i = 0;
		for (String key : averages.keySet()) {
			i++;
			body +=  (averages.get(key)/iterationNumber) + "" ;
			if (i < averages.size()){
				body +=	"," ;	
			}
			
		}
		body += "\n";
		String csv = headers + body;
		
		return csv;
	}	
}
