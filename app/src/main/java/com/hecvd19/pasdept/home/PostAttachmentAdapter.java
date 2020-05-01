package com.hecvd19.pasdept.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hecvd19.pasdept.R;
import com.hecvd19.pasdept.utils.Utils;

import java.util.List;

public class PostAttachmentAdapter extends RecyclerView.Adapter<PostAttachmentAdapter.PostAttachmentViewHolder> {

    private static final String TAG = "PostAttachmentAdapter";
    private List<String> attachmentList;
    private OnAttachmentClickListener listener;

    public interface OnAttachmentClickListener {
        void onClick(String type, String uri);
    }

    PostAttachmentAdapter(List<String> attachmentPath, OnAttachmentClickListener listener) {
        attachmentList = attachmentPath;
        this.listener = listener;
    }


    @NonNull
    @Override
    public PostAttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_post_attachments, parent, false);
        return new PostAttachmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAttachmentViewHolder holder, final int position) {

        final String[] file = Utils.getFileDetails(attachmentList.get(position));

        if (file[1].equals("application/pdf")) {
            Log.d(TAG, "onBindViewHolder: pdf file." + attachmentList.get(position));
            holder.pdfDescriptor.setVisibility(View.VISIBLE);
            holder.fileName.setText(file[0]);

            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.pdf_placeholder)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.imageView);

        } else {
            Log.d(TAG, "onBindViewHolder: image file." + attachmentList.get(position));
            holder.pdfDescriptor.setVisibility(View.GONE);
            Glide.with(holder.itemView.getContext())
                    .load(attachmentList.get(position))
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.imageView);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (file[1]) {
                    case "application/pdf": {
                        listener.onClick("application/pdf", attachmentList.get(position));
                    }
                    default: {
                        listener.onClick("image/*", attachmentList.get(position));
                    }
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return attachmentList.size();
    }

    static class PostAttachmentViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        LinearLayout pdfDescriptor;
        TextView fileName;

        PostAttachmentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivAttachment);
            pdfDescriptor = itemView.findViewById(R.id.pdfDescriptor);
            fileName = itemView.findViewById(R.id.pdfName);
        }

    }
}
