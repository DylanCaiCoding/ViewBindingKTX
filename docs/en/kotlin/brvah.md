# Use in BRVAH

Use ViewBinding without affecting the original code in BRVAH.

Add dependency:

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-brvah:1.2.6'
```

Overwrite the method of create view holder and call the `withBinding()` method at the end, and then you can get the binding object with `holder.getViewBinding()`.

```kotlin
class FooAdapter : BaseQuickAdapter<Foo, BaseViewHolder>(R.layout.item_foo) {

  override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    return super.onCreateDefViewHolder(parent, viewType).withBinding { ItemFooBinding.bind(it) }
  }
  
  override fun convert(holder: BaseViewHolder, item: Foo) {
    holder.getViewBinding<ItemFooBinding>().apply {
      tvFoo.text = item.value
    }
  }
}
```

You can also create a `BaseBindingQuickAdapter` class for use. There are two usages of [using reflection](/en/kotlin/baseclass?id=adapter) and [not using reflection](/en/kotlin/baseclass?id=adapter-1).
