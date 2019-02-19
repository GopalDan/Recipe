package com.example.gopal.f_bakingapp.networking;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Movie;
import android.util.Log;

import com.example.gopal.f_bakingapp.Recipe;

import java.util.List;

/**
 * Created by Gopal on 2/13/2019.
 */


public class CustomLoader extends android.support.v4.content.AsyncTaskLoader {
    String LOG_TAG = CustomLoader.class.getSimpleName();
    private String mUrl;
    private int mSelectedId;
    List<Movie> movies;
    public CustomLoader(Context context, String url, int selectedId) {
        super(context);
        mUrl = url;
        mSelectedId = selectedId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Recipe loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a recipe.
        return QueryUtils.fetchRecipe(mUrl, mSelectedId);
    }
}
