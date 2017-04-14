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
import com.kenportal.users.R;
import com.kenportal.users.TwitterActivity;
import com.kenportal.users.WebViewFBActivity;
import com.kenportal.users.adapter.EISAdapter;
import com.kenportal.users.datamodels.WishAdapterModel;
/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {
    GridView gridView;
    WishAdapterModel[] wishAdapterModels;
    EISAdapter eisAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        gridView = (GridView) rootView.findViewById(R.id.grid_social_feed);

        wishAdapterModels = new WishAdapterModel[]{
                new WishAdapterModel("{ic-orgfb}", "Facebook", "-"),
                new WishAdapterModel("{ic-orgtw}", "Twitter", "-"),
//                new WishAdapterModel("{ic-orgin}", "Linked in", "dis"),
        };
        eisAdapter = new EISAdapter(getActivity(), wishAdapterModels);
        gridView.setAdapter(eisAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
//                        Intent intent = new Intent(getActivity(), FacebookActivity.class);
                        Intent intent = new Intent(getActivity(), WebViewFBActivity.class);
                        intent.putExtra("filter_flag", "");
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent_twitter = new Intent(getActivity(), TwitterActivity.class);
                        startActivity(intent_twitter);
                        break;
//                    case 2:
//                        snackBarAlert();
//                        break;
                    default:
                        break;
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void snackBarAlert(){
        Snackbar.make(getActivity().findViewById(android.R.id.content), "Comming soon...", Snackbar.LENGTH_LONG).show();
    }

}
