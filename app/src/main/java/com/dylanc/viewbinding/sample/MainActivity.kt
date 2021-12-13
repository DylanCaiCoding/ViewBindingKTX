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

package com.dylanc.viewbinding.sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import com.dylanc.viewbinding.base.simpleListAdapter
import com.dylanc.viewbinding.nonreflection.binding
import com.dylanc.viewbinding.sample.databinding.ActivityMainBinding
import com.dylanc.viewbinding.sample.databinding.ItemFooBinding
import com.dylanc.viewbinding.sample.item.Foo
import com.dylanc.viewbinding.sample.widget.LoadingDialogFragment

class MainActivity : AppCompatActivity() {

  private val binding by binding(ActivityMainBinding::inflate)
  private val loadingDialog by lazy { LoadingDialogFragment() }
  private val handler = Handler(Looper.getMainLooper())
  private val list = listOf(Foo("item 1"), Foo("item 2"), Foo("item 3"))

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    with(binding) {
      customView.setOnClickListener {
        loadingDialog.show(supportFragmentManager, "loading")
        handler.postDelayed({
          loadingDialog.dismiss()
        }, 2000)
      }
      recyclerView.adapter = adapter
    }
    adapter.submitList(list)
    adapter.doOnItemClick { item, _ ->
      Toast.makeText(this, item.value, Toast.LENGTH_SHORT).show()
    }
    adapter.doOnItemLongClick { item, _ ->
      Toast.makeText(this, "long click ${item.value}", Toast.LENGTH_SHORT).show()
    }
  }

  private val adapter = simpleListAdapter<Foo, ItemFooBinding>(DiffCallback()) { item ->
    tvFoo.text = item.value
  }

  class DiffCallback : DiffUtil.ItemCallback<Foo>() {
    override fun areItemsTheSame(oldItem: Foo, newItem: Foo) = oldItem.value == newItem.value
    override fun areContentsTheSame(oldItem: Foo, newItem: Foo) = oldItem.value == newItem.value
  }
}