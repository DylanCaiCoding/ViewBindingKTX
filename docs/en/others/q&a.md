# Q&A

### How do I use ViewBinding for the layout of `<include/>`?

- #### The layout of include without `<merge/>` tag

Add id to the `<include/>` tag.

```xml
<include
    android:id="@+id/toolbar"
    layout="@layout/layout_toolar" />
```

```kotlin
binding.toolbar.tvTitle.text = title
```

- #### The layout of include with `<merge/>` tag

Cannot add id to the `<include/>` tag.

```kotlin
val mergeBinding = LayoutMergeBinding.bind(binding.root)
```