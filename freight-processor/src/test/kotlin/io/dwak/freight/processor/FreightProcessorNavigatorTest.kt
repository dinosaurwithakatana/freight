package io.dwak.freight.processor

import com.google.testing.compile.JavaFileObjects
import io.dwak.freight.util.processAndAssertEquals
import org.junit.Test

class FreightProcessorNavigatorTest {

  companion object {
    private const val NAVIGATOR_PACKAGE = "io.dwak.freight.navigator"
    private val FREIGHT_NAVIGATOR = listOf(
        "package io.dwak.freight.internal;",
        "import com.bluelinelabs.conductor.Router;",
        "import io.dwak.freight.Navigator;",
        "public abstract class FreightNavigator implements Navigator {",
        "  @SuppressWarnings(\"WeakerAccess\")",
        "  protected final Router router;",
        "  public FreightNavigator(Router router) {",
        "    this.router = router;",
        "  }",
        "  @Override",
        "  public boolean popToRoot() {",
        "    return router.popToRoot();",
        "  }",
        "  @Override",
        "  public boolean goBack() {",
        "    return router.popCurrentController();",
        "  }",
        "}")

    private val freightNavigatorJavaSource
        = JavaFileObjects.forSourceLines("io.dwak.freight.internal.FreightNavigator",
                                         FREIGHT_NAVIGATOR)
  }

  @Test
  fun singleControllerNoExtrasNoScope() {
    val inputFile
        = JavaFileObjects.forSourceLines("test.TestController",
                                         "package test;",
                                         "import com.bluelinelabs.conductor.Controller;",
                                         "import io.dwak.freight.annotation.ControllerBuilder;",
                                         "import android.support.annotation.NonNull;",
                                         "import android.support.annotation.Nullable;",
                                         "import android.view.LayoutInflater;",
                                         "import android.view.View;",
                                         "import android.view.ViewGroup;",
                                         "@ControllerBuilder(value = \"Test\")",
                                         "public class TestController extends Controller {",
                                         "",
                                         "  @NonNull",
                                         "  @Override",
                                         "  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {",
                                         "    return null;",
                                         "  }",
                                         "}")

    val expectedInterfaceOutput
        = JavaFileObjects.forSourceLines("$NAVIGATOR_PACKAGE.main.MainNavigator",
                                         "package $NAVIGATOR_PACKAGE.main;",
                                         "",
                                         "import io.dwak.freight.Navigator;",
                                         "",
                                         "public interface MainNavigator extends Navigator {",
                                         "  void goToTest();",
                                         "}")
    val expectedFreightImplOutput
        = JavaFileObjects.forSourceLines("$NAVIGATOR_PACKAGE.main.Freight_MainNavigator",
                                         "package $NAVIGATOR_PACKAGE.main;",
                                         "import com.bluelinelabs.conductor.Router;",
                                         "import com.bluelinelabs.conductor.RouterTransaction;",
                                         "import io.dwak.freight.internal.FreightNavigator;",
                                         "import java.lang.Override;",
                                         "import test.TestControllerBuilder;",
                                         "public class Freight_MainNavigator extends FreightNavigator implements MainNavigator {",
                                         "  public Freight_MainNavigator(Router router) {",
                                         "    super(router);",
                                         "  }",
                                         "  @Override",
                                         "  public void goToTest() {",
                                         "    final TestControllerBuilder builder = new TestControllerBuilder();",
                                         "    RouterTransaction rt = builder.asTransaction()",
                                         "        .tag(\"Test\");",
                                         "    router.pushController(rt);",
                                         "  }",
                                         "}")

    processAndAssertEquals(listOf(freightNavigatorJavaSource, inputFile),
                           "$NAVIGATOR_PACKAGE.main.MainNavigator" to expectedInterfaceOutput,
                           "$NAVIGATOR_PACKAGE.main.Freight_MainNavigator" to expectedFreightImplOutput)
  }

