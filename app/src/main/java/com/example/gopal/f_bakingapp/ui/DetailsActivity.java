package com.example.gopal.f_bakingapp.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.gopal.f_bakingapp.AddWidgetService;
import com.example.gopal.f_bakingapp.R;
import com.example.gopal.f_bakingapp.Step;
import com.example.gopal.f_bakingapp.fragment.DetailsFragment;
import com.example.gopal.f_bakingapp.fragment.InstructionFragment;

import java.io.Serializable;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.OnStepClickListener{
    boolean mTwoPane = false;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        context = getApplicationContext();
        // In case of two pane initially we don't need to show dynamically created item that's why nothing
        // and for static fragment it inflates automatically
        if(findViewById(R.id.two_pane_layout)!=null){
            mTwoPane = true;
        }
    }

    @Override
    public void onStepSelected(List<Step> steps, int position) {
        // Update the Instruction Fragment here as a step is clicked
        if(mTwoPane){
//            InstructionFragment instructionFragment = new InstructionFragment.newInstance(steps, position);
//                   getSupportFragmentManager().beginTransaction()
//                           .add(R.id.instruction_fragment_container, instructionFragment)
//                           .commit();

        }
        // In case of single pane pass List<Step> & position & start another activity i.e. InstructionActivity
        else{
            Intent intent = new Intent(DetailsActivity.this, InstructionActivity.class);
            intent.putExtra("list",(Serializable) steps);
            intent.putExtra("position",position);
            startActivity(intent);
        }

    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_recipe_ingredient:
               // AddWidgetService.addRecipeIngredientWidget(this);break;
                Intent intent = new Intent(context, RecipeWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra("recipe","Brownie");
                context.sendBroadcast(intent);
        }
        return super.onOptionsItemSelected(item);
    }*/
}
