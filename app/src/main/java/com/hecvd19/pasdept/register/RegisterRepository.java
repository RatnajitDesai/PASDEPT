package com.hecvd19.pasdept.register;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hecvd19.pasdept.models.Department;
import com.hecvd19.pasdept.utils.Constants;

public class RegisterRepository {

    private static final String TAG = "RegisterRepository";
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    RegisterRepository() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }


    public MutableLiveData<Department> registerUser(final String emailId, String password) {

        final MutableLiveData<Department> data = new MutableLiveData<>();

        mAuth.createUserWithEmailAndPassword(emailId, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                if (authResult.getUser() != null) {
                    Department department = new Department();
                    department.setDepartmentUserId(authResult.getUser().getUid());
                    data.setValue(department);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
                data.setValue(null);
            }
        });

        return data;
    }

    public MutableLiveData<Department> saveUser(final Department department) {

        final MutableLiveData<Department> data = new MutableLiveData<>();

        firestore.collection(Constants.COL_DEPARTMENTS)
                .document(department.getDepartmentUserId())
                .set(department).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                data.postValue(department);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                data.postValue(null);
            }
        });

        return data;
    }
}
