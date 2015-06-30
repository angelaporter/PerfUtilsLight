package perflab;

public class SeleniumConnectivity {
	
	
	public static long getWaitTimeout() {

		return Long.parseLong(Environment
				.getParameter("driver.timeout.seconds"));
	}

	
	

}
