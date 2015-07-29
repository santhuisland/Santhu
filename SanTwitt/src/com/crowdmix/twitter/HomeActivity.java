package com.crowdmix.twitter;


import java.util.Date;
import java.util.List;

import twitter4j.Status;
import twitter4j.User;


import com.san.twitter.R;

import TwitterService.LoadImageAsyncTask;
import TwitterService.TwitterResponseData;
import TwitterService.TwitterServices.TWITTERL_OPERATION_TYPES;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	 private ListView mList = null;
	 private TwittListAdapter mTwittListAdapter = null;
	 private LayoutInflater mInflater = null;
	 
	 private ProgressBar	mProgressBar = null;
	 
	 TextView	mLogout = null;
	 TextView	mRefresh = null;
	 TextView   mPost = null;
	 
	 int 	TWITT_LIMIT = 20; // Twitts limit
	 
	 BitmapCache		mBitmapCache = null;
	 List<Status> mTimeLineResult = null;
	 
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.time_line);
	        
	        prepareUI();
	        
	        refreshTimeline();
	        	       
	        prepareUIClicks();
	       
	 }
	 
	 private void prepareUI()
	 {
		 mList = (ListView)findViewById(R.id.list);
        mLogout = (TextView)findViewById(R.id.Top_bar_Logout_Button_Text);
        mRefresh = (TextView)findViewById(R.id.Top_bar_rigth_refresh_Text);
        mPost = (TextView)findViewById(R.id.Top_bar_rigth_post_Text);
        
        mProgressBar = (ProgressBar)findViewById(R.id.progressbar);
        
        mInflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mTwittListAdapter = new TwittListAdapter();
			
        mBitmapCache = new BitmapCache(this);
					
        mList.setDividerHeight(4);
        mList.setSelection(0);
        mList.setEmptyView((View) findViewById(R.id.content_empty));
			
        mList.setAdapter(mTwittListAdapter);
	 }
	 
	 private void prepareUIClicks()
	 {
		 mPost.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LayoutInflater li = LayoutInflater.from(HomeActivity.this);
					View promptsView = li.inflate(R.layout.inputview, null);
	 
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
	 
					// set prompts.xml to alertdialog builder
					alertDialogBuilder.setView(promptsView);
	 
					final EditText userInput = (EditText) promptsView.findViewById(R.id.tweetinput);
	 
					// set dialog message
					alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Post",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
						    	
						    	if(userInput.getText().length() > 0)
						    	{
						    		Globals.mTwitterService.performPostTwitt(userInput.getText().toString().trim(), new TwitterCallback());
						    	}
						    }
						  })
						.setNegativeButton("Cancel",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						    }
						  });
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
				}
			});
	        
	        mLogout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Intent i = new Intent(HomeActivity.this,MainActivity.class);
					
					HomeActivity.this.startActivity(i);
					finish();
					
				}
			});
	        
	        mRefresh.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mProgressBar.setVisibility(View.VISIBLE);
					refreshTimeline();
				}
			});
	 }
	 
	 private void refreshTimeline()
	 {
		 Globals.mTwitterService.performTimeline( new TwitterCallback());
	 }
	 
	 /*
	  * Twitter call Back class
	  */
	 class TwitterCallback implements TwitterResponseData
	 {

		@Override
		public boolean responseData(TWITTERL_OPERATION_TYPES pOperationType,
				String pResult, String pErrorMessage, boolean mISsuccess) {
			// TODO Auto-generated method stub
			if(pOperationType == TWITTERL_OPERATION_TYPES.TWITT_OPERATION_POST && mISsuccess)
			{
				refreshTimeline();
			}
			return false;
		}

		@Override
		public boolean responseTimeLineData(
				TWITTERL_OPERATION_TYPES pOperationType, List<Status> pResult,
				String pErrorMessage, boolean mISsuccess) {
			// TODO Auto-generated method stub
			mProgressBar.setVisibility(View.GONE);
			
			if(pOperationType == TWITTERL_OPERATION_TYPES.TWITT_OPERATION_TIMELINE && mISsuccess)
			{
				if(pResult != null)
				{
					mTimeLineResult = pResult;
					mTwittListAdapter.notifyDataSetChanged();
					
				}
			}
			else if(pErrorMessage != null)
			{
				Toast toastMessage = Toast.makeText(HomeActivity.this, pErrorMessage, Toast.LENGTH_SHORT);
				toastMessage.show();
			}
			return false;
		}
		 
	 }
	 
	
	 
	 @Override
	public void onResume()
	{
		super.onResume();
	}
	 
	 /*
	  * List Adapter class
	  */
	 public class TwittListAdapter extends BaseAdapter 
		{

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if(mTimeLineResult != null)
				{
					return (mTimeLineResult.size() >= TWITT_LIMIT) ? TWITT_LIMIT : mTimeLineResult.size();
				}
				else
					return 0;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				
				ViewHolder   vh = null;
				
				if(convertView == null)
				{
					convertView =  mInflater.inflate(R.layout.single_timeline_item, parent, false);
					
					
					TextView lName = (TextView)convertView.findViewById(R.id.Name);
					TextView lAge = (TextView)convertView.findViewById(R.id.age);
					TextView lContent = (TextView)convertView.findViewById(R.id.tweet);
					ImageView lThumb = (ImageView)convertView.findViewById(R.id.ThumbIcon);
					
					vh = new ViewHolder(lName,lThumb,lAge,lContent);
					convertView.setTag(vh);
				}
				else
					vh = (ViewHolder)convertView.getTag();
				
				setData(vh,position);
				
				return convertView;
			}
		}
		
		private void setData(ViewHolder pViewHolder,int pPosition)
		{
			try{
				if(mTimeLineResult != null && pPosition < mTimeLineResult.size())
				{
					twitter4j.Status status = mTimeLineResult.get(pPosition);
					if(status != null)
					{
						//Created Date
						Date lDate = status.getCreatedAt();
						pViewHolder.mage.setText(lDate.toString());
						
						User lUser =  status.getUser();
						if(lUser != null)
						{
							//Name
							pViewHolder.mName.setText(lUser.getName());
	
							//Twitt
							pViewHolder.mContent.setText(status.getText());
							
							// Try to download from Server
							//User Icon
				        	if(mBitmapCache == null)
				        		mBitmapCache = new BitmapCache(this); 
				        	if(lUser.getProfileImageURL() != null)
				        	{
				        		new LoadImageAsyncTask(mBitmapCache, pViewHolder.mThumb, lUser.getProfileImageURL(),null).execute();
				        	}

						}
						
					}
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		
		/*
		 * Data struct to Hold View controls
		 */
		class ViewHolder
		{
			TextView	mName = null;
			ImageView	mThumb = null;
			TextView	mage = null;
			TextView	mContent =null;
			
			public ViewHolder(TextView pTitle,ImageView pThumb,TextView pAge,TextView pContent)
			{
				mName = pTitle;
				mThumb = pThumb;
				mage = pAge;
				mContent = pContent;
			}
		}
		
		

		///THumbnail loading from URL
		
		public static class BitmapCache extends LruCache<String, Bitmap> { 

		    public BitmapCache(int sizeInBytes) {
		        super(sizeInBytes);
		    }   

		    public BitmapCache(Context context) {
		        super(getOptimalCacheSizeInBytes(context));
		    }   

		    public static int getOptimalCacheSizeInBytes(Context context) {
		        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		        int memoryClassBytes = am.getMemoryClass() * 1024 * 1024;

		        return memoryClassBytes / 8;
		    }

		    @Override
		    protected int sizeOf(String key, Bitmap value) {
		        return value.getRowBytes() * value.getHeight();
		    }
		}
}
