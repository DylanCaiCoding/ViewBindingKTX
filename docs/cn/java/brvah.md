# 适配 BRVAH

因为这是个高频使用场景，所以单独进行了适配，可以在不影响原有的功能的情况下，直接兼容使用上 ViewBinding。

首先添加依赖：

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-brvah:2.0.2'
```

通过 `BaseViewHolderUtil.getBinding(holder, VB::bind)` 方法来获取 binding 对象了。

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