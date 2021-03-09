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

package com.dylanc.viewbinding.sample.item

import com.dylanc.viewbinding.sample.base.nonreflection.kotlin.BaseBindingQuickAdapter
import com.dylanc.viewbinding.sample.databinding.ItemFooBinding


//class FooAdapter(var list: List<Foo>) : RecyclerView.Adapter<BindingViewHolder<ItemFooBinding>>() {
//
//  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
//    BindingViewHolder(ItemFooBinding::inflate, parent)
//
//  override fun onBindViewHolder(holder: BindingViewHolder<ItemFooBinding>, position: Int) {
//    holder.binding.apply {
//      tvFoo.text = list[position].value
//    }
//  }
//
//  override fun getItemCount() = list.size
//}

//class FooAdapter : BaseQuickAdapter<Foo, BaseViewHolder>(R.layout.item_foo) {
//
//  override fun createBaseViewHolder(view: View): BaseViewHolder {
//    return super.createBaseViewHolder(view).withBinding { ItemFooBinding.bind(it) }
//  }
//
//  override fun convert(holder: BaseViewHolder, item: Foo) {
//    holder.getViewBinding<ItemFooBinding>()
//      .apply {
//        tvFoo.text = item.value
//      }
//  }
//
//}

class FooAdapter : BaseBindingQuickAdapter<Foo, ItemFooBinding>(ItemFooBinding::inflate) {

  override fun convert(holder: BaseBindingHolder<ItemFooBinding>, item: Foo) {
    holder.binding.apply {
      tvFoo.text = item.value
    }
  }
}