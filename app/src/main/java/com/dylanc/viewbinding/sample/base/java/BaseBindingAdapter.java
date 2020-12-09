package com.dylanc.viewbinding.sample.base.java;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.dylanc.viewbinding.BindingViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseBindingAdapter<VB extends ViewBinding, T> extends RecyclerView.Adapter<BindingViewHolder<VB>> {

  private List<T> list = new ArrayList<>();

  public void setList(List<T> list) {
    this.list = list;
  }

  @NonNull
  @Override
  public BindingViewHolder<VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return BindingViewHolder.createWithGeneric(this, parent);
  }

  @Override
  public void onBindViewHolder(@NonNull BindingViewHolder<VB> holder, int position) {
    onBindViewHolder(holder, list.get(position));
  }

  public abstract void onBindViewHolder(@NonNull BindingViewHolder<VB> holder, T item);

  @Override
  public int getItemCount() {
    return list.size();
  }
}
