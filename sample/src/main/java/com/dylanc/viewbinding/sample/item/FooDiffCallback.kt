package com.dylanc.viewbinding.sample.item

import androidx.recyclerview.widget.DiffUtil

class FooDiffCallback : DiffUtil.ItemCallback<Foo>() {
  override fun areItemsTheSame(oldItem: Foo, newItem: Foo) = oldItem.value == newItem.value
  override fun areContentsTheSame(oldItem: Foo, newItem: Foo) = oldItem.value == newItem.value
}