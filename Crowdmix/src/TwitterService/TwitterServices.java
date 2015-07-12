package TwitterService;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterServices {
	
	public static enum TWITTERL_OPERATION_TYPES
	{
		OPERATION_DEFAULT,
		TWITT_OPERATION_LOGIN,
		TWITT_OPERATION_LOGOUT,
		TWITT_OPERATION_POST,
		TWITT_OPERATION_TIMELINE
	}
	
	private static  TwitterServices instance = null;
//HNy2Pb3c9sW3qlkd4OC3gHQh0
	//6j8dVV4sst7mGYwpfpF0LgVTe3IGEbpsSbD6ELcHvunHzE5eRz
	private String kOAuthConsumerKey =  "oTCwpUKMowI1cAq9e2w8zA";//  "oTCwpUKMowI1cAq9e2w8zA";
	private String kOAuthConsumerSecret = "uoOSDt1ic4NJTmqIy8XiFuN4XvJmehMq9waRH4jI7c";// "uoOSDt1ic4NJTmqIy8XiFuN4XvJmehMq9waRH4jI7c";
	private Configuration mTwittConfiguration = null;
	private Twitter mTwitter = null;
	private AccessToken mTwitterToken = null;
	private User	mTwitterUser = null;
	
	
	public static TwitterServices getTwitterService()
	{
		 if(null == instance) 
		 {
             synchronized(TwitterServices.class)
             { 
               instance = new TwitterServices();   
             }
          }
          return instance;
	}
	
	public void performTwitterLogin(String pUserName,String pPass,TwitterResponseData pResponseHandler)
	{
		if(pResponseHandler != null && pUserName != null && pPass != null)
		{
			TwitterAsyncThread	lTWThread = new TwitterAsyncThread(TWITTERL_OPERATION_TYPES.TWITT_OPERATION_LOGIN,pResponseHandler);
			lTWThread.execute(pUserName,pPass);
		}
	}
	
	public void performPostTwitt(String pPostStr,TwitterResponseData pResponseHandler)
	{
		if( pPostStr != null )
		{
			TwitterAsyncThread	lTWThread = new TwitterAsyncThread(TWITTERL_OPERATION_TYPES.TWITT_OPERATION_POST,pResponseHandler);
			lTWThread.execute(pPostStr);
		}
	}
	
	
	public void performTimeline(TwitterResponseData pResponseHandler)
	{
		if(pResponseHandler != null)
		{
			TwitterAsyncThread	lTWThread = new TwitterAsyncThread(TWITTERL_OPERATION_TYPES.TWITT_OPERATION_TIMELINE,pResponseHandler);
			lTWThread.execute();
		}
	}
	
	private void InitTwitter()
	{
		if(mTwittConfiguration == null)
		{
			ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

			configurationBuilder.setOAuthConsumerKey(kOAuthConsumerKey);
			configurationBuilder.setOAuthConsumerSecret(kOAuthConsumerSecret);
				
			mTwittConfiguration = configurationBuilder.build();
		}
		if(mTwitter == null && mTwittConfiguration != null)
		{
			mTwitter = new TwitterFactory(mTwittConfiguration).getInstance();
			//mTwitter = new TwitterFactory(mTwittConfiguration).getInstance(new BasicAuthorization("sdsd", "ssdsd"));
		}
	}
	
	private void DeInitTwitter()
	{
		mTwittConfiguration = null;
		mTwitter = null;
		mTwitterToken = null;
		mTwitterUser = null;
	}
	
	/////////////////Twitter Process thread
	
	private class TwitterAsyncThread extends AsyncTask<String, Integer, String>{
		
		private TWITTERL_OPERATION_TYPES		mOperationType = TWITTERL_OPERATION_TYPES.OPERATION_DEFAULT;
		String mErrorMessage = null;
		String	mResponse = null;
		TwitterResponseData mResponseHandler = null;
		List<twitter4j.Status> statuses = null;
		
		public TwitterAsyncThread(TWITTERL_OPERATION_TYPES pOperationType,TwitterResponseData pResponseHandler)
		{
			mOperationType = pOperationType;
			mErrorMessage = null;
			mResponse = null;
			mResponseHandler = pResponseHandler;
		}
		
		@Override
		protected String doInBackground(String... arg0) 
		{
			
			InitTwitter();
			//Login Process
			if(mOperationType == TWITTERL_OPERATION_TYPES.TWITT_OPERATION_LOGIN)
			{
				if(arg0.length == 2) //username, password
				{
					try{
						//mTwitter = new TwitterFactory(mTwittConfiguration).getInstance(new BasicAuthorization("user","pass"));
						//mTwitter.setOAuthConsumer(kOAuthConsumerKey,kOAuthConsumerSecret);
					
						mTwitterToken = mTwitter.getOAuthAccessToken(arg0[0], arg0[1]);
						
						mTwitterUser = mTwitter.verifyCredentials();
						if(mTwitterUser != null)
						{
		              	  JSONObject	lJSONObject = new JSONObject();
		              		try {
		              			lJSONObject.put("userid",mTwitterUser.getScreenName());
		              			lJSONObject.put("name",mTwitterUser.getName());
		              			lJSONObject.put("profileImage",mTwitterUser.getProfileImageURL().toString());
		              			lJSONObject.put("desc",mTwitterUser.getDescription());
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		              		mResponse = lJSONObject.toString();
						}
						
					
					}catch (Exception e) {
						// TODO: handle exception
						mErrorMessage = e.getMessage();
						e.printStackTrace();
					}
				}
			}
			else if(mOperationType == TWITTERL_OPERATION_TYPES.TWITT_OPERATION_LOGOUT)
			{
				DeInitTwitter(); // logout twitter
			}
			else if(mOperationType == TWITTERL_OPERATION_TYPES.TWITT_OPERATION_POST)
			{
				if(mTwitter != null && arg0.length == 1)
				{
					try
					{
						 twitter4j.Status lStatus = mTwitter.updateStatus(arg0[0]);
					}
					catch (Exception e) 
					{
						mErrorMessage = e.getMessage();
						e.printStackTrace();
					}
				}
			}
			else if(mOperationType == TWITTERL_OPERATION_TYPES.TWITT_OPERATION_TIMELINE)
			{
				
				try{
					
					statuses = mTwitter.getHomeTimeline();

				}catch(Exception e)
				{
					e.printStackTrace();
					mErrorMessage = e.getMessage();
				}
			}
			
			return null;
		}
		
		
		protected void onPostExecute(String result) {
			// Notify the caller using 
			if(mOperationType == TWITTERL_OPERATION_TYPES.TWITT_OPERATION_LOGIN)
			{
				if(mResponseHandler != null)
					mResponseHandler.responseData(mOperationType, mResponse, mErrorMessage, (mErrorMessage == null)?true:false);
			}
			else if(mOperationType == TWITTERL_OPERATION_TYPES.TWITT_OPERATION_POST)
			{
				if(mResponseHandler != null)
					mResponseHandler.responseData(mOperationType, null, mErrorMessage, (mErrorMessage == null)?true:false);
			}
			else if(mOperationType == TWITTERL_OPERATION_TYPES.TWITT_OPERATION_LOGOUT)
			{
				if(mResponseHandler != null)
					mResponseHandler.responseData(mOperationType, null, mErrorMessage, (mErrorMessage == null)?true:false);
			}
			else if(mOperationType == TWITTERL_OPERATION_TYPES.TWITT_OPERATION_TIMELINE)
			{
				if(mResponseHandler != null)
					mResponseHandler.responseTimeLineData(mOperationType, statuses, mErrorMessage, (mErrorMessage == null)?true:false);
			}
			
			mErrorMessage = null;
			
		}
	}
	
}
