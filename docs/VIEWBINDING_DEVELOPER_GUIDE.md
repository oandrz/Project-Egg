# ViewBinding Developer Guide - Chefnut App

## Table of Contents
1. [Overview](#overview)
2. [Setup Requirements](#setup-requirements)
3. [ViewBinding Patterns](#viewbinding-patterns)
4. [Common Issues & Solutions](#common-issues--solutions)
5. [Best Practices](#best-practices)
6. [Migration Checklist](#migration-checklist)

## Overview

This project has been migrated from deprecated `kotlin-android-extensions` to ViewBinding. This guide will help developers understand the new patterns and maintain code consistency.

### What is ViewBinding?
ViewBinding generates a binding class for each XML layout file. These classes contain direct references to all views that have an ID in the corresponding layout.

### Benefits
- **Null Safety**: ViewBinding doesn't generate nullable fields
- **Type Safety**: Fields have correct types matching the views
- **Compile-time Safety**: Errors are caught at compile time, not runtime
- **Performance**: No runtime overhead like findViewById

## Setup Requirements

### Build Configuration
```gradle
android {
    buildFeatures {
        viewBinding true
    }
}
```

### Dependencies
- SwipeRefreshLayout: `implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"`
- Min SDK: 24
- Target SDK: 34

### Build Command
Due to environment-specific gradle configuration:
```bash
GRADLE_USER_HOME=~/.gradle ./gradlew clean assembleDebug
```

## ViewBinding Patterns

### Activities

#### Basic Activity Setup
```kotlin
class MyActivity : BaseActivity<MyContract.View, MyPresenter>() {
    
    private lateinit var binding: ActivityMyBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBinding.bind((findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0))
    }
    
    // Access views
    fun setupViews() {
        binding.myButton.setOnClickListener { /* ... */ }
        binding.myTextView.text = "Hello"
    }
}
```

#### Activity with Included Layouts
```kotlin
class RecipeDetailActivity : BaseActivity<...>() {
    
    private lateinit var binding: ActivityRecipeDetailBinding
    private lateinit var contentBinding: ContentRecipeDetailBodyBinding
    
    // For accessing nested views in included layouts
    private val scrollContainer: View by lazy {
        contentBinding.root.findViewById(R.id.scroll_container)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.bind(...)
        contentBinding = ContentRecipeDetailBodyBinding.bind(
            binding.root.findViewById(R.id.swipe_refresh_container)
        )
    }
}
```

### Fragments

#### Basic Fragment Setup
```kotlin
class MyFragment : BaseFragment<MyContract.View, MyPresenter>() {
    
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

#### Fragment with Included Layouts
```kotlin
class RecipeHomeFragment : BaseFragment<...>() {
    
    private var _binding: FragmentRecipeHomeBinding? = null
    private val binding get() = _binding!!
    
    // Access included layout views
    private val layoutError: View by lazy { binding.layoutError.root }
    private val ivFailImage: AppCompatImageView by lazy { 
        binding.root.findViewById(R.id.iv_fail_image)
    }
    
    // For included layouts, access visibility through root
    fun showError() {
        layoutError.visibility = View.VISIBLE
    }
}
```

### RecyclerView Adapters

#### ViewHolder with ViewBinding
```kotlin
class MyAdapter(private val context: Context) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return ViewHolder(binding)
    }
    
    inner class ViewHolder(val binding: ItemMyBinding) : RecyclerView.ViewHolder(binding.root) {
        // Access views through binding
    }
}
```

### FastAdapter Custom Items

#### Custom Item with ViewBinding
```kotlin
class RecipeItem(val recipe: Recipe) : AbstractBindingItem<ItemRecipeBinding>() {
    
    override val type: Int get() = R.id.recipe_item_id
    
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemRecipeBinding {
        return ItemRecipeBinding.inflate(inflater, parent, false)
    }
    
    override fun bindView(binding: ItemRecipeBinding, payloads: List<Any>) {
        binding.tvRecipeName.text = recipe.name
        // ... other bindings
    }
    
    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(ItemRecipeBinding.bind(v))
    }
    
    class ViewHolder(val binding: ItemRecipeBinding) : FastAdapter.ViewHolder<RecipeItem>(binding.root)
}
```

## Common Issues & Solutions

### Issue 1: Included Layout Visibility
**Problem**: `binding.includedLayout.visibility` doesn't work

**Solution**: Access through root
```kotlin
binding.includedLayout.root.visibility = View.VISIBLE
```

### Issue 2: View Import Conflicts
**Problem**: `View` conflicts with contract interfaces

**Solution**: Use qualified name
```kotlin
binding.myView.visibility = android.view.View.GONE
```

### Issue 3: Nested Views in Included Layouts
**Problem**: Views inside included layouts not accessible

**Solution**: Use findViewById or lazy properties
```kotlin
private val nestedView: TextView by lazy {
    binding.root.findViewById(R.id.nested_view)
}
```

### Issue 4: CamelCase View IDs
**Problem**: snake_case IDs not following conventions

**Solution**: IDs are automatically converted to camelCase
- `tv_recipe_name` → `binding.tvRecipeName`
- `btn_submit` → `binding.btnSubmit`

## Best Practices

### 1. Null Safety in Fragments
Always use nullable binding with proper cleanup:
```kotlin
private var _binding: FragmentMyBinding? = null
private val binding get() = _binding!!

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
```

### 2. Lazy Initialization for Complex Views
Use lazy delegates for views that need findViewById:
```kotlin
private val complexView: MyCustomView by lazy {
    binding.root.findViewById(R.id.complex_view)
}
```

### 3. Handler Usage
Always use Handler with Looper.getMainLooper():
```kotlin
Handler(Looper.getMainLooper()).post {
    // UI updates
}
```

### 4. Adapter Position
Use bindingAdapterPosition instead of deprecated adapterPosition:
```kotlin
holder.bindingAdapterPosition
```

### 5. Network Connectivity
Use newer connectivity API for API 23+:
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    // Check capabilities
} else {
    @Suppress("DEPRECATION")
    connectivityManager.activeNetworkInfo?.isConnected
}
```

## Migration Checklist

When creating new features or maintaining existing code:

- [ ] Remove all synthetic imports (`import kotlinx.android.synthetic.*`)
- [ ] Initialize binding in onCreate/onCreateView
- [ ] Use camelCase for all view references
- [ ] Handle nullable bindings in fragments
- [ ] Access included layout views through root
- [ ] Use qualified View names to avoid conflicts
- [ ] Update deprecated APIs (Handler, adapterPosition, etc.)
- [ ] Test thoroughly on different screen sizes

## Troubleshooting

### Build Errors
If you encounter build errors:
1. Clean project: `./gradlew clean`
2. Use correct gradle command: `GRADLE_USER_HOME=~/.gradle ./gradlew assembleDebug`
3. Check for missing view IDs in layouts
4. Verify all synthetic imports are removed

### Runtime Crashes
Common causes:
1. Accessing binding before initialization
2. Not clearing fragment binding in onDestroyView
3. Wrong binding class for included layouts

## Future Considerations

### Jetpack Compose Migration
Consider migrating to Jetpack Compose for:
- New features
- Complex UI components
- Better performance and maintainability

### Performance Optimization
- ViewBinding has no runtime overhead
- Consider view recycling in adapters
- Use ViewStub for rarely used views

---

*Last updated: December 2024*
*For questions or issues, contact the Android team* 