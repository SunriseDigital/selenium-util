package jp.gomo.selenium;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class DriverEnumerator implements Enumeration<WebDriver> {
	
	private List<String> driversKey = new ArrayList<String>();
	private List<String> driversValue = new ArrayList<String>();
	private int current = 0;
	private String executeDriver;
	
	public DriverEnumerator()
	{
		driversKey.add("ChromeDriver");
		driversValue.add("org.openqa.selenium.chrome.ChromeDriver");
		
		driversKey.add("FirefoxDriver");
		driversValue.add("org.openqa.selenium.firefox.FirefoxDriver");
		
		driversKey.add("InternetExplorerDriver");
		driversValue.add("org.openqa.selenium.ie.InternetExplorerDriver");
	}
	
	public DriverEnumerator(String driverName)
	{
		this();
		executeDriver = driverName;
	}

	@Override
	public boolean hasMoreElements() {
		return current < driversKey.size();
	}

	@Override
	public WebDriver nextElement() 
	{
		String key = null;
		String klass = null;
		if(executeDriver == null)
		{
			key = driversKey.get(current);
			klass = driversValue.get(current);
			++current;
		}
		else
		{
			key = executeDriver;
			klass = driversValue.get(driversKey.indexOf(executeDriver));
			current = driversKey.size();
		}
		
		
		WebDriver driver = null;
		try 
		{
			if(key.equals("FirefoxDriver"))
			{
				FirefoxProfile profile = new FirefoxProfile();
				profile.setAcceptUntrustedCertificates(true);
				profile.setAssumeUntrustedCertificateIssuer(false);

				driver = (WebDriver)Class.forName(klass).getConstructor(FirefoxProfile.class).newInstance(profile);
			}
			else
			{
				driver = (WebDriver)Class.forName(klass).newInstance();
			}
			
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			
			e.printStackTrace();
		}
		
		return driver;
	}

}
