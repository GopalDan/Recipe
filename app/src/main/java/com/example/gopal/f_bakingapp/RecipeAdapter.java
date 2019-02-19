package com.example.gopal.f_bakingapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gopal on 2/15/2019.
 */


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{
    private List<Step> mSteps;
    private OnStepClickedListener mClickListener;

    public RecipeAdapter( OnStepClickedListener stepClickedListener){
        mClickListener = stepClickedListener;
    }

    // Interface acts a listener in RecyclerView
    public interface OnStepClickedListener{
        void stepClicked(int position);
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder viewHolder, int position) {
        Step currentStep = mSteps.get(position);
        viewHolder.mStepNumber.setText(currentStep.getStepNumber());
        viewHolder.mStepInfo.setText(currentStep.getStepInfo());
        String videoUrl = currentStep.getStepVideoUrl();
        if(!TextUtils.isEmpty(videoUrl)){
            viewHolder.mVideoUrl.setImageResource(R.drawable.play_icon);
        }

    }

    @Override
    public int getItemCount() {
        if(mSteps==null){
            return 0;
        }
        return mSteps.size();
    }

    /**
     * This method is used to load data on the Recipe Adapter
     * This is handy when we get new data from the web but don't want to create a
     * new RecipeAdapter to display it.
     *
     * @param steps The new instruction steps to be displayed.
     */

    public void setRecipeStep(List<Step> steps){
        mSteps = steps;
        notifyDataSetChanged();
    }

    /*
     * ViewHolder class as inner class
     */
    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mStepNumber;
        TextView mStepInfo;
        ImageView mVideoUrl;


        public RecipeViewHolder(View view ){
            super(view);
            mStepNumber =view.findViewById(R.id.step_number);
            mStepInfo =view.findViewById(R.id.step_instruction);
            mVideoUrl =view.findViewById(R.id.play_video_tv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickListener.stepClicked(position);
        }
    }

}
