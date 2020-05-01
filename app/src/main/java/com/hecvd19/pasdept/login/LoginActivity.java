package com.hecvd19.pasdept.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.hecvd19.pasdept.home.MainActivity;
import com.hecvd19.pasdept.models.Department;
import com.hecvd19.pasdept.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements TextWatcher {
    private static final String TAG = "LoginActivity";

    //widgets
    private TextInputEditText mEmail, mPassword;
    private MaterialButton mLogin, mRegister;
    private ProgressBar mProgressBar;

    //vars
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.txtEmail);
        mPassword = findViewById(R.id.txtPassword);
        mLogin = findViewById(R.id.login);
        mRegister = findViewById(R.id.register);
        mProgressBar = findViewById(R.id.progressBar);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        mEmail.addTextChangedListener(this);
        mPassword.addTextChangedListener(this);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disableViews(mEmail, mPassword, mLogin, mRegister);

                loginViewModel.loginUser(mEmail.getText().toString(), mPassword.getText().toString()).observe(LoginActivity.this, new Observer<Department>() {
                    @Override
                    public void onChanged(Department department) {

                        if (department != null) {
                            if (department.isApproved()) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                enableViews(mEmail, mPassword, mLogin, mRegister);
                            } else {
                                Toast.makeText(LoginActivity.this, "Account is not approved by admin!", Toast.LENGTH_SHORT).show();
                                enableViews(mEmail, mPassword, mLogin, mRegister);

                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Unable to login! Please try after some time.", Toast.LENGTH_SHORT).show();
                            enableViews(mEmail, mPassword, mLogin, mRegister);
                        }

                    }
                });


            }
        });


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
            mEmail.setError("Invalid email");
            mLogin.setEnabled(false);
        } else if (!(mPassword.getText().toString().length() > 5)) {
            mPassword.setError("Must be 6 characters long");
            mLogin.setEnabled(false);
        } else {
            mLogin.setEnabled(true);
        }

    }

    void enableViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
        mProgressBar.setVisibility(View.GONE);
    }


    void disableViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
        mProgressBar.setVisibility(View.VISIBLE);
    }

}
