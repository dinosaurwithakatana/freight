package io.dwak.freight.processor

import com.google.testing.compile.JavaFileObjects
import io.dwak.freight.util.processAndAssertEquals
import org.junit.Test

class FreightProcessorExtraTest {

  @Test
  @Throws(Exception::class)
  fun testSingleBoolean() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Extra boolean booleanExtra;",
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
                                             "    target.booleanExtra = bundle.getBoolean(\"BOOLEANEXTRA\");",
                                             "  }",
                                             "}")

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  @Throws(Exception::class)
  fun testSingleBooleanObject() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
                                             "import java.lang.Boolean;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Extra Boolean booleanExtra;",
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
                                             "    target.booleanExtra = bundle.getBoolean(\"BOOLEANEXTRA\");",
                                             "  }",
                                             "}")

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  fun singleOptionalBooleanObject() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
                                             "import java.lang.Boolean;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Nullable @Extra Boolean booleanExtra;",
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
                                             "    target.booleanExtra = bundle.containsKey(\"BOOLEANEXTRA\") ? bundle.getBoolean(\"BOOLEANEXTRA\") : null ;",
                                             "  }",
                                             "}")

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  @Throws(Exception::class)
  fun testSingleInteger() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
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

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  fun singleOptionalInteger() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
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

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  @Throws(Exception::class)
  fun testSingleIntegerObject() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
                                             "import java.lang.Integer;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Extra Integer integerExtra;",
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

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  fun singleOptionalIntegerObject() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
                                             "import java.lang.Integer;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Nullable @Extra Integer integerExtra;",
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

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  @Throws(Exception::class)
  fun testSingleFloat() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Extra float floatExtra;",
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
                                             "    target.floatExtra = bundle.getFloat(\"FLOATEXTRA\");",
                                             "  }",
                                             "}")

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  fun singleOptionalFloat() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Nullable @Extra float floatExtra;",
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
                                             "    target.floatExtra = bundle.containsKey(\"FLOATEXTRA\") ? bundle.getFloat(\"FLOATEXTRA\") : null ;",
                                             "  }",
                                             "}")

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  @Throws(Exception::class)
  fun testSingleFloatObject() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
                                             "import java.lang.Float;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Extra Float floatExtra;",
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
                                             "    target.floatExtra = bundle.getFloat(\"FLOATEXTRA\");",
                                             "  }",
                                             "}")

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  fun singleOptionalFloatObject() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
                                             "import java.lang.Float;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Nullable @Extra Float floatExtra;",
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
                                             "    target.floatExtra = bundle.containsKey(\"FLOATEXTRA\") ? bundle.getFloat(\"FLOATEXTRA\") : null ;",
                                             "  }",
                                             "}")

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  @Throws(Exception::class)
  fun testSingleCharSequence() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
                                             "import java.lang.CharSequence;",
                                             "import android.support.annotation.NonNull;",
                                             "import android.support.annotation.Nullable;",
                                             "import android.view.LayoutInflater;",
                                             "import android.view.View;",
                                             "import android.view.ViewGroup;",
                                             "public class TestController extends Controller {",
                                             "  @Extra CharSequence charSequenceExtra;",
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
                                             "    target.charSequenceExtra = bundle.getCharSequence(\"CHARSEQUENCEEXTRA\");",
                                             "  }",
                                             "}")

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  fun singleOptionalCharSequence() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
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

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  @Throws(Exception::class)
  fun testSingleString() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
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

    processAndAssertEquals(inputFile, expectedOutput)
  }

  @Test
  fun singleOptionalString() {
    val inputFile
            = JavaFileObjects.forSourceLines("test.TestController",
                                             "package test;",
                                             "import com.bluelinelabs.conductor.Controller;",
                                             "import io.dwak.freight.annotation.Extra;",
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

    processAndAssertEquals(inputFile, expectedOutput)
  }
}
