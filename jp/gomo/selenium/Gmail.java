package jp.gomo.selenium;

import java.security.Security;
import java.text.ParseException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

public class Gmail {
	
	private Store store;
	private Session session;
	private URLName urlName;
	
	public Gmail(String login_id, String password) throws MessagingException
	{
		Properties props = new Properties();
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		props.setProperty("mail.pop3.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.pop3.socketFactory.fallback","false");

		urlName = new URLName(
			"pop3s", 
		    "pop.gmail.com",
		    995, 
		    null, 
		    login_id, 
		    password
		);

		session = Session.getDefaultInstance(props, null);
		
	}
	
	public Message waitForFindMessage(MessageFinder finder) throws InterruptedException, MessagingException, ParseException
	{
		Message message = null;
		for (int i = 0; i < 20; i++) 
		{
			store = session.getStore(urlName);
			store.connect();
			
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			
			Message[] messages = folder.getMessages();
			for (int j = 0; j < messages.length; j++) 
			{
				if(finder.isMatch(messages[j]))
				{
					message = messages[j];
					break;
				}
			}
			
			if(message != null)
			{
				break;
			}
			
			store.close();
			store = null;
			Thread.sleep(800);
		}
		
		return message;
	}
	
	public void close() throws MessagingException
	{
		if(store != null)
		{
			store.close();
		}
	}

}
