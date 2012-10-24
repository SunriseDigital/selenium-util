package jp.gomo.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;

public class SeleniumUtil{
	
	private int sleep_interval = 500;
	
	private int default_wait_count = 50;
	
	public SeleniumUtil() throws IOException
	{
		this("system.properties");
	}
	
	public SeleniumUtil(String configFilePath) throws IOException
	{
		Properties config = new Properties();
		InputStream inputStream = new FileInputStream(new File(configFilePath));
		config.load(inputStream);
		
		Set<Entry<Object, Object>> configSet = config.entrySet();
		for (Entry<Object, Object> entry : configSet) 
		{
			System.setProperty((String)entry.getKey(), (String)entry.getValue());
		}
	}
	
	/**
	 * ワークスペースの相対パスを指定してファイルシステムの絶対パスを取得する
	 * アップロードで使用します。
	 * 
	 * @param path
	 * @return
	 */
	public String getFileAbsolutePath(String path)
	{
		File image = new File(path);
		return image.getAbsolutePath();
	}
	
	private void sleep()
	{
		try
		{
			Thread.sleep(sleep_interval);
		}
		catch (InterruptedException e)
		{
			throw new SleepException(e.getMessage());
		}
	}
	
	/**
	 * hiddenのエレメントが現れるのを待つ
	 * @param element
	 * @return
	 */
	public WebElement waitForDisplay(WebElement element)
	{
		for (int i = 0; i < default_wait_count; i++)
		{
			if(!element.isDisplayed())
			{
				sleep();
			}
			else
			{
				break;
			}
		}
		
		if(!element.isDisplayed())
		{
			throw new NotHappenException(element+" is not displayed.");
		}
		
		sleep();
		
		return element;
	}
	
	public WebElement waitForFindElement(SearchContext element, By selector)
	{
		WebElement target = null;
		
		for (int i = 0; i < default_wait_count; i++)
		{
			try 
			{
				target = element.findElement(selector);
				break;
				
			} catch (NoSuchElementException e) {
				sleep();
			}
		}
		
		if(target == null)
		{
			throw new NotHappenException("Not found element for "+selector);
		}
		
		return target;
	}
	
	public List<WebElement> waitForFindElements(SearchContext element, By selector)
	{	
		for (int i = 0; i < default_wait_count; i++)
		{
			List<WebElement> list = element.findElements(selector);
			if(list.size() != 0)
			{
				return list;
			}
			
			sleep();
		}
		
		throw new NotHappenException("Not found elements for "+selector);
	}
	
	public List<WebElement> waitForCountElements(SearchContext element, By selector, int expectedCount)
	{	
		for (int i = 0; i < default_wait_count; i++)
		{
			List<WebElement> list = element.findElements(selector);
			if(list.size() == expectedCount)
			{
				return list;
			}
			
			sleep();
		}
		
		throw new NotHappenException("Not found elements for "+selector);
	}
	
	public void waitForDisappearElement(WebElement element)
	{
		for (int i = 0; i < default_wait_count; i++)
		{
			try {
				
				if(!element.isDisplayed())
				{
					return;
				}
				
			} catch (StaleElementReferenceException e) {
				return;
			}
			
			sleep();
		}
		
		throw new NotHappenException(element+" is still exists.");
	}
	
	public void waitForNotFoundElements(SearchContext element, By selector)
	{
		for (int i = 0; i < default_wait_count; i++)
		{
			List<WebElement> list = element.findElements(selector);
			if(list.size() == 0)
			{
				return;
			}
			
			sleep();
		}
		
		throw new NotHappenException("Still found elements for "+selector);
	}
	
	public boolean exists(WebElement element)
	{
		try 
		{
			element.isDisplayed();
		}
		catch (StaleElementReferenceException e)
		{
			return false;
		}
		
		return true;
	}

	public void acceptAlert(WebDriver driver)
	{
		try 
		{
			Thread.sleep(300);
			driver.switchTo().alert().accept();
			Thread.sleep(300);
		}
		catch (InterruptedException e)
		{
			throw new SleepException(e.getMessage());
		}
	}
	
	public void scrollTo(SearchContext driver, By selector) 
	{
		scrollTo(driver, waitForFindElement(driver, selector));
	}

