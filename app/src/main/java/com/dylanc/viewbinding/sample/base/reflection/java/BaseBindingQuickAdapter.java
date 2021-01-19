package com.dylanc.viewbinding.sample.base.reflection.java;

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.viewbinding.ViewBinding;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dylanc.viewbinding.base.ViewBindingUtil;

import org.jetbrains.annotations.NotNull;

public abstract class BaseBindingQuickAdapter<T, VB extends ViewBinding>
    extends BaseQuickAdapter<T, BaseBindingQuickAdapter.BaseBindingHolder<VB>> {

  public BaseBindingQuickAdapter() {
    this(-1);
  }

  public BaseBindingQuickAdapter(@LayoutRes int layoutResId) {
    super(layoutResId);
  }

  @NotNull
  @Override
  protected BaseBindingHolder<VB> onCreateDefViewHolder(@NotNull ViewGroup parent, int viewType) {
    return new BaseBindingHolder<>(ViewBindingUtil.inflateWithGeneric(this, parent));
  }

  @Override
  protected void convert(@NotNull BaseBindingHolder<VB> holder, T item) {
    convert(holder.getViewBinding(), holder.getLayoutPosition(), item);
  }

  protected abstract void convert(VB binding, int layoutPosition, T item);


  public static class BaseBindingHolder<B extends ViewBinding> extends BaseViewHolder {

    private final B binding;

    public BaseBindingHolder(@NotNull B binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public B getViewBinding() {
      return binding;
    }
  }
}
