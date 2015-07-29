package TwitterService;

import java.util.List;

public interface TwitterResponseData {
	
    public boolean responseData(TwitterServices.TWITTERL_OPERATION_TYPES pOperationType,String  pResult ,String pErrorMessage,boolean mISsuccess);
    
    public boolean responseTimeLineData(TwitterServices.TWITTERL_OPERATION_TYPES pOperationType,List<twitter4j.Status> pResult ,String pErrorMessage,boolean mISsuccess);
}
