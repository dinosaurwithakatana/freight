package io.dwak.freight_navigator;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;

import io.dwak.freight.annotation.ControllerBuilder;

@ControllerBuilder(value = "WelcomeScreen",
        scope = "Welcome",
        pushChangeHandler = FadeChangeHandler.class)
public class WelcomeController extends Controller {
    public WelcomeController() {
    }

    public WelcomeController(Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return null;
    }
}
