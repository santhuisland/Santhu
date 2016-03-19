package AsyncCommService;

import android.os.AsyncTask;

import AppManager.RequestStructures;
import AsyncCommService.RequestHandler.RequestHandlerBase;
import AsyncCommService.RequestHandler.WeatherRequest;
import AsyncCommService.ResponseHandler.ResponseNotifier;
import AsyncCommService.TransportLayer.TransportError;
import AsyncCommService.TransportLayer.TransportLayer;
import Common.Globals;
import Common.Utility;

/**
 * Created by Island on 17/03/16.
 */

/*
This class is AsyncTask runs in background without blocking UI thread when intraction with server
 */
public class AsyncCommService extends AsyncTask<Object, Integer, String>
{

	private RequestHandlerBase mRequestBase = null;
	private int	mServiceType = 0;
	private ResponseNotifier mParentCallBackHandle = null;
	
	String mCacheID = null;
	
	TransportLayer		mTransport = null;
	int 	mErrorCode = -1;
	String 	mErrorMessage = null;
	
	public AsyncCommService(int pServiceType,ResponseNotifier pParentCallBackHandle,String pCacheID)
	{
		mServiceType = pServiceType;
		mParentCallBackHandle = pParentCallBackHandle;
		mCacheID = pCacheID;
	}

	@Override
	protected String doInBackground(Object... params) {
		// TODO Auto-generated method stub
		
		String lResponseData  = null;
		//Notify the Start of the Process
		if(mParentCallBackHandle != null)
			mParentCallBackHandle.OnApiNotifyOnStart(0);

		if(mServiceType == RequestStructures.REQUEST_TYPE_WEATHER_REPORT)
		{
			//Typecast Object Param to LoginDataStruct
			RequestStructures.WeatherDataStruct lStruct = (RequestStructures.WeatherDataStruct) params[0];
			mRequestBase = new WeatherRequest(mParentCallBackHandle,lStruct);
		}

		//check for internet connection
		if(Utility.IsNetworkConnectivityAvailable(Globals.getAppContext()))
		{
			//Prepare the Service request Data
			PrepareServiceRequestData();

			//Process the request with server
			mTransport = new TransportLayer();

			lResponseData = mTransport.doAPICall(mRequestBase);
			if(mTransport.getResponseCode() == 200 )
			{
				mErrorCode = TransportError.ERROR_TRANS_SUCCESS;
				mErrorMessage = null;
			}
			else
			{
				mErrorCode = TransportError.ERROR_TRANS_FAILED;
				mErrorMessage = mTransport.getErrorMsg();
				lResponseData = null;
			}

			//Notify the corresponding Response class to process the response
			mRequestBase.getResponseHandler().OnApiTransportResponse(lResponseData, mErrorCode, mErrorMessage);
		}
		else
		{
			//Notify the corresponding Response class to process the response
			mRequestBase.getResponseHandler().OnApiTransportResponse(lResponseData, TransportError.ERROR_NO_INTERNETCONNECTION, mErrorMessage);
		}
		return lResponseData;
	}
	
	private void PrepareServiceRequestData()
	{
		if(mRequestBase != null)
		{
			mRequestBase.doPrepareUrl();
			mRequestBase.doPrepareReqBody();
			mRequestBase.doPrepareReqHeadder();
		}
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress)
	{

	}

	@Override
	protected void onPostExecute(String result)
	{
		//Notify the response to parent about completion of the request
		if(mRequestBase != null)
			mRequestBase.getResponseHandler().NotifyOnCompletion();
		//Finally delete this Async object from  Cache
		Globals.getGlobalInstance().removeAsyncEntryFromCache(mCacheID);
	}

	
}
