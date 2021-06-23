package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//parameterizes ViewHolder
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnLongClickListener {
        //pass in position of long press to notify adapter of delete pos
        void onItemLongClicked(int position);
    }
    //member vars for model
    List<String> items;
    OnLongClickListener longClickListener;

    //constructor setting member vars = to vars passed in
    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //step 1: use layout inflater to inflate a view
        //pass in file, root, and false (recyclerView attaching instead of attaching to root) and return a View
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        //step 2: pass into ViewHolder and return it
        return new ViewHolder(todoView);
    }

    //binds data to particular ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ViewHolder holder, int position) {
        //step 1: Grab item at the position
        String item = items.get(position);
        //step 2: bind item to specified ViewHolder
        holder.bind(item);
    }

    //tells RV how many items in list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //container to provide easy access to views of each row in list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        //requires constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //update view inside of ViewHolder with data passed in
        public void bind(String item) {
        tvItem.setText(item);
        //communicate existing item in RV was clicked to main activity to implement
        tvItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //notifying listener of pos long pressed
                longClickListener.onItemLongClicked(getAbsoluteAdapterPosition());
                //callback consuming long click
                return true;
            }
        });
        }
    }
}
