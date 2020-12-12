package com.dylanc.viewbinding.sample

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.dylanc.viewbinding.sample.base.java.BaseBindingActivity
import com.dylanc.viewbinding.sample.databinding.ActivityMainBinding
import com.dylanc.viewbinding.sample.item.Foo
import com.dylanc.viewbinding.sample.item.FooViewDelegate

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.apply {
      tvHelloWorld.text = "Hello Android!"
//      val adapter = FooAdapter(listOf(Foo("1"), Foo("2"), Foo("3")))
      val items = mutableListOf<Any>(Foo("1"), Foo("2"))
      val adapter = MultiTypeAdapter(items)
      adapter.register(FooViewDelegate())
      recyclerView.adapter = adapter
      recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
    }
  }
}