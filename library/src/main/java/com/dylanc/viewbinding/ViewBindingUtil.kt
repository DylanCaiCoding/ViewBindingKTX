@file:Suppress("unused")
@file:JvmName("ViewBindingUtil")

package com.dylanc.viewbinding

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


/**
 * @author Dylan Cai
 */

inline fun <reified VB : ViewBinding> Fragment.autoInflate(): Lazy<VB> = lazy { inflateBinding(layoutInflater) }

inline fun <reified VB : ViewBinding> Activity.autoInflate(): Lazy<VB> = lazy { inflateBinding(layoutInflater) }

inline fun <reified VB : ViewBinding> Dialog.autoInflate(): Lazy<VB> = lazy { inflateBinding(layoutInflater) }

@JvmName("inflate")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> inflateBinding(clazz: Class<VB>, layoutInflater: LayoutInflater) =
  clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB

inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
  inflateBinding(VB::class.java, layoutInflater)

@JvmName("inflate")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> inflateBinding(clazz: Class<VB>, layoutInflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) =
  clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    .invoke(null, layoutInflater, parent, attachToParent) as VB

inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) =
  inflateBinding(VB::class.java, layoutInflater, parent, attachToParent)

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(layoutInflater: LayoutInflater): VB =
  doForGenericBindingClass { inflateBinding(it, layoutInflater) }

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(layoutInflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean): VB =
  doForGenericBindingClass { inflateBinding(it, layoutInflater, parent, attachToParent) }

private fun <VB : ViewBinding> Any.doForGenericBindingClass(block: (Class<VB>) -> VB): VB {
  var index = 0
  while (true) {
    try {
      return block.invoke(findGenericBindingClass(index))
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