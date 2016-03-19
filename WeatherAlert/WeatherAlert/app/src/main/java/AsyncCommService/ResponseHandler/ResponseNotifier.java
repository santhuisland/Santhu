package AsyncCommService.ResponseHandler;


/**
 * Created by Island on 17/03/16.
 */
//call Back INterface to notify with data
public interface ResponseNotifier {

	/*
	 * THis call back will be sent from Async thread on UI context so we can notify to UI layer of the parent
	 */
	void OnApiNotifyOnStart(int pStatusCode);
	void OnApiNotifyOnCompletion(Object pRequestData, String pResponseData, int pErrorCode, String pErrorMessage);
	
	void OnApiNotifyOnIntermediate(int pStatusCode);
}