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

import android.content.Context
import android.view.ViewGroup
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.viewbinding.BindingViewHolder
import com.dylanc.viewbinding.sample.databinding.ItemFooBinding

class FooViewDelegate : ItemViewDelegate<Foo, BindingViewHolder<ItemFooBinding>>() {

  override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
    BindingViewHolder<ItemFooBinding>(parent)

  override fun onBindViewHolder(holder: BindingViewHolder<ItemFooBinding>, item: Foo) {
    holder.binding.tvFoo.text = item.value
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