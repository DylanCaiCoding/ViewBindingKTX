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

package com.dylanc.viewbinding.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


@Suppress("UNCHECKED_CAST")
@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(layoutInflater: LayoutInflater): VB =
  withGenericBindingClass(this) {
    it.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
  }

@Suppress("UNCHECKED_CAST")
@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(view: View): VB =
  withGenericBindingClass(this) {
    it.getMethod("bind", LayoutInflater::class.java).invoke(null, view) as VB
  }

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(parent: ViewGroup): VB =
  withGenericBindingClass(this) { inflateBinding(it, LayoutInflater.from(parent.context), parent, false) }

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(
  layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
): VB =
  withGenericBindingClass(this) { inflateBinding(it, layoutInflater, parent, attachToParent) }

@Suppress("UNCHECKED_CAST")
private fun <VB : ViewBinding> inflateBinding(
  clazz: Class<VB>, layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
) =
  clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    .invoke(null, layoutInflater, parent, attachToParent) as VB

private fun <VB : ViewBinding> withGenericBindingClass(any: Any, block: (Class<VB>) -> VB): VB {
  var index = 0
  while (true) {
    try {
      return block.invoke(any.findGenericBindingClass(index))
    } catch (e: NoSuchMethodException) {
      index++
    }
  }
}

@Suppress("UNCHECKED_CAST")
private fun <VB : ViewBinding> Any.findGenericBindingClass(index: Int): Class<VB> {
  val type = javaClass.genericSuperclass
  if (type is ParameterizedType && index < type.actualTypeArguments.size) {
    return type.actualTypeArguments[index] as Class<VB>
  }
  throw IllegalArgumentException("There is no generic of ViewBinding.")
}