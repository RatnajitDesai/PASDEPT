package com.hecvd19.pasdept.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hecvd19.pasdept.models.Department;
import com.hecvd19.pasdept.utils.Constants;

import static com.hecvd19.pasdept.utils.Constants.COL_POSTS;
import static com.hecvd19.pasdept.utils.Constants.FL_POSTS_POST_DEPARTMENT_ID;

public class FirestorePostListRepositoryCallback implements PostViewModel.PostListRepository, PostListLiveData.OnLastVisiblePostCallback,
        PostListLiveData.OnLastPostReachedCallback, PostViewModel.GetUserInfo {

    private static final String TAG = "FirestorePostListReposi";


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = firebaseFirestore.collection(COL_POSTS);
    private Query query;

    private DocumentSnapshot lastVisiblePost;
    private boolean isLastPostReached;


    @Override
    public void setLastVisiblePost(DocumentSnapshot lastVisiblePost) {
        this.lastVisiblePost = lastVisiblePost;

    }

    @Override
    public void setLastPostReached(boolean isLastPostReached) {
        this.isLastPostReached = isLastPostReached;
    }


    @Override
    public PostListLiveData getMyPosts(String deptId) {
        query = productsRef.whereEqualTo(FL_POSTS_POST_DEPARTMENT_ID, deptId);

        if (isLastPostReached) {
            Log.d(TAG, "getPostListLiveData: isLastPostReached");
            return null;
        }
        if (lastVisiblePost != null) {
            Log.d(TAG, "getPostListLiveData: Last Visible : ");
            query = query.startAfter(lastVisiblePost);
        }
        return new PostListLiveData(query, this, this);
    }


    @Override
    public MutableLiveData<Department> getUserInfo() {

        final MutableLiveData<Department> departmentMutableLiveData = new MutableLiveData<>();
        String userID = mAuth.getCurrentUser().getUid();
        firebaseFirestore.collection(Constants.COL_DEPARTMENTS)
                .document(userID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot == null) {
                    return;
                } else {
                    Department department = documentSnapshot.toObject(Department.class);
                    departmentMutableLiveData.postValue(department);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
                departmentMutableLiveData.postValue(null);
            }
        });
        return departmentMutableLiveData;
    }

}
