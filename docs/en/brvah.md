# Use in BRVAH

Use ViewBinding without affecting the original code in BRVAH.

Add dependency:

```groovy
// If viewbinding-nonreflection-ktx dependency is used, it can not be added
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-brvah:2.1.0'
```

You can get the ViewBinding object with holder.

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
