package com.crowdmix.twitter;

import java.util.List;

import twitter4j.Status;

import TwitterService.TwitterResponseData;
import TwitterService.TwitterServices;
import TwitterService.TwitterServices.TWITTERL_OPERATION_TYPES;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	
	EditText			mUserName = null;
	EditText			mPass = null;
	Button				mLoginButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Globals.mAppContext = getApplicationContext();
        
        Globals.mTwitterService = TwitterServices.getTwitterService();
        
        
        mUserName = (EditText)findViewById(R.id.UserName);
        mPass = (EditText)findViewById(R.id.Password);

        mLoginButton = (Button)findViewById(R.id.Login);
        mLoginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Globals.isNetworkAvailable())
				{
					if(mUserName.getText().length() > 0 && mPass.getText().length() > 0)
					{
						Globals.mTwitterService.performTwitterLogin(mUserName.getText().toString().trim(), mPass.getText().toString().trim(), new TwitterResponse());
					}
					else
					{
						Toast toastMessage = Toast.makeText(MainActivity.this, "Please enter the login credentials", Toast.LENGTH_SHORT);
						toastMessage.show();
					}
				}
				else
				{
					Toast toastMessage = Toast.makeText(MainActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT);
					toastMessage.show();
				}
			}
		});
    }

    
    class TwitterResponse implements TwitterResponseData
    {

		@Override
		public boolean responseData(TWITTERL_OPERATION_TYPES pOperationType,
				String pResult, String pErrorMessage, boolean mISsuccess) {
			// TODO Auto-generated method stub
			if(mISsuccess && pResult != null)
			{
				Intent i = new Intent(MainActivity.this,HomeActivity.class);
				
				i.putExtra("UserDetails", pResult);
				MainActivity.this.startActivity(i);
				finish();
			}
			else if(pErrorMessage != null)
			{
				Toast toastMessage = Toast.makeText(MainActivity.this, pErrorMessage, Toast.LENGTH_LONG);
				toastMessage.show();
			}
			return false;
		}

		@Override
		public boolean responseTimeLineData(
				TWITTERL_OPERATION_TYPES pOperationType, List<Status> pResult,
				String pErrorMessage, boolean mISsuccess) {
			// TODO Auto-generated method stub
			return false;
		}
    	
    }
   
}
