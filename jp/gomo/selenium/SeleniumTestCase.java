package jp.gomo.selenium;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import junit.framework.TestCase;

public class SeleniumTestCase extends TestCase {
	
	protected String getFileAbsolutePath(String path)
	{
		File image = new File(path);
		return image.getAbsolutePath();
	}
	
	protected WebElement waitForDisplay(WebElement element) throws InterruptedException
	{
		for (int i = 0; i < 10; i++)
		{
			if(!element.isDisplayed())
			{
				Thread.sleep(200);
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
		
		Thread.sleep(200);
		
		return element;
	}
	
	protected SearchContext waitForFindElement(String message, SearchContext element, By selector) throws InterruptedException 
	{
		return waitForFindElement(message, element, selector, 10);
	}
	
	protected SearchContext waitForFindElement(SearchContext element, By selector) throws InterruptedException 
	{
		return waitForFindElement("Not found element for "+selector, element, selector, 10);
	}

	protected SearchContext waitForFindElement(String message, SearchContext element, By selector, int waitCount) throws InterruptedException 
	{
		WebElement target = null;
		
		for (int i = 0; i < waitCount; i++)
		{
			try 
			{
				target = element.findElement(selector);
				break;
				
			} catch (NoSuchElementException e) {
				Thread.sleep(200);
			}
		}
		
		if(target == null)
		{
			fail(message);
		}
		
		return target;
	}
	
}
