Freight
===

Use this to ship data between `Controller` instances when you're using [Conductor](https://github.com/bluelinelabs/Conductor)



Using the `Freight`
-------------------

```java
public class MainController extends Controller{
  @Extra String testStringExtra;
  @Extra int testIntExtra;
  @Extra("booleanExtraYo") boolean testBooleanExtra;

  public MainController(Bundle args) {
    super(args);
  }

  @NonNull
  @Override
  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
    View view = inflater.inflate(R.layout.controller_main, container, false);
    Freight.ship(this);
    return view;
  }
}
```

Using the builder
---

```java
public class MainActivity extends AppCompatActivity {

  private Router router;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ViewGroup container = (ViewGroup) findViewById(R.id.container);

    router = Conductor.attachRouter(this, container, savedInstanceState);
    if(!router.hasRootController()){
      router.setRoot(new MainControllerBuilder()
              .booleanExtraYo(false)
              .testStringExtra("Yeah this is a string")
              .testIntExtra(1)
              .build());
    }
  }
}
```

Acknowledgements
--

BluelineLabs for the fantastic library

Michael Evans for the name for this library
