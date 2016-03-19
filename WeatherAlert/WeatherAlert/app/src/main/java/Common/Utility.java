package Common;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Island on 17/03/16.
 */

public class Utility
{
	/*
	Check the internet connection is Available
	 */
	public static boolean IsNetworkConnectivityAvailable(Context context)
	{
		NetworkInfo liInfo = null;

		boolean state = false;
		ConnectivityManager 	lConnectivityMgr = null;
		lConnectivityMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(lConnectivityMgr != null)
		{
			liInfo = lConnectivityMgr.getActiveNetworkInfo();

			if(liInfo != null)
				state = liInfo.isConnected();
		}
		return state;
	}

}
