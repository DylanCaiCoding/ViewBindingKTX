package com.dylanc.viewbinding.sample.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.*
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.dylanc.viewbinding.binding
import com.dylanc.viewbinding.sample.R
import com.dylanc.viewbinding.sample.databinding.DialogLoadingBinding

/**
 * @author Dylan Cai
 */
class LoadingDialogFragment(private val text: String? = null) : DialogFragment() {

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return LoadingDialog(requireContext())
  }

  inner class LoadingDialog(context: Context) : Dialog(context, R.style.DialogTheme) {
    val binding: DialogLoadingBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setCancelable(false)
      window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
      with(binding){
        if (text.isNullOrBlank()){
          tvMsg.isVisible = false
        }else{
          tvMsg.isVisible = true
          tvMsg.text = text
        }
      }
    }
  }
}