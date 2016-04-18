package io.dwak.freight.processor

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.vishnurajeevan.javapoet.dsl.classType
import com.vishnurajeevan.javapoet.dsl.model.JavaPoetValue
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.*

class BuilderBindingClass(classPackage: String,
                          className: String,
                          targetClass: String,
                          processingEnvironment: ProcessingEnvironment)
: AbstractBindingClass(classPackage, className, targetClass, processingEnvironment) {

  companion object {
    val CLASS_SUFFIX = "Builder"
  }

  override fun createAndAddBinding(element: Element) {
    val binding = FieldBinding(element)
    bindings.put(binding.name, binding)
  }

  override fun generate(): TypeSpec {
    return classType(setOf(PUBLIC, FINAL), className) {
      annotations = setOf(AnnotationSpec.builder(SuppressWarnings::class.java)
                                  .addMember("value", "\$S", "unused")
                                  .build())
      field(setOf(PRIVATE, FINAL), bundle, "bundle")
      constructor(PUBLIC) {
        statement("this.bundle = new \$T()", bundle)
      }

      bindings.values
              .forEach {
                method(setOf(PUBLIC, FINAL),
                       ClassName.get(classPackage, className),
                       it.name,
                       setOf(JavaPoetValue(FINAL, TypeName.get(it.type), "value"))){

                  statement(getBundleStatement(it, "value"))
                  statement("return this")
                }
              }

      method(setOf(PUBLIC, FINAL), targetClassName , "build") {
        statement("return new \$T(bundle)", targetClassName)
      }

    }
  }

  fun getBundleStatement(it: FieldBinding, param : String): String {
    var bundleStatement = ""
    when (TypeName.get(it.type)) {
      string       -> bundleStatement = "bundle.putString(\"${it.key}\", $param)"
      charsequence -> bundleStatement = "bundle.putCharSequence(\"${it.key}\", $param)"
      integer      -> bundleStatement = "bundle.putInt(\"${it.key}\", $param)"
      float        -> bundleStatement = "bundle.putFloat(\"${it.key}\", $param)"
      character    -> bundleStatement = "bundle.putChar(\"${it.key}\", $param)"
      bundle       -> bundleStatement = "bundle.putBundle(\"${it.key}\", $param)"
      iBinder      -> bundleStatement = "bundle.putBinder(\"${it.key}\", $param)"
      byte         -> bundleStatement = "bundle.putByte(\"${it.key}\", $param)"
      short        -> bundleStatement = "bundle.putShort(\"${it.key}\", $param)"
      boolean      -> bundleStatement = "bundle.putBoolean(\"${it.key}\", $param)"
    }

    return bundleStatement
  }
}