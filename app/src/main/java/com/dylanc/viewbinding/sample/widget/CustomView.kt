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

package com.dylanc.viewbinding.sample.widget

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.dylanc.viewbinding.binding
import com.dylanc.viewbinding.sample.R
import com.dylanc.viewbinding.sample.databinding.LayoutCustomViewBinding

/**
 * @author Dylan Cai
 */
class CustomView(context: Context, attrs: AttributeSet? = null) :
  ConstraintLayout(context, attrs) {

  val binding: LayoutCustomViewBinding by binding()

  init {
    binding.tvTitle.setText(R.string.app_name)
  }

}