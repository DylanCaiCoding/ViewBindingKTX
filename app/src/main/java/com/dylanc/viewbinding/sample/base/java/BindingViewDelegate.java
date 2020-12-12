package com.dylanc.viewbinding.sample.base.java;

import android.content.Context;
import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;

import com.drakeet.multitype.ItemViewDelegate;
import com.dylanc.viewbinding.BindingViewHolder;

import org.jetbrains.annotations.NotNull;

public abstract class BindingViewDelegate<T, VB extends ViewBinding> extends ItemViewDelegate<T, BindingViewHolder<VB>> {
  @NotNull
  @Override
  public BindingViewHolder<VB> onCreateViewHolder(@NotNull Context context, @NotNull ViewGroup parent) {
    return BindingViewHolder.createWithGeneric(this, parent);
  }
}
