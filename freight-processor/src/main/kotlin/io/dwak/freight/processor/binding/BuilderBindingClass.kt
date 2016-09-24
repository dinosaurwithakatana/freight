package io.dwak.freight.processor.binding

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.vishnurajeevan.javapoet.dsl.classType
import com.vishnurajeevan.javapoet.dsl.model.JavaPoetValue
import io.dwak.freight.processor.model.FieldBinding
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
      val suppressWarningAnnotation = AnnotationSpec.builder(SuppressWarnings::class.java)
              .addMember("value", "\$S", "unused")
              .build()
      annotations = setOf(suppressWarningAnnotation)
      field(setOf(PRIVATE, FINAL), bundle, "bundle")
      constructor(PUBLIC) {
        statement("this.bundle = new \$T()", bundle)
      }

      bindings.values
              .forEach {
                val parameterType = JavaPoetValue(FINAL, TypeName.get(it.type), "value")
                method(setOf(PUBLIC, FINAL),
                       generatedClassName,
                       it.builderMethodName,
                       setOf(parameterType)) {
                  statement(getBundleStatement(it, "value").second)
                  statement("return this")
                }
              }

      method(setOf(PUBLIC, FINAL), targetClassName, "build") {
        annotations = setOf(generateHasRequiredMethodsAnnotation())
        statement("return new \$T(bundle)", targetClassName)
      }

      val routerTransaction = ClassName.get("com.bluelinelabs.conductor", "RouterTransaction")

      method(setOf(PUBLIC, FINAL), routerTransaction, "asTransaction"){
        annotations = setOf(generateHasRequiredMethodsAnnotation())
        statement("return \$T.with(build())", routerTransaction)
      }

    }
  }

  private fun generateHasRequiredMethodsAnnotation(): AnnotationSpec {
    val hasRequiredMethodAnnotation = ClassName.get("io.dwak.freight.internal.annotation",
                                                    "HasRequiredMethods")
    val hasRequiredAnnotationBuilder = AnnotationSpec.builder(hasRequiredMethodAnnotation)
    var arrayString = "{"
    bindings.values
            .filter { it.isRequired }
            .take(bindings.values.size - 1)
            .forEach {
              arrayString += "\"${it.builderMethodName}\""
              arrayString += ",\n"
            }
    val last = bindings.values.last()
    if(last.isRequired && !arrayString.contains(last.builderMethodName)) {
      arrayString += "\"${last.builderMethodName}\""
    }

    arrayString += "}"
    hasRequiredAnnotationBuilder.addMember("value", "\$L", arrayString);
    return hasRequiredAnnotationBuilder.build()
  }

  private fun getBundleStatement(fieldBinding: FieldBinding, param: String): Pair<Boolean, String> {
    return handleType(fieldBinding, {
      "bundle.put$it(\"${fieldBinding.key}\", $param)"
    })
  }

}