package com.crowdmix.twitter;





import TwitterService.TwitterServices;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Globals {

	public static Context mAppContext = null;
	public static TwitterServices		mTwitterService = null; 
	
	public static  boolean isNetworkAvailable()
	{
		NetworkInfo 			lWifiInfo = null;
		NetworkInfo 			lMobileInfo = null;
		
		boolean state = false;	
		boolean wifiState  = false;
		boolean mobilestate = false;
		ConnectivityManager 	lConnectivityMgr = null;
		lConnectivityMgr = (ConnectivityManager) mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(lConnectivityMgr != null)
		{
			lWifiInfo = lConnectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			lMobileInfo = lConnectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			
			if(lWifiInfo != null)
				wifiState = lWifiInfo.isConnected();
			if(lMobileInfo != null)
				mobilestate = lMobileInfo.isConnected();
				
			if ((!wifiState) && (!mobilestate)) {
			} else {
				state = true;
			}
		}
		return state;
	}

}
