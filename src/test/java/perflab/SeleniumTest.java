package perflab;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import junit.framework.TestCase;

public class SeleniumTest extends TestCase {
	
	private static String url = null;
	private static String username = null;
	private static String password = null;
	private static String proxyURL = null;
	private static String proxyBypass = null;
	
	PerformanceTransactions transactions;
	HashMap<String, String> test_data;
	static WebDriver driver = null;
	int how_many_tiles = 17;
	boolean sso = true;
	
	protected void setUp() throws Exception {	
		Runtime.getRuntime().exec("taskkill /f /IM chromedriver.exe ");
		super.setUp();
		transactions = PerformanceTransactions.getInstance();
		test_data = SeleniumVerificationUtils.readCSV("C:/github/PerfUtilsLight/src/test/java/perflab/test_data.csv");
		url = test_data.get("url");
		username = test_data.get("username");
		password = test_data.get("password");
		proxyURL = test_data.get("proxyURL");
	    proxyBypass = test_data.get("proxyBypass");
			
	}

	protected void tearDown() throws Exception {
		
		super.tearDown();
		destroyDriver();
	}
	
	public void testFullCycle() {
				
		PerformanceTransactions transactions = PerformanceTransactions.getInstance();
		Logger.getRootLogger().setLevel(Level.DEBUG);
		
		int dashboardSize = 0;
		List<WebElement> fioriAppTiles = null;		
		String _transactionName = null;

//scenario start	
		for (int iteration = 0; iteration < 1 ; iteration++) {													 	 		
				driver = SeleniumVerificationUtils.openLocalChromeBrowser(Arrays.asList("--incognito", "-start-maximized"), proxyURL, proxyBypass);		
//start measurements
				SeleniumVerificationUtils.shluff(2);
				if (!sso){
					 /*****************************************
				     * 000_Open_Login_Form
				     *****************************************/
				    PerfUtilsLight.PerformanceTransactionStart(transactions, "000_Open_Login_Form", iteration);
					driver.get(url);
					SeleniumVerificationUtils.VerifyElement( driver, By.name("j_username"), 30);
					PerfUtilsLight.PerformanceTransactionEnd(transactions, "000_Open_Login_Form", iteration);
											
					SeleniumVerificationUtils.waitForElement(driver, 10, By.name("j_username")).sendKeys(username);
					SeleniumVerificationUtils.waitForElement(driver, 10, By.name("j_password")).sendKeys(password);		              
				    WebElement okBtn = SeleniumVerificationUtils.waitForElement(driver, 10, By.id("logOnFormSubmit"));
				    /*****************************************
				     * 010_Submit_login_Open_Launchpad
				     *****************************************/
				    _transactionName = "010_Submit_login_Open_Launchpad";	
				    PerfUtilsLight.PerformanceTransactionStart(transactions, _transactionName, iteration);
				    okBtn.click();
				    SeleniumVerificationUtils.VerifyElementsAsync(driver, By.cssSelector(".sapUshellTileBase"), how_many_tiles, 30);
				    PerfUtilsLight.PerformanceTransactionEnd(transactions,_transactionName, iteration);
			    /*****************************************/
				}else{
					
					_transactionName = "010_Just_Open_Launchpad";	
					PerfUtilsLight.PerformanceTransactionStart(transactions, _transactionName, iteration);
					driver.get(url);
					SeleniumVerificationUtils.VerifyElementsAsync(driver, By.cssSelector(".sapUshellTileBase"), how_many_tiles, 30);
					PerfUtilsLight.PerformanceTransactionEnd(transactions,_transactionName, iteration);	
					
				}
				SeleniumVerificationUtils.shluff(2);
						
				if (iteration % 2 == 0){
					_transactionName = "ASYNCH_Reload_Launchpad";			
					PerfUtilsLight.PerformanceTransactionStart(transactions, _transactionName, iteration);
					driver.get(url);
					SeleniumVerificationUtils.VerifyElementsAsync(driver, By.cssSelector(".sapUshellTileBase"), how_many_tiles, 30);
					PerfUtilsLight.PerformanceTransactionEnd(transactions, _transactionName, iteration);					
					SeleniumVerificationUtils.shluff(2);
				}
				else{
					_transactionName = "SYNCH_Reload_Launchpad";		
					PerfUtilsLight.PerformanceTransactionStart(transactions, _transactionName, iteration);
					driver.get(url);
					SeleniumVerificationUtils.VerifyElementsSync(driver, By.cssSelector(".sapUshellTileBase"), how_many_tiles, 30);
					PerfUtilsLight.PerformanceTransactionEnd(transactions, _transactionName, iteration);
					SeleniumVerificationUtils.shluff(2);
				}
				
			    			
				
			    /*****************************************
			     * Bonus part of iterating through applications by clicking on tiles
			     *****************************************/
				dashboardSize = driver.findElements(By.className("sapUshellTileInner")).size();			
				System.out.println("Dashboard Size is: " + dashboardSize +" LP loaded........ :)" );				
				//get app names from the fiori app tiles to iterate over the apps	
				fioriAppTiles = SeleniumVerificationUtils.fetchTiles(driver, fioriAppTiles);		
				if(fioriAppTiles.size() == 0 )throw new NotFoundException("\n\nERROR: fiori APPs tiles not found\n\n"  );
				System.out.println("INFO: found " + fioriAppTiles.size() + " APPs");		
				ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
				
				for (int tile = 1 ; tile < (fioriAppTiles.size()); tile++ ){
				
					SeleniumVerificationUtils.shluff(2);	
					_transactionName = null;
					fioriAppTiles = SeleniumVerificationUtils.fetchTiles(driver, fioriAppTiles);				
					_transactionName = fioriAppTiles.get(tile).getText();
					System.out.println("INFO: transaction " + _transactionName);							 
					/**
					 * Open application
					 * */ 
					//SeleniumVerificationUtils.waitUntilElementIsClickable(driver, 30, fioriAppTiles.get(tile)); 				
					PerfUtilsLight.PerformanceTransactionStart(transactions, _transactionName, iteration);					
					try {
						fioriAppTiles.get(tile).click();
					} catch (Exception e) {
						e.printStackTrace();
					}
					/**************
					 * this is not valid!!! missing verifications
					 */	
					
					tabs = new ArrayList<String> (driver.getWindowHandles());
					if (tabs.size()>1){
						driver.switchTo().window(tabs.get(1));
					}  
					SeleniumVerificationUtils.VerifyElement(driver,By.tagName(test_data.get(_transactionName)), 10); 
					
					/*********
					//"html" should be replaced with relevant DOM element for validation, By.tagName -> By.cssSelector 
					*/
					
					PerfUtilsLight.PerformanceTransactionEnd(transactions, _transactionName , iteration);
					SeleniumVerificationUtils.shluff(2);
					
					if (tabs.size()>1){					
						driver.close();
						driver.switchTo().window(tabs.get(0));
					}  
					
					/**
					 * Open application finished
					 * */ 
				    
			    
				    /*****************************************
				     * 020_Reload_Launchpad - to be sure we are in the first tab
				     *****************************************/						
					driver.get(url);									
				}
				destroyDriver();			
		}
		
		String summary =  PerfUtilsLight.PerformanceTransactionsSummary(transactions);
		System.out.println(summary);	
		
	}
	
	private void destroyDriver() {
		if (driver != null) {
			driver.close();
			driver.quit();
			driver = null;
		}
		
	}

	


}