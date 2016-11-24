package com.bluelinelabs.conductor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class Controller {
    public Controller() {
        this(null);
    }

    public Controller(Bundle bundle) {
    }

    public Bundle getArgs() {
        return Bundle.EMPTY;
    }

    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return null;
    }
}
