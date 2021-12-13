# 适配 BRVAH

因为这是个高频使用场景，所以单独进行了适配，可以在不影响原有的功能的情况下，直接兼容使用上 ViewBinding。

首先添加依赖：

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-brvah:1.2.6'
```

在适配器重写创建 BaseViewHolder 的方法，创建对象时调用 withBinding() 方法使其伴随一个 binding 对象。然后就能通过 holder.getViewBinding() 来使用 ViewBinding 了。

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

还可以自行封装一个 `BaseBindingQuickAdapter` 基类进行使用，能简化更多的代码。这里已有[使用反射](/kotlin/baseclass?id=adapter)和[不使用反射](/kotlin/baseclass?id=adapter-1)的基类代码供大家拷贝使用。

> 查看 BRVAH 源码可知原本在创建 ViewHolder 时有使用反射，所以用反射进行封装只是替换了反射的内容，在性能上和原来的基本一样。