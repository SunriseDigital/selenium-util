package jp.gomo.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;

public class SeleniumUtil{
	
	private int sleep_interval = 200;
	
	private int default_wait_count = 50;
	
	public SeleniumUtil() throws IOException{
		
		Properties config = new Properties();
		InputStream inputStream = new FileInputStream(new File(System.getProperty("user.home")+File.separator+"selenium-lib.properties"));
		config.load(inputStream);
		
		Set<Entry<Object, Object>> configSet = config.entrySet();
		for (Entry<Object, Object> entry : configSet) 
		{
			System.setProperty((String)entry.getKey(), (String)entry.getValue());
		}
	}
	
	/*public List<WebDriver> getChromeDriver()
	{
		List<WebDriver> drivers = new ArrayList<WebDriver>();
		
		drivers.add(new ChromeDriver());
		
		return drivers;
	}
	
	public List<WebDriver> getInternetExplorerDriver()
	{
		List<WebDriver> drivers = new ArrayList<WebDriver>();
		
		drivers.add(new InternetExplorerDriver());
		
		return drivers;
	}
	
	public List<WebDriver> getFirefoxDriver()
	{
		List<WebDriver> drivers = new ArrayList<WebDriver>();
		
		FirefoxProfile profile = new FirefoxProfile();
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(false);
		drivers.add(new FirefoxDriver(profile));
		
		return drivers;
	}
	
	public List<WebDriver> getAllDrivers()
	{
		List<WebDriver> drivers = new ArrayList<WebDriver>();
		
		drivers.addAll(getChromeDriver());
		drivers.addAll(getInternetExplorerDriver());
		drivers.addAll(getFirefoxDriver());
		
		return drivers;
	}*/
	
	public String getFileAbsolutePath(String path)
	{
		File image = new File(path);
		return image.getAbsolutePath();
	}
	
	public WebElement waitForDisplay(WebElement element) throws InterruptedException, NotDisplayException
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
			throw new NotDisplayException(element+" is not displayed.");
		}
		
		Thread.sleep(sleep_interval);
		
		return element;
	}
	
	public WebElement waitForFindElement(SearchContext element, By selector) throws InterruptedException, NotFoundException 
	{
		return waitForFindElement(element, selector, default_wait_count);
	}

	public WebElement waitForFindElement(SearchContext element, By selector, int waitCount) throws InterruptedException, NotFoundException 
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
			throw new NotFoundException("Not found element for "+selector);
		}
		
		return target;
	}
	
	public List<WebElement> waitForFindElements(SearchContext element, By selector) throws InterruptedException, NotFoundException 
	{
		return waitForFindElements(element, selector, default_wait_count);
	}
	
	public List<WebElement> waitForFindElements(SearchContext element, By selector, int waitCount) throws InterruptedException, NotFoundException 
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
		
		throw new NotFoundException("Not found elements for "+selector);
	}
	
	public List<WebElement> waitForCountElements(SearchContext element, By selector, int expectedCount) throws InterruptedException, NotFoundException 
	{
		return waitForCountElements(element, selector, expectedCount, default_wait_count);
	}
	
	public List<WebElement> waitForCountElements(SearchContext element, By selector, int expectedCount, int waitCount) throws InterruptedException, NotFoundException 
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
		
		throw new NotFoundException("Not found elements for "+selector);
	}
	
	public void waitForDisappearElement(WebElement element) throws InterruptedException, NotDisappearException 
	{
		waitForDisappearElement(element, default_wait_count);
	}
	
	public void waitForDisappearElement(WebElement element, int waitCount) throws InterruptedException, NotDisappearException 
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
		
		throw new NotDisappearException(element+" is still exists.");
	}
	
	public void waitForNotFoundElements(SearchContext element, By selector) throws InterruptedException, NotFoundException 
	{
		waitForNotFoundElements(element, selector, default_wait_count);
	}
	
	public void waitForNotFoundElements(SearchContext element, By selector, int waitCount) throws InterruptedException, NotFoundException 
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
		
		throw new NotFoundException("Still found elements for "+selector);
	}
	
	public void assertNotExistsElement(WebElement element) throws NotDisappearException
	{
		try 
		{
			element.isDisplayed();
			throw new NotDisappearException(element+" is still exists.");
		}
		catch (StaleElementReferenceException e)
		{
			//例外が出なければDOMは存在するので成功
		}
	}
	
	public boolean hasClass(WebElement element) {
		String _class = element.getAttribute("class");
		
		String[] part = _class.split(" ");
		
		System.out.println(part);
		
		return true;
	}

	public void acceptAlert(WebDriver driver) throws InterruptedException 
	{
		Thread.sleep(300);
		driver.switchTo().alert().accept();
		Thread.sleep(300);
	}
	
	public void scrollTo(SearchContext driver, By selector) throws InterruptedException, NotFoundException 
	{
		scrollTo(driver, waitForFindElement(driver, selector));
	}

	public void scrollTo(SearchContext driver, WebElement element) 
	{
		Locatable loc = (Locatable) element;
	    int y = loc.getCoordinates().getLocationOnScreen().getY();
	    ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,"+y+");");
	}
}