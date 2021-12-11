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

package com.dylanc.viewbinding

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private var View.binding: Any?
  get() = getTag(-1)
  set(value) = setTag(-1, value)

inline fun <reified VB : ViewBinding> ComponentActivity.binding() = lazy {
  inflateBinding<VB>(layoutInflater).also { binding ->
    setContentView(binding.root)
    if (binding is ViewDataBinding) binding.lifecycleOwner = this
  }
}

inline fun <reified VB : ViewBinding> Fragment.binding() =
  FragmentBindingDelegate<VB> { requireView().getBinding() }

inline fun <reified VB : ViewBinding> Fragment.binding(method: Method) =
  FragmentBindingDelegate<VB> { if (method == Method.BIND) requireView().getBinding() else inflateBinding(layoutInflater) }

inline fun <reified VB : ViewBinding> Dialog.binding() = lazy {
  inflateBinding<VB>(layoutInflater).also { setContentView(it.root) }
}

inline fun <reified VB : ViewBinding> ViewGroup.binding(attachToParent: Boolean = false) = lazy {
  inflateBinding<VB>(LayoutInflater.from(context), if (attachToParent) this else null, attachToParent)
}

inline fun <reified VB : ViewBinding> ViewGroup.inflate() =
  inflateBinding<VB>(LayoutInflater.from(context), this, true)

inline fun <reified VB : ViewBinding> TabLayout.Tab.setCustomView(block: VB.() -> Unit) {
  inflateBinding<VB>(LayoutInflater.from(parent!!.context)).apply(block).let { binding ->
    customView = binding.root
    customView?.tag = binding
  }
}

inline fun <reified VB : ViewBinding> TabLayout.doOnCustomTabSelected(
  crossinline onTabUnselected: VB.(TabLayout.Tab) -> Unit = {},
  crossinline onTabReselected: VB.(TabLayout.Tab) -> Unit = {},
  crossinline onTabSelected: VB.(TabLayout.Tab) -> Unit = {},
) =
  addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab) {
      tab.customView?.getBinding<VB>()?.onTabSelected(tab)
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
      tab.customView?.getBinding<VB>()?.onTabUnselected(tab)
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
      tab.customView?.getBinding<VB>()?.onTabReselected(tab)
    }
  })

inline fun <reified VB : ViewBinding> NavigationView.setHeaderView(index: Int = 0, block: VB.() -> Unit) =
  getHeaderView(index)?.getBinding<VB>()?.run(block)

inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
  VB::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB

inline fun <reified VB : ViewBinding> inflateBinding(parent: ViewGroup) =
  inflateBinding<VB>(LayoutInflater.from(parent.context), parent, false)

inline fun <reified VB : ViewBinding> inflateBinding(
  layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
) =
  VB::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    .invoke(null, layoutInflater, parent, attachToParent) as VB

inline fun <reified VB : ViewBinding> View.getBinding() = getBinding(VB::class.java)

@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> View.getBinding(clazz: Class<VB>) =
  if (binding != null) {
    binding as VB
  } else {
    (clazz.getMethod("bind", View::class.java).invoke(null, this) as VB).also { binding = it }
  }

enum class Method { BIND, INFLATE }

interface BindingLifecycleOwner {
  fun onDestroyViewBinding(destroyingBinding: ViewBinding)
}

class FragmentBindingDelegate<VB : ViewBinding>(private val block: () -> VB) : ReadOnlyProperty<Fragment, VB> {
  private var binding: VB? = null

  override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
    if (binding == null) {
      binding = try {
        block().also { binding ->
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