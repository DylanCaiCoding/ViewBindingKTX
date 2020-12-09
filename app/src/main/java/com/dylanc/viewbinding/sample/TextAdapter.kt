package com.dylanc.viewbinding.sample

import android.view.ViewGroup
import com.dylanc.viewbinding.BindingViewHolder
import com.dylanc.viewbinding.newBindingViewHolder
import com.dylanc.viewbinding.sample.base.java.BaseBindingAdapter
import com.dylanc.viewbinding.sample.databinding.ItemTextBinding


class TextAdapter : BaseBindingAdapter<ItemTextBinding, String>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    newBindingViewHolder<ItemTextBinding>(parent)

  override fun onBindViewHolder(holder: BindingViewHolder<ItemTextBinding>, item: String) {
    holder.binding.apply {
      tvContent.text = item
    }
  }
}