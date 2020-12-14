package com.dylanc.viewbinding.sample.base.java;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.dylanc.viewbinding.ViewBindingUtil;

public abstract class BaseBindingDialog<VB extends ViewBinding> extends Dialog {

  private VB binding;

  public BaseBindingDialog(@NonNull Context context) {
    this(context, 0);
  }

  public BaseBindingDialog(@NonNull Context context, int themeResId) {
    super(context, themeResId);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ViewBindingUtil.inflateWithGeneric(this, getLayoutInflater());
    setContentView(binding.getRoot());
  }

  public VB getBinding() {
    return binding;
  }
}
