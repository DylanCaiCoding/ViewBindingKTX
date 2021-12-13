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

@file:Suppress("unused")

package com.dylanc.viewbinding.nonreflection

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <VB : ViewBinding> Fragment.binding(bind: (View) -> VB) = FragmentBindingDelegate(bind)

class FragmentBindingDelegate<VB : ViewBinding>(private val bind: (View) -> VB) : ReadOnlyProperty<Fragment, VB> {
  private var binding: VB? = null

  override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
    if (binding == null) {
      binding = try {
        bind(thisRef.requireView()).also { binding ->
          if (binding is ViewDataBinding) binding.lifecycleOwner = thisRef.viewLifecycleOwner
        }
      } catch (e: IllegalStateException) {
        throw IllegalStateException("The binding property has been destroyed.")
      }
      thisRef.viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
          if (thisRef is BindingLifecycleOwner) thisRef.onDestroyViewBinding(binding!!)
          binding = null
        }
      })
    }
    return binding!!
  }
}

interface BindingLifecycleOwner {
  fun onDestroyViewBinding(destroyingBinding: ViewBinding)
}
