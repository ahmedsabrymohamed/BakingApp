package com.example.mine.BakingApp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;


import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements ListAdapter.SetOncLickListener {

    private static final String INGREDIENTS_JSON = "Ingredients";
    private ListAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        RecyclerView recipeList = findViewById(R.id.recipe_list);
        recipeAdapter = new ListAdapter(this, (int) dpWidth);
        recipeList.setAdapter(recipeAdapter);
        recipeList.setHasFixedSize(true);


        if (dpWidth >= 600) {
            recipeList.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recipeList.setLayoutManager(new LinearLayoutManager(this));
        }


        ApiInterface apiService =
                APIClient.getClient().create(ApiInterface.class);

        Call<List<RecipeData>> call = apiService.getRecipesData();
        call.enqueue(new Callback<List<RecipeData>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeData>> call
                    , @NonNull Response<List<RecipeData>> response) {
                recipeAdapter.setRecipes(response.body());

                //setting the default widget Ingredients
                if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .getString(INGREDIENTS_JSON, null) == null) {
                    setDefaultWidget(getApplicationContext(), response.body().get(0).getIngredients(), new ComponentName(getApplicationContext(), IngredientsWidget.class));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipeData>> call, @NonNull Throwable t) {
                // Log error here since request failed
                Log.e(getLocalClassName(), t.toString());
            }
        });
    }

    @Override
    public void SetOnclick(Object obj) {

        startActivity((new Intent(this, RecipeInfoActivity.class))
                .putExtra("recipe", (RecipeData) obj));

    }

    private void setDefaultWidget(Context context, List<Ingredients> ingredientsList, ComponentName componentName) {
        Gson gson = new Gson();
        String ingredients = gson.toJson(ingredientsList);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(INGREDIENTS_JSON, ingredients).apply();

        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);

        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(componentName);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredient_list_widget);

        IngredientsWidget.updateAppWidget(context, appWidgetManager, appWidgetIds);
    }
}
