package com.dylanc.viewbinding.sample

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.dylanc.viewbinding.binding
import com.dylanc.viewbinding.sample.databinding.LayoutCustomViewBinding

/**
 * @author Dylan Cai
 */
class CustomView(context: Context, attrs: AttributeSet? = null) :
  ConstraintLayout(context, attrs) {

  val binding: LayoutCustomViewBinding = binding()

  init {
    binding.tvTitle.text = "test"
  }

}