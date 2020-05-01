package com.hecvd19.pasdept.home.bottomsheet;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hecvd19.pasdept.models.Post;

import java.util.ArrayList;

public class BottomSheetViewModel extends ViewModel {

    private static final String TAG = "BottomSheetViewModel";
    private BottomSheetRepository repository;

    public BottomSheetViewModel() {
        repository = new BottomSheetRepository();
    }

    LiveData<ArrayList<String>> uploadAttachments(ArrayList<Uri> uris) {

        return repository.uploadAttachments(uris);
    }

    LiveData<Post> uploadPost(Post post) {
        Log.d(TAG, "uploadPost: " + post);
        return repository.uploadPost(post);
    }

}
