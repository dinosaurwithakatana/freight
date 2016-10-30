package io.dwak.freight.processor

import com.google.testing.compile.JavaFileObjects
import io.dwak.freight.util.processAndAssertEquals
import org.junit.Test

class FreightProcessorBuilderTest {
  @Test
  fun testBuilderNoExtras() {
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
                                             "@ControllerBuilder",
                                             "public class TestController extends Controller {",
                                             "",
                                             "  @NonNull",
                                             "  @Override",
                                             "  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {",
                                             "    return null;",
                                             "  }",
                                             "}")
    val outputFile =
            JavaFileObjects.forSourceLines("test.TestControllerBuilder",
                    "package test;",
                    "",
                    "import com.bluelinelabs.conductor.RouterTransaction;",
                    "import io.dwak.freight.internal.annotation.HasRequiredMethods;",
                    "import java.lang.SuppressWarnings;",
                    "",
                    "@SuppressWarnings(\"unused\")",
                    "public final class TestControllerBuilder {",
                    "  public TestControllerBuilder () {",
                    "  }",
                    "",
                    "  @HasRequiredMethods({})",
                    "  public final test.TestController build () {",
                    "    return new test.TestController();",
                    "  }",
                    "",
                    "  @HasRequiredMethods({})",
                    "  public final RouterTransaction asTransaction () {",
                    "    return RouterTransaction.with(build());",
                    "  }",
                    "}")

    processAndAssertEquals(inputFile, outputFile)

  }
}