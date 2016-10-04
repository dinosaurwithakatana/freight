package io.dwak.freight.processor;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class FreightProcessorTest {
    @Test
    public void testSingleIntegerExtra() throws Exception {
        JavaFileObject inputFile = JavaFileObjects.forSourceLines("test.TestController",
                "package test;",
                "import com.bluelinelabs.conductor.Controller;",
                "import io.dwak.Extra;",
                "public class TestController extends Controller {",
                "  @Extra int integerExtra;",
                "}");
        JavaFileObject expectedOutput = JavaFileObjects.forSourceLines("test/TestController$$FreightTrain",
                "package test;",
                "import android.os.Bundle;",
                "import io.dwak.freight.internal.IFreightTrain;",
                "import java.lang.Override;",
                "import java.lang.SuppressWarnings;",
                "@SuppressWarnings(\"unused\")",
                "public class TestController$$FreightTrain<T extends test.TestController> implements IFreightTrain<T> {",
                "  @Override",
                "  @SuppressWarnings(\"unused\")",
                "  public void ship(final T target) {",
                "    final Bundle bundle = target.getArgs();",
                "    target.integerExtra = bundle.getInt(\"INTEGEREXTRA\");",
                "  }",
                "}");

        assertAbout(javaSource())
                .that(inputFile)
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(new FreightProcessor())
                .compilesWithoutWarnings();
//                .and()
//                .generatesSources(expectedOutput);
    }
}
