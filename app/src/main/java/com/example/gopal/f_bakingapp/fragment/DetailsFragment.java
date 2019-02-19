package com.example.gopal.f_bakingapp.fragment;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gopal.f_bakingapp.R;
import com.example.gopal.f_bakingapp.Recipe;
import com.example.gopal.f_bakingapp.RecipeAdapter;
import com.example.gopal.f_bakingapp.Step;
import com.example.gopal.f_bakingapp.networking.CustomLoader;
import com.example.gopal.f_bakingapp.ui.MainActivity;
import com.example.gopal.f_bakingapp.ui.RecipeWidgetProvider;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/*
 * either use getContext() or getActivity to compatible/support in activity
 */

public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Recipe>,
        RecipeAdapter.OnStepClickedListener {
    private List<List<String>> ingredients;
    private List<List<String>> steps;
    private List<Step> instructions;
    private int mSelectedId;
    private final String mUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private final int LOADER_ID = 5;
    private TextView mIngredientTextView;
    private RecipeAdapter mRecipeAdapter;
    private String mRecipeName;
    private String mRecipeList = "";
    TextView noNetworkTextView;


    public DetailsFragment() {}

    OnStepClickListener mCallback;

    // Interface that acts as a listener to communicate b/w two fragments
    public interface OnStepClickListener {
        void onStepSelected(List<Step> steps, int position);
    }

    // While creating menu in fragment, you have to set setHasOptionsMenu() true
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        mSelectedId = getActivity().getIntent().getIntExtra(MainActivity.CARD_VIEW_ID, 1);
        instructions = new ArrayList<>();

        mIngredientTextView = rootView.findViewById(R.id.ingredient_tv);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mRecipeAdapter = new RecipeAdapter(this);
        recyclerView.setAdapter(mRecipeAdapter);

        // Checking network connection
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if (isConnected) {
            // start the loader
            getLoaderManager().initLoader(LOADER_ID, null, this);

        } else {
            noNetworkTextView = rootView.findViewById(R.id.no_network);
            noNetworkTextView.setText("Oops! looks like no Network");
        }

        return rootView;
    }

    //In case of Fragment, RecyclerView Listener will be used to trigger the Fragment Listener & the activity that's implemented the
    // Fragment Listener perform the action i.e. data you want to send
    @Override
    public void stepClicked(int position) {
        // Trigger the Fragment Listener when a list item is clicked in Recyclerview
        mCallback.onStepSelected(instructions, position);
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
     *@param singleRecipe The Recipe that is selected
     */
    public void showIngredient(Recipe singleRecipe) {
        //Log.e(TAG, "ID is" + singleRecipe.getId());
        if (singleRecipe != null) {
            mRecipeName = singleRecipe.getName();
            ingredients = singleRecipe.getIngredients();
            steps = singleRecipe.getSteps();

            // setting title in action bar
            getActivity().setTitle(mRecipeName);

            // clearing the instruction list because this making the listview coping the steps
            instructions.clear();

            for (int j = 0; j < steps.size(); j++) {
                List<String> singleInstruction = steps.get(j);
                String stepNumber = singleInstruction.get(0);
                String stepInfo = singleInstruction.get(1);
                String description = singleInstruction.get(2);
                String videoUrl = singleInstruction.get(3);
                String thumbnailUrl = singleInstruction.get(4);

                instructions.add(new Step(stepNumber, stepInfo, description, videoUrl, thumbnailUrl));
            }

            int length = ingredients.size();
            Log.e(TAG, "Number of ingredients are: " + length);
            String s = getString(R.string.ingredient_list_header);
            TextView ingredientListTextView = getActivity().findViewById(R.id.ingredient_list_header);
            SpannableString ss1 = new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(1.2f), 0, 11, 0); // set size
            ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 11, 0);// set color
            ingredientListTextView.setText(ss1);


            //Clear the txtView(ingredient list) before adding
            mIngredientTextView.setText("");

            for (int i = 0; i < length; i++) {
                List<String> singleIngredient = ingredients.get(i);
                String quantity = singleIngredient.get(0);
                String measure = singleIngredient.get(1);
                String ingredient = singleIngredient.get(2);
                String mixUp = getActivity().getString(R.string.filled_bullet) + " " + ingredient + " (" + quantity + " " + measure + ")\n";
                mRecipeList = mRecipeList + mixUp;

                SpannableString ss = new SpannableString(mixUp);
                ss1.setSpan(new RelativeSizeSpan(2.5f), 1, 5, 0); // set size
                ss1.setSpan(new ForegroundColorSpan(Color.RED), 1, 5, 0);// set color
                mIngredientTextView.append(ss);

            }
        }
    }

    @Override
    public Loader<Recipe> onCreateLoader(int i, Bundle bundle) {
        return new CustomLoader(getContext(), mUrl, mSelectedId);

    }

    @Override
    public void onLoadFinished(Loader<Recipe> loader, Recipe recipe) {
        showIngredient(recipe);
        // Add data to the adapter manually unlike ArrayAdapter where using addAll() method
        mRecipeAdapter.setRecipeStep(instructions);

    }

    @Override
    public void onLoaderReset(Loader<Recipe> loader) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_recipe_ingredient:
                Intent intent = new Intent(getContext(), RecipeWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra("recipe", mRecipeList);
                getContext().sendBroadcast(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
