/*
 * Copyright (c) 2020. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dylanc.viewbinding.sample.base.reflection.java;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.drakeet.multitype.ItemViewDelegate;
import com.dylanc.viewbinding.base.ViewBindingUtil;

import org.jetbrains.annotations.NotNull;

/**
 * @author Dylan Cai
 */
public abstract class BindingViewDelegate<T, VB extends ViewBinding> extends
    ItemViewDelegate<T, BindingViewDelegate.BindingViewHolder<VB>> {

  @NotNull
  @Override
  public BindingViewHolder<VB> onCreateViewHolder(@NotNull Context context, @NotNull ViewGroup parent) {
    return new BindingViewHolder<>(ViewBindingUtil.inflateWithGeneric(this, parent));
  }

  @Override
  public void onBindViewHolder(@NonNull BindingViewHolder<VB> holder, T t) {
    onBindViewHolder(holder.getBinding(), t, holder.getAdapterPosition());
  }

  abstract void onBindViewHolder(VB binding, T item, int position);

  public static class BindingViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {

    private final VB binding;

    public BindingViewHolder(@NonNull VB binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    @NonNull
    public VB getBinding() {
      return binding;
    }
  }
}