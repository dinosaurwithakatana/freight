package io.dwak.freight.processor

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import org.junit.Test

class FreightProcessorTest {
  @Test
  fun testSingleIntegerExtraNoBuilder() {
    val inputFileLines = listOf("package test;",
                                "import com.bluelinelabs.conductor.Controller;",
                                "import io.dwak.Extra;",
                                "public class TestController extends Controller {",
                                "  @Extra int integerExtra;",
                                "}")
    val expectedOutputLines = listOf("package test;",
                                     "import android.os.Bundle;",
                                     "import io.dwak.freight.internal.IFreightTrain;",
                                     "import java.lang.Override;",
                                     "import java.lang.SuppressWarnings;",
                                     "@SuppressWarnings(\"unused\")",
                                     "public class TestController\$\$FreightTrain<T extends test.TestController> implements IFreightTrain<T> {",
                                     "  @Override",
                                     "  @SuppressWarnings(\"unused\")",
                                     "  public void ship(final T target) {",
                                     "    final Bundle bundle = target.getArgs();",
                                     "    target.integerExtra = bundle.getInt(\"INTEGEREXTRA\");",
                                     "  }",
                                     "}")

    val inputFile = JavaFileObjects.forSourceLines("test.TestController", inputFileLines)
    val expectedOutput = JavaFileObjects.forSourceLines("test/TestController\$\$FreightTrain",
                                                        expectedOutputLines)

    assertAbout(javaSource())
            .that(inputFile)
            .withCompilerOptions("-Xlint:-processing")
            .processedWith(FreightProcessor())
            .compilesWithoutWarnings()
//            .and()
//            .generatesSources(expectedOutput)
  }
}
