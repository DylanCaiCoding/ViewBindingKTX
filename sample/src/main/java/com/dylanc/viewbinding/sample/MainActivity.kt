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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dylanc.viewbinding.doOnCustomTabSelected
import com.dylanc.viewbinding.sample.base.reflection.kotlin.BaseBindingActivity
import com.dylanc.viewbinding.sample.databinding.ActivityMainBinding
import com.dylanc.viewbinding.sample.databinding.LayoutBottomTabBinding
import com.dylanc.viewbinding.setCustomView
import com.dylanc.viewbinding.updateCustomTab
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

  private val tabs: List<CustomTab> by lazy {
    listOf(
      CustomTab(R.string.tab_home, R.drawable.ic_home_selector, HomeFragment()),
      CustomTab(R.string.tab_message, R.drawable.ic_message_selector, MessageFragment()),
      CustomTab(R.string.tab_me, R.drawable.ic_account_selector, MeFragment())
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.viewPager2.adapter = object : FragmentStateAdapter(this) {
      override fun getItemCount() = tabs.size
      override fun createFragment(position: Int) = tabs[position].fragment
    }

    TabLayoutMediator(binding.tabLayout, binding.viewPager2, false) { tab, position ->
      tab.setCustomView<LayoutBottomTabBinding> {
        tvTitle.text = getString(tabs[position].title)
        ivIcon.setImageResource(tabs[position].icon)
        tvTitle.contentDescription = getString(tabs[position].title)
      }
    }.attach()

    binding.tabLayout.doOnCustomTabSelected<LayoutBottomTabBinding>(
      onTabSelected = { tab ->
        if (tab.position == 1) {
          ivUnreadState.isVisible = false
        }
      })

    supportFragmentManager.setFragmentResultListener("add_message", this) { _, _ ->
      binding.tabLayout.updateCustomTab<LayoutBottomTabBinding>(1) {
        ivUnreadState.isVisible = true
      }
    }
  }

  data class CustomTab(
    val title: Int,
    val icon: Int,
    val fragment: Fragment
  )
}