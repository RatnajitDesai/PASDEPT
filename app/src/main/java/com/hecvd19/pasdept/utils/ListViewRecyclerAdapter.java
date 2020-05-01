package com.hecvd19.pasdept.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hecvd19.pasdept.R;

import java.util.List;

public class ListViewRecyclerAdapter extends RecyclerView.Adapter<ListViewRecyclerAdapter.ListViewHolder> {

    List<String> list;
    ListItemClickListener listener;

    public interface ListItemClickListener {
        void sendText(String text);
    }

    ListViewRecyclerAdapter(List<String> list, ListItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.string_list_item_view, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        holder.mText.setText(list.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.sendText(holder.mText.getText().toString());

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        private TextView mText;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.text);
        }
    }
}
