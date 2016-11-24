package io.dwak.freight.util

import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler.javac
import io.dwak.freight.processor.FreightProcessor
import javax.tools.JavaFileObject

fun processAndAssertEquals(inputFile: List<JavaFileObject>,
                           vararg nameOutputPair: Pair<String, JavaFileObject>){
  println(inputFile.toString())
  val compilation = javac().withProcessors(FreightProcessor())
      .withOptions("-Xlint:-processing")
      .compile(inputFile)

  assertThat(compilation).succeededWithoutWarnings()

  nameOutputPair.forEach {
    assertThat(compilation).generatedSourceFile(it.first)
        .hasSourceEquivalentTo(it.second)
  }
}

fun processAndAssertEquals(inputFile: JavaFileObject,
                           vararg nameOutputPair: Pair<String, JavaFileObject>){
  val compilation = javac().withProcessors(FreightProcessor())
          .withOptions("-Xlint:-processing")
          .compile(inputFile)

  assertThat(compilation).succeededWithoutWarnings()

  nameOutputPair.forEach {
    assertThat(compilation).generatedSourceFile(it.first)
        .hasSourceEquivalentTo(it.second)
  }
}
