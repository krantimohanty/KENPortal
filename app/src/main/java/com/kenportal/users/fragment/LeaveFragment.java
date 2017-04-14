package com.kenportal.users.fragment;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenportal.users.R;

import com.kenportal.users.adapter.LeaveAdapter;

import com.kenportal.users.datamodels.LeaveModel;
import com.kenportal.users.widgets.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * LeaveFragment class
 */
public class LeaveFragment extends Fragment {

    private View rootView;                       //Call rootview reference
    private RecyclerView mRecyclerView;          //Call Recyclerview reference
    private LinearLayoutManager mLayoutManager;  //Call Layoutmanager reference
    List<LeaveModel> allLeaveModelData;          //Leave Model object reference


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this Leavefragment
        rootView=inflater.inflate(R.layout.fragment_leave,container,false);
        mRecyclerView= (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        //Adding data in to ArraylistLeaveModel
        allLeaveModelData=new ArrayList<LeaveModel>();

        //Method call for PopulateLeave data
        populateLeaveData();

        //Adding data in head section
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

       //data to set leaveAdapter
        LeaveAdapter leaveAdapter = new LeaveAdapter(getActivity(), (ArrayList<LeaveModel>) allLeaveModelData,mRecyclerView);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.grid_span_1));

        // By using setAdapter method, you plugged the RecyclerView with adapter
        mRecyclerView.setAdapter(leaveAdapter);
        leaveAdapter.setLayoutManager(manager);
        return rootView;
    }


    private void populateLeaveData() {
        for (int i = 1; i <= 10; i++) {

            LeaveModel leaveModel = new LeaveModel();
            leaveModel.setTitle("Section " + i);

            ArrayList<String> singleItem = new ArrayList<>();
            for (int j = 1; j <= 4; j++) {

                singleItem.add("Item " + j);
            }

            leaveModel.setAllItemsInSection(singleItem);

            allLeaveModelData.add(leaveModel);

        }
    }
}
