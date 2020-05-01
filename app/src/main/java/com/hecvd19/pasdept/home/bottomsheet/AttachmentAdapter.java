package com.hecvd19.pasdept.home.bottomsheet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.hecvd19.pasdept.R;

import java.util.ArrayList;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentRecycler> {

    ArrayList<String> list;
    RemoveAttachmentListener listener;

    interface RemoveAttachmentListener {
        void removeAttachment(int position);
    }

    public AttachmentAdapter(ArrayList<String> list, RemoveAttachmentListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AttachmentRecycler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attachment_list, parent, false);

        return new AttachmentRecycler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentRecycler holder, final int position) {
        holder.chip.setText(list.get(position));

        holder.chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.removeAttachment(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AttachmentRecycler extends RecyclerView.ViewHolder {

        private Chip chip;

        public AttachmentRecycler(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.attachmentChip);
        }
    }
}
