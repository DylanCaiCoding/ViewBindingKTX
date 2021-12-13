# Use in BRVAH

Use ViewBinding without affecting the original code in BRVAH.

Add dependency:

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-brvah:1.2.6'
```

Overwrite the method of create view holder and call the `BindingHolderUtil.bind()` method, and then you can get the binding object with `BindingHolderUtil.getBinding()`.

```java
public class FooAdapter extends BaseQuickAdapter<Foo, BaseViewHolder> {

  public FooAdapter() {
    super(R.layout.item_foo);
  }

  @NotNull
  @Override
  protected BaseViewHolder onCreateDefViewHolder(@NotNull ViewGroup parent, int viewType) {
    BaseViewHolder holder = super.onCreateDefViewHolder(parent, viewType);
    return BindingHolderUtil.bind(holder, ItemFooBinding::bind);
  }

  @Override
  protected void convert(@NotNull BaseViewHolder holder, Foo foo) {
    ItemFooBinding binding = BindingHolderUtil.getBinding(holder);
    binding.tvFoo.setText(foo.getValue());
  }
}
```

You can also create a `BaseBindingQuickAdapter` class for use. There are two usages of [using reflection](/en/java/baseclass?id=adapter) and [not using reflection](/en/java/baseclass?id=adapter-1).
