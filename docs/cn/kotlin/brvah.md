# 适配 BRVAH

因为这是个高频使用场景，所以单独进行了适配，可以在不影响原有的功能的情况下，直接兼容使用上 ViewBinding。

首先添加依赖：

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-brvah:2.0.0'
```

通过 `holder.getBinding(VB::bind)` 来获取 ViewBinding 实例。

```kotlin
class FooAdapter : BaseQuickAdapter<Foo, BaseViewHolder>(R.layout.item_foo) {

  override fun convert(holder: BaseViewHolder, item: Foo) {
    holder.getBinding(ItemFooBinding::bind).apply {
      tvFoo.text = item.value
    }
  }
}
```

如果使用了 `viewbinding-nonreflection-ktx` 依赖，无需增加 `viewbinding-brvah` 依赖就能支持。