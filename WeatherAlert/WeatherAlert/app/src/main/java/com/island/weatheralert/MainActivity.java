package com.island.weatheralert;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import AppManager.AppMgr;
import AppManager.RequestStructures;
import AsyncCommService.ResponseHandler.ResponseNotifier;
import AsyncCommService.TransportLayer.TransportError;
import Common.Globals;
import Common.Jsonconsts;
import Common.PreferencesManager;
import Common.progHandler;
import datamodels.Weather;
import datamodels.baseJson;

public class MainActivity extends AppCompatActivity {

    AppMgr          mAppManager = null;
    ImageView       mDirectionImageView = null;
    ListView        mFavouriteList = null;
    TextView        mWindDetailsText = null;
    TextView        mWeatherDetailsText = null;

    FavouriteListAdapter	mFavouriteAdapter = null;
    JSONArray       mFavouriteArray = null;
    private LayoutInflater mInflater = null;

    progHandler     mProgressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);

        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //create Global instance and App Manager
        Globals.CreateGlobalsInstance(this.getApplicationContext());
        mAppManager = new AppMgr();

        /*
        Initialize the UI Controls
         */
        mDirectionImageView = (ImageView)findViewById(R.id.direction);
        mFavouriteList = (ListView)findViewById(R.id.FavourateListView);
        mWindDetailsText = (TextView)findViewById(R.id.WindDetailsText);
        mWeatherDetailsText = (TextView)findViewById(R.id.WeatherExtraDetailsText);
        //create progressBar
        mProgressBar = new progHandler(this);

        setupFavourateCreateButton();
        setupDefaultUIFavouriteCity();
        setupListAdaptor();

        //Get Weather report for Last Recorded Favourite City
        getWeatherDatafromServer(PreferencesManager.getCurrentFavCity(), PreferencesManager.getCurrentFavCode());

    }

    /*
    Setup ADD Favourite Button
     */
    private void setupFavourateCreateButton()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LaunchNewFavouriteView();

            }
        });
    }

    /*
    This function is used to create Input view to enter Favourite city and country code for Weather Report
     */
    private void LaunchNewFavouriteView()
    {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.inputprompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptsView);

        final EditText cityInput = (EditText) promptsView.findViewById(R.id.editTextCity);
        final EditText codeInput = (EditText) promptsView.findViewById(R.id.editTextCode);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and add to favourite list and
                                getWeatherDatafromServer(cityInput.getText().toString(), codeInput.getText().toString());
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

    /*
    This function is used to show the cardinal direction to rotate the bitmap based on the degree
     */
    private Drawable getRotateDrawable( final Bitmap b, final float angle)
    {
        final BitmapDrawable drawable = new BitmapDrawable(getResources(), b) {
            @Override
            public void draw(final Canvas canvas) {
                canvas.save();
                canvas.rotate(angle, (b.getWidth() / 2), (b.getHeight() / 2));
                super.draw(canvas);
                canvas.restore();
            }
        };
        return drawable;
    }

    /*
    This function is used to show the initial Favourite UI details
     */
    private void setupDefaultUIFavouriteCity()
    {
        //default Direction
        BitmapDrawable drawable = (BitmapDrawable) mDirectionImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Drawable rotatedDrawable = null;

        rotatedDrawable = getRotateDrawable(bitmap, 0.0f);
        if(rotatedDrawable != null)
            mDirectionImageView.setImageDrawable(rotatedDrawable);

        String  lLocation = PreferencesManager.getCurrentFavCity() ;
        if(lLocation != null && lLocation.length() > 0)
            mWindDetailsText.setText(lLocation + " - " + "Speed:0.0 , Degree:0");
        else
            mWindDetailsText.setText(getString(R.string.default_city_text) + " - " + "Speed:0.0 , Degree:0");

        mWeatherDetailsText.setText(getString(R.string.default_weather_text) );
    }

    /*
    This function is used to fetch new Weather report for selected Favouite Item from Favourite List
     */
    private void UpdateWeatherForSelectedCity(int pPosition)
    {
        if(mFavouriteArray != null && pPosition <= mFavouriteArray.length())
        {
            JSONObject lFevObj = mFavouriteArray.optJSONObject(pPosition);
            String lCity = lFevObj.optString(Jsonconsts.JSON_CITY);
            String lCountryCode = lFevObj.optString(Jsonconsts.JSON_COUNTRY_CODE);

            //Make request to Server
            getWeatherDatafromServer(lCity, lCountryCode);
        }
    }

    /*
    This function is used to make Server request and wait for response on callbacks
     */
    private void getWeatherDatafromServer(final String pCity, final String pCountryCode)
    {
        if(pCity != null && pCity.length() > 0) {

            mProgressBar.show();

            RequestStructures.WeatherDataStruct lStruct = new RequestStructures.WeatherDataStruct(pCity, pCountryCode);
            // Actual API call
            mAppManager.performWeatherRequestProcess(new ResponseNotifier() {
                @Override
                public void OnApiNotifyOnStart(int pStatusCode) {

                }

                @Override
                public void OnApiNotifyOnCompletion(Object pRequestData, String pResponseData, int pErrorCode, String pErrorMessage) {

                    if (pErrorCode == RequestStructures.API_WEATHER_REPORT_SUCCESS) {

                        RequestStructures.WeatherDataStruct lStruct = (RequestStructures.WeatherDataStruct) pRequestData;
                        //Save the current Selected City and Code
                        PreferencesManager.setCurrentFavCity(lStruct.mCity);
                        PreferencesManager.setCurrentFavCode(lStruct.mCountryCode);

                        //add to Favourite List
                        PreferencesManager.setWeatherFavourite(pCity, pCountryCode);

                        //Update Weather report in UI
                        updateWeatherReportOnUI(pResponseData);
                    }
                    else if(pErrorCode == RequestStructures.ERROR_NO_APPID_FOUND)
                    {
                        Toast.makeText(MainActivity.this, R.string.NO_APPID_Response_Text,Toast.LENGTH_LONG).show();
                    }
                    else if(pErrorCode == TransportError.ERROR_NO_INTERNETCONNECTION )
                    {
                        Toast.makeText(MainActivity.this, R.string.NO_Internet_Response_Text,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, R.string.Fail_Response_Text,Toast.LENGTH_LONG).show();
                    }

                    mProgressBar.hide();
                }
                @Override
                public void OnApiNotifyOnIntermediate(int pStatusCode) {

                }
            }, lStruct);
        }
        else
        {
            Toast.makeText(MainActivity.this, R.string.NO_Favourate_Text,Toast.LENGTH_LONG).show();
        }
    }

    /*
    This function is used to update the UI with Server Details received from Above API call
     */
    private void updateWeatherReportOnUI(String pData)
    {
        try {
                JSONObject lResponseJson = (JSONObject) new JSONTokener(pData).nextValue();
                BitmapDrawable drawable = (BitmapDrawable) mDirectionImageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Drawable rotatedDrawable = null;

                //Parse and Update DataStructure Models
                baseJson lbaseJsonModel = new baseJson(lResponseJson);

                String  lLocation = PreferencesManager.getCurrentFavCity() ;
                if(lbaseJsonModel != null && lbaseJsonModel.getWind() != null ) {
                    String lReportString = String.format("%s - Speed:%.1f , Degree:%.1f",lLocation,lbaseJsonModel.getWind().getSpeed(),lbaseJsonModel.getWind().getDeg());
                    mWindDetailsText.setText(lReportString);

                    ArrayList<Weather>  lWeatherArray =  lbaseJsonModel.getWeather();

                    mWeatherDetailsText.setText(String.format("%s - %s",getString(R.string.weather_text) ,lWeatherArray.get(0).getDescription()));

                    rotatedDrawable = getRotateDrawable(bitmap, (float)lbaseJsonModel.getWind().getDeg());
                }
                else {
                    mWindDetailsText.setText(lLocation + " - " + "Speed:0.0 , Degree:0");
                    rotatedDrawable = getRotateDrawable(bitmap, 0.0f);
                    mWeatherDetailsText.setText(getString(R.string.default_weather_text) );
                }
                if(rotatedDrawable != null)
                    mDirectionImageView.setImageDrawable(rotatedDrawable);

                //finally Update the Favourite List UI list
                updateFavouriteList();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /*
    This function is used to construct and setup the Favourite List with Event handling to delete and change the city
     */
    private void setupListAdaptor()
    {
        mFavouriteList.setDividerHeight(0);
        mFavouriteList.setEmptyView((View) findViewById(R.id.FavouriteEmptyLavout));

        mFavouriteAdapter = new FavouriteListAdapter();
        mFavouriteList.setAdapter(mFavouriteAdapter);

        mFavouriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Fetch the Weather report for selected Favourate City
                UpdateWeatherForSelectedCity(position);
            }
        });

        mFavouriteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.DELETE_Favourite_Header)
                        .setMessage(R.string.DELETE_Favourite_Conformation_Message)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    JSONObject lFavouriteDetails = null;
                                    if(mFavouriteArray != null)
                                    {
                                        lFavouriteDetails = (JSONObject) mFavouriteArray.get(position);
                                        PreferencesManager.removeFavouriteFromList(lFavouriteDetails.optString(Jsonconsts.JSON_CITY));
                                    }
                                }catch (Exception e) {
                                    // TODO: handle exception
                                }

                                //Trigger Adaptor to refresh the list
                                updateFavouriteList();
                            }

                        })
                        .setNegativeButton("NO", null)
                        .show();
                return true;
            }
        });

        updateFavouriteList();
    }

    private void updateFavouriteList()
    {
        //read Saved Favourites from Preference store
        JSONObject lFavObj = PreferencesManager.getFavourites();
        if(lFavObj != null)
        {
            mFavouriteArray = lFavObj.optJSONArray(Jsonconsts.JSON_FAVOURITES);
        }
        //render the Favourite List
        mFavouriteAdapter.notifyDataSetChanged();
        if(mFavouriteArray != null && mFavouriteArray.length() == 0)
        {
            //Remove current Favourite from Preference if no favourite not found in the list
            PreferencesManager.removeCurrentFavCity();
            PreferencesManager.removeCurrentFavCode();
            setupDefaultUIFavouriteCity();
        }
    }

    /*
    Favourate list Adapter to render  each row dynamically based on the Favourite items
     */
    public class FavouriteListAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(mFavouriteArray != null)
                return mFavouriteArray.length();
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
                convertView =  mInflater.inflate(R.layout.single_favourite_row, parent, false);

                TextView lTitle = (TextView)convertView.findViewById(R.id.CityText);
                ImageView lThumb = (ImageView)convertView.findViewById(R.id.ThumbIcon);

                vh = new ViewHolder(lTitle,lThumb);
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
            JSONObject lFavouriteDetails = null;
            if(mFavouriteArray != null)
            {
                lFavouriteDetails = (JSONObject) mFavouriteArray.get(pPosition);
                pViewHolder.mTitleCity.setText(lFavouriteDetails.optString(Jsonconsts.JSON_CITY));
            }
        }catch (Exception e) {
            // TODO: handle exception
        }
    }

    /*
    view holder which contains row cells UI elements to customise
     */
    class ViewHolder
    {
        TextView	mTitleCity = null;
        ImageView	mThumb = null;

        public ViewHolder(TextView pTitleCity,ImageView pThumb)
        {
            mTitleCity = pTitleCity;
            mThumb = pThumb;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //refresh the Weather report
        if (id == R.id.action_refresh) {

            getWeatherDatafromServer(PreferencesManager.getCurrentFavCity(), PreferencesManager.getCurrentFavCode());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
