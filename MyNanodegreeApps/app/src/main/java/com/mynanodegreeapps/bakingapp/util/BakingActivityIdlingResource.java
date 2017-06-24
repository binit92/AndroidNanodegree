package com.mynanodegreeapps.bakingapp.util;

import android.support.test.espresso.IdlingResource;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.mynanodegreeapps.bakingapp.BakingActivity;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created by inbkumar01 on 6/24/2017.
 */

public class BakingActivityIdlingResource implements IdlingResource {

    private static final String LOG_TAG = BakingActivityIdlingResource.class.getSimpleName();

    private BakingActivity bakingActivity;
    private ResourceCallback callback;

    private Field mCurrentRequests;
    private RequestQueue mVolleyRequestQueue;

    public BakingActivityIdlingResource(BakingActivity bakingActivity){
        this.bakingActivity = bakingActivity;
        mVolleyRequestQueue = bakingActivity.getRequestQueue();
    }

    @Override
    public String getName() {
        return "BakingActivityIdleName";
    }

    @Override
    public boolean isIdleNow() {
        try {
            Set<Request> set = (Set<Request>) mCurrentRequests.get(mVolleyRequestQueue);
            int count = set.size();
            if (set != null) {

                if (count == 0) {
                    Log.d(LOG_TAG, "Volley is idle now! with: " + count);
                    callback.onTransitionToIdle();
                } else {
                    Log.d(LOG_TAG, "Not idle... " +count);
                }
                return count == 0;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "Eita porra.. ");
        return true;
    }


    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }
}
