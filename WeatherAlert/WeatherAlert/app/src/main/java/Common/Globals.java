package Common;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import AsyncCommService.AsyncCommService;

/**
 * Created by Island on 17/03/16.
 */

public class Globals {

    private static Globals m_cGlobalInstance = null;
    private static Context m_cContext = null;

    public Map<String, AsyncCommService> mSyncCommServiceCache = new HashMap<String, AsyncCommService>();
    public int mSyncCommServiceCacheID = 0;

    private Globals(){}

    public static Globals CreateGlobalsInstance(Context mContext) {
        if (m_cGlobalInstance == null) {
            m_cGlobalInstance = new Globals();
            m_cContext = mContext;
        }
        return m_cGlobalInstance;
    }

    public static Context getAppContext() {
        return m_cContext;
    }

    public static  Globals getGlobalInstance()
    {
        return m_cGlobalInstance;
    }

    /*
    * This function is called from AsyncCommService
    */
    public void removeAsyncEntryFromCache(String pCacheID) {
        mSyncCommServiceCache.remove(pCacheID);
    }

    public static String APPID_KEY = "b1b15e88fa797225412429c1c50c122a";

    public static String BASE_URL_OPEN_WEATHER_MAP   = "http://api.openweathermap.org/data/2.5/";
    public static String URL_WEATHER_REPORT          = "weather";

}
