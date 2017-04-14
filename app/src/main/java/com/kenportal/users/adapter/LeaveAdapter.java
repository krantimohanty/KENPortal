package com.kenportal.users.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kenportal.users.R;
import com.kenportal.users.app_interface.Item;
import com.kenportal.users.datamodels.LeaveModel;
import com.kenportal.users.datamodels.SectionItem;
import com.kenportal.users.utils.SectionedRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by kranti on 11/26/2016.
 * Leave Adapter class
 */
public class LeaveAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private Context context;    //context -  The current context.
    private ArrayList<LeaveModel> leMoArList;//
    private LayoutInflater vi;
    private final int VIEW_ITEM = 1;
    RecyclerView mRecyclerView;

    public LeaveAdapter(Context context, ArrayList<LeaveModel> leMoArList, RecyclerView mRecyclerView) {

        this.context = context;
        this.leMoArList = leMoArList;
        this.mRecyclerView = mRecyclerView;


    }

    @Override
    public int getSectionCount() {
        return leMoArList.size();
    }

    @Override
    public int getItemCount(int section) {
         return leMoArList.get(section).getAllItemsInSection().size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, boolean header) {

        View v = null;

        //setting action duration
        if (header)

        {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_headsection, parent, false);
            return new SectionViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_entry, parent, false);
            return new ItemViewHolder(v);
        }


    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

       /* String sectionName = items.get(section).getTitle();
        SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
        sectionViewHolder.sectionTitle.setText(sectionName);*/
        try{
            final Item i = leMoArList.get(section);
             SectionItem si = (SectionItem)i;
            ((SectionViewHolder) holder).sectionTitle.setText(si.getTitle());
            ((SectionViewHolder) holder).sectionNumber.setText(si.getTag());
        }catch (Exception e){

        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int relativePosition, int absolutePosition) {

        //ArrayList<String> itemsInSection = items.get(section).getAllItemsInSection();
       /* String itemName = itemsInSection.get(relativePosition);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.itemTitle.setText(itemName);*/

     try {

           final Item i = leMoArList.get(section);
           LeaveModel ei = (LeaveModel)i;
           String itemName = ei.getTitle();
           ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
           itemViewHolder.itemTitle.setText(itemName);

       }   catch (Exception e){

     }
    }


    // SectionViewHolder Class for Leave Sections
    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        final TextView sectionTitle;
        final TextView sectionNumber;

        public SectionViewHolder(View itemView) {
            super(itemView);

            sectionTitle = (TextView) itemView.findViewById(R.id.sectionTitle);
            sectionNumber = (TextView) itemView.findViewById(R.id.sectionNumber);

        }
    }

    // ItemViewHolder Class for Leave Items in each Section
    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        final TextView itemTitle;
        final TextView itemSubtitle;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            itemSubtitle = (TextView) itemView.findViewById(R.id.itemSubtitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     //to show toast message when user click any item in leave section
                    Toast.makeText(v.getContext(), itemTitle.getText(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

}




