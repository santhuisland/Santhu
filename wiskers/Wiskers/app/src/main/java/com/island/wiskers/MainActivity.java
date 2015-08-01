package com.island.wiskers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.island.wiskers.datamodel.Cats;
import com.island.wiskers.datamodel.Dataprovider;
import com.island.wiskers.datamodel.Food;
import com.island.wiskers.datamodel.bw;


public class MainActivity extends Activity {

    SwitchableViewPager mPhotoPager = null;
    PhotoAdapter mPhotoAdapter = null;
    LayoutInflater mInflater = null;
    RelativeLayout  mBottomLayout = null;
    int             mScreenWidth = 0;
    int             mScreenHeight = 0;
    bw              mDatamodel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppGlobals.mAppContext = getApplicationContext();


        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //fetch data from Data file or server and parse to datamodel
        mDatamodel = Dataprovider.getInstance().getBaseJson();

        //prepare for page adapter
        setupPageAdapter();

        //render data
        populateData(mDatamodel);

        Toast.makeText(this, "Swipe me to find more Cats!", Toast.LENGTH_LONG).show();
    }


    private void setupPageAdapter()
    {
        mPhotoPager = (SwitchableViewPager) findViewById(R.id.photoPage);
        mBottomLayout = (RelativeLayout)findViewById(R.id.bottomlayout);

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;

        mPhotoAdapter = new PhotoAdapter(this, mInflater,mScreenWidth,(mScreenHeight-mBottomLayout.getLayoutParams().height));
        mPhotoPager.setAdapter(mPhotoAdapter);

        mPhotoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                updatePageinfo(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void updatePageinfo(int pindex)
    {
        if(mDatamodel != null && pindex >= 0)
        {
            Cats lCat =  mDatamodel.getCats().get(pindex);


            TextView  lFoodView = (TextView)findViewById(R.id.preferedfood);
            TextView  lSizeView = (TextView)findViewById(R.id.size);
            TextView  lWiskerView = (TextView)findViewById(R.id.whiskers);
            TextView  lPackageView = (TextView)findViewById(R.id.foodpackage);

            lFoodView.setText(lCat.getPreferedFood());
            lSizeView.setText(lCat.getSize());
            lWiskerView.setText(""+lCat.getWhiskers());
            String lfoodName = "";
            for(int i=0;i<mDatamodel.getFood().size();i++) {
                Food lFood = mDatamodel.getFood().get(i);
                if (lFood.getName().equalsIgnoreCase(lCat.getPreferedFood())) {
                    lfoodName = lFood.getPackageProperty(); break;
                }
            }
            lPackageView.setText(lfoodName);
        }
    }

    private void populateData(bw pDatamodel)
    {
        if(mPhotoAdapter != null && pDatamodel != null)
            mPhotoAdapter.loadData(pDatamodel);
        updatePageinfo(0);//default first page
    }



}
