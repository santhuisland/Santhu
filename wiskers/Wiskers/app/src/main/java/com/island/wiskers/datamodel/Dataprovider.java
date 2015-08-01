package com.island.wiskers.datamodel;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.common.io.ByteStreams;
import com.island.wiskers.AppGlobals;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

/**
 * Created by Island on 01/08/15.
 */


public class Dataprovider {

    private static Dataprovider  mInstance = null;
    private bw          mJsonBase = null;

    private Dataprovider()
    {}

    public static  Dataprovider getInstance()
    {
        if(mInstance == null)
            mInstance = new Dataprovider();

        return mInstance;
    }

    public bw getBaseJson()
    {
        if (mJsonBase == null) {
            //Read from the Assets
            JSONObject lJson = readDataFromAssets(AppGlobals.mAppContext,AppGlobals.TEST_JSON_FILENAME);
            //Parse the Json data to Data models
            mJsonBase = new bw(lJson);
        }

        return mJsonBase;
    }

    private JSONObject readDataFromAssets(Context pcontext, String pFileName) {
        JSONObject lJsonData = null;
        // read the entrys from json file
        String lFileData = "";

        try {
            AssetManager am = pcontext.getApplicationContext().getAssets();
            InputStream is = am.open(pFileName);

            byte[] lbuff = ByteStreams.toByteArray(is);
            lFileData = new String(lbuff);

            is.close();

        } catch (Exception e) {
        }

        if (lFileData != null) {
            try {
                lJsonData = (JSONObject) new JSONTokener(lFileData)
                        .nextValue();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return lJsonData;
    }

}
