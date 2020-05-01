package com.hecvd19.pasdept.home.bottomsheet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.hecvd19.pasdept.R;

import java.util.ArrayList;

class PinCodeAdapter extends RecyclerView.Adapter<PinCodeAdapter.PincodeRecycler> {

    ArrayList<String> mPincodeList = new ArrayList<>();
    RemovePinCodeListener listener;

    interface RemovePinCodeListener {
        void removePinCode(int position);
    }

    PinCodeAdapter(ArrayList<String> pincodes, RemovePinCodeListener codeListener) {
        mPincodeList = pincodes;
        this.listener = codeListener;
    }

    @NonNull
    @Override
    public PincodeRecycler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pincode_list, parent, false);

        return new PincodeRecycler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PincodeRecycler holder, final int position) {
        holder.chip.setText(mPincodeList.get(position));

        holder.chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.removePinCode(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPincodeList.size();
    }

    public class PincodeRecycler extends RecyclerView.ViewHolder {

        private Chip chip;

        public PincodeRecycler(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.pinCodeChip);
        }
    }
}
