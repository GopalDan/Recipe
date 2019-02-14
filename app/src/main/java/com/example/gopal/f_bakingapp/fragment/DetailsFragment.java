package com.example.gopal.f_bakingapp.fragment;



import android.content.Context;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gopal.f_bakingapp.AddWidgetService;
import com.example.gopal.f_bakingapp.R;
import com.example.gopal.f_bakingapp.Recipe;
import com.example.gopal.f_bakingapp.RecipeAdapter;
import com.example.gopal.f_bakingapp.Step;
import com.example.gopal.f_bakingapp.networking.CustomLoader;
import com.example.gopal.f_bakingapp.ui.DetailsActivity;
import com.example.gopal.f_bakingapp.ui.InstructionActivity;
import com.example.gopal.f_bakingapp.ui.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/*
 * either use getContext() or getActivity to compatible/support in activity
 */

public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Recipe>{
    private List<List<String>> ingredients;
    private List<List<String>> steps;
    private List<Step> instructions;
    private int mSelectedId;
    private RecipeAdapter mAdapter;
    private final String mUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private final int LOADER_ID = 5;
    private TextView mIngredientTextView;


    public DetailsFragment() {};

    OnStepClickListener mCallback;

    public interface OnStepClickListener{
        void onStepSelected( List<Step> steps, int position );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_details, container, false);

        mSelectedId = getActivity().getIntent().getIntExtra(MainActivity.CARD_VIEW_ID, 1);
        instructions = new ArrayList<>();

        mIngredientTextView = rootView.findViewById(R.id.ingredient_tv);


        mAdapter = new RecipeAdapter( getContext(),new ArrayList<Step>());
        ListView listView = rootView.findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);
        // when a list item is clicked go to InstructionActivity for showing video & brief description
        // Using Serializable to pass the list of custom object i.e. List<Step>
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               // Trigger the listener
                mCallback.onStepSelected(instructions, position);
            }
        });

        // start the loader
        getLoaderManager().initLoader(LOADER_ID,null, this);


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    /**
     * Showing ingredients & steps of selected Recipe in MainActivity
     * @param singleRecipe The Recipe that is selected
     */
    public void showIngredient(Recipe singleRecipe) {
        //Log.e(TAG, "ID is" + singleRecipe.getId());
        if (singleRecipe != null) {
            String recipeName = singleRecipe.getName();
            ingredients = singleRecipe.getIngredients();
            steps = singleRecipe.getSteps();

            // setting title in action bar
            getActivity().setTitle(recipeName);
            for(int j=0;j<steps.size();j++){
                List<String> singleInstruction = steps.get(j);
                String stepNumber = singleInstruction.get(0);
                String stepInfo = singleInstruction.get(1);
                String description = singleInstruction.get(2);
                String videoUrl = singleInstruction.get(3);
                String thumbnailUrl = singleInstruction.get(4);

                instructions.add(new Step(stepNumber,stepInfo,description, videoUrl, thumbnailUrl));
            }

            int length = ingredients.size();
            Log.e(TAG, "Number of ingredients are: " + length);

            for (int i = 0; i <length; i++) {
                List<String> singleIngredient = ingredients.get(i);
                String quantity = singleIngredient.get(0);
                String measure = singleIngredient.get(1);
                String ingredient = singleIngredient.get(2);
                String mixUp = getActivity().getString(R.string.filled_bullet) + ingredient +"(" + quantity + " " +  measure + ")\n";
                mIngredientTextView.append(mixUp);
               // mIngredientTextView.append("\n");


            }
        }
    }

    @Override
    public Loader<Recipe> onCreateLoader(int i, Bundle bundle) {
        return  new CustomLoader(getContext(), mUrl, mSelectedId);

    }

    @Override
    public void onLoadFinished(Loader<Recipe> loader, Recipe recipe) {
        mAdapter.clear();
        showIngredient(recipe);
        mAdapter.addAll(instructions);

    }

    @Override
    public void onLoaderReset(Loader<Recipe> loader) {

    }


}
