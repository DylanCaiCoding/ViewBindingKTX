package com.dylanc.viewbinding.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.dylanc.viewbinding.inflate
import com.dylanc.viewbinding.sample.databinding.ActivityMainBinding
import com.dylanc.viewbinding.sample.item.Foo
import com.dylanc.viewbinding.sample.item.FooViewDelegate

class MainActivity : AppCompatActivity() {

  private val binding: ActivityMainBinding by inflate()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.apply {
      val adapter = MultiTypeAdapter(listOf(Foo("1"), Foo("2"), Foo("3")))
      adapter.register(FooViewDelegate())
      recyclerView.adapter = adapter
      recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
    }
  }
}