package com.hecvd19.pasdept.home;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.hecvd19.pasdept.R;
import com.hecvd19.pasdept.models.Post;
import com.hecvd19.pasdept.utils.Constants;
import com.hecvd19.pasdept.utils.Utils;

import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> implements PostAttachmentAdapter.OnAttachmentClickListener {

    private static final String TAG = "PostAdapter";


    private List<Post> posts;
    private IntentListener intentListener;

    public interface IntentListener {
        void startIntent(Intent intent);
    }

    public PostAdapter(List<Post> posts, IntentListener intentListener) {
        this.posts = posts;
        this.intentListener = intentListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_postview, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {

        Post post = posts.get(position);
        if (posts.get(position).getPostAttachments() != null) {
            holder.postAttachments.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            PostAttachmentAdapter adapter = new PostAttachmentAdapter(posts.get(position).getPostAttachments(), this);
            holder.postAttachments.setAdapter(adapter);

        } else {
            holder.postAttachments.setVisibility(View.GONE);
        }
        holder.bindTo(post);
        holder.priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "Highly Important Info", Toast.LENGTH_SHORT).show();
            }
        });

        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        String.format(Locale.US, "%s\nDepartment: %s\nMessage: %s\nData:",
                                Utils.APP_MESSAGE, posts.get(position).getDepartmentName(), posts.get(position).getPostMessage()));
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                intentListener.startIntent(shareIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onClick(String type, String uri) {
        Intent intent = new Intent(Intent.ACTION_QUICK_VIEW);
        intent.setDataAndType(Uri.parse(uri), type);
        intentListener.startIntent(intent);
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        //widgets
        ImageView deptLogo;
        ImageView priority;
        RecyclerView postAttachments;
        MaterialButton mShare;
        TextView deptName;
        TextView timestamp;
        TextView postMessage;
        TextView postAttachmentDesc;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            deptLogo = itemView.findViewById(R.id.departmentLogo);
            priority = itemView.findViewById(R.id.ivPostPriority);
            postAttachments = itemView.findViewById(R.id.rvPostAttachments);
            deptName = itemView.findViewById(R.id.departmentName);
            timestamp = itemView.findViewById(R.id.timestamp);
            postMessage = itemView.findViewById(R.id.postMessage);
            mShare = itemView.findViewById(R.id.btnShare);
            postAttachmentDesc = itemView.findViewById(R.id.attachmentsDescriptor);
        }

        void bindTo(Post post) {

            Glide.with(itemView.getContext())
                    .load(post.getDepartmentLogo())
                    .placeholder(R.drawable.default_dept_profile)
                    .error(R.drawable.default_dept_profile)
                    .circleCrop().into(deptLogo);

            deptName.setText(post.getDepartmentName());

            postMessage.setText(post.getPostMessage());

            timestamp.setText(Utils.getTime(post.getTimestamp()));

            if (post.getPriority().equals(Constants.POST_PRIORITY_HIGH)) {
                priority.setVisibility(View.VISIBLE);
            } else {
                priority.setVisibility(View.GONE);
            }

            //attachment descriptor
            if (post.getPostAttachments().size() > 1) {
                postAttachmentDesc.setText(String.format(Locale.US, "%d files attached.(Scroll sideways to view)", post.getPostAttachments().size()));
            } else {
                postAttachmentDesc.setVisibility(View.GONE);
            }

            Log.d(TAG, "bindTo: post view updated." + post.toString());
        }
    }
}
