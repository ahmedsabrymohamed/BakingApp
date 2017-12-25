package com.example.mine.BakingApp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


interface ApiInterface {


    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<RecipeData>> getRecipesData();
}
