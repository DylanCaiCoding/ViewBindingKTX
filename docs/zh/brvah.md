# 适配 BRVAH

因为这是个高频使用场景，所以单独进行了适配，可以在不影响原有的功能的情况下，直接兼容使用上 ViewBinding。

首先添加依赖：

```groovy
// 如果使用了 viewbinding-nonreflection-ktx 可以不添加
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-brvah:2.1.0'
```

可直接通过 `holder` 来获取 ViewBinding 实例。

<!-- tabs:start -->

#### **Kotlin**

```kotlin
class FooAdapter : BaseQuickAdapter<Foo, BaseViewHolder>(R.layout.item_foo) {

  override fun convert(holder: BaseViewHolder, item: Foo) {
    holder.getBinding(ItemFooBinding::bind).tvFoo.text = item.value
  }
}
```

#### **Java**

```java
public class FooAdapter extends BaseQuickAdapter<Foo, BaseViewHolder> {

  public FooAdapter() {
    super(R.layout.item_foo);
  }

  @Override
  protected void convert(@NotNull BaseViewHolder holder, Foo foo) {
    ItemFooBinding binding = BaseViewHolderUtil.getBinding(holder, ItemFooBinding::bind);
    binding.tvFoo.setText(foo.getValue());
  }
}
```

<!-- tabs:end -->