  @Test
  fun singleControllerSingleExtraNoScope() {
    val inputFile
        = JavaFileObjects.forSourceLines("test.TestController",
                                         "package test;",
                                         "import com.bluelinelabs.conductor.Controller;",
                                         "import io.dwak.freight.annotation.ControllerBuilder;",
                                         "import io.dwak.freight.annotation.Extra;",
                                         "import android.os.Bundle;",
                                         "import android.support.annotation.NonNull;",
                                         "import android.support.annotation.Nullable;",
                                         "import android.view.LayoutInflater;",
                                         "import android.view.View;",
                                         "import android.view.ViewGroup;",
                                         "import java.lang.String;",
                                         "@ControllerBuilder(value = \"Test\")",
                                         "public class TestController extends Controller {",
                                         "  @Extra String stringExtra;",
                                         "",
                                         "  public TestController(Bundle bundle){",
                                         "    super(bundle);",
                                         "  }",
                                         "",
                                         "  @NonNull",
                                         "  @Override",
                                         "  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {",
                                         "    return null;",
                                         "  }",
                                         "}")

    val expectedInterfaceOutput
        = JavaFileObjects.forSourceLines("$NAVIGATOR_PACKAGE.main.MainNavigator",
                                         "package $NAVIGATOR_PACKAGE.main;",
                                         "",
                                         "import io.dwak.freight.Navigator;",
                                         "import java.lang.String;",
                                         "",
                                         "public interface MainNavigator extends Navigator {",
                                         "  void goToTest(final String stringExtra);",
                                         "}")
    val expectedFreightImplOutput
        = JavaFileObjects.forSourceLines("$NAVIGATOR_PACKAGE.main.Freight_MainNavigator",
                                         "package $NAVIGATOR_PACKAGE.main;",
                                         "import com.bluelinelabs.conductor.Router;",
                                         "import com.bluelinelabs.conductor.RouterTransaction;",
                                         "import io.dwak.freight.internal.FreightNavigator;",
                                         "import java.lang.Override;",
                                         "import java.lang.String;",
                                         "import test.TestControllerBuilder;",
                                         "public class Freight_MainNavigator extends FreightNavigator implements MainNavigator {",
                                         "  public Freight_MainNavigator(Router router) {",
                                         "    super(router);",
                                         "  }",
                                         "  @Override",
                                         "  public void goToTest(final String stringExtra) {",
                                         "    final TestControllerBuilder builder = new TestControllerBuilder();",
                                         "    builder.stringExtra(stringExtra);",
                                         "    RouterTransaction rt = builder.asTransaction()",
                                         "        .tag(\"Test\");",
                                         "    router.pushController(rt);",
                                         "  }",
                                         "}")

    processAndAssertEquals(listOf(freightNavigatorJavaSource, inputFile),
                           "$NAVIGATOR_PACKAGE.main.MainNavigator" to expectedInterfaceOutput,
                           "$NAVIGATOR_PACKAGE.main.Freight_MainNavigator" to expectedFreightImplOutput)
  }

  @Test
  fun singleControllerNoExtrasCustomScope() {
    val inputFile
        = JavaFileObjects.forSourceLines("test.TestController",
                                         "package test;",
                                         "import com.bluelinelabs.conductor.Controller;",
                                         "import io.dwak.freight.annotation.ControllerBuilder;",
                                         "import android.support.annotation.NonNull;",
                                         "import android.support.annotation.Nullable;",
                                         "import android.view.LayoutInflater;",
                                         "import android.view.View;",
                                         "import android.view.ViewGroup;",
                                         "@ControllerBuilder(value = \"Test\", scope = \"Test\")",
                                         "public class TestController extends Controller {",
                                         "",
                                         "  @NonNull",
                                         "  @Override",
                                         "  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {",
                                         "    return null;",
                                         "  }",
                                         "}")

    val expectedInterfaceOutput
        = JavaFileObjects.forSourceLines("$NAVIGATOR_PACKAGE.test.TestNavigator",
                                         "package $NAVIGATOR_PACKAGE.test;",
                                         "",
                                         "import io.dwak.freight.Navigator;",
                                         "",
                                         "public interface TestNavigator extends Navigator {",
                                         "  void goToTest();",
                                         "}")
    val expectedFreightImplOutput
        = JavaFileObjects.forSourceLines("$NAVIGATOR_PACKAGE.test.Freight_TestNavigator",
                                         "package $NAVIGATOR_PACKAGE.test;",
                                         "import com.bluelinelabs.conductor.Router;",
                                         "import com.bluelinelabs.conductor.RouterTransaction;",
                                         "import io.dwak.freight.internal.FreightNavigator;",
                                         "import java.lang.Override;",
                                         "import test.TestControllerBuilder;",
                                         "public class Freight_TestNavigator extends FreightNavigator implements TestNavigator {",
                                         "  public Freight_TestNavigator(Router router) {",
                                         "    super(router);",
                                         "  }",
                                         "  @Override",
                                         "  public void goToTest() {",
                                         "    final TestControllerBuilder builder = new TestControllerBuilder();",
                                         "    RouterTransaction rt = builder.asTransaction()",
                                         "        .tag(\"Test\");",
                                         "    router.pushController(rt);",
                                         "  }",
                                         "}")

    processAndAssertEquals(listOf(freightNavigatorJavaSource, inputFile),
                           "$NAVIGATOR_PACKAGE.test.TestNavigator" to expectedInterfaceOutput,
                           "$NAVIGATOR_PACKAGE.test.Freight_TestNavigator" to expectedFreightImplOutput)
  }

