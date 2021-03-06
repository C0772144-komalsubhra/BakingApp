package com.example.myapplication.ui.detail;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.utils.Constants;

import java.util.ArrayList;

public class StepListActivity  extends AppCompatActivity {

    private boolean mTwoPane;
    private ArrayList<Parcelable> steps = new ArrayList<>();
    private ArrayList<Parcelable> ingredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mTwoPane = findViewById(R.id.two_pane_layout_root) != null;

        if (getIntent().getExtras() != null) {
            steps = getIntent().getParcelableArrayListExtra(Constants.STEPS);
            ingredients = getIntent().getParcelableArrayListExtra(Constants.INGREDIENTS);
            setTitle(getIntent().getStringExtra(Constants.RECIPE_NAME));
        }

        if (mTwoPane) {
            replaceFragment(
                    StepListFragment.newInstance(steps, ingredients),
                    R.id.step_container
            );

        } else {
            replaceFragment(
                    StepListFragment.newInstance(steps, ingredients),
                    R.id.container
            );
        }
    }

    private void replaceFragment(Fragment fragment, int id) {
        if (steps.size() != 0 && ingredients.size() != 0) {
            //Fragment fragment = StepListFragment.newInstance(steps, ingredients);
            getSupportFragmentManager().beginTransaction()
                    .replace(id, fragment)
                    .commit();
        }
    }
}
