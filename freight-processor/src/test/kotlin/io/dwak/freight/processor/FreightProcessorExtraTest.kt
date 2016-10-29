package io.dwak.freight.processor

import com.google.testing.compile.JavaFileObjects

import org.junit.Test

import javax.tools.JavaFileObject

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource

class FreightProcessorExtraTest {
  @Test
  @Throws(Exception::class)
  fun testSingleInteger() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.Extra;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Extra int integerExtra;",
                                             "",
                                             "  @NonNull",
                                             "  @Override",
                                             "  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {",
                                             "    return null;",
                                             "  }",
                                             "}")
    val expectedOutput
            = JavaFileObjects.forSourceLines("test/TestController$\$FreightTrain",
                                             "package test;",
                                             "import android.os.Bundle;",
                                             "import io.dwak.freight.internal.IFreightTrain;",
                                             "import java.lang.Override;",
                                             "import java.lang.SuppressWarnings;",
                                             "@SuppressWarnings(\"unused\")",
                                             "public class TestController$\$FreightTrain<T extends test.TestController> implements IFreightTrain<T> {",
                                             "  @Override",
                                             "  @SuppressWarnings(\"unused\")",
                                             "  public void ship(final T target) {",
                                             "    final Bundle bundle = target.getArgs();",
                                             "    target.integerExtra = bundle.getInt(\"INTEGEREXTRA\");",
                                             "  }",
                                             "}")

    assertAbout(javaSource())
            .that(inputFile)
            .withCompilerOptions("-Xlint:-processing")
            .processedWith(FreightProcessor())
            .compilesWithoutWarnings()
            .and()
            .generatesSources(expectedOutput)
  }

  @Test
  fun singleOptionalInteger() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.Extra;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Nullable @Extra int integerExtra;",
                                             "",
                                             "  @NonNull",
                                             "  @Override",
                                             "  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {",
                                             "    return null;",
                                             "  }",
                                             "}")
    val expectedOutput
            = JavaFileObjects.forSourceLines("test/TestController$\$FreightTrain",
                                             "package test;",
                                             "import android.os.Bundle;",
                                             "import io.dwak.freight.internal.IFreightTrain;",
                                             "import java.lang.Override;",
                                             "import java.lang.SuppressWarnings;",
                                             "@SuppressWarnings(\"unused\")",
                                             "public class TestController$\$FreightTrain<T extends test.TestController> implements IFreightTrain<T> {",
                                             "  @Override",
                                             "  @SuppressWarnings(\"unused\")",
                                             "  public void ship(final T target) {",
                                             "    final Bundle bundle = target.getArgs();",
                                             "    target.integerExtra = bundle.containsKey(\"INTEGEREXTRA\") ? bundle.getInt(\"INTEGEREXTRA\") : null ;",
                                             "  }",
                                             "}")

    assertAbout(javaSource())
            .that(inputFile)
            .withCompilerOptions("-Xlint:-processing")
            .processedWith(FreightProcessor())
            .compilesWithoutWarnings()
            .and()
            .generatesSources(expectedOutput)
  }

  @Test
  @Throws(Exception::class)
  fun testSingleString() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.Extra;",
                                             "import java.lang.String;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Extra String stringExtra;",
                                             "",
                                             "  @NonNull",
                                             "  @Override",
                                             "  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {",
                                             "    return null;",
                                             "  }",
                                             "}")
    val expectedOutput
            = JavaFileObjects.forSourceLines("test/TestController$\$FreightTrain",
                                             "package test;",
                                             "import android.os.Bundle;",
                                             "import io.dwak.freight.internal.IFreightTrain;",
                                             "import java.lang.Override;",
                                             "import java.lang.SuppressWarnings;",
                                             "@SuppressWarnings(\"unused\")",
                                             "public class TestController$\$FreightTrain<T extends test.TestController> implements IFreightTrain<T> {",
                                             "  @Override",
                                             "  @SuppressWarnings(\"unused\")",
                                             "  public void ship(final T target) {",
                                             "    final Bundle bundle = target.getArgs();",
                                             "    target.stringExtra = bundle.getString(\"STRINGEXTRA\");",
                                             "  }",
                                             "}")

    assertAbout(javaSource())
            .that(inputFile)
            .withCompilerOptions("-Xlint:-processing")
            .processedWith(FreightProcessor())
            .compilesWithoutWarnings()
            .and()
            .generatesSources(expectedOutput)
  }

  @Test
  fun singleOptionalString() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.Extra;",
                                             "import java.lang.String;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Nullable @Extra String stringExtra;",
                                             "",
                                             "  @NonNull",
                                             "  @Override",
                                             "  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {",
                                             "    return null;",
                                             "  }",
                                             "}")
    val expectedOutput
            = JavaFileObjects.forSourceLines("test/TestController$\$FreightTrain",
                                             "package test;",
                                             "import android.os.Bundle;",
                                             "import io.dwak.freight.internal.IFreightTrain;",
                                             "import java.lang.Override;",
                                             "import java.lang.SuppressWarnings;",
                                             "@SuppressWarnings(\"unused\")",
                                             "public class TestController$\$FreightTrain<T extends test.TestController> implements IFreightTrain<T> {",
                                             "  @Override",
                                             "  @SuppressWarnings(\"unused\")",
                                             "  public void ship(final T target) {",
                                             "    final Bundle bundle = target.getArgs();",
                                             "    target.stringExtra = bundle.containsKey(\"STRINGEXTRA\") ? bundle.getString(\"STRINGEXTRA\") : null ;",
                                             "  }",
                                             "}")

    assertAbout(javaSource())
            .that(inputFile)
            .withCompilerOptions("-Xlint:-processing")
            .processedWith(FreightProcessor())
            .compilesWithoutWarnings()
            .and()
            .generatesSources(expectedOutput)
  }
}
