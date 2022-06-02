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

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dylanc.viewbinding.base.ViewBindingUtil;

import org.jetbrains.annotations.NotNull;

public abstract class BaseBindingQuickAdapter<T, VB extends ViewBinding>
    extends BaseQuickAdapter<T, BaseBindingQuickAdapter.BaseBindingHolder> {

  public BaseBindingQuickAdapter() {
    this(-1);
  }

  public BaseBindingQuickAdapter(@LayoutRes int layoutResId) {
    super(layoutResId);
  }

  @NotNull
  @Override
  protected BaseBindingHolder onCreateDefViewHolder(@NotNull ViewGroup parent, int viewType) {
    VB viewBinding = ViewBindingUtil.inflateWithGeneric(this, parent);
    return new BaseBindingHolder(viewBinding);
  }

  public static class BaseBindingHolder extends BaseViewHolder {

    private final ViewBinding binding;

    public BaseBindingHolder(@NotNull View view) {
      this(() -> view);
    }

    public BaseBindingHolder(@NotNull ViewBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public <VB extends ViewBinding> VB getViewBinding() {
      return (VB) binding;
    }
  }
}