	public void scrollTo(SearchContext driver, WebElement element) 
	{
		Locatable loc = (Locatable) element;
	    int y = loc.getCoordinates().getLocationOnScreen().getY();
	    ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,"+y+");");
	}
	
	public void get(WebDriver driver, String uri) 
	{
		UnhandledAlertException exception = null;
		for (int i = 0; i < default_wait_count; i++)
		{
			try {
				driver.get(uri);
				return;
			} catch (UnhandledAlertException e) {
				exception = e;
			}
			
			sleep();
		}
		
		throw exception;
	}
	
	public boolean exists(SearchContext context, By selector) 
	{
		sleep();
		sleep();
		return context.findElements(selector).size() != 0;
	}
	
	public List<String> getClassNameList(WebElement buttonWrap)
	{
		String[] classes = buttonWrap.getAttribute("class").split(" ");
		
		return Arrays.asList(classes);
	}

	public void waitForClassDisappear(WebDriver driver, WebElement element, String className) 
	{
		for (int i = 0; i < default_wait_count; i++)
		{
			List<String> classes = getClassNameList(element);
			if(!classes.contains(className))
			{
				return;
			}
			
			sleep();
		}
		
		throw new NotHappenException("Class " + className + " did not disappear");
	}

	public void waitForClassAppear(WebDriver driver, WebElement element, String className) 
	{
		for (int i = 0; i < default_wait_count; i++)
		{
			List<String> classes = getClassNameList(element);
			if(classes.contains(className))
			{
				return;
			}
			
			sleep();
		}
		
		throw new NotHappenException("Class " + className + " did not disappear");
		
	}
	
	public boolean hasClass(WebElement element, String className)
	{
		return getClassNameList(element).contains(className);
	}
	
	public Matcher getRegexMatcher(String pattern, String text) 
	{
		return getRegexMatcher(pattern, text, 0);
	}

	public Matcher getRegexMatcher(String pattern, String text, int flag) 
	{
    	Pattern pttr = Pattern.compile(pattern, flag);
		Matcher matcher = pttr.matcher(text);
		
		matcher.find();
		
		return matcher;
	}
	
	public Long getTimestamp(){
		Date now = new Date();
		return now.getTime();
	}
	
	public WebElement find(String selector, WebDriver driver)
	{
		return waitForFindElement(driver, By.cssSelector(selector));
		//return driver.findElement(By.cssSelector(selector));
	}

	public List<WebElement> findElements(String selector, WebDriver driver)
	{
		return driver.findElements(By.cssSelector(selector));
	}

	/**
	 * クリック動作の便利メソッド
	 * @param selector
	 * @param driver
	 */
	public void click(String selector, WebDriver driver)
	{
		find(selector, driver).click();
		sleep();
	}
	
	/**
	 * 文字を入力する便利メソッド
	 * @param selector
	 * @param driver
	 */
	public void type(String text, String selector, WebDriver driver)
	{
		find(selector, driver).sendKeys(text);
	}
	
	/**
	 * selectの中から指定した値と同じ値optionをクリックする
	 * @param value
	 * @param selector
	 * @param driver
	 */
	public void select(String value, String selector, WebDriver driver)
	{
//TODO:複数選択可のものは想定していない
		WebElement select = waitForFindElement(driver, By.cssSelector(selector));
		Select click = new Select(select);
		click.selectByValue(value);
	}
	
	/**
	 * 要素をクリックして新しいウィンドウにフォーカスする
	 * TODO:もう少し整理できそうな気がする
	 * @param ele
	 * @param driver
	 */
	public void clickAndFocusNewWindow(WebElement ele, WebDriver driver)
	{
		String current_window_id = driver.getWindowHandle();
		ele.click();
		sleep();
		
		//TODO:ウィンドウが開くのを待つ処理がいるか？
		java.util.Set<String> window_ids = driver.getWindowHandles();
		if(window_ids.size() < 2)
		{
			return;
		}
		
		//新しいウインドウIDを取得
		String new_window_id = null;
		for(String id :window_ids)
		{
			//現在のウインドウと違うIDのものがあったらそのIDを格納
			//最後の値が新しくひらいたものと判定している。
			if(!id.equals(current_window_id))
			{
				new_window_id = id;
			}
		}
		
		driver.switchTo().window(new_window_id);
	}
	
	/**
	 * 指定の文言がページ内のテキストに存在するかをページングしながら判定するメソッド
	 * search_string: 調べたい文字列
	 * next_link_selector: 次ページへのリンクを取得するためのセレクター
	 * @param search_string
	 * @param next_link_selector
	 * @param driver
	 * @return Boolean
	 */
	public Boolean searchStringWithPaging(String search_string, String next_link_selector, String next_link_text, WebDriver driver)
	{
		return searchStringWithPagingSource(search_string, next_link_selector, next_link_text, driver, true);
	}
	
	/**
	 * ページのhtmlソースに指定の文字列があるかどうかページングしながら判定する
	 * @param search_string
	 * @param next_link_selector
	 * @param driver
	 * @return Boolean
	 */
	public Boolean searchStringWithPagingSource(
		String search_string,
		String next_link_selector,
		String next_link_text,
		WebDriver driver
	){
		return searchStringWithPagingSource(
			search_string,
			next_link_selector,
			next_link_text,
			driver,
			false);
	}
	
	private Boolean searchStringWithPagingSource(
			String search_string,
			String next_link_selector,
			String next_link_text,
			WebDriver driver,
			Boolean flag)
	{
		sleep();
		sleep();
System.out.println(driver.getCurrentUrl());
		String text = (flag == true) ? find("body", driver).getText() : driver.getPageSource();

		
		sleep();

		if(text.indexOf(search_string) != -1)
		{
			return true;
		}

		sleep();
		sleep();
		sleep();
		sleep();
		//次のページへのリンクがなければfalse
		if(!exists(driver, By.cssSelector(next_link_selector)))
		{
			return false;
		}

		//次ページへ移動
		//次ページへのリンク要素を取得
		java.util.List<WebElement> links = findElements(next_link_selector, driver);
		WebElement link_element = null;
		for(WebElement link :links)
		{
			if(link.getText().equals(next_link_text))
			{
				link_element = link;
				break;
			}
		}
		
		if(link_element == null)
		{
			return false;
		}
		
		
		link_element.click();
		sleep();
		sleep();
		sleep();
		sleep();
		//再帰的に調べる
		return searchStringWithPagingSource(search_string, next_link_selector, next_link_text, driver, flag);
	}
	
	public Boolean isTextFound(String search_string, WebDriver driver)
	{
		String text = find("body", driver).getText();
		return (text.indexOf(search_string) != -1);
	}
	
	public WebElement findLastElement(String selector, WebDriver driver)
	{
		java.util.List<WebElement> list = driver.findElements(By.cssSelector(selector));
		return list.get(list.size() - 1);
	}
}