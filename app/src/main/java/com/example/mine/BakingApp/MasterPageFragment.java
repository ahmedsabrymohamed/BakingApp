package com.example.mine.BakingApp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MasterPageFragment extends Fragment implements ListAdapter.SetOncLickListener {

    private static final String TWO_PANE = "param1";
    private static final String RECIPE_DATA = "param2";


    private RecipeData recipeData;


    private OnFragmentInteractionListener mListener;

    public MasterPageFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MasterPageFragment.
     */
    public static MasterPageFragment newInstance(boolean param1, RecipeData param2) {
        MasterPageFragment fragment = new MasterPageFragment();
        Bundle args = new Bundle();
        args.putBoolean(TWO_PANE, param1);
        args.putParcelable(RECIPE_DATA, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeData = getArguments().getParcelable(RECIPE_DATA);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_page, container, false);
        RecyclerView recipeInfo = root.findViewById(R.id.ingredient_list);
        ListAdapter recipeInfoAdapter = new ListAdapter(getActivity(), this);
        recipeInfoAdapter.setRecipeSteps(recipeData.getSteps());
        recipeInfoAdapter.setIngredients(recipeData.getIngredients());
        recipeInfo.setAdapter(recipeInfoAdapter);
        recipeInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        recipeInfo.setHasFixedSize(true);


        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void SetOnclick(Object obj) {
        mListener.onMasterFragmentInteraction(obj);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onMasterFragmentInteraction(Object obj);
    }


}
