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

@file:JvmName("ViewBindingUtil")
@file:Suppress("UNCHECKED_CAST", "unused")

package com.dylanc.viewbinding.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(layoutInflater: LayoutInflater): VB =
  withGenericBindingClass<VB>(this) { clazz ->
    clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
  }.also { binding ->
    if (this is ComponentActivity && binding is ViewDataBinding) {
      binding.lifecycleOwner = this
    }
  }

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): VB =
  withGenericBindingClass<VB>(this) { clazz ->
    clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
      .invoke(null, layoutInflater, parent, attachToParent) as VB
  }.also { binding ->
    if (this is Fragment && binding is ViewDataBinding) {
      binding.lifecycleOwner = viewLifecycleOwner
    }
  }

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(parent: ViewGroup): VB =
  inflateBindingWithGeneric(LayoutInflater.from(parent.context), parent, false)

fun <VB : ViewBinding> Any.bindViewWithGeneric(view: View): VB =
  withGenericBindingClass<VB>(this) { clazz ->
    clazz.getMethod("bind", LayoutInflater::class.java).invoke(null, view) as VB
  }.also { binding ->
    if (this is Fragment && binding is ViewDataBinding) {
      binding.lifecycleOwner = viewLifecycleOwner
    }
  }

private fun <VB : ViewBinding> withGenericBindingClass(any: Any, block: (Class<VB>) -> VB): VB {
  any.allParameterizedType.forEach { parameterizedType ->
    parameterizedType.actualTypeArguments.forEach {
      try {
        return block.invoke(it as Class<VB>)
      } catch (e: Exception) {
      }
    }
  }
  throw IllegalArgumentException("There is no generic of ViewBinding.")
}

private val Any.allParameterizedType: List<ParameterizedType>
  get() {
    val genericParameterizedType = mutableListOf<ParameterizedType>()
    var genericSuperclass = javaClass.genericSuperclass
    var superclass = javaClass.superclass
    while (superclass != null) {
      if (genericSuperclass is ParameterizedType) {
        genericParameterizedType.add(genericSuperclass)
      }
      genericSuperclass = superclass.genericSuperclass
      superclass = superclass.superclass
    }
    return genericParameterizedType
  }