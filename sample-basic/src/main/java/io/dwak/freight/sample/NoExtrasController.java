package io.dwak.freight.sample;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;

import io.dwak.freight.annotation.ControllerBuilder;

@ControllerBuilder
public class NoExtrasController extends Controller{
  @NonNull
  @Override
  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
    return null;
  }
}
