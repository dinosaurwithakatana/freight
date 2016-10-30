package io.dwak.freight.util

import com.google.common.truth.Truth
import com.google.testing.compile.JavaSourceSubjectFactory
import io.dwak.freight.processor.FreightProcessor
import javax.tools.JavaFileObject

fun processAndAssertEquals(inputFile: JavaFileObject,
                           expectedOutput: JavaFileObject) {
  Truth.assertAbout(JavaSourceSubjectFactory.javaSource())
          .that(inputFile)
          .withCompilerOptions("-Xlint:-processing")
          .processedWith(FreightProcessor())
          .compilesWithoutWarnings()
          .and()
          .generatesSources(expectedOutput)
}
