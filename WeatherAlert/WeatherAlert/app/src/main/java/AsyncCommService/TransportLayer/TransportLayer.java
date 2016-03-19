/**
 *
 */
package AsyncCommService.TransportLayer;


import android.content.Context;
import android.net.ConnectivityManager;

import com.google.common.io.ByteStreams;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import AsyncCommService.RequestHandler.RequestHandlerBase;
import Common.Globals;

/**
 * Created by Island on 17/03/16.
 */
public class TransportLayer{
	private ConnectivityManager 				m_cObjConnectivityMgr;

	String TAG = "TransportLayer";
	int mResponseCode = 0;
	String mErrorMsg = null;
	/**
	 *
	 */
	public TransportLayer()
	{
		m_cObjConnectivityMgr = (ConnectivityManager) Globals.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

	}

	public void resetHTTTP() {
		mResponseCode = 0;
		mErrorMsg = null;
	}

	public String doConnection(String pUrl,String pData, String pReqType,RequestHeaders[] pHeaders){
		String lResponse = null;
		URL url;
		try {
			url = new URL(pUrl);
			HttpURLConnection  urlConnection =  (HttpURLConnection)url.openConnection();

			if(pHeaders != null && pHeaders.length > 0)
			{
				for(RequestHeaders lHeader : pHeaders)
				{
					urlConnection.setRequestProperty(lHeader.m_cHeader, lHeader.m_cValue);
				}
			}

			urlConnection.setUseCaches(false);
			if(pReqType != null && (pReqType.equalsIgnoreCase(TansportMacros.REQUEST_METHOD_POST) || pReqType.equalsIgnoreCase(TansportMacros.REQUEST_METHOD_PUT))) {

				urlConnection.setRequestMethod(pReqType);
			//	urlConnection.setConnectTimeout(5 * 1000); // 5seconds
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);
				if (pData != null)
					urlConnection.setRequestProperty("Content-Length", Integer.toString(pData.length()));
				else
					urlConnection.setRequestProperty("Content-Length", Integer.toString(0));

				if (pData != null) {
					DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());

					wr.writeBytes(pData.toString());
					wr.flush();
					wr.close();
				}
			}

			mResponseCode = urlConnection.getResponseCode();

			if(mResponseCode == 200 || mResponseCode == 201)
			{
				InputStream in = urlConnection.getInputStream();

				byte[] lbuff = ByteStreams.toByteArray(in);
				lResponse = new String(lbuff,"utf-8");
				in.close();
			}
			else
			{
				InputStream in = urlConnection.getErrorStream();
				byte[] lbuff = ByteStreams.toByteArray(in);
				lResponse = new String(lbuff,"utf-8");
				in.close();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lResponse;
	}

	public int getResponseCode()
	{
		return mResponseCode;
	}

	public String getErrorMsg()
	{
		return mErrorMsg;
	}


	public String  doAPICall(RequestHandlerBase pReqBase )
	{
		String pServerURLPtr = null;
		String pReqBody = null;
		RequestHeaders[] pReqHeaders = null;
		String pRequestType = null;

		String					lResponseValue = null;

		if(pReqBase != null)
		{
			pServerURLPtr = pReqBase.getActualURL();
			pReqBody = pReqBase.getReqBody();
			pReqHeaders = pReqBase.getReqHeaders();
			pRequestType = pReqBase.getRequestType();

			lResponseValue = doConnection(pServerURLPtr, pReqBody, pRequestType, pReqHeaders);
		}

		return lResponseValue;
	}
}

