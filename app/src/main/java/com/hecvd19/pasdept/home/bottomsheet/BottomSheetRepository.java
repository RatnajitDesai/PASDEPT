package com.hecvd19.pasdept.home.bottomsheet;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hecvd19.pasdept.models.Department;
import com.hecvd19.pasdept.models.Post;
import com.hecvd19.pasdept.utils.Constants;

import java.util.ArrayList;
import java.util.UUID;

public class BottomSheetRepository {

    private static final String TAG = "BottomSheetRepository";
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    StorageReference storageReference;

    BottomSheetRepository() {

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public MutableLiveData<ArrayList<String>> uploadAttachments(final ArrayList<Uri> uris) {

        final MutableLiveData<ArrayList<String>> arrayAttachments = new MutableLiveData<>();
        final ArrayList<String> attachmentPath = new ArrayList<>();
        if (!uris.isEmpty()) {
            for (Uri uri : uris) {
                Log.d(TAG, "uploadAttachments: " + uri.toString());
                storageReference = storage.getReference("/attachments/");


                UploadTask uploadTask = storageReference.putFile(uri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        if (taskSnapshot != null) {
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();

                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d(TAG, "onSuccess: " + uri.toString());
                                    attachmentPath.add(uri.toString());
                                    if (uris.size() == attachmentPath.size()) {
                                        arrayAttachments.postValue(attachmentPath);
                                    }
                                }
                            });
                        }
                    }
                });
            }

        } else {
            Log.d(TAG, "uploadAttachments: is Empty.");
            arrayAttachments.postValue(new ArrayList<String>());
        }

        return arrayAttachments;
    }


    public MutableLiveData<Post> uploadPost(final Post post) {

        Log.d(TAG, "uploadPost: " + post.toString());
        final MutableLiveData<Post> postMutableLiveData = new MutableLiveData<>();

        post.setPostId(getPostId());
        Log.d(TAG, "uploadPost: post ID generated: " + post.getPostId());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "uploadPost: user ID: " + uid);
        firestore.collection(Constants.COL_DEPARTMENTS)
                .document(uid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot == null) {
                    Log.d(TAG, "onSuccess: null returned!");
                    return;
                } else {

                    Department department = documentSnapshot.toObject(Department.class);

                    Log.d(TAG, "onSuccess: department :" + department.toString());
                    post.setDepartmentId(department.getDepartmentId());
                    post.setDepartmentLogo(department.getDepartmentLogo());
                    post.setDepartmentName(department.getDepartmentName());

                    Log.d(TAG, "onSuccess: before set: " + post.toString());

                    firestore.collection(Constants.COL_POSTS)
                            .document(post.getPostId())
                            .set(post)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: " + post.toString());
                                    postMutableLiveData.postValue(post);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            postMutableLiveData.postValue(null);
                        }
                    });

                }

            }
        });


        return postMutableLiveData;
    }


    private String getPostId() throws UnsupportedOperationException {
        return UUID.randomUUID().toString();
    }

}
