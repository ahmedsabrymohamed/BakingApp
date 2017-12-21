package com.example.mine.BakingApp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeInfoActivity extends AppCompatActivity
        implements MasterPageFragment.OnFragmentInteractionListener
        , RecipeDetailsFragment.OnFragmentInteractionListener {

    private static final String MASTER_PAGE_KEY = "MasterPageFragment";
    private static final String RECIPE_DETAILS_KEY = "RecipeDetailsFragment";
    private RecipeDetailsFragment recipeDetailsFragment;
    private boolean twoPane = false;
    private MasterPageFragment masterPageFragment;
    private RecipeData recipeData;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_info);
        twoPane = (findViewById(R.id.detail_fragment) != null);
        fragmentManager = getSupportFragmentManager();
        recipeData = getIntent().getParcelableExtra("recipe");
        if (savedInstanceState != null) {
            masterPageFragment = (MasterPageFragment) fragmentManager
                    .getFragment(savedInstanceState, MASTER_PAGE_KEY);
            recipeDetailsFragment = (RecipeDetailsFragment) fragmentManager
                    .getFragment(savedInstanceState, RECIPE_DETAILS_KEY);

        } else {
            masterPageFragment = MasterPageFragment.newInstance(twoPane, recipeData);
            masterPageFragment.onAttach(this);
            fragmentManager.beginTransaction()
                    .replace(R.id.master_fragment, masterPageFragment).commit();

            if (twoPane) {
                recipeDetailsFragment = RecipeDetailsFragment.newInstance(recipeData.getSteps().get(0));
                recipeDetailsFragment.onAttach(this);
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_fragment, recipeDetailsFragment).commit();

            }
        }
    }

    @Override
    public void onMasterFragmentInteraction(Object obj) {

        if (!twoPane) {

            masterPageFragment = null;

            recipeDetailsFragment = RecipeDetailsFragment.newInstance((RecipeSteps) obj);
            recipeDetailsFragment.onAttach(this);
            fragmentManager.beginTransaction()
                    .replace(R.id.master_fragment, recipeDetailsFragment).addToBackStack(RECIPE_DETAILS_KEY).commit();

        } else {

            recipeDetailsFragment.setRecipeSteps((RecipeSteps) obj);
        }
    }

    @Override
    public void getNextStep(int position) {

        position++;

        if (recipeData.getSteps().size() > position) {
            recipeDetailsFragment.setRecipeSteps(recipeData.getSteps().get(position));
        }

    }

    @Override
    public void getPreviousStep(int position) {

        position--;
        // int r=R.id.master_fragment;
        if (-1 < position) {
            recipeDetailsFragment.setRecipeSteps(recipeData.getSteps().get(position));
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (masterPageFragment != null)
            fragmentManager.putFragment(outState, MASTER_PAGE_KEY, masterPageFragment);

        if (recipeDetailsFragment != null)
            fragmentManager.putFragment(outState, RECIPE_DETAILS_KEY, recipeDetailsFragment);


        super.onSaveInstanceState(outState);

    }


}
