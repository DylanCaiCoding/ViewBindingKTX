package com.dylanc.viewbinding.sample

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dylanc.viewbinding.sample.base.java.BaseBindingActivity
import com.dylanc.viewbinding.sample.databinding.ActivityMainBinding

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.tvHelloWorld.text = "haha"
    binding.apply {
      val textAdapter = TextAdapter()
//      textAdapter.list = listOf("1", "2", "3")
      textAdapter.setList(listOf("1", "2", "3"))
      recyclerView.adapter = textAdapter
      recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
    }
  }
}