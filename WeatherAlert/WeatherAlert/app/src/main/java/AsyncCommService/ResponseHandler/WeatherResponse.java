package AsyncCommService.ResponseHandler;

/**
 * Created by Island on 17/03/16.
 */
public class WeatherResponse extends ResponseHandlerBase {


    public WeatherResponse(ResponseNotifier pParentCallBackHandle, Object pRequestObject) {
        super(pParentCallBackHandle, pRequestObject);
        // TODO Auto-generated constructor stub
    }

    /*
    This callback will be triggered from AsyncCommService in background function for each specific response
    you can process/save/modify the response depends on the application requirements
     */
    @Override
    public void OnApiTransportResponse(String pResponseData, int pErrorCode,
                                       String pErrorMessage) {
        // TODO Auto-generated method stub

        mResponseData = pResponseData;
        mErrorCode = pErrorCode;
        mErrorMessage = pErrorMessage;

    }

    /*
    This callback will be triggered from AsyncCommService on Async task completion
    so we can notifiy to AppManager to propagate response and handling the states based on the Application Logic required
     */
    @Override
    public void NotifyOnCompletion() {
        // TODO Auto-generated method stub
        //finally notify the caller
        if (mParentCallBackHandle != null)
            mParentCallBackHandle.OnApiNotifyOnCompletion(mRequestDataObj, mResponseData, mErrorCode, mErrorMessage);

    }
}