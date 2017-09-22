package com.solutionco.android.bakingapp;


import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.google.android.exoplayer2.util.Util;
import com.solutionco.android.bakingapp.Data.Step;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeVideoFragment extends Fragment {


    private SimpleExoPlayer simpleExoPlayer;
    private SimpleExoPlayerView playerView;
    Step step;
    TextView descriptionTextView;
    ImageView stepImageView;
    CardView stepImageHolder;
    public RecipeVideoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_video, container, false);

        descriptionTextView = (TextView)view.findViewById(R.id.full_description_textView);
        playerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);



        if(getActivity() instanceof RecipeDetailActivity){
            step = RecipeDetailActivity.choosenStep;
        }else{
            step = RecipeVideoActivity.choosenStep;
        }


        descriptionTextView.setText(step.getDescription());
        if(! step.getVideoURL().equals("") ){
            Uri uri = Uri.parse(step.getVideoURL());
            initializePlayer(uri);
        }else{
            Uri uri = null;
            initializePlayer(uri);
        }

        stepImageView = (ImageView)view.findViewById(R.id.step_image);
        stepImageHolder = (CardView)view.findViewById(R.id.step_image_holder);
        if(! step.getThumbnailURL().equals("")){
            Picasso.with(getContext())
                    .load(step.getThumbnailURL())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image)
                    .into(stepImageView);
        }else
        {
            stepImageHolder.setVisibility(View.GONE);
        }

        return view;
    }

    private void initializePlayer(Uri mediaUri) {
        if(mediaUri ==null ){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector, loadControl);
            playerView.setPlayer(simpleExoPlayer);

            playerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.not_available));
            return;
        }
        if (simpleExoPlayer == null ) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector, loadControl);
            playerView.setPlayer(simpleExoPlayer);

            String userAgent = Util.getUserAgent(this.getContext(), step.getShortDescription());
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void releasePlayer() {
        if(simpleExoPlayer != null){
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }


}
