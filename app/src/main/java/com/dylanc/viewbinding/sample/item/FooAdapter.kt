package com.dylanc.viewbinding.sample.item

import com.dylanc.viewbinding.BindingViewHolder
import com.dylanc.viewbinding.sample.base.kotlin.BaseBindingAdapter
import com.dylanc.viewbinding.sample.databinding.ItemFooBinding


class FooAdapter(var list: List<Foo>) : BaseBindingAdapter<ItemFooBinding, Foo>() {

  override fun onBindViewHolder(holder: BindingViewHolder<ItemFooBinding>, position: Int) {
    holder.binding.apply {
      foo.text = list[position].value
    }
  }

  override fun getItemCount() = list.size
}