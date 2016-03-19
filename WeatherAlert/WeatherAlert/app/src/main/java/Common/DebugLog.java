
package Common;

import android.util.Log;

/**
 * Created by Island on 17/03/16.
 */
public class DebugLog
{
    private static final String LOGTAG = "Weather";
    
    
    /** Logging functions to generate ADB logcat messages. */
    
    public static final void LOGE(String nMessage)
    {
        Log.e(LOGTAG, nMessage);
    }
    
    
    public static final void LOGW(String nMessage)
    {
        Log.w(LOGTAG, nMessage);
    }
    
    
    public static final void LOGD(String nMessage)
    {
        Log.d(LOGTAG, nMessage);
    }
    
    
    public static final void LOGI(String nMessage)
    {
        Log.i(LOGTAG, nMessage);
    }
}
