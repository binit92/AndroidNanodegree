package com.mynanodegreeapps.bakingapp.widget;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.widget.RemoteViewsService;
import android.widget.Toast;

public class BakingAppRemoteViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
           return new BakingAppRemoteViewFactory(this.getApplicationContext());
    }
}
