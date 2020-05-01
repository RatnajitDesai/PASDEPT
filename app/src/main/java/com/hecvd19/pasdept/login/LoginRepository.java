package com.hecvd19.pasdept.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hecvd19.pasdept.models.Department;
import com.hecvd19.pasdept.utils.Constants;

public class LoginRepository {

    private static final String TAG = "LoginRepository";

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    LoginRepository() {

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<Department> loginUser(String email, String password) {

        final MutableLiveData<Department> data = new MutableLiveData<>();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        firestore.collection(Constants.COL_DEPARTMENTS)
                                .document(authResult.getUser().getUid())
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                Department department = documentSnapshot.toObject(Department.class);
                                data.postValue(department);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                data.setValue(null);
                            }
                        });
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
}
