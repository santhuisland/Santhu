package AsyncCommService.RequestHandler;

import AsyncCommService.ResponseHandler.ResponseHandlerBase;
import AsyncCommService.ResponseHandler.ResponseNotifier;
import AsyncCommService.TransportLayer.RequestHeaders;

/**
 * Created by Island on 17/03/16.
 */
public abstract class RequestHandlerBase
{
	String 						mRequestType = null;
	RequestHeaders[]  			mReqHeaders = null;
	String						mReqBody = null;
	String 						mActualUrl = null;
	ResponseHandlerBase mResponseBaseHandler = null;
	
	ResponseNotifier mParentCallBackHandle = null;


	public RequestHandlerBase()
	{
	}
	
	/*
	 * 
	 */
	public abstract void doPrepareUrl();
	/*
	 * This function has to implement on all request handler
	 *  to prepare and set their respective Header for any request
	 */
	public abstract void doPrepareReqHeadder();
	
	
	/*
	 * 
	 * Prepare and Set the Request body for all request
	 */
	public abstract void doPrepareReqBody();
	
	public String getReqBody()
	{
		return mReqBody;
	}
	
	public RequestHeaders[] getReqHeaders()
	{
		return mReqHeaders;
	}
	
	public String getRequestType()
	{
		return mRequestType;
	}
	
	public String getActualURL()
	{
		return mActualUrl;
	}
	
	public ResponseHandlerBase getResponseHandler()
	{
		return mResponseBaseHandler;
	}
	
	public ResponseNotifier getParentCallBackHandle()
	{
		return mParentCallBackHandle;
	}
	
}