  @Test
  fun twoControllersNoExtrasNoScope() {
    val testController
        = JavaFileObjects.forSourceLines("test.TestController",
                                         "package test;",
                                         "import com.bluelinelabs.conductor.Controller;",
                                         "import io.dwak.freight.annotation.ControllerBuilder;",
                                         "import android.support.annotation.NonNull;",
                                         "import android.support.annotation.Nullable;",
                                         "import android.view.LayoutInflater;",
                                         "import android.view.View;",
                                         "import android.view.ViewGroup;",
                                         "@ControllerBuilder(value = \"Test\")",
                                         "public class TestController extends Controller {",
                                         "",
                                         "  @NonNull",
                                         "  @Override",
                                         "  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {",
                                         "    return null;",
                                         "  }",
                                         "}")

    val testController2
        = JavaFileObjects.forSourceLines("test.TestController2",
                                         "package test;",
                                         "import com.bluelinelabs.conductor.Controller;",
                                         "import io.dwak.freight.annotation.ControllerBuilder;",
                                         "import android.support.annotation.NonNull;",
                                         "import android.support.annotation.Nullable;",
                                         "import android.view.LayoutInflater;",
                                         "import android.view.View;",
                                         "import android.view.ViewGroup;",
                                         "@ControllerBuilder(value = \"Test2\")",
                                         "public class TestController2 extends Controller {",
                                         "",
                                         "  @NonNull",
                                         "  @Override",
                                         "  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {",
                                         "    return null;",
                                         "  }",
                                         "}")

    val expectedInterfaceOutput
        = JavaFileObjects.forSourceLines("$NAVIGATOR_PACKAGE.main.MainNavigator",
                                         "package $NAVIGATOR_PACKAGE.main;",
                                         "",
                                         "import io.dwak.freight.Navigator;",
                                         "",
                                         "public interface MainNavigator extends Navigator {",
                                         "  void goToTest();",
                                         "  void goToTest2();",
                                         "}")
    val expectedFreightImplOutput
        = JavaFileObjects.forSourceLines("$NAVIGATOR_PACKAGE.main.Freight_MainNavigator",
                                         "package $NAVIGATOR_PACKAGE.main;",
                                         "import com.bluelinelabs.conductor.Router;",
                                         "import com.bluelinelabs.conductor.RouterTransaction;",
                                         "import io.dwak.freight.internal.FreightNavigator;",
                                         "import java.lang.Override;",
                                         "import test.TestController2Builder;",
                                         "import test.TestControllerBuilder;",
                                         "public class Freight_MainNavigator extends FreightNavigator implements MainNavigator {",
                                         "  public Freight_MainNavigator(Router router) {",
                                         "    super(router);",
                                         "  }",
                                         "  @Override",
                                         "  public void goToTest() {",
                                         "    final TestControllerBuilder builder = new TestControllerBuilder();",
                                         "    RouterTransaction rt = builder.asTransaction()",
                                         "        .tag(\"Test\");",
                                         "    router.pushController(rt);",
                                         "  }",
                                         "  @Override",
                                         "  public void goToTest2() {",
                                         "    final TestController2Builder builder = new TestController2Builder();",
                                         "    RouterTransaction rt = builder.asTransaction()",
                                         "        .tag(\"Test2\");",
                                         "    router.pushController(rt);",
                                         "  }",
                                         "}")

    processAndAssertEquals(listOf(freightNavigatorJavaSource, testController, testController2),
                           "$NAVIGATOR_PACKAGE.main.MainNavigator" to expectedInterfaceOutput,
                           "$NAVIGATOR_PACKAGE.main.Freight_MainNavigator" to expectedFreightImplOutput)
  }

