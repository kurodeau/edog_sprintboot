package com.util;

import java.io.IOException;
import java.util.Properties;

import com.twilio.Twilio;
import com.twilio.rest.microvisor.v1.App;

public class TwilloService {

	public static String ACCOUNT_SID = "ACb8cab11cdbb9a3c3e032fc597dc9f76d";
	public static String AUTH_TOKEN = "";
	public static String MY_PHONE = "";
	public static String TWILLOPHONE = "";

	public static String loadInfo(String path) {
		Properties prop = new Properties();

		try {
			prop.load(App.class.getClassLoader().getResourceAsStream(path));

			// get the property value and print it out

			AUTH_TOKEN = prop.getProperty("TWILLOTOKEN");
			MY_PHONE = prop.getProperty("MYPHONE");
			TWILLOPHONE = prop.getProperty("TWILLOPHONE");

			System.out.println(AUTH_TOKEN);
			System.out.println(MY_PHONE);
			System.out.println(TWILLOPHONE);
			return "success";

		} catch (IOException ex) {
			ex.printStackTrace();
			return "failure";
		}

	}

	public static String sendMessage(String targetPhone) {
		String TARGET_PHONE = targetPhone;

		String status = loadInfo("secret.properties");
		try {
			if (status.equals("success")) {
				Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
				Message message = Message.creator(new com.twilio.type.PhoneNumber(MY_PHONE),
						new com.twilio.type.PhoneNumber(TARGET_PHONE), "PAS:").create();

				System.out.println(message.getSid());
				return "success";
			}
		} catch (Exception e) {
			return e.getMessage();

		}
		return "failure";

	}

}