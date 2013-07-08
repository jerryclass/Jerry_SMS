package com.jerrywu.plugin.sms.test;

import com.jerrywu.plugin.sms.SMS;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SMS sms = new SMS("account","password","phoneNumber");
		sms.setPopup(true);

		System.out.println(sms.sendMessage("訊息內容"));
	}

}