  @Test
  fun twoControllersNoExtrasCustomScope() {
    val testController
        = JavaFileObjects.forSourceLines("test.TestController",
                                         "package test;",
                                         "import com.bluelinelabs.conductor.Controller;",
                                         "import io.dwak.freight.annotation.ControllerBuilder;",
                                         "import android.support.annotation.NonNull;",
                                         "import android.support.annotation.Nullable;",
                                         "import android.view.LayoutInflater;",
                                         "import android.view.View;",
                                         "import android.view.ViewGroup;",
                                         "@ControllerBuilder(value = \"Test\", scope = \"Test\")",
                                         "public class TestController extends Controller {",
                                         "",
                                         "  @NonNull",
                                         "  @Override",
                                         "  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {",
                                         "    return null;",
                                         "  }",
                                         "}")

    val testController2
        = JavaFileObjects.forSourceLines("test.TestController2",
                                         "package test;",
                                         "import com.bluelinelabs.conductor.Controller;",
                                         "import io.dwak.freight.annotation.ControllerBuilder;",
                                         "import android.support.annotation.NonNull;",
                                         "import android.support.annotation.Nullable;",
                                         "import android.view.LayoutInflater;",
                                         "import android.view.View;",
                                         "import android.view.ViewGroup;",
                                         "@ControllerBuilder(value = \"Test2\", scope = \"Test\")",
                                         "public class TestController2 extends Controller {",
                                         "",
                                         "  @NonNull",
                                         "  @Override",
                                         "  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {",
                                         "    return null;",
                                         "  }",
                                         "}")

    val expectedInterfaceOutput
        = JavaFileObjects.forSourceLines("$NAVIGATOR_PACKAGE.test.TestNavigator",
                                         "package $NAVIGATOR_PACKAGE.test;",
                                         "",
                                         "import io.dwak.freight.Navigator;",
                                         "",
                                         "public interface TestNavigator extends Navigator {",
                                         "  void goToTest();",
                                         "  void goToTest2();",
                                         "}")
    val expectedFreightImplOutput
        = JavaFileObjects.forSourceLines("$NAVIGATOR_PACKAGE.test.Freight_TestNavigator",
                                         "package $NAVIGATOR_PACKAGE.test;",
                                         "import com.bluelinelabs.conductor.Router;",
                                         "import com.bluelinelabs.conductor.RouterTransaction;",
                                         "import io.dwak.freight.internal.FreightNavigator;",
                                         "import java.lang.Override;",
                                         "import test.TestController2Builder;",
                                         "import test.TestControllerBuilder;",
                                         "public class Freight_TestNavigator extends FreightNavigator implements TestNavigator {",
                                         "  public Freight_TestNavigator(Router router) {",
                                         "    super(router);",
                                         "  }",
                                         "  @Override",
                                         "  public void goToTest() {",
                                         "    final TestControllerBuilder builder = new TestControllerBuilder();",
                                         "    RouterTransaction rt = builder.asTransaction()",
                                         "        .tag(\"Test\");",
                                         "    router.pushController(rt);",
                                         "  }",
                                         "  @Override",
                                         "  public void goToTest2() {",
                                         "    final TestController2Builder builder = new TestController2Builder();",
                                         "    RouterTransaction rt = builder.asTransaction()",
                                         "        .tag(\"Test2\");",
                                         "    router.pushController(rt);",
                                         "  }",
                                         "}")

    processAndAssertEquals(listOf(freightNavigatorJavaSource, testController, testController2),
                           "$NAVIGATOR_PACKAGE.test.TestNavigator" to expectedInterfaceOutput,
                           "$NAVIGATOR_PACKAGE.test.Freight_TestNavigator" to expectedFreightImplOutput)
  }

}