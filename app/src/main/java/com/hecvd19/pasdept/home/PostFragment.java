package com.hecvd19.pasdept.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.hecvd19.pasdept.R;
import com.hecvd19.pasdept.home.bottomsheet.AddPostBottomSheet;
import com.hecvd19.pasdept.models.Department;
import com.hecvd19.pasdept.models.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostFragment extends Fragment implements PostAdapter.IntentListener {
    private static final String TAG = "PostFragment";

    //var
    private List<Post> postList = new ArrayList<>();
    private RecyclerView postRecyclerView;
    private PostAdapter postAdapter;
    private PostViewModel postViewModel;
    private boolean isScrolling;
    private String DEPT_ID;
    private FloatingActionButton mFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        postRecyclerView = view.findViewById(R.id.rvPostsList);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFab = view.findViewById(R.id.fab);
        initProductsAdapter();
        initProductListViewModel();
        initRecyclerViewOnScrollListener();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddPostBottomSheet.class));
            }
        });


        return view;
    }


    private void initProductsAdapter() {
        postAdapter = new PostAdapter(postList, this);
        postRecyclerView.setAdapter(postAdapter);
    }

    private void initProductListViewModel() {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.getUserInfo().observe(Objects.requireNonNull(getActivity()), new Observer<Department>() {
            @Override
            public void onChanged(Department department) {
                if (department != null) {
                    DEPT_ID = department.getDepartmentId();
                    Log.d(TAG, "onChanged: initProductListViewModel :" + DEPT_ID);
                    getMyPosts(DEPT_ID);

                } else {
                    Toast.makeText(getContext(), "Unexpected Error occurred!", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    getActivity().finish();
                }
            }
        });
    }


    private void addPost(Post addedPost) {
        if (!postList.contains(addedPost)) {
            postList.add(addedPost);
        }
    }

    private void modifyPost(Post modifiedPost) {
        for (int i = 0; i < postList.size(); i++) {
            Post currentPost = postList.get(i);
            if (currentPost.getPostId().equals(modifiedPost.getPostId())) {
                postList.remove(currentPost);
                postList.add(i, modifiedPost);
            }
        }
    }

    private void removePost(Post removedPost) {
        for (int i = 0; i < postList.size(); i++) {
            Post currentPost = postList.get(i);
            if (currentPost.getPostId().equals(removedPost.getPostId())) {
                postList.remove(currentPost);
            }
        }
    }

    private void getMyPosts(String deptId) {

        PostListLiveData postListLiveData = postViewModel.getMyPosts(deptId);
        if (postListLiveData != null) {
            postListLiveData.observe(Objects.requireNonNull(getActivity()), new Observer<Operation>() {
                @Override
                public void onChanged(Operation operation) {
                    Log.d(TAG, "getPosts: operation :" + operation.post.toString());
                    switch (operation.type) {
                        case R.string.added:
                            Post addedProduct = operation.post;
                            PostFragment.this.addPost(addedProduct);
                            break;

                        case R.string.modified:
                            Post modifiedPost = operation.post;
                            PostFragment.this.modifyPost(modifiedPost);
                            break;

                        case R.string.removed:
                            Post removedPost = operation.post;
                            PostFragment.this.removePost(removedPost);
                    }
                    postAdapter.notifyDataSetChanged();
                }
            });
        }
    }


    private void initRecyclerViewOnScrollListener() {
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                if (layoutManager != null) {
                    int firstVisiblePostPosition = layoutManager.findFirstVisibleItemPosition();
                    int visiblePostCount = layoutManager.getChildCount();
                    int totalPostCount = layoutManager.getItemCount();
                    if (isScrolling && (firstVisiblePostPosition + visiblePostCount == totalPostCount)) {
                        isScrolling = false;
                        getMyPosts(DEPT_ID);
                    }
                }
            }
        };
        postRecyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void startIntent(Intent intent) {
        startActivity(intent);
    }

}
