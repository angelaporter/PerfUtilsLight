package perflab;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CopyOfSeleniumTest extends TestCase {	
	
	private static String url = null;
	private static String username = null;
	private static String password = null;
	private static String proxyURL = null;
	private static String proxyBypass = null;
	
	PerformanceTransactions transactions;
	HashMap<String, String> test_data;
	static WebDriver m_driver = null;
	int numOfExpectedObjects = 17;
	boolean sso = true;
    private String m_systemType, _userName, _password, m_siteURL, m_pathTostartup;
    
	
	protected void setUp() throws Exception {	
		Runtime.getRuntime().exec("taskkill /f /IM chromedriver.exe ");
		super.setUp();
		
		transactions = PerformanceTransactions.getInstance();
		test_data = SeleniumVerificationUtils.readCSV("C:/selenium/PerfUtilsLight/src/test/java/perflab/test_data.csv");
		
	    _userName = test_data.get("UserName").toString();
		System.out.println("UserName: " + _userName);
		_password = test_data.get("Password").toString();
		System.out.println("Password: " + _password);

		proxyURL = test_data.get("proxyURL");
	    proxyBypass = test_data.get("proxyBypass");
	    
	   	// denotes the SystemType (HANA/TOMCAT/ABAP) found in property file
		m_systemType = test_data.get("SystemType");
		System.out.println("System Type = " + m_systemType);		

		// Construct complete URL from values found in property file
		StringBuilder _builder = new StringBuilder();
		_builder.append(test_data.get("protocol"));
		_builder.append(test_data.get("host"));
		_builder.append(test_data.get("port"));
		_builder.append(test_data.get("loginPath"));
		m_siteURL = _builder.toString();
		System.out.println("Path to Login form is: " + m_siteURL);

		if (m_systemType.equalsIgnoreCase("Tomcat")) {
			_builder.delete(0, _builder.length());
			_builder.append(test_data.get("protocol"));
			_builder.append(test_data.get("login_user"));
			_builder.append(test_data.get("host"));
			_builder.append(test_data.get("port"));
			_builder.append(test_data.get("start_up"));
			m_pathTostartup = _builder.toString();
			System.out.println("Path to start_up is: " + m_pathTostartup);
		}   
			
	}

	
	public void testFullCycle() {
				
				
		PerformanceTransactions transactions = PerformanceTransactions.getInstance();
		System.out.println("transactions: " + transactions);
		
		//Logger.getRootLogger().setLevel(Level.DEBUG);
		
		List<WebElement> fioriAppTiles = null;		
		String _transactionName = null;
		WebElement _configButton = null;
		boolean _outcome, res;
		int _GroupSize;

//scenario start	
		for (int iteration = 0; iteration < 1 ; iteration++) {													 	 		
				m_driver = SeleniumVerificationUtils.openLocalChromeBrowser(Arrays.asList("--incognito", "-start-maximized"), proxyURL, proxyBypass);		
//start measurements
				SeleniumVerificationUtils.shluff(2);
				m_driver.get(m_pathTostartup);				
				
				_transactionName = "010_Open_Launchpad";
				PerfUtilsLight.PerformanceTransactionStart(transactions, _transactionName, iteration);
				m_driver.get(m_siteURL);
				//SeleniumVerificationUtils.VerifyElementsAsync(driver, By.cssSelector(test_data.get(_transactionName)), numOfExpectedObjects, 30);
				//Verification step
				SeleniumVerificationUtils.VerifyElement (m_driver, By.className("sapUshellTileInner"), 30);				
				PerfUtilsLight.PerformanceTransactionEnd(transactions,_transactionName, iteration);		
				
				// *******************************************
				// Open Group List step - 020_Open_Group_List
				// *******************************************

				try {
					_configButton = SeleniumVerificationUtils.waitForElement(m_driver, 10,
							By.id("configBtn"));
					System.out.println("Group button displayed: "
							+ _configButton.isDisplayed());

				} catch (org.openqa.selenium.StaleElementReferenceException ex) {
					Logger.getRootLogger().setLevel(Level.DEBUG);
					System.out.println("Exception in finding groups button: " + ex);
					_configButton = SeleniumVerificationUtils.waitForElement(m_driver, 10,
							By.id("configBtn"));
					Logger.getRootLogger().setLevel(Level.ERROR);
				}
				SeleniumVerificationUtils.shluff(4);
				res = SeleniumVerificationUtils.waitUntilElementIsClickable(m_driver, 10,
						_configButton);
				System.out.println("waitUntilElementIsClickable is " + res
						+ " yyyyyyyyyy");
				
				System.out.println("020_Open_Group_List step started........ ");				
				_transactionName = "020_Open_Group_List";
				PerfUtilsLight.PerformanceTransactionStart(transactions, _transactionName, iteration);		
				_configButton.click();
				
				// Check that Group List is visible
				SeleniumVerificationUtils.waitForElement(m_driver, 10, By.id("groupList"));
				_GroupSize = m_driver.findElements(
						By.className("sapUshellGroupListItem")).size();
				System.out.println("_GroupSize = " + _GroupSize);
				_outcome = SeleniumVerificationUtils.VerifyElement (m_driver, By.className("sapUshellGroupListItem"), 30);	
			
				//Verification step
				SeleniumVerificationUtils.VerifyElement (m_driver, By.className("sapUshellTileInner"), 30);				
				PerfUtilsLight.PerformanceTransactionEnd(transactions,_transactionName, iteration);	
				
				//_outcome = CheckIfSingleWebElementDisplayed("sapUshellGroupListItem", 2 , 10);				
				System.out.println("020_Open_Group_List step ended........ ");
				if (_outcome) {
					System.out
							.println("All "
									+ _GroupSize
									+ " groups within the group list are displayed and visible");
				} else {
					System.out
							.println("Not all groups within the group list are displayed and visible");
				}
				SeleniumVerificationUtils.shluff(4);
				
				destroyDriver();			
		}
		
		String summary =  PerfUtilsLight.PerformanceTransactionsSummary(transactions);
		
		System.out.println(summary);
		
		
		PerfUtilsLight.WriteCSV(transactions.getAveragesCsv(), "C:\\Users\\i040002\\.jenkins\\workspace\\Perf_Regression_test\\results.csv");
		
	}
	
	protected void tearDown() throws Exception {

		super.tearDown();
		destroyDriver();
		
	}
	
	
	public static List<WebElement> fetchTiles(WebDriver driver,  List<WebElement> fioriAppTiles) {
		try {
			fioriAppTiles = SeleniumVerificationUtils.waitForElements(driver,30, By.cssSelector(".sapUshellTileBase[id*=\"__tile\"]>div>h3"));
			System.out.println("INFO: found " + fioriAppTiles.size() + " APPs");
		} catch (Exception e) {
			System.out.println("ERROR: fioriAppTiles.get(i) exception is swallowed by purpose\nrefetching tiles");
			e.printStackTrace();
			fioriAppTiles = SeleniumVerificationUtils.waitForElements(driver,30,By.cssSelector(".sapUshellTileBase[id*=\"__tile\"]>div>h3"));
			if (fioriAppTiles.size() == 0) {
				throw new NotFoundException(
						"\n\nERROR: fiori APPs tiles not found\n\n");
			}
		}
		return fioriAppTiles;
	}
	
	private void destroyDriver() {
		if (m_driver != null) {
			m_driver.close();
			m_driver.quit();
			m_driver = null;
		}
		
	}

	


}
