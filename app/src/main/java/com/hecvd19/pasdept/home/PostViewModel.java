package com.hecvd19.pasdept.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hecvd19.pasdept.models.Department;

public class PostViewModel extends ViewModel {

    private PostListRepository postListRepository = new FirestorePostListRepositoryCallback();
    private GetUserInfo getUserInfo = new FirestorePostListRepositoryCallback();

    public PostListLiveData getMyPosts(String deptId) {
        return postListRepository.getMyPosts(deptId);
    }

    public LiveData<Department> getUserInfo() {
        return getUserInfo.getUserInfo();
    }


    interface PostListRepository {
        PostListLiveData getMyPosts(String deptId);
    }

    interface GetUserInfo {
        LiveData<Department> getUserInfo();
    }

}
