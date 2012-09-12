package jp.gomo.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class DriverEventListener extends AbstractWebDriverEventListener {
	
	private String tagname;
	protected Boolean isErrorDisplay = false;
	private String errorDisplayQuery;
	
	@Override
	public void beforeClickOn(WebElement ele, WebDriver driver)
	{
		//クリックするエレメントのタグ名を取得
		//aタグをクリックした場合、afterでは取得できないので、先に取得しておく
		tagname = null;
		
		try 
		{
			tagname = ele.getTagName();
		}
		catch (StaleElementReferenceException e)
		{
			throw e;
		}
	}
	
	@Override
	public void afterClickOn(WebElement ele, WebDriver driver)
	{
		if(tagname.equals("a"))
		{
			//リンクをクリック後はページが表示されているか確認する
//TODO:javascriptのイベントが登録されていて、ページ遷移しない場合はどうする？
//イベントを取得できるか？ページが遷移した際のイベントでチェックするか
			appendErrorDisplayQuery(driver);
			checkPageError(driver);
		}
	}
	
	@Override
	public void afterNavigateTo(String url, WebDriver driver)
	{
		checkPageError(driver);
		appendErrorDisplayQuery(driver);
	}
	
	public void setIsErrorDisplay(Boolean bool)
	{
		isErrorDisplay = bool;
	}
	
	public void setErrorDisplqyQuery(String query)
	{
		errorDisplayQuery = query;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	//private
	
	
	/**
	 * ページが読み込まれたかどうか判定。
	 * id=footerがあるかどうかで判定している
	 * @param driver
	 */
	protected void checkPageError(WebDriver driver)
	{
		//phpエラーチェック
		String pageSource = driver.getPageSource();
		String[] errors = {"Notice", "Fatal error", "Warning"};
		for(String error: errors){
			if(pageSource.indexOf(error) != -1)
			{
				System.out.println("[PHP ERROR]");
			}
		}

		//見つからなかったらエラーで止まる
		isPageLoaded(driver);
	}
	
	/**
	 * ページが読み込まれたか確認するメソッド
	 * 各サイト用の子孫クラスで実装する
	 * @param driver
	 * @return Boolean
	 */
	protected Boolean isPageLoaded(WebDriver driver)
	{
		return true;
	}
	
	/**
	 * 
	 * @return Boolean
	 */
	private Boolean isErrorDisplay()
	{
		return isErrorDisplay;
	}
	
	/**
	 * 
	 * @return String
	 */
	private String getErrorDisplayQuery()
	{
		return errorDisplayQuery;
	}
	
	private void appendErrorDisplayQuery(WebDriver driver)
	{
		if(isErrorDisplay() == false)
		{
			return;
		}
		
		String url = driver.getCurrentUrl();
		String appendQuery = getErrorDisplayQuery();
		
		//クエリ追加済みなら無視
		if(url.indexOf(appendQuery) != -1)
		{
			return;
		}
		
		String prefix = (url.indexOf("?") == -1) ? "?": "&";
		driver.get(url + prefix + appendQuery);
	}
}
