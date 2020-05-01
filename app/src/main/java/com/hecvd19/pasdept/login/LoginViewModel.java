package com.hecvd19.pasdept.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hecvd19.pasdept.models.Department;

public class LoginViewModel extends ViewModel {

    LoginRepository repository;

    public LoginViewModel() {
        repository = new LoginRepository();
    }

    LiveData<Department> loginUser(String email, String password) {

        return repository.loginUser(email, password);
    }

}
