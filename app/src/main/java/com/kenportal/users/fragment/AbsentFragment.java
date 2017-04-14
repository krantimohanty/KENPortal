package com.kenportal.users.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenportal.users.R;
import com.kenportal.users.adapter.AbsentAdapter;
import com.kenportal.users.datamodels.AbsentModel;
import com.kenportal.users.widgets.SimpleDividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by kranti on 11/26/2016.
 * AbsentFragment Class
 */
public class AbsentFragment extends Fragment {




    private View rootView;                      //Call rootView Reference
    ArrayList<AbsentModel> arrayAbsent=null;  //Declare ArraylistAbsent Reference
    // Declare UI components
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLayoutManager; //Call Layoutmanager reference
    private AbsentAdapter absentAdapter;
    //private List<String> data;



     //Create default construct
    public AbsentFragment() {
        // Required empty public constructor
    }

    //declare list Item
    String[] listArray = {"ListVIew Android",
            "Simple Android ListView","ListView in Android",
            };
    //declare list slno
    public static final String[] slno = new String[] {
            "1",
            "2",
            "3" };
        //declare list status
    public static final String[] status = {
            "OK", "OK",  "OK" };

    /** Called when the absent fragment is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        // Inflate the layout for this Absentfragment
        rootView=inflater.inflate(R.layout.fragment_absent,container,false);

        // Initialize the UI components
        mRecycleView=(RecyclerView)rootView.findViewById(R.id.my_recycler_view);

        mRecycleView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecycleView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);

        //Adding data in to ArraylistAbsentmodel
        // Initialize an array to put absent objects with absent's all attributes which we have defined in AbsentModel.java
        arrayAbsent=new ArrayList<>();

        for (int i = 0; i < listArray.length; i++) {

            AbsentModel itemAbsentList = new AbsentModel(slno[i], listArray[i], status[i]);
            arrayAbsent.add(itemAbsentList);
        }

        //data to set Absentadapter
        absentAdapter = new AbsentAdapter(getActivity(), arrayAbsent);

        // By using setAdapter method, you plugged the RecyclerView with adapter
        mRecycleView.setAdapter(absentAdapter);
        return rootView;
    }

}
