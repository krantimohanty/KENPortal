package com.kenportal.users.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kenportal.users.LeaveAbsenceActivity;
import com.kenportal.users.MeetingScheduleActivity;
import com.kenportal.users.R;
import com.kenportal.users.adapter.EISAdapter;
import com.kenportal.users.datamodels.WishAdapterModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {


    public InformationFragment() {
        // Required empty public constructor
    }

    View rootView;
    GridView gridView;
    EISAdapter eisAdapter;
    WishAdapterModel[] wishAdapterModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_information, container, false);

        gridView = (GridView) rootView.findViewById(R.id.grid_information);


        wishAdapterModels = new WishAdapterModel[]{
                new WishAdapterModel("{ic-leave}", "Leave", "-"),
//                new WishAdapterModel("{ic-tour}", "Tour", "-"),
                new WishAdapterModel("{ic-meetSchedule}", "Meeting Schedule", "-"),
//                new WishAdapterModel("{ic-claim}", "Claim", "dis")
        };
        eisAdapter = new EISAdapter(getActivity(), wishAdapterModels);
        gridView.setAdapter(eisAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), LeaveAbsenceActivity.class));
                        break;
//                    case 1:
//                        startActivity(new Intent(getActivity(), TourActivity.class));
//                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), MeetingScheduleActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });

        return rootView;
    }
    private void snackBarAlert(){
        Snackbar.make(getActivity().findViewById(android.R.id.content), "Comming soon...", Snackbar.LENGTH_LONG).show();
    }

}
