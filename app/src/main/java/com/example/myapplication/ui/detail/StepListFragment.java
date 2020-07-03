package com.example.myapplication.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Ingredients;
import com.example.myapplication.model.Steps;
import com.example.myapplication.ui.adapter.IngredientsAdapter;
import com.example.myapplication.ui.adapter.StepsAdapter;
import com.example.myapplication.utils.Constants;
import com.example.myapplication.utils.ItemClickSupport;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListFragment extends Fragment {

    @BindView(R.id.ingredientsList)
    RecyclerView ingredientsRcl;
    @BindView(R.id.stepsList) RecyclerView stepsRcl;

    private ArrayList<Steps> stepsList = new ArrayList<>();
    private ArrayList<Ingredients> ingredientsList = new ArrayList<>();

    boolean mTwoPane;
    public StepListFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, view);
        readBundle(getArguments());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null){
            if (mTwoPane)
                getActivity().findViewById(R.id.select_step).setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();

        mTwoPane = getActivity().findViewById(R.id.two_pane_layout_root) !=null;
        setListener();
    }

    private void setListener() {
        ItemClickSupport.addTo(stepsRcl).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (!mTwoPane) {
                    Intent intent = new Intent(getActivity(), StepDetailActivity.class);
                    intent.putExtra(Constants.STEPS, stepsList);
                    intent.putExtra(Constants.POSITION, position);

                    startActivity(intent);
                }else {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detail_container,
                                    StepDetailFragment.newInstance(stepsList, position))
                            .commit();

                    getActivity().findViewById(R.id.select_step).setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupViews() {
        ingredientsRcl.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        IngredientsAdapter adapter = new IngredientsAdapter(getActivity(), ingredientsList);
        ingredientsRcl.setAdapter(adapter);

        stepsRcl.setLayoutManager(new LinearLayoutManager(getActivity()));

        StepsAdapter stepsAdapter = new StepsAdapter(getActivity(), stepsList);
        stepsRcl.setAdapter(stepsAdapter);
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            stepsList = bundle.getParcelableArrayList(Constants.STEPS);
            ingredientsList = bundle.getParcelableArrayList(Constants.INGREDIENTS);
        }
    }

    public static StepListFragment newInstance(ArrayList<Parcelable> steps,
                                               ArrayList<Parcelable> ingredients) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.STEPS, steps);
        bundle.putParcelableArrayList(Constants.INGREDIENTS, ingredients);
        StepListFragment fragment = new StepListFragment();
        fragment.setArguments(bundle);

        return fragment;
    }
}
