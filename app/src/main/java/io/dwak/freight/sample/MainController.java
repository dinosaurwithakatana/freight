package io.dwak.freight.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;

import java.util.Date;

import io.dwak.Extra;
import io.dwak.freight.Freight;

public class MainController extends Controller{
  @Extra String testStringExtra;
  @Extra int testIntExtra;
  @Extra char testCharExtra;
  @Extra Bundle testBundleExtra;
  @Extra byte testByteExtra;
  @Extra CharSequence testCharSequenceExtra;
  @Nullable @Extra float testFloat;
  @Extra short testShortExtra;
  @Extra("booleanExtraYo") boolean testBooleanExtra;
  @Extra ParcelableModel testParcelable;
  @Extra Date testSerializable;
  @Nullable @Extra float[] testFloatArray;
  @Extra char[] testCharArray;
  @Extra byte[] testByteArray;
  @Extra CharSequence[] testCharSequenceArray;
  private String myStringExtra;

  public MainController(Bundle args) {
    super(args);
  }

  @NonNull
  @Override
  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
    Freight.ship(this);

    return null;
  }

  @Extra
  void setMyStringExtra(String myStringExtra){
    this.myStringExtra = myStringExtra;
  }
}
