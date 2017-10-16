package com.hagame.e758moneyapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by chineseword on 2015/2/26.
 */
public class Utils {

	/**
	 * Returns MAC address of the given interface name.
	 * @param interfaceName eth0, wlan0 or NULL=use first interface
	 * @return  mac address or empty string
	 */
	public static String getMACAddress(String interfaceName) {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				if (interfaceName != null) {
					if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
				}
				byte[] mac = intf.getHardwareAddress();
				if (mac==null) return "";
				StringBuilder buf = new StringBuilder();
				for (int idx=0; idx<mac.length; idx++)
					buf.append(String.format("%02X:", mac[idx]));
				if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
				return buf.toString();
			}
		} catch (Exception ex) { } // for now eat exceptions
		return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
	}


	public static String getIdentifier(Activity mActivity, Context mContext){
		String identifier = null;

		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

		if (tm != null)
			identifier = tm.getDeviceId();
		if (identifier == null || identifier.length() == 0)
			identifier = Settings.Secure.getString(mActivity.getContentResolver(), Settings.Secure.ANDROID_ID);

		return identifier;
	}

	public static String getIdentifier(Context mContext){
		String identifier = null;

		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

		if (tm != null)
			identifier = tm.getDeviceId();
		if (identifier == null || identifier.length() == 0)
			identifier = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

		return identifier;
	}

	public static String sha256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.reset();

		input = input + com.hagame.e758moneyapp.common.Settings.hKey;

		byte[] byteData = digest.digest(input.getBytes("UTF-8"));
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public static String ssha256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.reset();

		byte[] byteData = digest.digest(input.getBytes("UTF-8"));
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public static String getOtp(Context mContext){
		SharedPreferences settings = mContext.getSharedPreferences("1758_cpi_info", 0);

		if (settings == null) {
			return "";
		}

		return settings.getString("userOtp", "");
	}

	public static String getTimeStamp(){
		long timeMillis = System.currentTimeMillis();
		return Long.toString(TimeUnit.MILLISECONDS.toSeconds(timeMillis));
	}

	public static void setOtp(Context mContext, String userOtp){
		SharedPreferences settings = mContext.getSharedPreferences("1758_cpi_info", 0);
		settings.edit().putString("userOtp", userOtp).apply();
	}


	public static boolean hasKitKat() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}


	public static String getUrlResponse(String url) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		String retSrc;
		try
		{
			HttpResponse response = client.execute(request);
			retSrc = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			throw new Exception(e.getMessage());
		} catch (IOException e1) {
			throw new Exception(e1.getMessage());
		} catch (Exception e2) {
			throw new Exception(e2.getMessage());
		}

		return retSrc;
	}
}
