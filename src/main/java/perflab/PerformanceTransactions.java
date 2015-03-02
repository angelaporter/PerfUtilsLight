package perflab;

import java.util.ArrayList;
import java.util.HashMap;

public class PerformanceTransactions {
	
	//Collection of performance transactions
	//private ArrayList<PerformanceTransaction> transactions = null;
	private ArrayList<ArrayList<PerformanceTransaction>> iterative_transactions = null;
	
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
		this.iterative_transactions = new ArrayList<ArrayList<PerformanceTransaction>>();
	}
	/* Singletone implementation end */
	
	public ArrayList<ArrayList<PerformanceTransaction>> getTransactions() {
		//return this.transactions;
		return this.iterative_transactions;
	}
	
	public PerformanceTransaction getPerformanceTransactionByName(String transactionName, int iteration){
		PerformanceTransaction tr = null;
		for (PerformanceTransaction p : this.iterative_transactions.get(iteration)){
		    if (p.getName().equals(transactionName)){
		    	tr = p;
		    	break;
		    }
		}
		return tr;
	}

	public boolean removePerformanceTransactionbyName(String transactionName, int iteration){
		boolean removed = false;
		for (PerformanceTransaction tr : this.iterative_transactions.get(iteration)){
		    if (tr.getName().equals(transactionName)){
		    	this.iterative_transactions.get(iteration).remove(tr);
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
		
		this.iterative_transactions.get(iteration).add(tr);
		return true;
	}

	public String getSummary() {
		String summary = "";
	
		HashMap<String, Long> averages = new HashMap<String, Long>(); 
		
		/*Detailed - per iteration*/
		int iterationNumber = 0;
		for (ArrayList<PerformanceTransaction> iteration :this.iterative_transactions) {
			iterationNumber++;
			for (PerformanceTransaction tr : iteration){
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
}
