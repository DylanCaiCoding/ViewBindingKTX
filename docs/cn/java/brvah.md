# 适配 BRVAH

因为这是个高频使用场景，所以单独进行了适配，可以在不影响原有的功能的情况下，直接兼容使用上 ViewBinding。

首先添加依赖：

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-brvah:1.2.6'
```

在适配器重写创建 BaseViewHolder 的方法，通过 BindingHolderUtil 将 holder 和 绑定类进行绑定。然后就能通过 BindingHolderUtil.getBinding() 方法来获取 binding 对象了。

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

还可以自行封装一个 `BaseBindingQuickAdapter` 基类进行使用，这里已有[使用反射](/java/baseclass?id=adapter)和[不使用反射](/java/baseclass?id=adapter-1)的基类代码供大家拷贝使用。

> 查看 BRVAH 源码可得知原本在创建 ViewHolder 时有使用反射，所以用反射进行封装只是替换了反射的内容，在性能上和原来一样。