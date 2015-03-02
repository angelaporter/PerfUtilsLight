package perflab;

/**
 * Performance Transaction object
 *
 */
public class PerformanceTransaction {
	private String name = "";
	private long beginTimestamp = 0; 	
	private long endTimestamp = 0;
	
	public PerformanceTransaction(String name){
		this.name = name;
	}
	
	public long getBeginTimestamp() {
		return beginTimestamp;
	}
	
	public void setBeginTimestamp() {
		this.beginTimestamp = System.currentTimeMillis();
	}
	
	public void setBeginTimestamp(long beginTimestamp) {
		this.beginTimestamp = beginTimestamp;
	}
	public long getEndTimestamp() {
		return endTimestamp;
	}
	public void setEndTimestamp() {
		this.endTimestamp = System.currentTimeMillis();
	}
	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public long getDurationMillis(){
		return (this.endTimestamp - this.beginTimestamp);
	}
}
