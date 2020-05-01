package com.hecvd19.pasdept.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hecvd19.pasdept.R;
import com.hecvd19.pasdept.home.bottomsheet.BottomSheetViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewBottomSheet extends BottomSheetDialogFragment implements ListViewRecyclerAdapter.ListItemClickListener {

    private int LIST_TYPE;

    public interface SendListener {
        void sendState(String text);

        void sendDistrict(String text);
    }

    private List<String> list = new ArrayList<>();
    private SendListener listener;

    public ListViewBottomSheet(int textType, List<String> list, SendListener listener) {
        LIST_TYPE = textType;
        this.list = list;
        this.listener = listener;
    }

    private BottomSheetViewModel sheetViewModel;
    private RecyclerView recyclerView;
    private TextView mTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_view, container, false);
        recyclerView = view.findViewById(R.id.list_view);
        mTitle = view.findViewById(R.id.titleText);
        sheetViewModel = new ViewModelProvider(this).get(BottomSheetViewModel.class);
        mTitle.setText(String.format(Locale.US, "Select %s", getString(LIST_TYPE)));
        setUpRecyclerView();
        return view;

    }

    private void setUpRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(manager);
        ListViewRecyclerAdapter adapter = new ListViewRecyclerAdapter(list, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void sendText(String text) {
        if (LIST_TYPE == R.string.state) {
            listener.sendState(text);
        } else {
            listener.sendDistrict(text);
        }
        dismiss();
    }
}
