package com.hecvd19.pasdept.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hecvd19.pasdept.models.Department;

public class RegisterViewModel extends ViewModel {
    private RegisterRepository repository;

    public RegisterViewModel() {
        repository = new RegisterRepository();
    }

    LiveData<Department> registerUser(String emailID, String password) {

        return repository.registerUser(emailID, password);
    }

    LiveData<Department> saveUser(Department department) {

        return repository.saveUser(department);
    }

}
