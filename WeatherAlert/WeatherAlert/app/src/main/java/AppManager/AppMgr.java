package AppManager;

import org.json.JSONObject;
import org.json.JSONTokener;

import AsyncCommService.AsyncCommService;
import AsyncCommService.ResponseHandler.ResponseNotifier;
import AsyncCommService.TransportLayer.TransportError;
import Common.Globals;

/**
 * Created by Island on 17/03/16.
 */
public class AppMgr {

    public static final int ACTION_INCLUDE_SERVICE_WEATHER_REPORT           			 = 0x01;

    ResponseNotifier mParentUIcallBack = null;
    RequestStructures.WeatherDataStruct     mWeatherStruct = null;

    int		mProcessActions = 0;

    /*
        This API is used to get Weather report
     */
    public void performWeatherRequestProcess(ResponseNotifier pParentCallBackHandle, RequestStructures.WeatherDataStruct pStruct)
    {
        mParentUIcallBack = pParentCallBackHandle;
        mWeatherStruct = pStruct;

        mProcessActions = 0;
        mProcessActions = ACTION_INCLUDE_SERVICE_WEATHER_REPORT;

        executeWFProcess();
    }


    private void executeWFProcess()
    {
        if((mProcessActions & ACTION_INCLUDE_SERVICE_WEATHER_REPORT) == ACTION_INCLUDE_SERVICE_WEATHER_REPORT)
        {
            if(Globals.APPID_KEY != null) {
                performInternalWeatherRequest(new ProcessCallBack(ACTION_INCLUDE_SERVICE_WEATHER_REPORT), mWeatherStruct);
            }
            else
            {
                mProcessActions = 0;
                if(mParentUIcallBack != null)
                    mParentUIcallBack.OnApiNotifyOnCompletion(mWeatherStruct, null, RequestStructures.ERROR_NO_APPID_FOUND, null);
            }
        }
    }

    private void performInternalWeatherRequest(ResponseNotifier pCallBackHandle, RequestStructures.WeatherDataStruct pStruct) {
        //create Async Task
        AsyncCommService lNewAsyncCommService = new AsyncCommService(RequestStructures.REQUEST_TYPE_WEATHER_REPORT, pCallBackHandle, "" + ++Globals.getGlobalInstance().mSyncCommServiceCacheID);

        //Add newly created Async Task object to cache , so we can delete later
        Globals.getGlobalInstance().mSyncCommServiceCache.put("" + Globals.getGlobalInstance().mSyncCommServiceCacheID, lNewAsyncCommService);
        //Trigger the Async Task with Input Datastructure
        lNewAsyncCommService.execute(pStruct);
    }




    //call back class for single process notified from response handlers and filter and propogate the proper response to caller
    class ProcessCallBack implements ResponseNotifier
    {
        int mCurrentAction = 0;

        public  ProcessCallBack(int pCurrentAction)
        {
            mCurrentAction = pCurrentAction;
        }

        @Override
        public void OnApiNotifyOnStart(int pStatusCode) {
            if (mParentUIcallBack != null)
                mParentUIcallBack.OnApiNotifyOnStart(mCurrentAction);
        }

        @Override
        public void OnApiNotifyOnCompletion(Object pRequestData,
                                            String pResponseData, int pErrorCode, String pErrorMessage) {
            // TODO Auto-generated method stub

            /*
            Process all Events for each API calls as per the Application Logic
             */
            if(pErrorCode == TransportError.ERROR_TRANS_SUCCESS )
            {
                try {

                    if ((mCurrentAction & ACTION_INCLUDE_SERVICE_WEATHER_REPORT) == ACTION_INCLUDE_SERVICE_WEATHER_REPORT)
                    {
                        JSONObject lResponseJson = (JSONObject) new JSONTokener(pResponseData).nextValue();

                        mProcessActions = 0;
                        if ( lResponseJson != null) {
                            if (mParentUIcallBack != null)
                                mParentUIcallBack.OnApiNotifyOnCompletion(pRequestData, pResponseData, RequestStructures.API_WEATHER_REPORT_SUCCESS, null);
                        } else {
                            if (mParentUIcallBack != null)
                                mParentUIcallBack.OnApiNotifyOnCompletion(pRequestData, pResponseData, RequestStructures.ERROR_PARSING_RESPONSE_FAIL, null);
                        }
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                    //Process failed notify the UIcallback based on the process failed
                    if (mParentUIcallBack != null)
                        mParentUIcallBack.OnApiNotifyOnCompletion(pRequestData, null, RequestStructures.ERROR_PARSING_RESPONSE_FAIL, pErrorMessage);

                }

            }
            else if(pErrorCode == TransportError.ERROR_NO_INTERNETCONNECTION )
            {
                mProcessActions = 0;
                if (mParentUIcallBack != null)
                    mParentUIcallBack.OnApiNotifyOnCompletion(pRequestData, null, pErrorCode, pErrorMessage);

            }
            else    //fail
            {
                if ((mCurrentAction & ACTION_INCLUDE_SERVICE_WEATHER_REPORT) == ACTION_INCLUDE_SERVICE_WEATHER_REPORT)
                {
                    //reset the processAction
                    mProcessActions = 0;
                    //Process failed notify the UIcallback based on the process failed
                    if (mParentUIcallBack != null)
                        mParentUIcallBack.OnApiNotifyOnCompletion(pRequestData, null, RequestStructures.API_WEATHER_REPORT_FAIL, pErrorMessage);

                }
                else
                {
                    mProcessActions = 0;

                    if (mParentUIcallBack != null)
                        mParentUIcallBack.OnApiNotifyOnCompletion(pRequestData, null, RequestStructures.API_WEATHER_REPORT_FAIL, pErrorMessage);

                }
            }
        }

        @Override
        public void OnApiNotifyOnIntermediate(int pStatusCode) {
            // TODO Auto-generated method stub

        }
    }
}
