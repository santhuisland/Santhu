package AsyncCommService.ResponseHandler;

import AsyncCommService.TransportLayer.ResponseTransportNotifier;

/**
 * Created by Island on 17/03/16.
 */
public abstract class ResponseHandlerBase implements ResponseTransportNotifier
{
	ResponseNotifier mParentCallBackHandle = null;
	String mResponseData = null;
	int 	mErrorCode  =-1;
	String mErrorMessage = null;
	
	Object mRequestDataObj = null;
	
	
	public ResponseHandlerBase(ResponseNotifier pParentCallBackHandle,Object pRequestObject)
	{
		mParentCallBackHandle = pParentCallBackHandle;
		mRequestDataObj = pRequestObject;
	}
		
}