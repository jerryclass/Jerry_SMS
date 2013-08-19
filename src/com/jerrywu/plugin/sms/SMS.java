package com.jerrywu.plugin.sms;

import com.jerrywu.http.HTTPConnection;
import com.jerrywu.http.HTTPMethod;
import com.jerrywu.http.HTTPRequestTag;
import com.jerrywu.http.HTTPResponseTag;


/**
 * 簡訊基礎類別
 * @author Jerry
 *
 */
public class SMS {

	//API 網址
	private final String httpApiUrl = "http://api.twsms.com";
	private final String httpsApiUrl = "https://api.twsms.com";
	
	private String username; //使用者帳號
	private String password; //使用者密碼
	private String sendtime; //預計發送時間
	private int expirytime;  //有效時間
	private boolean popup;   //強迫將簡訊內容顯示
	private boolean mo;      //簡訊發送門號使用 0961295325 顯示
	private String mobile;   //接收者的門號
	private boolean longsms; //使用長簡訊發送
	private boolean usingSSL;
	
	
	//建構元,設定要傳送的帳號 密碼 手機
	public SMS(String account,String password,String mobile)
	{
		this.username = account;
		this.password = password;
		this.mobile = mobile;
		this.expirytime = -1;
		this.sendtime = null;
		this.popup = false;
		this.mo = false;
		this.longsms = false;
		this.usingSSL = false;
	}
	
	public void setPopup(boolean popup)
	{
		this.popup = popup;
	}
	
	//取得剩餘通數
	public String getCash()
	{
		HTTPRequestTag requestTag = new HTTPRequestTag("http://jerryclass.twsms.com/index.php",HTTPMethod.POST);
		requestTag.pushAttributes("page","index.htm");
		requestTag.pushAttributes("username","shengjhe");
		requestTag.pushAttributes("password","1234");
		requestTag.pushAttributes("status","　　登入　　","big5");
		requestTag.pushAttributes("x","61");
		requestTag.pushAttributes("y","10");
		
		//接收Server端的回應
		HTTPResponseTag responseTag = HTTPConnection.getResult(requestTag, "big5");
				
		//取得Cookiese
		String Cookies = responseTag.getCookies();
		
		
		//取得餘額網址
		HTTPRequestTag cashTag = new HTTPRequestTag("http://jerryclass.twsms.com/index.php?page=send_now.htm",HTTPMethod.GET);
		cashTag.setCookies(Cookies);
		
		//接收Server端的回應
		HTTPResponseTag cashResponseTag = HTTPConnection.getResult(cashTag, "big5");

		return (cashResponseTag.getResponse().split("即時發送 - 您的通數還有 <font color=#FF0000>")[1].split("</font>")[0]);
	}
	
	//發送訊息
	public String sendMessage(String message)
	{
		String targetAddress = null;
		
		if(this.usingSSL)
		{
			targetAddress = String.format("%s/smsSend.php", httpsApiUrl);
		}
		else
		{
			targetAddress = String.format("%s/smsSend.php", httpApiUrl);
		}
		
		//建立請求Tag
		HTTPRequestTag requestTag = new HTTPRequestTag(targetAddress,HTTPMethod.GET);
		
		//建立基本參數
		requestTag.pushAttributes("username", this.username);
		requestTag.pushAttributes("password", this.password);
		requestTag.pushAttributes("mobile", this.mobile);
		
		
		//設定簡訊預約發送時間
		if(this.sendtime != null)
		{
			requestTag.pushAttributes("sendtime", String.valueOf(this.sendtime));
		}
		
		//設定簡訊有效時間
		if(this.expirytime > 0)
		{
			if(this.expirytime <= 86400)
			{
				requestTag.pushAttributes("expirytime", String.valueOf(this.expirytime));
			}
			else
			{
				System.out.println("expirytime is too long");
			}
		}
		
		//設定是否強制顯示簡訊
		if(this.popup)
		{
			requestTag.pushAttributes("popup", "Y");
		}
		
		//簡訊發送門號使用 0961295325 顯示
		if(this.mo)
		{
			requestTag.pushAttributes("mo", "Y");
		}

		
		//使用長簡訊發送
		if(this.longsms)
		{
			requestTag.pushAttributes("longsms", "Y");
		}
		
		requestTag.pushAttributes("message", message,"utf-8");
		
		HTTPResponseTag responseTag = HTTPConnection.getResult(requestTag, "utf-8");
				
		return responseTag.getResponse();
	}
	
	
	
	
}
