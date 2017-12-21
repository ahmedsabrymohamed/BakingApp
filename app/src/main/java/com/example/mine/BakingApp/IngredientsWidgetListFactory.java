package com.example.mine.BakingApp;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class IngredientsWidgetListFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String INGREDIENTS_JSON = "Ingredients";
    private final Context mContext;
    private final List<Ingredients> listItemList;

    IngredientsWidgetListFactory(Context mContext) {

        this.mContext = mContext;
        Gson gson = new Gson();

        String jsonIngredients = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(INGREDIENTS_JSON, null);

        Type listType = new TypeToken<List<Ingredients>>() {
        }.getType();

        List<Ingredients> ingredients = gson.fromJson(jsonIngredients, listType);

        if (jsonIngredients == null)
            listItemList = new ArrayList<>();
        else {
            listItemList = ingredients;
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (listItemList == null)
            return 0;
        return listItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.list_view_item);
        if (listItemList != null) {
            Ingredients ingredients = listItemList.get(i);
            remoteView.setTextViewText(R.id.ingredient_name_text_view, ingredients.getIngredient());
            remoteView.setTextViewText(R.id.ingredient_quantity_text_view, Double.toString(ingredients.getQuantity()));
            remoteView.setTextViewText(R.id.ingredient_measure_text_view, ingredients.getMeasure());
            remoteView.setTextViewText(R.id.ingredient_num_text_view, mContext.getResources().getString(R.string.Ingredient_number) + Integer.toString(i + 1));
        }
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
