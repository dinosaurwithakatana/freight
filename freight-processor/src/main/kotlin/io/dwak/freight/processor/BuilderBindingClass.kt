package io.dwak.freight.processor

import com.squareup.javapoet.AnnotationSpec
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
                       generatedClassName,
                       it.builderMethodName,
                       setOf(JavaPoetValue(FINAL, TypeName.get(it.type), "value"))) {

                  statement(getBundleStatement(it, "value").second)
                  statement("return this")
                }
              }

      method(setOf(PUBLIC, FINAL), targetClassName, "build") {
        statement("return new \$T(bundle)", targetClassName)
      }

    }
  }

  fun getBundleStatement(fieldBinding: FieldBinding, param: String): Pair<Boolean, String> {
    return handleType(fieldBinding, {
      "bundle.put$it(\"${fieldBinding.key}\", $param)"
    })
  }

}