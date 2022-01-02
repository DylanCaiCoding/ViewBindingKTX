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
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dylanc.viewbinding.doOnCustomTabSelected
import com.dylanc.viewbinding.nonreflection.binding
import com.dylanc.viewbinding.nonreflection.setCustomView
import com.dylanc.viewbinding.sample.databinding.ActivityMainBinding
import com.dylanc.viewbinding.sample.databinding.LayoutBottomTabBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

  private val binding by binding(ActivityMainBinding::inflate)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val fragments = listOf(HomeFragment(), CustomViewFragment())
    binding.viewPager2.adapter = object : FragmentStateAdapter(this) {
      override fun getItemCount() = fragments.size
      override fun createFragment(position: Int) = fragments[position]
    }

    TabLayoutMediator(binding.tabLayout, binding.viewPager2, false) { tab, position ->
      tab.setCustomView(LayoutBottomTabBinding::inflate) {
        if (position == 0) {
          textView.textSize = 16f
        }
      }
    }.attach()

    binding.tabLayout.doOnCustomTabSelected<LayoutBottomTabBinding>(
      // binding.tabLayout.doOnCustomTabSelected(LayoutBottomTabBinding::bind,
      onTabSelected = {
        textView.textSize = 16f
      },
      onTabUnselected = {
        textView.textSize = 14f
      }
    )
  }

}