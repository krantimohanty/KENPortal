package com.kenportal.users.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.kenportal.users.R;
import com.kenportal.users.adapter.EISAdapter;
import com.kenportal.users.datamodels.WishAdapterModel;
/**
 * A simple {@link Fragment} subclass.
 */
public class CommunicationFragment extends Fragment {

    public CommunicationFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_communication, container, false);

        //get badge count thread

        gridView = (GridView) rootView.findViewById(R.id.grid_communication);


        wishAdapterModels = new WishAdapterModel[]{
                new WishAdapterModel("{ic-messengr}", "Messenger", "dis"),
                new WishAdapterModel("{ic-gal}", "Gallery", "dis"),
        };
        eisAdapter = new EISAdapter(getActivity(), wishAdapterModels);
        gridView.setAdapter(eisAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(new Intent(getActivity(), ComingSoon.class));
                snackBarAlert();
            }
        });

        return rootView;
    }

    private void snackBarAlert() {
        Snackbar.make(getActivity().findViewById(android.R.id.content), "Comming soon...", Snackbar.LENGTH_LONG).show();
    }


}
