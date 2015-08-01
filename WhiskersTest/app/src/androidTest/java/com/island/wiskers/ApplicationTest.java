package com.island.wiskers;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.island.wiskers.datamodel.Dataprovider;
import com.island.wiskers.datamodel.bw;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityUnitTestCase<MainActivity> {
    public ApplicationTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        // Starts the MainActivity of the target application
        startActivity(new Intent(getInstrumentation().getTargetContext(), MainActivity.class), null, null);

        // Getting a reference to the MainActivity of the target application
        AppGlobals.mAppContext = getActivity();

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDataModel()
    {
        //fetch data from Data file or server and parse to datamodel
        bw mDatamodel  = Dataprovider.getInstance().getBaseJson();

        assertNotNull("The Json data is null", mDatamodel);
    }
}