# Injected ViewModel Provider
This is a small lib to use easily use Android's ViewModels with a depedency injection framework like dagger. You obtain a `ViewModel` from a `javax.inject.Provider` instead of a `ViewModelProvider.Factory`.

## Usage

### From Java

#### Download
```groovy
implementation 'me.tatarka.injectedvmprovider:injectedvmprovider-extensions:1.0'
```
For androidx, use version 2.0 instead.

#### Usage

Set up your ViewModel
```java
public class MyViewModel extends ViewModel {
    private final MyDependency source;

    @Inject
    public MainViewModel(MyDependency source) {
        this.source = source;
    }
}
```

Inject your ViewModel provider into the desired Fragment or Activity
```java
public class MyActivity extends AppCompatActivity {

    @Inject
    Provider<MyViewModel> vmProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        MyViewModel vm = InjectedViewModelProviders.of(this).get(vmProvider);
    }
}
```

Note: If you aren't using fragments, you can use `me.tatarka.injectedvmprovider:injectedvmprovider:1.0`, and use `new InjectedViewModelProvider(viewModelStoreOwner)` instead.

### From Kotlin

#### Download
```groovy
implementation 'me.tatarka.injectedvmprovider:injectedvmprovider-ktx:1.0'
```

For androidx, use version 2.0 instead.

#### Usage

ViewModel
```kotlin
class MainViewModel @Inject constructor(val dependency: Dependency): ViewModel()
```

Injection
```kotlin
class KotlinDaggerMainActivity : AppCompatActivity() {

    @Inject
    lateinit var vmProvider: Provider<MainViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ...
        val vm = injectedViewModelProvider[vmProvider]
    }
}
```
