package com.example.mine.BakingApp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;


class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final String itemType;
    private final SetOncLickListener mListener;
    private final Context mContext;
    private int Width;
    private List<RecipeData> recipes;
    private List<Ingredients> ingredients;
    private int ingSize;
    private List<RecipeSteps> recipeSteps;

    ListAdapter(Context Listener, int dpWidth) {

        this.itemType = "recipes";
        mContext = Listener;
        mListener = (SetOncLickListener) Listener;
        this.Width = dpWidth;

    }

    ListAdapter(Context context, SetOncLickListener Listener) {

        this.itemType = "recipeInfo";
        mContext = context;
        mListener = Listener;


    }

    void setRecipes(List<RecipeData> recipes) {
        this.recipes = recipes;
        this.notifyDataSetChanged();
    }

    void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
        ingSize = ingredients.size();
        this.notifyDataSetChanged();
    }

    void setRecipeSteps(List<RecipeSteps> recipeSteps) {
        this.recipeSteps = recipeSteps;
        this.notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root;
        switch (itemType) {
            case "recipeInfo":
                if (viewType == 1) {
                    root = inflater.inflate(R.layout.step_item, parent, false);

                    return new RecipeStepsListViewHolder(root);
                } else {
                    root = inflater.inflate(R.layout.ingredient_item, parent, false);

                    return new IngredientsListViewHolder(root);
                }

            default:
                root = inflater.inflate(R.layout.recipe_item, parent, false);

                return new RecipesListViewHolder(root);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (itemType) {
            case "recipeInfo":
                if (holder.getItemViewType() == 1) {
                    RecipeStepsListViewHolder recipeStepsViewHolder = (RecipeStepsListViewHolder) holder;
                    recipeStepsViewHolder.description.setText(recipeSteps.get(position - ingSize).getShortDescription());
                    recipeStepsViewHolder.stepNum.setText(mContext.getResources().getString(R.string.Step_number) + Integer.toString(position + 1 - ingSize));
                    break;
                } else {
                    IngredientsListViewHolder ingredientsViewHolder = (IngredientsListViewHolder) holder;
                    ingredientsViewHolder.ingredientNum.setText(mContext.getResources().getString(R.string.Ingredient_number) + Integer.toString(position + 1));
                    ingredientsViewHolder.measure.setText(ingredients.get(position).getMeasure());
                    ingredientsViewHolder.ingredientName.setText(ingredients.get(position).getIngredient());
                    ingredientsViewHolder.quantity.setText(ingredients.get(position).getQuantity().toString());
                    break;
                }
            default:
                RecipesListViewHolder recipesViewHolder = (RecipesListViewHolder) holder;
                recipesViewHolder.recipesName.setText(recipes.get(position).getName());

        }

    }

    @Override
    public int getItemCount() {
        switch (itemType) {
            case "recipeInfo":
                return (recipeSteps != null ? recipeSteps.size() : 0)
                        + (ingredients != null ? ingredients.size() : 0);
            default:
                return (recipes != null ? recipes.size() : 0);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (ingredients != null && position < ingredients.size())
            return 0;
        return 1;
    }

    public interface SetOncLickListener {
        void SetOnclick(Object obj);
    }

    class RecipesListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView recipesName;
        final CardView recipeCard;

        RecipesListViewHolder(View itemView) {

            super(itemView);
            recipesName = itemView.findViewById(R.id.recipe_name);
            recipeCard = itemView.findViewById(R.id.recipe_item_card_view);
            if (Width < 600)
                recipeCard.setMinimumWidth(Width);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            mListener.SetOnclick(recipes.get(getAdapterPosition()));
        }
    }

    class IngredientsListViewHolder extends RecyclerView.ViewHolder {

        final TextView ingredientName;
        final TextView measure;
        final TextView quantity;
        final TextView ingredientNum;


        IngredientsListViewHolder(View itemView) {

            super(itemView);
            ingredientNum = itemView.findViewById(R.id.ingredient_num_text_view);
            ingredientName = itemView.findViewById(R.id.ingredient_name_text_view);
            measure = itemView.findViewById(R.id.ingredient_measure_text_view);
            quantity = itemView.findViewById(R.id.ingredient_quantity_text_view);


        }


    }

    class RecipeStepsListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView description;
        final TextView stepNum;

        RecipeStepsListViewHolder(View itemView) {

            super(itemView);
            description = itemView.findViewById(R.id.short_description);
            stepNum = itemView.findViewById(R.id.stepNum);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            mListener.SetOnclick(recipeSteps.get(getAdapterPosition() - ingSize));
        }
    }
}
