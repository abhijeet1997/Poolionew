package com.travelwithpoolio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by kjaganmohan on 14/09/16.
 */
public class Recycler_View_Adapter_Message extends RecyclerView.Adapter<Recycler_View_Adapter_Message.View_Holder> {


    List<Data> list = Collections.emptyList();
    Context context;
    private ItemClickListener clickListener;
    public View view;

    public Recycler_View_Adapter_Message(final List<Data> list, Context context)
    {
        this.list = list;
        this.context = context;

    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }




    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_row, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Recycler_View_Adapter_Message.View_Holder holder, int position) {

        holder.message.setText(list.get(position).getMessage());
        holder.timestamp.setText(list.get(position).getTimestamp());
        holder.mobile_book=list.get(position).getMobile_book();


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public  class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView message , timestamp;
        CardView cv;
        String mobile_book;
        ImageView caller_button;

        public View_Holder(final View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            timestamp=(TextView)itemView.findViewById(R.id.timestamp);
            message = (TextView)itemView.findViewById(R.id.message);
            caller_button=(ImageView)itemView.findViewById(R.id.call_btn);
            caller_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = "tel:"+mobile_book;
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
                    intent.putExtra(Intent.EXTRA_PHONE_NUMBER, number);
                    Log.d("imageView caller ","called in messages");
                    Intent chosenIntent = Intent.createChooser(intent, "Call using...");
                    chosenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    itemView.getContext().startActivity(chosenIntent); ;
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
     }
