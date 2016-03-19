package AppManager;

/**
 * Created by Island on 17/03/16.
 */
public class RequestStructures {

	public static int 		REQUEST_TYPE_WEATHER_REPORT										= 1;

	//API ERROR Codes
	public static int		API_WEATHER_REPORT_SUCCESS										= 100;
	public static int		API_WEATHER_REPORT_FAIL											= -100;

	public static int		ERROR_NO_APPID_FOUND											= -1;
	public static int		ERROR_PARSING_RESPONSE_FAIL										= -2;

	/*
		DataStructure to send city and code details to API
	 */
	public static class WeatherDataStruct
	{
		public String 		mCity = null;
		public String 		mCountryCode = null;

		public WeatherDataStruct(String pCity,String pCountryCode)
		{
			mCity = pCity;
			mCountryCode = pCountryCode;
		}
	}
}
