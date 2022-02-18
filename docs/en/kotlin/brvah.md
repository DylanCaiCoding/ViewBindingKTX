# Use in BRVAH

Use ViewBinding without affecting the original code in BRVAH.

Add dependency:

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-brvah:2.0.2'
```

You can get the binding object with `holder.getBinding(VB::bind)`.

```kotlin
class FooAdapter : BaseQuickAdapter<Foo, BaseViewHolder>(R.layout.item_foo) {

  override fun convert(holder: BaseViewHolder, item: Foo) {
    holder.getBinding(ItemFooBinding::bind).apply {
      tvFoo.text = item.value
    }
  }
}
```

If the `viewbinding-nonreflection-ktx` dependency is used, it can be supported without adding the `viewbinding-brvah` dependency.
