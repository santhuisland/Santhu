package AsyncCommService.TransportLayer;

/**
 * Created by Island on 17/03/16.
 */
//call Back INterface to notify with data from Async Transport Layers
public interface ResponseTransportNotifier {

	/*
	 * THis Call back will be sent my Background Thread with Response and corresponding error message and code
	 */
	void OnApiTransportResponse(String pResponseData, int pErrorCode, String pErrorMessage);
	
	void NotifyOnCompletion();
}