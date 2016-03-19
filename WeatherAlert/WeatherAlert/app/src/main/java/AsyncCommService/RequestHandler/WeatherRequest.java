package AsyncCommService.RequestHandler;

import AppManager.RequestStructures;
import AsyncCommService.ResponseHandler.ResponseNotifier;
import AsyncCommService.ResponseHandler.WeatherResponse;
import AsyncCommService.TransportLayer.TansportMacros;
import Common.Globals;
import Common.Jsonconsts;

/**
 * Created by Island on 17/03/16.
 */
public class WeatherRequest extends RequestHandlerBase {

    RequestStructures.WeatherDataStruct mDataStruct = null;
    public WeatherRequest(ResponseNotifier pParentCallBackHandle, RequestStructures.WeatherDataStruct pDataStruct) {
        super();
        // TODO Auto-generated constructor stub
        mDataStruct = pDataStruct;

        mRequestType = TansportMacros.REQUEST_METHOD_GET;
        //Set the ResponseHandler to propagate response to specific response handler
        mResponseBaseHandler = new WeatherResponse(pParentCallBackHandle,mDataStruct);
    }

    @Override
    public void doPrepareUrl() {
        // TODO Auto-generated method stub
        mActualUrl = Globals.BASE_URL_OPEN_WEATHER_MAP;

        if(!mActualUrl.endsWith("/"))  mActualUrl +="/";

        mActualUrl += Globals.URL_WEATHER_REPORT;
        mActualUrl += "?" + Jsonconsts.JSON_Q + "=" + mDataStruct.mCity;
        if(mDataStruct.mCountryCode != null && mDataStruct.mCountryCode.length() > 0)
            mActualUrl += "," + mDataStruct.mCountryCode;
        mActualUrl += "&" + Jsonconsts.JSON_APP_ID + "=" + Globals.APPID_KEY;
    }

    @Override
    public void doPrepareReqHeadder() {
        // TODO Auto-generated method stub

        //no headers for this request
    }

    @Override
    public void doPrepareReqBody() {
        // TODO Auto-generated method stub

        if(mRequestType == TansportMacros.REQUEST_METHOD_POST)
        {
            //No body for this Request
        }
    }

}