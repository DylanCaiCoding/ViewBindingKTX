package com.dylanc.viewbinding.sample.base.reflection.java;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.dylanc.viewbinding.BindingViewHolder;
import com.dylanc.viewbinding.base.ViewBindingUtil;

public abstract class BaseBindingAdapter<VB extends ViewBinding> extends RecyclerView.Adapter<BindingViewHolder<VB>> {

  @NonNull
  @Override
  public BindingViewHolder<VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new BindingViewHolder<>(ViewBindingUtil.inflateWithGeneric(this, parent));
  }
}
