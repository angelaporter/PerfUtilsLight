package perflab;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Environment {
	
	
	private static Properties confProps = new Properties();
	static String configFileSuffix = System.getProperty("testDataFile");
	
	static {
		//InputStream is = Environment.class.getResourceAsStream("/"+ configFileSuffix);		
		InputStream is = Environment.class.getResourceAsStream("C:/selenium/PerfUtilsLight/src/test/java/perflab/test_data.properties");
		System.out.println("Configuration properties file is " + is);
		try {			
			
			confProps.load(is);
		} 
		catch (Exception e) {
			System.out.println("Could not load Environment properties file." + e.getMessage());
		}
		finally{
			try {
				is.close();
			} catch (IOException e) {
				System.out.println("Could not close connection to Environment properties file." + e.getMessage());
				e.printStackTrace();
			}
		}			
	}
	
	public static String getParameter (String key) {
		return confProps.getProperty(key);
	}

}
