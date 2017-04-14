package com.kenportal.users.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.joanzapata.iconify.widget.IconTextView;
import com.kenportal.users.R;
import com.kenportal.users.datamodels.WishAdapterModel;

/**
 * Created by kranti on 11/26/2016.
 */
public class EISAdapter extends ArrayAdapter<WishAdapterModel> {
    private LayoutInflater mInflater;
    Context context;

    public EISAdapter(Context context, WishAdapterModel[] values) {
        super(context, R.layout.horizontal_list_item, values);
        this.context = context;
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.horizontal_list_item, parent, false);
            holder.icon_image = (IconTextView) convertView.findViewById(R.id.list_icon);
            holder.textView = (TextView) convertView.findViewById(R.id.list_text);
            holder.num_of_wish = (TextView) convertView.findViewById(R.id.num_of_wish);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        // Populate the text
        holder.textView.setText(getItem(position).getList_text());
        holder.icon_image.setText(getItem(position).getList_icon());
        holder.num_of_wish.setText(getItem(position).getNum_wish());

        if(holder.num_of_wish.getText().toString().equals("dis")){
            holder.icon_image.setBackgroundResource(R.drawable.circular_gray_button_normal);
            holder.icon_image.setTextColor(Color.parseColor("#50000000"));
            holder.textView.setTextColor(Color.parseColor("#50000000"));
        }

        if (holder.num_of_wish.getText().toString().equals("-")||holder.num_of_wish.getText().toString().equals("0") || holder.num_of_wish.getText().toString().equals("dis")) {
            holder.num_of_wish.setVisibility(View.GONE);
        } else {
            holder.num_of_wish.setVisibility(View.VISIBLE);
            holder.num_of_wish.setText(getItem(position).getNum_wish());
            Animation popnaim = AnimationUtils.loadAnimation(context, R.anim.popup_in);
            holder.num_of_wish.setAnimation(popnaim);
        }
        return convertView;
    }

    /**
     * View holder for the views we need access to
     */
    private static class Holder {
        public IconTextView icon_image;
        public TextView textView;
        public TextView num_of_wish;
    }


}
