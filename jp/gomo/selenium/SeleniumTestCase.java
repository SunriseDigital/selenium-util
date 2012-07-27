package jp.gomo.selenium;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import junit.framework.TestCase;

public class SeleniumTestCase extends TestCase {
	
	private int sleep_interval = 200;
	
	private int default_wait_count = 10;
	
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
	
	protected void gmailOpen(WebDriver driver, String id, String password)
	{
		driver.get("https://mail.google.com/mail?hl=ja");
		driver.findElement(By.xpath("//*[@id=\"Email\"]")).sendKeys(id);
		driver.findElement(By.xpath("//*[@id=\"Passwd\"]")).sendKeys(password);
		driver.findElement(By.xpath("//*[@id=\"signIn\"]")).click();
	}
	
	protected void gmailOpenFirstMail(WebDriver driver, String mailAddress) throws InterruptedException
	{
		WebElement iframe = waitForFindElement(driver, By.cssSelector("#canvas_frame"));
		driver.switchTo().frame(iframe);
		List<WebElement> email_list = driver.findElements(By.cssSelector("div.Cp table.F tr span[email]"));
		for (Iterator<WebElement> iterator = email_list.iterator(); iterator.hasNext();) {
			WebElement email = (WebElement) iterator.next();
			if(email.getAttribute("email").equals(mailAddress))
			{
				email.click();
				break;
			}
		}
	}
	
	protected WebElement gmailFindLastAnchor(WebDriver driver, String containsString)
	{
		return gmailFindLastAnchor("Not found anchor having "+containsString, driver, containsString);
	}
	
	protected WebElement gmailFindLastAnchor(String message, WebDriver driver, String containsString)
	{
		List<WebElement> link_list = driver.findElements(By.cssSelector("div.h7 a[target=_blank]"));
		int count = link_list.size();
		System.out.println("TotalCount:"+count);
		for (int i = count - 1; i >= 0; i--) {
			
			WebElement link = link_list.get(i);
			System.out.println(i+":"+link.getAttribute("href"));
			if(link.getAttribute("href").contains(containsString))
			{
				return link;
			}
		}
		
		fail(message);
		
		return null;
	}
}
