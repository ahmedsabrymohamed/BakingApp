package com.example.mine.BakingApp;

import android.content.Intent;
import android.widget.RemoteViewsService;


public class IngredientsWidgetListService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsWidgetListFactory(this.getApplicationContext());
    }
}
