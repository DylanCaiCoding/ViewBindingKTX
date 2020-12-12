package com.dylanc.viewbinding.sample.item

import com.dylanc.viewbinding.BindingViewHolder
import com.dylanc.viewbinding.sample.base.java.BindingViewDelegate
import com.dylanc.viewbinding.sample.databinding.ItemFooBinding

class FooViewDelegate : BindingViewDelegate<Foo, ItemFooBinding>() {

  override fun onBindViewHolder(holder: BindingViewHolder<ItemFooBinding>, item: Foo) {
    holder.binding.foo.text = item.value
  }
}

//class FooViewDelegate : ItemViewDelegate<Foo, FooViewDelegate.ViewHolder>() {
//
//  override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
//    return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_foo, parent, false))
//  }
//
//  override fun onBindViewHolder(holder: ViewHolder, item: Foo) {
//    holder.fooView.text = item.value
//  }
//
//  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    val fooView: TextView = itemView.findViewById(R.id.foo)
//  }
//}