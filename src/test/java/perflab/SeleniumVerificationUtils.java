package perflab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumVerificationUtils {
	static int tt = 2;
	/****
	 * check if 1 element is visible
	 * @param driver
	 * @param tr_name
	 * @param what
	 * @param timeout
	 * @return
	 */
	 
	public static boolean VerifyElement(WebDriver driver, By what, int timeout) {
		WebElement _ele = null;
		long _startTime ;
		_startTime = System.currentTimeMillis();
		long _timeout = _startTime + (timeout * 1000);	
		boolean _isDiplayed=false;
		//System.out.println("INFO: verifying for " + what + " visiblity" );
		int _cnt = 0;		
		//_ele.getText() can be verified by predefined data	
		while (_isDiplayed==false && System.currentTimeMillis() <= _timeout){
			shluff_ms(10);
			if(_ele == null || _cnt % 100 == 0/*re-fetch element every second*/){
				try {
					_ele = waitForElement( driver, timeout ,what);
					System.out.println("DEBUG: fetched element " + what );
				} catch (Exception e) {
					System.out.println("\nINFO: cant get element" + what  + " will retry");
					e.printStackTrace();
				}
			}else{
				try {
					_isDiplayed= _ele.isDisplayed();
					
		    	} 
		    	catch (Exception e) {
		    		
		    		e.printStackTrace();
		    		_isDiplayed= false;
		    	}
			}
			_cnt ++;
		}	
		String msg =  what  + " is displayed=  "  + _isDiplayed + " took: " + (System.currentTimeMillis() - _startTime);
		if(!_isDiplayed){
			throw new NotFoundException("ERROR:\n"+ msg + " -FAILED"  );
		}else{
			System.out.println("\nINFO: PASSED : " + msg + "\n<=================================================>\n");
		}
		return _isDiplayed;
	}
	
	/*******
	 * checks that  first and last elements are displayed
	 * @param webdriver
	 * @param what objects to verify <List>
	 * @param numOfExpectedElements
	 * @param timeout
	 * @return
	 */
	public static boolean VerifyElementsAsync(WebDriver driver, By what, int numOfExpectedElements, int timeout) {
	
		boolean _displayed = false;
		List<WebElement> listOfElements;
		long _startTime = System.currentTimeMillis();
		long _timeout = _startTime + (timeout * 1000);
		
		listOfElements = waitForElements( driver, timeout, what);
		
		int numOfFoundObjects = listOfElements.size();
		String msg = null;
		System.out.println("DEBUG: VerifyElementsAsync:  found  " + numOfFoundObjects + " instances of expected " + numOfExpectedElements+ " of "+  what );
		
		int refetchCounter=0;	
		while ((System.currentTimeMillis()<_timeout) && (!_displayed )) {
			shluff_ms(10);
			
			//checking first and last elems			
			if (numOfFoundObjects != numOfExpectedElements){
				//System.out.println("DEBUG: VerifyElementsAsync: refetching elements list -wrong number");
				listOfElements = waitForElements( driver, timeout , what);	
				numOfFoundObjects = listOfElements.size();
			}
			else if (numOfFoundObjects == numOfExpectedElements){
				if (listOfElements.get(numOfFoundObjects-1).isDisplayed() && listOfElements.get(0).isDisplayed()){				
					_displayed = true;
				}
				else if (refetchCounter % 100 == 0){
					System.out.println("DEBUG: refetching elements list: correct number , first and last are not visible");
					listOfElements = waitForElements( driver, timeout ,what);		
				}
			}
			refetchCounter++;
		}
		//end while 
		
		if (numOfFoundObjects  == 0 ){
			msg = "ERROR : VerifyElements : no " + what  + " elements found, took "  + (System.currentTimeMillis() - _startTime);
			throw new NotFoundException("ERROR: msg :\n"+ msg +" FAILED"  );
		}else if (numOfFoundObjects  != numOfExpectedElements ){
			msg = "ERROR : VerifyElements: not all  " + what  + "found "
					+ numOfFoundObjects   + "out of " + numOfExpectedElements+ " elements found, took "  + (System.currentTimeMillis() - _startTime);
			throw new NotFoundException("ERROR: msg :\n"+ msg + " FAILED"  );		
		}else if(!_displayed){
			throw new NotFoundException("ERROR: VerifyElementsAsync FAILED"  );
		}else{
			System.out.println("\nINFO:nVerifyElementsAsync  PASSED" );
		}	
		System.out.println("DEBUG:  VerifyElementsAsync took "  + (System.currentTimeMillis() - _startTime) + "\n<=================================================>\n");
		return _displayed;
	}	
	/**
	 * this will iterate over the entire list of elements and check they are all visible. has bigger overhead
	 * @param webdriver
	 * @param what kind of element 
	 * @param numOfExpectedElements
	 * @param timeout
	 * @return
	 */

	public static boolean VerifyElementsSync(WebDriver driver, By what, int numOfExpectedElements, int timeout) {
					
		boolean _displayed = false;
		List<WebElement> listOfElements;
		long _startTime = System.currentTimeMillis();
		long _timeout = _startTime + (timeout * 1000);
		
		listOfElements = waitForElements( driver, timeout, what);
		
		int numOfFoundObjects = listOfElements.size();

		System.out.println("DEBUG: VerifyElementsSync:  found  " + numOfFoundObjects + " instances of expected " + numOfExpectedElements+ " of "+  what );
		
		int numOfDisplayedObjects = 0;
		while ( (System.currentTimeMillis() < _timeout) && ((numOfFoundObjects < numOfExpectedElements) || (numOfDisplayedObjects < numOfExpectedElements))) {
			
			/* Fetch objects list if number of found elements smaller than expected */
			if (numOfFoundObjects < numOfExpectedElements){			
				listOfElements = waitForElements( driver, timeout , what);
				numOfFoundObjects = listOfElements.size();
			}
			
			/* Check for visibility of all founded objects */ 
			if (numOfFoundObjects == numOfExpectedElements){
				for (WebElement e : listOfElements ){			
						try {
							if(e.isDisplayed()){
								numOfDisplayedObjects++;
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							long begin = System.currentTimeMillis();
							listOfElements = waitForElements( driver, timeout , what);
							System.out.println("DEBUG :  VerifyElementsSync -> waitForElements (refetch) took "  + (System.currentTimeMillis() - begin) + "\n\n");
						}
						
				}
					System.out.println("number of dispalayed objects " + numOfDisplayedObjects );
				}else{
					numOfDisplayedObjects = 0;
				}
				
				shluff_ms(10);
				
		}

		if(numOfDisplayedObjects == numOfExpectedElements){
			_displayed = true;
		}
		
		System.out.println("DEBUG :  VerifyElementsSync took "  + (System.currentTimeMillis() - _startTime) + "\n<=================================================>\n");
		return _displayed;
	}	
	
	
	// refactor
	
	public static WebElement waitForElement(WebDriver driver, long timeOutInSecs, final By by)  
	{
		WebElement dynamicElement = waitForElement(driver, timeOutInSecs, new ExpectedCondition<WebElement>(){ 
			public WebElement apply(WebDriver d) { 
				return d.findElement(by); 
			}});
		return dynamicElement;
	}
	
	public static WebElement waitForElement(WebDriver driver,
			long timeOutInSecs, final ExpectedCondition<WebElement> ec) {
		WebElement dynamicElement = null;

		try {
			dynamicElement = (new WebDriverWait(driver, timeOutInSecs))
					.until(ec);
		} catch (org.openqa.selenium.TimeoutException te) {
			System.out.println("ERROR: Can't find object by this condition.");
		}
		return dynamicElement;
	}
	
	public static List<WebElement> waitForElements(WebDriver driver, long timeOutInSecs, final By by)
    {
		long start = System.currentTimeMillis();
		List<WebElement> dynamicElements = waitForElements(driver, timeOutInSecs, new ExpectedCondition<List<WebElement>>() {
            public List<WebElement>apply(WebDriver d) {
                return d.findElements(by); 
        }});
		System.out.println("waitForElements EBD : " + (System.currentTimeMillis() - start));
		return dynamicElements;
    }
	
	public static List<WebElement> waitForElements(WebDriver driver,
			long timeOutInSecs, final ExpectedCondition<List<WebElement>> ec) {
		List<WebElement> dynamicElements = null;

		try {
			dynamicElements = (new WebDriverWait(driver, timeOutInSecs))
					.until(ec);
		} catch (org.openqa.selenium.TimeoutException te) {
			System.out.println("ERROR: Can't find objects by this condition.");
		}
		return dynamicElements;
	}
		
	public static HashMap <String,String> readCSV (String csv){
		//read data
			BufferedReader br = null;
			HashMap<String,String> map = new HashMap<String, String>();
			try {
				br = new BufferedReader(new FileReader(csv));
				String line =  null;   
				while((line=br.readLine())!=null){
					String str[] =(line.split(","));	
					map.put( str[0], str[1]);
					}
				} catch (IOException e) {
							e.printStackTrace();
				} finally {
						try {
								if (br != null)br.close();
						} catch (IOException ex) {
								ex.printStackTrace();
						}
				}
	return map;
	}
	
	
	public static void shluff(int sec ){
		
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			System.out.println("Insomnia ......");
			e.printStackTrace();
		}
		
	}
	public static void shluff_ms(int ms ){
		
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			System.out.println("Insomnia ......");
			e.printStackTrace();
		}
		
	}
	
}

