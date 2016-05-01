package io.dwak.freight.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;

import io.dwak.Extra;

public class SecondController extends Controller  {
  @Extra int integerExtra;

  public SecondController(Bundle args){

  }

  @NonNull
  @Override
  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
    return null;
  }
}
