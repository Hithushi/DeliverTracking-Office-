package com.example.delivertracking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivertracking.Model.ListItem;
import com.example.delivertracking.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyAdapterViewHolder>{

    List<ListItem> listItemArrayList;
    Context context;
    public MyAdapter (List<ListItem> listItemArrayList, Context context) {
        this.listItemArrayList = listItemArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_item,parent,false);
        return new MyAdapterViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyAdapterViewHolder holder, int position) {
        ListItem listItem = listItemArrayList.get(position);
        holder.textViewTime.setText(listItem.getTime());
        holder.textViewCode.setText(listItem.getCode());
    }
    @Override
    public int getItemCount() {
        return listItemArrayList.size();
    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCode, textViewTime;
        CardView cardView;
        public MyAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCode = itemView.findViewById(R.id.textViewCode);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(v -> {
                String type = listItemArrayList.get(getAdapterPosition()).getCode();

                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT,type);
                i.setType("text/plain");
                itemView.getContext().startActivity(i);
            });
        }
    }
}
