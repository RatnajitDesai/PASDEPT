package com.hecvd19.pasdept.home.bottomsheet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.hecvd19.pasdept.R;
import com.hecvd19.pasdept.models.Post;
import com.hecvd19.pasdept.utils.ListViewBottomSheet;
import com.hecvd19.pasdept.utils.Permissions;
import com.hecvd19.pasdept.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AddPostBottomSheet extends AppCompatActivity implements View.OnClickListener, PinCodeAdapter.RemovePinCodeListener, AttachmentAdapter.RemoveAttachmentListener, ListViewBottomSheet.SendListener {

    private static final String TAG = "AddPostBottomSheet";
    private static final int GET_IMAGE = 2001;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1001;

    private TextInputEditText mPostMessage, mPinCode, mDistrict, mState;
    private MaterialButton mAddPinCode, mSubmit, mBtnAttachment;
    private RadioGroup rgPriority, rgLocationFilter;
    private RecyclerView mAddPincodes, mAddAttachments;
    private LinearLayout mPinCodeLayout, mDistrictStateLayout;
    private RadioButton rbState, rbDistrict;

    //vars
    ArrayList<String> pinCodeList = new ArrayList<>();
    ArrayList<String> attachmentList = new ArrayList<>();
    ArrayList<Uri> attachmentUri = new ArrayList<>();
    PinCodeAdapter pinCodeAdapter;
    AttachmentAdapter attachmentAdapter;
    BottomSheetViewModel bottomSheetViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_sheet_fragment);
        mPostMessage = findViewById(R.id.postMessage);
        mPinCode = findViewById(R.id.pinCode);
        mDistrict = findViewById(R.id.district);
        mState = findViewById(R.id.state);
        mAddPinCode = findViewById(R.id.btnAddPincode);
        mSubmit = findViewById(R.id.btnSubmit);
        rgLocationFilter = findViewById(R.id.rgLocationFilter);
        rbState = findViewById(R.id.rbState);
        rbDistrict = findViewById(R.id.rbDistrict);

        rgPriority = findViewById(R.id.rgPriority);
        mAddPincodes = findViewById(R.id.rvPincodes);
        mAddAttachments = findViewById(R.id.rvAttachment);
        mBtnAttachment = findViewById(R.id.btnAddAttachment);
        mPinCodeLayout = findViewById(R.id.locationLayoutByPincode);
        mDistrictStateLayout = findViewById(R.id.locationLayoutByDistrictAndState);
        initializeRecyclerViews();
        intializeViewModel();

        mSubmit.setOnClickListener(this);
        mAddPinCode.setOnClickListener(this);
        mBtnAttachment.setOnClickListener(this);
        rbState.setOnClickListener(this);
        rbDistrict.setOnClickListener(this);
        mState.setOnClickListener(this);
        mDistrict.setOnClickListener(this);

        rgLocationFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rbPincode: {
                        mPinCodeLayout.setVisibility(View.VISIBLE);
                        mDistrictStateLayout.setVisibility(View.GONE);
                        break;
                    }
                    case R.id.rbDistrict: {
                        mPinCodeLayout.setVisibility(View.GONE);
                        mDistrictStateLayout.setVisibility(View.VISIBLE);
                        mDistrict.setVisibility(View.VISIBLE);
                        mState.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.rbState: {
                        mPinCodeLayout.setVisibility(View.GONE);
                        mDistrictStateLayout.setVisibility(View.VISIBLE);
                        mDistrict.setVisibility(View.GONE);
                        mState.setVisibility(View.VISIBLE);
                        break;
                    }

                }


            }
        });


    }

    private void intializeViewModel() {

        bottomSheetViewModel = new ViewModelProvider(this).get(BottomSheetViewModel.class);
    }

    private void initializeRecyclerViews() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mAddPincodes.setLayoutManager(manager);
        pinCodeAdapter = new PinCodeAdapter(pinCodeList, this);
        mAddPincodes.setAdapter(pinCodeAdapter);

        LinearLayoutManager attachmentManager = new LinearLayoutManager(this);
        mAddAttachments.setLayoutManager(attachmentManager);
        attachmentAdapter = new AttachmentAdapter(attachmentList, this);
        mAddAttachments.setAdapter(attachmentAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbDistrict:

            case R.id.district: {
                if (mState.getText().toString().isEmpty()) {
                    openBottomSheet(R.string.state, Utils.getStates());
                } else {
                    openBottomSheet(R.string.district, Utils.getDistricts(mState.getText().toString()));
                }
                break;
            }
            case R.id.rbState:
            case R.id.state: {
                openBottomSheet(R.string.state, Utils.getStates());
                break;
            }
            case R.id.btnSubmit: {
                if (checkValidInputs()) {
                    uploadPost();
                }
                break;
            }
            case R.id.btnAddPincode: {
                if (mPinCode.getText().toString().length() == 6) {
                    addPincode(mPinCode.getText().toString());
                } else {
                    mPinCode.setError("Invalid Pin Code!");
                }
                break;
            }
            case R.id.btnAddAttachment: {
                if (checkPermissionsArray(Permissions.PERMISSIONS)) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, GET_IMAGE);
                    break;
                } else {
                    verifyPermissions(Permissions.PERMISSIONS);
                }
            }

        }
    }


    private void openBottomSheet(int type, ArrayList<String> list) {
        ListViewBottomSheet sheet = new ListViewBottomSheet(type, list, this);
        sheet.show(getSupportFragmentManager(), "ListBottomSheet");
    }

    private void uploadPost() {

        Log.d(TAG, "uploadPost: ");
        bottomSheetViewModel.uploadAttachments(attachmentUri).observe(this, new Observer<ArrayList<String>>() {
                    @Override
                    public void onChanged(ArrayList<String> strings) {

                        if (strings != null) {
                            Post post = new Post();
                            switch (rgLocationFilter.getCheckedRadioButtonId()) {
                                case R.id.rbPincode: {
                                    post.setPinCodes(pinCodeList);

                                    break;
                                }
                                case R.id.rbDistrict: {
                                    post.setDistrict(mDistrict.getText().toString());
                                    post.setState(mState.getText().toString());
                                    break;
                                }
                                case R.id.rbState: {
                                    post.setState(mState.getText().toString());
                                    break;
                                }
                            }

                            post.setPostAttachments(strings);

                            switch (rgPriority.getCheckedRadioButtonId()) {
                                case R.id.rbPriorityHigh: {
                                    post.setPriority("1");
                                    break;
                                }
                                case R.id.rbPriorityLow: {
                                    post.setPriority("0");
                                    break;
                                }
                            }
                            post.setPostMessage(mPostMessage.getText().toString());
                            post.setTimestamp(new Timestamp(new Date()));
                            Log.d(TAG, "onChanged: " + post.toString());
                            bottomSheetViewModel.uploadPost(post).observe(AddPostBottomSheet.this, new Observer<Post>() {
                                @Override
                                public void onChanged(Post post) {
                                    if (post != null) {
                                        Toast.makeText(AddPostBottomSheet.this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(AddPostBottomSheet.this, "Post upload failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(AddPostBottomSheet.this, "Error uploading documents!", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
        );
    }

    private void addAttachment(Uri uri, String string, long aLong) {

        attachmentList.add(string);
        attachmentUri.add(uri);
        attachmentAdapter.notifyDataSetChanged();
    }

    private void addPincode(String pinCode) {
        pinCodeList.add(pinCode);
        pinCodeAdapter.notifyDataSetChanged();
    }


    private boolean checkValidInputs() {
        boolean isValidInput = true;

        if (mPostMessage.getText().toString().isEmpty()) {
            isValidInput = false;
            mPostMessage.setError("invalid input!");
        }

        switch (rgLocationFilter.getCheckedRadioButtonId()) {
            case R.id.rbPincode: {
                if (mPinCode.getText().toString().isEmpty() || mAddPincodes.getChildCount() < 1) {
                    isValidInput = false;
                    mPinCode.setError("invalid input!");
                }
                break;
            }
            case R.id.rbDistrict: {
                if (mDistrict.getText().toString().isEmpty()) {
                    isValidInput = false;
                    mDistrict.setError("invalid input!");
                }
                if (mState.getText().toString().isEmpty()) {
                    isValidInput = false;
                    mState.setError("invalid input!");
                }
                break;
            }
            case R.id.rbState: {
                if (mState.getText().toString().isEmpty()) {
                    isValidInput = false;
                    mState.setError("invalid input!");
                }
                break;
            }
        }


        return isValidInput;

    }

    @Override
    public void removePinCode(int position) {
        pinCodeList.remove(position);
        pinCodeAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeAttachment(int position) {
        attachmentList.remove(position);
        attachmentAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == GET_IMAGE) {
            if (intent != null) {
                Uri uri = intent.getData();

                if (uri != null) {
                    Cursor cursor =
                            getApplicationContext().getContentResolver().query(uri, null, null, null, null);
                    /*
                     * Get the column indexes of the data in the Cursor,
                     * move to the first row in the Cursor, get the data,
                     * and display it.
                     */
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    cursor.moveToFirst();
                    addAttachment(uri, cursor.getString(nameIndex), cursor.getLong(sizeIndex));
                    cursor.close();
                } else {
                    Toast.makeText(AddPostBottomSheet.this, "Unable to load attachment.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    /**
     * Verify all the @param permissions passed to the array
     *
     * @param permissions - CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
     */
    private void verifyPermissions(String[] permissions) {
        Log.d(TAG, "verifyPermissions: verifying permissions");
        ActivityCompat.requestPermissions(
                Objects.requireNonNull(this),
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    /**
     * check permissions array
     *
     * @param permissions - CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
     * @return permission granted or not
     */

    private boolean checkPermissionsArray(String[] permissions) {
        Log.d(TAG, "checkPermissionsArray: Checking permissions array.");
        for (String check : permissions) {
            if (!checkPermissions(check)) {
                return false;
            }
        }
        return true;
    }

    /**
     * check individual permission if it has been granted
     *
     * @param permission - single permission
     * @return permission granted or not
     */
    private boolean checkPermissions(String permission) {
        Log.d(TAG, "checkPermissions: checking individual permission");
        int permissionRequest = ActivityCompat.checkSelfPermission(Objects.requireNonNull(this), permission);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: \n permission was not granted for " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermissions: \n permission was granted for " + permission);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void sendState(String text) {
        mState.setText(text);
        if (rgLocationFilter.getCheckedRadioButtonId() == R.id.rbDistrict) {
            openBottomSheet(R.string.district, Utils.getDistricts(text));
        }
    }

    @Override
    public void sendDistrict(String text) {
        mDistrict.setText(text);
    }
}
