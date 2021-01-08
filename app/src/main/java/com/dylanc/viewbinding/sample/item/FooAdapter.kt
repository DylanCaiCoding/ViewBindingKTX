package com.dylanc.viewbinding.sample.item

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dylanc.viewbinding.BindingViewHolder
import com.dylanc.viewbinding.sample.databinding.ItemFooBinding


class FooAdapter(var list: List<Foo>) : RecyclerView.Adapter<BindingViewHolder<ItemFooBinding>>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    BindingViewHolder(ItemFooBinding::inflate, parent)

  override fun onBindViewHolder(holder: BindingViewHolder<ItemFooBinding>, position: Int) {
    holder.binding.apply {
      foo.text = list[position].value
    }
  }

  override fun getItemCount() = list.size
}