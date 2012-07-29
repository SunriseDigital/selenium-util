package jp.gomo.selenium;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.mail.Message;
import javax.mail.MessagingException;

abstract public class MessageFinder {
	DateFormat date_format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
	
	private Date getDate(Message message) throws ParseException, MessagingException
	{
		return date_format.parse(message.getHeader("Date")[0]);
	}
	
	public boolean isMatch(Message message) throws ParseException, MessagingException
	{
		Date date = getDate(message);
		Date now = new Date();
		return isMatch(message, (now.getTime() - date.getTime()) / 1000);
	}
	
	abstract public boolean isMatch(Message message, long elapsed_time) throws MessagingException;

}
