package com.erniwo.timetableconstruct.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erniwo.timetableconstruct.R;

//public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

//    private String[] mData;
//    private LayoutInflater mInflater;
//    private ItemClickListener mClickListener;
//
//    // data is passed into the constructor
//    public MyRecyclerViewAdapter(Context context, String[] data) {
//        this.mInflater = LayoutInflater.from(context);
//        this.mData = data;
//    }
//
//    // inflates the cell layout from xml when needed
//    @Override
//    @NonNull
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        return viewHolder;
//    }
//
//    // binds the data to the TextView in each cell
//    @Override
//    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {
//        holder.button.setText(mData[position]);
//    }
//
//    @Override
//    public int getItemCount() {
//
//        return mData.length;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        Button button;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            button = itemView.findViewById(R.id.course_card_button);
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View view) {
//            if(mClickListener != null){
//                mClickListener.onItemClick(view, getAdapterPosition());
//            }
//        }
//    }
//    String getItem(int id) {
//        return mData[id];
//    }
//
//    void setClickListener(ItemClickListener itemClickListener) {
//        this.mClickListener = itemClickListener;
//    }
//
//    public interface ItemClickListener {
//        void onItemClick(View view, int position);
//    }
//}
