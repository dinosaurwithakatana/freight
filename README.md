Freight
===

Use this to ship data between `Controller` instances when you're using [Conductor](https://github.com/bluelinelabs/Conductor)



Using `Freight`
-------------------

```java
@ControllerBuilder //This will generate a builder
public class MainController extends Controller{
  @Extra String testStringExtra;
  @Nullable @Extra int testIntExtra; //Fields annotated with @Nullable are optional
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
              .asTransaction());
    }
  }
}
```

Navigator
----

Freight can generate scoped navigators for use outside of the main controller.

```java
@ControllerBuilder(value = "Login",
                   scope = "Welcome",
                   popChangeHandler = FadeChangeHandler.class,
                   pushChangeHandler = FadeChangeHandler.class)
public class LoginController {
    @Extra String username;
    @Nullable @Extra String password;


}
```

this will generate a "scoped" navigator interface named `WelcomeNavigator`

```java
public interface WelcomeNavigator extends Navigator {
  void goToLogin(final String username, @Nullable final String password);
}
```

as well as an implementation handling the changes via conductor's router.

```java
public class Freight_WelcomeNavigator extends FreightNavigator implements WelcomeNavigator {
  private final Router router;

  public Freight_WelcomeNavigator(Router router) {
    this.router = router;
  }

  public void goToLogin(final String username, @Nullable final String password) {
    final LoginControllerBuilder builder = new LoginControllerBuilder();
    builder.username(username);
    builder.password(password);
    RouterTransaction rt = builder.asTransaction()
        .tag("Login");
    router.pushController(rt);
  }
}
```

To use this with DI (such as dagger) you can create a `NavigatorFactory` and inject that

```java
public class NavigatorFactory {
    private Router router;

    @Inject
    public NavigatorFactory(Router router) {
        this.router = router;
    }

    public WelcomeNavigator welcomeNavigator() {
        return new Freight_WelcomeNavigator(router);
    }
}
```
There is a lint check bundled with the API artifact that will verify you've called all required builder methods.

Setup
------------
```groovy
buildscript {
    repositories {
        maven {url "https://oss.sonatype.org/content/repositories/snapshots/" }
    }
    dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}

apply plugin: 'com.neenbedankt.android-apt'

dependencies {
    apt 'io.dwak:freight-processor:0.7-SNAPSHOT'
    compile 'io.dwak:freight:0.7-SNAPSHOT'
}
```

Acknowledgements
--

BluelineLabs for the fantastic library

Michael Evans for the name for this library
