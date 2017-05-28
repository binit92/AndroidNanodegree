package com.mynanodegreeapps.bakingapp.widget;

import android.app.IntentService;
import android.content.Intent;

public class BakingAppService extends IntentService {

    private static final String ACTION_OPEN_BAKINGAPP = "com.mynanodegreeapps.bakingapp.widget.action.openbakingapp";

    public BakingAppService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
