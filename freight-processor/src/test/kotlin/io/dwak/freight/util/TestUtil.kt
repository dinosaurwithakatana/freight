package io.dwak.freight.util

import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler.javac
import io.dwak.freight.processor.FreightProcessor
import javax.tools.JavaFileObject

fun processAndAssertEquals(inputFile: JavaFileObject,
                           outputQualifiedName: String,
                           expectedOutput: JavaFileObject) {
  val compilation = javac().withProcessors(FreightProcessor())
          .withOptions("-Xlint:-processing")
          .compile(inputFile)

  assertThat(compilation).succeededWithoutWarnings()

  assertThat(compilation).generatedSourceFile(outputQualifiedName)
          .hasSourceEquivalentTo(expectedOutput)
}
