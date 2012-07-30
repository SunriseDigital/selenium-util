package jp.gomo.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import junit.framework.TestCase;

public class SeleniumTestCase extends TestCase {
	
	private int sleep_interval = 200;
	
	private int default_wait_count = 50;
	
	public void setUp() throws Exception {
		
		Properties config = new Properties();
		InputStream inputStream = new FileInputStream(new File(System.getProperty("user.home")+File.separator+"selenium-lib.properties"));
		config.load(inputStream);
		
		Set<Entry<Object, Object>> configSet = config.entrySet();
		for (Entry<Object, Object> entry : configSet) 
		{
			System.setProperty((String)entry.getKey(), (String)entry.getValue());
		}
	}
	
	/*protected List<WebDriver> getChromeDriver()
	{
		List<WebDriver> drivers = new ArrayList<WebDriver>();
		
		drivers.add(new ChromeDriver());
		
		return drivers;
	}
	
	protected List<WebDriver> getInternetExplorerDriver()
	{
		List<WebDriver> drivers = new ArrayList<WebDriver>();
		
		drivers.add(new InternetExplorerDriver());
		
		return drivers;
	}
	
	protected List<WebDriver> getFirefoxDriver()
	{
		List<WebDriver> drivers = new ArrayList<WebDriver>();
		
		FirefoxProfile profile = new FirefoxProfile();
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(false);
		drivers.add(new FirefoxDriver(profile));
		
		return drivers;
	}
	
	protected List<WebDriver> getAllDrivers()
	{
		List<WebDriver> drivers = new ArrayList<WebDriver>();
		
		drivers.addAll(getChromeDriver());
		drivers.addAll(getInternetExplorerDriver());
		drivers.addAll(getFirefoxDriver());
		
		return drivers;
	}*/
	
	protected String getFileAbsolutePath(String path)
	{
		File image = new File(path);
		return image.getAbsolutePath();
	}
	
	protected WebElement waitForDisplay(WebElement element) throws InterruptedException
	{
		for (int i = 0; i < default_wait_count; i++)
		{
			if(!element.isDisplayed())
			{
				Thread.sleep(sleep_interval);
			}
			else
			{
				break;
			}
		}
		
		if(!element.isDisplayed())
		{
			fail(element+" is not displayed.");
		}
		
		Thread.sleep(sleep_interval);
		
		return element;
	}
	
	protected WebElement waitForFindElement(String message, SearchContext element, By selector) throws InterruptedException 
	{
		return waitForFindElement(message, element, selector, default_wait_count);
	}
	
	protected WebElement waitForFindElement(SearchContext element, By selector) throws InterruptedException 
	{
		return waitForFindElement("Not found element for "+selector, element, selector, default_wait_count);
	}

	protected WebElement waitForFindElement(String message, SearchContext element, By selector, int waitCount) throws InterruptedException 
	{
		WebElement target = null;
		
		for (int i = 0; i < waitCount; i++)
		{
			try 
			{
				target = element.findElement(selector);
				break;
				
			} catch (NoSuchElementException e) {
				Thread.sleep(sleep_interval);
			}
		}
		
		if(target == null)
		{
			fail(message);
		}
		
		return target;
	}
	
	protected List<WebElement> waitForFindElements(SearchContext element, By selector) throws InterruptedException 
	{
		return waitForFindElements("Not found elements for "+selector, element, selector, default_wait_count);
	}
	
	protected List<WebElement> waitForFindElements(String message, SearchContext element, By selector, int waitCount) throws InterruptedException 
	{
		for (int i = 0; i < waitCount; i++)
		{
			List<WebElement> list = element.findElements(selector);
			if(list.size() != 0)
			{
				return list;
			}
			
			Thread.sleep(sleep_interval);
		}
		
		fail(message);
		
		return null;
	}
	
	protected List<WebElement> waitForCountElements(SearchContext element, By selector, int expectedCount) throws InterruptedException 
	{
		return waitForCountElements("Not found elements for "+selector, element, selector, expectedCount, default_wait_count);
	}
	
	protected List<WebElement> waitForCountElements(String message, SearchContext element, By selector, int expectedCount, int waitCount) throws InterruptedException 
	{
		for (int i = 0; i < waitCount; i++)
		{
			List<WebElement> list = element.findElements(selector);
			if(list.size() == expectedCount)
			{
				return list;
			}
			
			Thread.sleep(sleep_interval);
		}
		
		fail(message);
		
		return null;
	}
	
	protected void waitForDisappearElement(WebElement element) throws InterruptedException 
	{
		waitForDisappearElement(element+" is still exists.", element, default_wait_count);
	}
	
	protected void waitForDisappearElement(String message, WebElement element, int waitCount) throws InterruptedException 
	{
		for (int i = 0; i < waitCount; i++)
		{
			try {
				
				if(!element.isDisplayed())
				{
					return;
				}
				
			} catch (StaleElementReferenceException e) {
				return;
			}
			
			Thread.sleep(sleep_interval);
		}
		
		fail(message);
	}
	
	protected void waitForNotFoundElements(SearchContext element, By selector) throws InterruptedException 
	{
		waitForNotFoundElements("Still found elements for "+selector, element, selector, default_wait_count);
	}
	
	protected void waitForNotFoundElements(String message, SearchContext element, By selector, int waitCount) throws InterruptedException 
	{
		for (int i = 0; i < waitCount; i++)
		{
			List<WebElement> list = element.findElements(selector);
			if(list.size() == 0)
			{
				return;
			}
			
			Thread.sleep(sleep_interval);
		}
		
		fail(message);
	}
	
	protected void assertNotExistsElement(WebElement element)
	{
		assertNotExistsElement(element+" is still exists.", element);
	}
	
	protected void assertNotExistsElement(String message, WebElement element)
	{
		try 
		{
			element.isDisplayed();
			fail(message);
		}
		catch (StaleElementReferenceException e)
		{
			//例外が出なければDOMは存在するので成功
		}
	}
	
	protected boolean hasClass(WebElement element) {
		String _class = element.getAttribute("class");
		
		String[] part = _class.split(" ");
		
		System.out.println(part);
		
		return true;
	}
}
