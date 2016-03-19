package com.island.weatheralert;

import android.test.ActivityUnitTestCase;

import AppManager.AppMgr;
import AppManager.RequestStructures;
import AsyncCommService.ResponseHandler.ResponseNotifier;
import Common.Globals;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityUnitTestCase<MainActivity> {

    AppMgr	mAppManager = null;
    private SyncronizeTalker asyncTalker = null;
    Globals global = null;

    public ApplicationTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        /*
        create the Global instance
         */
        global = Globals.CreateGlobalsInstance(getInstrumentation().getTargetContext());
        mAppManager  = new AppMgr();

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public void testWeatherDownloadApiTest()
    {
        /*this SyncronizeTalker is used to Causes the calling thread to wait
         until another thread calls the notify() or notifyAll() method of this object.
          */
        asyncTalker = new SyncronizeTalker();

        RequestStructures.WeatherDataStruct lStruct = new RequestStructures.WeatherDataStruct("London", "UK");
        mAppManager.performWeatherRequestProcess(new DownloadNotifier(), lStruct);
        asyncTalker.doWait(); // <--- wait till "async.doNotify()" is called
    }


    /*
    Callback Notifier class
     */
    class DownloadNotifier implements ResponseNotifier {

        @Override
        public void OnApiNotifyOnStart(int pStatusCode) {

        }

        @Override
        public void OnApiNotifyOnCompletion(Object pRequestData, String pResponseData, int pErrorCode, String pErrorMessage) {

            assertEquals("Fail to download weather report ", (RequestStructures.API_WEATHER_REPORT_SUCCESS == pErrorCode),true);
            asyncTalker.doNotify(); // <---Notify the wait thread
        }

        @Override
        public void OnApiNotifyOnIntermediate(int pStatusCode) {

        }
    }

}

