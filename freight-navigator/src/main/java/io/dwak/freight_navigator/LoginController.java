package io.dwak.freight_navigator;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;

import io.dwak.freight.annotation.ControllerBuilder;
import io.dwak.freight.annotation.Extra;

@ControllerBuilder(value = "Login", scope = "Welcome")
public class LoginController extends Controller{
    @Extra
    String username;
    @Nullable @Extra String password;
    public LoginController() {
    }

    public LoginController(Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return null;
    }
}
