package com.example.gopal.f_bakingapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gopal.f_bakingapp.R;
import com.example.gopal.f_bakingapp.Step;
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

import java.io.Serializable;
import java.util.List;

public class InstructionFragment extends Fragment {
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private List<Step> instructions;
    private int position;
    private Button mNextStepButton;
    private Button mPrevStepButton;
    private TextView mInfo;
    private int mTotalStep;
    boolean wasPlaying = false;
    boolean mTwoPane = false;

    // default constructor
    public InstructionFragment() {}

    // parametrized constructor
    public static InstructionFragment newInstance(List<Step> steps, int position) {
        InstructionFragment fragment = new InstructionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list",(Serializable) steps);
        bundle.putInt("pos", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            instructions = (List<Step>)getArguments().getSerializable("list");
            position = getArguments().getInt("pos");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_instruction, container, false);

        mInfo = rootView.findViewById(R.id.step_info);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.video_player);

        // fragment for two pane
        if(rootView.findViewById(R.id.two_pane_instruction_layout)!=null){
            mTwoPane = true;
            readBundle(getArguments());
            // play video & show instruction
            showCurrentStep(position);
        }// fragment for single pane
        else {
            mTwoPane = false;
            // Getting list of custom object(i.e. List<Step> ) using Serializable & position that is clicked
            instructions = (List<Step>) getActivity().getIntent().getSerializableExtra("list");
            position = getActivity().getIntent().getIntExtra("position", 1);

            mNextStepButton = rootView.findViewById(R.id.next_step_btn);
            mPrevStepButton = rootView.findViewById(R.id.previous_step_btn);

            mTotalStep = instructions.size() - 1;
            showCurrentStep(position);

            // when next button is clicked
            mNextStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position < mTotalStep) {
                        position = position + 1;
                    } else {
                        position = 0;
                    }
                    showCurrentStep(position);
                }
            });

            // when previous button is clicked
            mPrevStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position > 0) position = position - 1;
                    else position = mTotalStep;
                    showCurrentStep(position);
                }
            });
        }

        return rootView;
    }

    public void showCurrentStep(int position){
        Step currentStep = instructions.get(position);
        String stepNumber = currentStep.getStepNumber();
        String description = currentStep.getStepDescription();
        String videoUrl = currentStep.getStepVideoUrl();

        // it doesn't need in case of single pane
        if(!mTwoPane)getActivity().setTitle("Step " + stepNumber);
        mInfo.setText(description);

        // if currently video is playing then release it before initialising the others
        if( wasPlaying ) {
            wasPlaying = false;
            releasePlayer();}

        // Initialize the player.
        if(!TextUtils.isEmpty(videoUrl)){
            mPlayerView.setVisibility(View.VISIBLE);
            wasPlaying = true;
            initializePlayer(Uri.parse(videoUrl));}
        else{
            mPlayerView.setVisibility(View.GONE);
        }

    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }
    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }
}
