package com.hecvd19.pasdept.register;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hecvd19.pasdept.R;
import com.hecvd19.pasdept.models.Department;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    //vars
    RegisterViewModel viewModel;

    //widgets
    private TextInputEditText mDeptId, mDeptName, mDeptLogo, mDeptEmail, mPassword;
    private MaterialButton mRegister;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDeptId = findViewById(R.id.txtDepartmentId);
        mDeptEmail = findViewById(R.id.txtEmail);
        mPassword = findViewById(R.id.txtPassword);
        mDeptName = findViewById(R.id.txtDepartmentName);
        mRegister = findViewById(R.id.register);
        mProgressBar = findViewById(R.id.progressBar);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validInputsEntered()) {
                    registerUser();
                }
            }
        });

    }

    private void registerUser() {

        mProgressBar.setVisibility(View.VISIBLE);
        disableViews(mDeptEmail, mDeptName, mPassword, mDeptId, mRegister);
        viewModel.registerUser(mDeptEmail.getText().toString(), mPassword.getText().toString()).observe(this, new Observer<Department>() {
            @Override
            public void onChanged(Department department) {

                if (department.getDepartmentUserId() != null) {
                    saveUser(department);
                } else {
                    Toast.makeText(RegisterActivity.this, "Error registering account!\nPlease try again!", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    enableViews(mDeptEmail, mDeptName, mPassword, mDeptId, mRegister);
                }
            }
        });


    }


    private void saveUser(Department department) {

        department.setApproved(false);
        department.setDepartmentLogo("");
        department.setDepartmentId(mDeptId.getText().toString());
        department.setDepartmentName(mDeptName.getText().toString());
        department.setEmailId(mDeptEmail.getText().toString());
        mProgressBar.setVisibility(View.VISIBLE);
        disableViews(mDeptEmail, mDeptName, mPassword, mDeptId, mRegister);
        viewModel.saveUser(department).observe(this, new Observer<Department>() {
            @Override
            public void onChanged(Department department) {
                if (department != null) {
                    Toast.makeText(RegisterActivity.this, "User registered succesfully! You will be able to login once admin approves the account!", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    enableViews(mDeptEmail, mDeptName, mPassword, mDeptId, mRegister);
                } else {
                    Toast.makeText(RegisterActivity.this, "Error registering account!\nPlease try again!", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    enableViews(mDeptEmail, mDeptName, mPassword, mDeptId, mRegister);
                }
            }
        });


    }

    private boolean validInputsEntered() {
        boolean isInputValid = true;
        if (!(mDeptId.getText().toString().length() == 4)) {
            mDeptId.setError("Enter valid ID! (4)");
            isInputValid = false;
        }
        if (!(Patterns.EMAIL_ADDRESS.matcher(mDeptEmail.getText().toString()).matches())) {
            mDeptEmail.setError("Invalid Email ID");
            isInputValid = false;
        }
        if (!(mPassword.getText().toString().length() > 5)) {
            mDeptId.setError("Password must be 6 characters long.");
            isInputValid = false;
        }
        if (!(mDeptName.getText().toString().length() > 2)) {
            mDeptId.setError("Must be at least 3 characters long!");
            isInputValid = false;
        }

        return isInputValid;
    }

    void enableViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }


    void disableViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }


}
