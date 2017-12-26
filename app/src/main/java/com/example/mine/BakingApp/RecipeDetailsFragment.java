package com.example.mine.BakingApp;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.squareup.picasso.Picasso;


import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsFragment extends Fragment {

    private static final String TWO_PANE = "param1";
    private static final String RECIPE_STEPS = "recipeSteps";
    private long exoPosition;


    private RecipeSteps recipeSteps;
    private ImageView video;
    View root;

    private TextView longDescription;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer simpleExoPlayer;
    private boolean state;
    private boolean landScape;


    private OnFragmentInteractionListener mListener;

    public RecipeDetailsFragment() {

    }



    public static RecipeDetailsFragment newInstance(RecipeSteps param2) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putBoolean(TWO_PANE, true);
        args.putParcelable(RECIPE_STEPS, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exoPosition=0;
        state=true;

        if (savedInstanceState != null) {
            recipeSteps = savedInstanceState.getParcelable("recipeSteps");
            exoPosition=savedInstanceState.getLong("exo");
            state=savedInstanceState.getBoolean("exoState");
        } else if (getArguments() != null) {
            recipeSteps = getArguments().getParcelable(RECIPE_STEPS);

        }

        landScape = (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_recipe_details, container, false);



        video = root.findViewById(R.id.video);

        longDescription = root.findViewById(R.id.long_description);
        video.setContentDescription(recipeSteps.getShortDescription());
        longDescription.setText(recipeSteps.getDescription());
        if(!TextUtils.isEmpty(recipeSteps.getThumbnailURL()))
            Picasso.with(getActivity()).load(recipeSteps.getThumbnailURL()).into(video);
        FloatingActionButton next = root.findViewById(R.id.floatingActionButton_next);
        FloatingActionButton previous = root.findViewById(R.id.floatingActionButton_previous);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.getNextStep(recipeSteps.getId());
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.getPreviousStep(recipeSteps.getId());
            }
        });
        simpleExoPlayerView = root.findViewById(R.id.player);
        if (!TextUtils.isEmpty(recipeSteps.getVideoURL()) && landScape) {

            Dialog ved = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);

            simpleExoPlayerView = root.findViewById(R.id.landscape_player);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
            ved.addContentView(simpleExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ved.show();
            ved.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    simpleExoPlayerView=root.findViewById(R.id.player);
                    simpleExoPlayerView.setPlayer(simpleExoPlayer);
                }
            });
        }


        if (TextUtils.isEmpty(recipeSteps.getVideoURL())) {
            video.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setVisibility(View.GONE);
        } else
            initializePlayer(recipeSteps.getVideoURL(),state);


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
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(recipeSteps.getVideoURL()))
            initializePlayer(recipeSteps.getVideoURL(),state);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void setRecipeSteps(RecipeSteps recipeSteps) {

        this.recipeSteps = recipeSteps;
        simpleExoPlayerView.setVisibility(View.GONE);
        video.setContentDescription(recipeSteps.getShortDescription());
        if(!TextUtils.isEmpty(recipeSteps.getThumbnailURL()))
            Picasso.with(getActivity()).load(recipeSteps.getThumbnailURL()).into(video);
        longDescription.setText(recipeSteps.getDescription());
        if (!TextUtils.isEmpty(recipeSteps.getVideoURL())) {
            releasePlayer();
            initializePlayer(recipeSteps.getVideoURL(),state);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            video.setVisibility(View.GONE);
        } else {
            releasePlayer();
            simpleExoPlayerView.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);
        }


    }

    private void initializePlayer(String stepVideoURL,boolean state) {
        if (simpleExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory
                    .newSimpleInstance(getActivity(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            Uri uri = Uri.parse(stepVideoURL);
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    getActivity(), recipeSteps.getShortDescription()),
                    new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.seekTo(exoPosition);
            simpleExoPlayer.setPlayWhenReady(state);

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipeSteps", recipeSteps);
        if(!recipeSteps.getVideoURL().isEmpty())
        {
            outState.putLong("exo",simpleExoPlayer.getCurrentPosition());
            outState.putBoolean("exoState",(simpleExoPlayer.getPlayWhenReady()));
        }
    }

    public interface OnFragmentInteractionListener {
        void getPreviousStep(int position);

        void getNextStep(int position);
    }



}
