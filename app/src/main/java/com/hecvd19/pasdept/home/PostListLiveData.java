package com.hecvd19.pasdept.home;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hecvd19.pasdept.R;
import com.hecvd19.pasdept.models.Post;

public class PostListLiveData extends LiveData<Operation> implements EventListener<QuerySnapshot> {

    private static final String TAG = "PostListLiveData";

    private static final int LIMIT = 5;

    interface OnLastVisiblePostCallback {
        void setLastVisiblePost(DocumentSnapshot lastVisiblePost);
    }

    interface OnLastPostReachedCallback {
        void setLastPostReached(boolean isLastPostReached);
    }


    private Query query;
    private ListenerRegistration listenerRegistration;
    private OnLastVisiblePostCallback onLastVisiblePostCallback;
    private OnLastPostReachedCallback onLastPostReachedCallback;

    public PostListLiveData(Query query, OnLastVisiblePostCallback onLastVisiblePostCallback, OnLastPostReachedCallback onLastPostReachedCallback) {
        this.query = query;
        this.onLastVisiblePostCallback = onLastVisiblePostCallback;
        this.onLastPostReachedCallback = onLastPostReachedCallback;
        onLastPostReachedCallback.setLastPostReached(false);
    }

    @Override
    protected void onActive() {
        listenerRegistration = query.addSnapshotListener(this);
    }

    @Override
    protected void onInactive() {
        listenerRegistration.remove();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
        if (e != null) return;

        for (DocumentChange documentChange : querySnapshot.getDocumentChanges()) {

            Post post = documentChange.getDocument().toObject(Post.class);

            switch (documentChange.getType()) {
                case ADDED:
                    // Post addedPost = documentChange.getDocument().toObject(Post.class);
                    Operation addOperation = new Operation(post, R.string.added);
                    setValue(addOperation);
                    break;

                case MODIFIED:
                    Operation modifyOperation = new Operation(post, R.string.modified);
                    setValue(modifyOperation);
                    break;

                case REMOVED:
                    Operation removeOperation = new Operation(post, R.string.removed);
                    setValue(removeOperation);
                    break;
            }
        }

        int querySnapshotSize = querySnapshot.size();
        if (querySnapshotSize < LIMIT) {
            onLastPostReachedCallback.setLastPostReached(true);
        } else {
            DocumentSnapshot lastVisiblePost = querySnapshot.getDocuments().get(querySnapshotSize - 1);
            onLastVisiblePostCallback.setLastVisiblePost(lastVisiblePost);
        }
    }


}
