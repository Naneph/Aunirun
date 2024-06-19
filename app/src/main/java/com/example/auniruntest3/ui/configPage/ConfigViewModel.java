package com.example.auniruntest3.ui.configPage;

import android.util.Patterns;

import androidx.lifecycle.ViewModel;

public class ConfigViewModel extends ViewModel {













    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.length()==11) {
            return Patterns.PHONE.matcher(username).matches();
        } else {
            return username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 3;
    }
}
