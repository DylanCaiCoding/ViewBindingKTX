# Use in BRVAH

Use ViewBinding without affecting the original code in BRVAH.

Add dependency:

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-brvah:2.0.0'
```

You can get the binding object with `BaseViewHolderUtil.getBinding(holder, VB::bind)`.

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