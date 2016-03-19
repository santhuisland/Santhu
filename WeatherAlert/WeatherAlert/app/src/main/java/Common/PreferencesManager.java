package Common;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Island on 17/03/16.
 */
/**
 * Utility class to handle getting/setting various application preferences.
 */
public final class PreferencesManager {
	private static final String TAG = "Weather";
	
	private static final String PREFERENSE_NAME_BASE = "Weather";



	private static final String PREFERENCES_FAVOURITES			= "Weather_favourites";
	private static final String PREFERENCES_CURRENT_FAV_CITY			= "current_city";
	private static final String PREFERENCES_CURRENT_FAV_CODE			= "current_code";



	public static void clearAllPrefs() {
        SharedPreferences preferences = Globals.getAppContext().getSharedPreferences(PREFERENSE_NAME_BASE, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
	}


	public static void setWeatherFavourite( String pFavouriteCity,String pFavourateCode)
	{

		try {
			if (pFavouriteCity != null) {
				JSONObject lFevObject = getFavourites();
				JSONArray	lFavArray = null;

				SharedPreferences prefs = Globals.getAppContext().getApplicationContext().getSharedPreferences(PREFERENSE_NAME_BASE, Context.MODE_PRIVATE);
				Editor editor = prefs.edit();

				if (lFevObject == null) {
					lFevObject = new JSONObject();
					lFevObject.put(Jsonconsts.JSON_FAVOURITES, new JSONArray());
				}
				lFavArray = lFevObject.optJSONArray(Jsonconsts.JSON_FAVOURITES);

				//check if already in Array before adding
				boolean lPresent = false;
				for(int i=0;i<lFavArray.length();i++)
				{
					JSONObject  lFevObj  = lFavArray.getJSONObject(i);
					String lCode = lFevObj.optString(Jsonconsts.JSON_CITY);
					if(lCode != null && lCode.equalsIgnoreCase(pFavouriteCity))
					{
						lPresent = true;
						break;
					}
				}
				if(!lPresent) {
					JSONObject lFevObj = new JSONObject();
					lFevObj.put(Jsonconsts.JSON_CITY, pFavouriteCity);
					lFevObj.put(Jsonconsts.JSON_COUNTRY_CODE, (pFavourateCode == null) ? "" : pFavourateCode);

					lFavArray.put(lFevObj);

					editor.putString(PREFERENCES_FAVOURITES, lFevObject.toString().trim());
					editor.commit();
				}
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void removeFavouriteFromList(String pCity)
	{
		try{
			JSONObject	lFevObjects = getFavourites();
			JSONArray  lFavouriteArray = lFevObjects.optJSONArray(Jsonconsts.JSON_FAVOURITES);
			for(int i=0;i<lFavouriteArray.length();i++)
			{
				JSONObject lFavouriteDetails = (JSONObject) lFavouriteArray.get(i);
				if(lFavouriteDetails.optString(Jsonconsts.JSON_CITY).equalsIgnoreCase(pCity))
				{
					lFavouriteArray.remove(i);

					SharedPreferences prefs = Globals.getAppContext().getApplicationContext().getSharedPreferences(PREFERENSE_NAME_BASE, Context.MODE_PRIVATE);
					Editor editor = prefs.edit();
					editor.putString(PREFERENCES_FAVOURITES, lFevObjects.toString().trim());
					editor.commit();
					break;
				}
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static JSONObject getFavourites()
	{
		JSONObject	lFevObjects = null;
		String 	lTempText = null;
		SharedPreferences prefs = Globals.getAppContext().getApplicationContext().getSharedPreferences(PREFERENSE_NAME_BASE, Context.MODE_PRIVATE);
		lTempText = prefs.getString(PREFERENCES_FAVOURITES, null);

		try {
			if(lTempText != null)
				lFevObjects = (JSONObject) new JSONTokener(lTempText).nextValue();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return  lFevObjects;
	}

	public static void setCurrentFavCity( String pFavouriteCity)
	{
		if(pFavouriteCity != null) {
			SharedPreferences prefs = Globals.getAppContext().getApplicationContext().getSharedPreferences(PREFERENSE_NAME_BASE, Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putString(PREFERENCES_CURRENT_FAV_CITY, pFavouriteCity);
			editor.commit();
		}
	}

	public static void setCurrentFavCode( String pFavouriteCode)
	{
		if(pFavouriteCode != null) {
			SharedPreferences prefs = Globals.getAppContext().getApplicationContext().getSharedPreferences(PREFERENSE_NAME_BASE, Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putString(PREFERENCES_CURRENT_FAV_CODE, pFavouriteCode);
			editor.commit();
		}
	}

	public static String getCurrentFavCity()
	{
		String 	lTempText = null;
		SharedPreferences prefs = Globals.getAppContext().getApplicationContext().getSharedPreferences(PREFERENSE_NAME_BASE, Context.MODE_PRIVATE);
		lTempText = prefs.getString(PREFERENCES_CURRENT_FAV_CITY, "");

		return  lTempText;
	}

	public static String getCurrentFavCode()
	{
		String 	lTempText = null;
		SharedPreferences prefs = Globals.getAppContext().getApplicationContext().getSharedPreferences(PREFERENSE_NAME_BASE, Context.MODE_PRIVATE);
		lTempText = prefs.getString(PREFERENCES_CURRENT_FAV_CODE, "");

		return  lTempText;
	}

	public static void removeCurrentFavCity()
	{
		SharedPreferences prefs = Globals.getAppContext().getApplicationContext().getSharedPreferences(PREFERENSE_NAME_BASE, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();

		editor.remove(PREFERENCES_CURRENT_FAV_CITY);
		editor.commit();
	}
	public static void removeCurrentFavCode()
	{
		SharedPreferences prefs = Globals.getAppContext().getApplicationContext().getSharedPreferences(PREFERENSE_NAME_BASE, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();

		editor.remove(PREFERENCES_CURRENT_FAV_CODE);
		editor.commit();
	}

	public static void removeAllFavourites()
	{
		SharedPreferences prefs = Globals.getAppContext().getApplicationContext().getSharedPreferences(PREFERENSE_NAME_BASE, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();

		editor.remove(PREFERENCES_FAVOURITES);
		editor.commit();
	}
}
