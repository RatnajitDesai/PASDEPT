package com.hecvd19.pasdept.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.firebase.Timestamp;

import java.util.List;

public class Post {

    public static DiffUtil.ItemCallback<Post> DIFF_CALLBACK = new DiffUtil.ItemCallback<Post>() {
        @Override
        public boolean areItemsTheSame(@NonNull Post oldItem, @NonNull Post newItem) {
            return oldItem.postId.equals(newItem.postId);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Post oldItem, @NonNull Post newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Post article = (Post) obj;
        return article.postId.equals(this.postId);
    }


    private String postId;
    private String departmentName;
    private String departmentLogo;
    private String departmentId;
    private String postMessage;
    private String priority; // 0 - Low, 1 - High
    private List<String> postAttachments;
    private Timestamp timestamp;
    private List<String> pinCodes;
    private String district;
    private String state;
    private String channel;

    public Post() {

    }

    public Post(String postId, String departmentName, String departmentLogo, String departmentId, String postMessage, String priority, List<String> postAttachments, Timestamp timestamp, List<String> pinCodes, String district, String state, String channel) {
        this.postId = postId;
        this.departmentName = departmentName;
        this.departmentLogo = departmentLogo;
        this.departmentId = departmentId;
        this.postMessage = postMessage;
        this.priority = priority;
        this.postAttachments = postAttachments;
        this.timestamp = timestamp;
        this.pinCodes = pinCodes;
        this.district = district;
        this.state = state;
        this.channel = channel;
    }

    public List<String> getPinCodes() {
        return pinCodes;
    }

    public void setPinCodes(List<String> pinCodes) {
        this.pinCodes = pinCodes;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentLogo() {
        return departmentLogo;
    }

    public void setDepartmentLogo(String departmentLogo) {
        this.departmentLogo = departmentLogo;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<String> getPostAttachments() {
        return postAttachments;
    }

    public void setPostAttachments(List<String> postAttachments) {
        this.postAttachments = postAttachments;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", departmentLogo='" + departmentLogo + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", postMessage='" + postMessage + '\'' +
                ", priority='" + priority + '\'' +
                ", postAttachments=" + postAttachments +
                ", timestamp=" + timestamp +
                ", pinCodes=" + pinCodes +
                ", district='" + district + '\'' +
                ", state='" + state + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
