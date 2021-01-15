package com.dylanc.viewbinding.sample.item

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dylanc.viewbinding.brvah.getViewBinding
import com.dylanc.viewbinding.brvah.withBinding
import com.dylanc.viewbinding.sample.R
import com.dylanc.viewbinding.sample.base.reflection.kotlin.BaseBindingQuickAdapter
import com.dylanc.viewbinding.sample.databinding.ItemFooBinding


//class FooAdapter(var list: List<Foo>) : RecyclerView.Adapter<BindingViewHolder<ItemFooBinding>>() {
//
//  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
//    BindingViewHolder(ItemFooBinding::inflate, parent)
//
//  override fun onBindViewHolder(holder: BindingViewHolder<ItemFooBinding>, position: Int) {
//    holder.binding.apply {
//      foo.text = list[position].value
//    }
//  }
//
//  override fun getItemCount() = list.size
//}

class FooAdapter : BaseQuickAdapter<Foo, BaseViewHolder>(R.layout.item_foo) {

  override fun createBaseViewHolder(view: View): BaseViewHolder {
    return super.createBaseViewHolder(view).withBinding(ItemFooBinding::bind)
  }

  override fun convert(holder: BaseViewHolder, item: Foo) {
    holder.getViewBinding<ItemFooBinding>()
      .apply {
        foo.text = item.value
      }
  }

}

//class FooAdapter : BaseBindingQuickAdapter<Foo, ItemFooBinding>() {
//
//  override fun convert(binding: ItemFooBinding, item: Foo) {
//    binding.apply {
//      foo.text = item.value
//    }
//  }
//
//}