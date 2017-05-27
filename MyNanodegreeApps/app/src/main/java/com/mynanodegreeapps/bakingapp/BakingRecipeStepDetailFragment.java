package com.mynanodegreeapps.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mynanodegreeapps.R;
import com.mynanodegreeapps.bakingapp.recipe.Ingredient;
import com.mynanodegreeapps.bakingapp.recipe.Step;

import java.util.List;

import butterknife.Bind;

/*  This Fragment will the show the details of Recipe Step i.e. details of "ingredients"
    and details of each recipe step that includes picture/video, recipe instruction and navigation etc"
 */

public class BakingRecipeStepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    View rootView;
    LinearLayout parentLayout;
    List<Ingredient> ingredients ;
    Step step;

    SimpleExoPlayerView simpleExoPlayerView;

    private static MediaSessionCompat   mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    SimpleExoPlayer simpleExoPlayer;

    private static final String LOG_TAG = BakingRecipeStepDetailFragment.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bakingrecipestepdetail_fragment,container,false);
        Bundle b = getArguments();

        if(rootView != null){
            ingredients = b.getParcelableArrayList("ingredients");
            step = b.getParcelable("step");
            parentLayout = (LinearLayout) rootView.findViewById(R.id.recipestepsdetailscontainer);

            if (ingredients != null) {
                createIngredientLayout(ingredients, parentLayout);
            }else{
                createStepLayout(step,parentLayout);
            }

        }
        return rootView;
    }

    public void createIngredientLayout(List<Ingredient> ingredients , LinearLayout parentLayout){

        for(Ingredient ingredient : ingredients){
            LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.LEFT);
            linearLayout.setPadding(5,5,5,5);

            TextView quantityTextView = new TextView(getActivity().getApplicationContext());
            TextView measureTextView = new TextView(getActivity().getApplicationContext());
            TextView ingredientTextView = new TextView(getActivity().getApplicationContext());

            quantityTextView.setText(ingredient.getQuantity());
            quantityTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            quantityTextView.setPadding(2,2,2,2);
            measureTextView.setText(ingredient.getMeasure());
            measureTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            measureTextView.setPadding(2,2,2,2);
            ingredientTextView.setText(ingredient.getIngredient());
            ingredientTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            ingredientTextView.setPadding(2,2,2,2);

            linearLayout.addView(quantityTextView);
            linearLayout.addView(measureTextView);
            linearLayout.addView(ingredientTextView);

            parentLayout.addView(linearLayout);
        }

    }

    public void createStepLayout(Step step, LinearLayout parentLayout){
        TextView stepDescription = new TextView(getActivity().getApplicationContext());
        simpleExoPlayerView = (SimpleExoPlayerView) parentLayout.findViewById(R.id.video_view);

        if(step != null){
            simpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(
                    getResources(), R.drawable.baking_junkfood
            ));

            initializeMediaSession();
            initializePlayer(Uri.parse(step.getVideoUrl()));
            stepDescription.setText("==> Sample Text To Remove");

            // show image if imageView !
        }

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        System.out.println("--> onTimelineChanged ! ");
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        System.out.println("--> onTracksChanged ! ");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        System.out.println("--> onLoadingChanged ! ");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        // Everytime user presses a button, the media session will get updated
        System.out.println("--> begin onPlayerStateChanged ! ");
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
        System.out.println("--> onPlayerStateChanged ! ");
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        System.out.println("--> onPlayerError ! ");
    }

    @Override
    public void onPositionDiscontinuity() {
        System.out.println("--> onPositionDiscontinutiy ! ");
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        System.out.println("--> onPlaybackParametersChanged ! ");
    }

    // Todo : make change in this two initialization methods !
    private void initializeMediaSession() {

        System.out.println("--> Initialize Media Session ");
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), LOG_TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
        System.out.println("--> Media Session is Active ! ");
    }
    private void initializePlayer(Uri videoURL) {
        System.out.println("--> videoURL is " + videoURL.toString());
        if (simpleExoPlayer == null) {
            System.out.println("--> Initialize Player ! ");
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext())
                                ,trackSelector,loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);

            simpleExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(videoURL, new DefaultDataSourceFactory(getContext(), userAgent)
                    , new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
           // simpleExoPlayer.setPlayWhenReady(true);
            System.out.println("--> Start When Ready ! ");
        }
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
        System.out.println("--> release Player ! ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
        System.out.println("--> onDestroy View ! ");
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            simpleExoPlayer.setPlayWhenReady(true);
        }


        @Override
        public void onPause() {
            simpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            simpleExoPlayer.seekTo(0);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

}
