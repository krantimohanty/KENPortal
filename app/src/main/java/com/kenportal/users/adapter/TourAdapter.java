package com.kenportal.users.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kenportal.users.R;
import com.kenportal.users.datamodels.TourModel;

import java.util.ArrayList;


/**
 * Created by kranti on 11/26/2016.
 */
public class TourAdapter extends RecyclerView.Adapter {

    ArrayList<TourModel> tourModel;
    static  Context context;
    AlertDialog.Builder alertDialog;
    String userId="";

    ArrayList<String> userArrList;
    ArrayList<String> list =new ArrayList<>();
    //int position;


    public TourAdapter(Context context, ArrayList<TourModel> tourModelArrayList, ArrayList<String> userArrList) {
        this.context=context;
        this.tourModel=tourModelArrayList;
        this.userArrList=userArrList;
        Log.d("ResultAdapter", String.valueOf(tourModel.size()));

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_list,parent,false);
        vh=new TourViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

           if(holder instanceof TourViewHolder){

                                      //String  id =userArrList.get(position);
                      ((TourViewHolder) holder).tour_name.setText(tourModel.get(position).getNAME());
                      ((TourViewHolder) holder).text_city.setText(tourModel.get(position).getTCITY());
                      ((TourViewHolder) holder).text_end_city.setText(tourModel.get(position).getFCITY());
                      ((TourViewHolder) holder).text_fcity.setText(tourModel.get(position).getFCITY());

                      /*userArr =new ArrayList<>();
                      userArr.add(tourModel.get(position).getINT_USERID());*/


              /* ((TourViewHolder) holder).tour_start_date.setText(userArrList[position]);
               ((TourViewHolder) holder).tour_start_date.setBackgroundResource(R.color.black);
               ((TourViewHolder) holder).tour_city.setText(userArrList.get(position));
               ((TourViewHolder) holder).tour_city.setBackgroundResource(R.color.black);
               ((TourViewHolder) holder).tour_country.setText(userArrList.get(position));
               ((TourViewHolder) holder).tour_country.setBackgroundResource(R.color.black);*/



               ((TourViewHolder) holder).tour_city.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       Dialog dialog = new Dialog(v.getContext(),R.style.FullHeightDialog);

                       dialog.setContentView(R.layout.popup_layout);
                       //dialog.setTitle(tourModel.get(position).getTCITY());
                       //dialog.getWindow().setBackgroundDrawableResource();
                       TextView textStart = (TextView) dialog.findViewById(R.id.text_startdate);
                       TextView textEnd = (TextView) dialog.findViewById(R.id.text_enddate);
                       TextView textStext = (TextView) dialog.findViewById(R.id.text_title);
                       textStext.setText(tourModel.get(position).getTCITY() + "(" + tourModel.get(position).getTCOUNTRY() + ")");
                       textStext.setTextColor(v.getContext().getResources().getColor(R.color.dark));
                       textStart.setText(tourModel.get(position).getSTARTDATE());
                       textStart.setTextColor(v.getContext().getResources().getColor(R.color.black));
                       textEnd.setText(tourModel.get(position).getENDDATE());
                       textEnd.setTextColor(v.getContext().getResources().getColor(R.color.black));
                       dialog.setCancelable(true);
                       dialog.setCanceledOnTouchOutside(true);
                       dialog.show();
                       /*Toast.makeText(v.getContext(),"Message", Toast.LENGTH_SHORT).show();
                        getShowData(v.getContext());*/
                   }
               });

               ((TourViewHolder) holder).tour_country.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       Dialog dialog = new Dialog(v.getContext(),R.style.FullHeightDialog);
                       //dialog .requestWindowFeature(Window.FEATURE_NO_TITLE);
                       dialog.setContentView(R.layout.popup_layout);
                       //dialog.setTitle(tourModel.get(position).getFCITY());
                       //dialog.getWindow().setBackgroundDrawableResource();
                       TextView textStart = (TextView) dialog.findViewById(R.id.text_startdate);
                       TextView textEnd = (TextView) dialog.findViewById(R.id.text_enddate);
                       TextView textStext = (TextView) dialog.findViewById(R.id.text_title);
                       textStext.setText(tourModel.get(position).getFCITY() + "(" + tourModel.get(position).getFCOUNTRY() + ")");
                       textStext.setTextColor(v.getContext().getResources().getColor(R.color.dark));
                       textStart.setText(tourModel.get(position).getDTM_TOUR_START_DATE());
                       textStart.setTextColor(v.getContext().getResources().getColor(R.color.dark));
                       textEnd.setText(tourModel.get(position).getDTM_TOUR_END_DATE());
                       textEnd.setTextColor(v.getContext().getResources().getColor(R.color.dark));
                       dialog.setCancelable(true);
                       dialog.setCanceledOnTouchOutside(true);
                       dialog.show();
                       /*Toast.makeText(v.getContext(),"Message", Toast.LENGTH_SHORT).show();
                        getShowData(v.getContext());*/

                   }
               });
           }


    }

   /* private void getShowData(Context context) {

         Dialog dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.popup_layout);
        dialog.setTitle("Title");
        TextView text = (TextView) dialog.findViewById(R.id.text_startdate);
        text.setText(tourModel.get(position).getFCITY());
        dialog.show();

    }*/

    @Override
    public int getItemCount() {
        return tourModel.size();
    }

    public class TourViewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView tour_name;
        AppCompatTextView tour_country;
        AppCompatTextView text_city;
        AppCompatTextView text_end_city;
        AppCompatTextView tour_city;
        AppCompatTextView text_fcity;
        AppCompatTextView tour_start_date;
        Context context;

        public TourViewHolder(View itemView) {
            super(itemView);

            tour_name= (AppCompatTextView)itemView.findViewById(R.id.tour_name);
            text_fcity= (AppCompatTextView)itemView.findViewById(R.id.text_fcity);
            text_end_city= (AppCompatTextView)itemView.findViewById(R.id.text_end_city);
            text_city= (AppCompatTextView)itemView.findViewById(R.id.text_city);
            tour_city= (AppCompatTextView)itemView.findViewById(R.id.tour_city);
            tour_country= (AppCompatTextView)itemView.findViewById(R.id.tour_country);
            tour_start_date= (AppCompatTextView)itemView.findViewById(R.id.tour_start_date);

        }


    }
}
