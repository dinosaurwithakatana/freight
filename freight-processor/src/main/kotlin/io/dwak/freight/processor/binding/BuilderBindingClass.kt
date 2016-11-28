package io.dwak.freight.processor.binding

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.vishnurajeevan.javapoet.dsl.classType
import com.vishnurajeevan.javapoet.dsl.model.JavaPoetValue
import io.dwak.freight.processor.model.ClassBinding
import io.dwak.freight.processor.model.FieldBinding
import java.lang.invoke.MethodHandleProxies
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.*

class BuilderBindingClass(classPackage: String,
                          className: String,
                          targetClass: String,
                          processingEnvironment: ProcessingEnvironment)
  : AbstractBindingClass(classPackage, className, targetClass, processingEnvironment) {

  private var enclosedExtrasBindings: List<FieldBinding> = emptyList()
  private val hasBundle by lazy { enclosedExtrasBindings.isNotEmpty() }

  companion object {
    val CLASS_SUFFIX = "Builder"
  }

  override fun createAndAddBinding(element: Element) {
    val binding = ClassBinding(element)
    bindings.put(binding.name, binding)
  }

  fun createAndAddBinding(element: Element, enclosedExtrasBindings: List<FieldBinding>) {
    createAndAddBinding(element)
    this.enclosedExtrasBindings = enclosedExtrasBindings
  }

  override fun generate(): TypeSpec {
    val classBuilder = TypeSpec.classBuilder(className)
        .addModifiers(PUBLIC, FINAL)
        .addAnnotation(AnnotationSpec.builder(SuppressWarnings::class.java)
                           .addMember("value", "\$S", "unused")
                           .build())

    val constructorBuilder = MethodSpec.constructorBuilder().addModifiers(PUBLIC)

    if (hasBundle) {
      classBuilder.addField(FieldSpec.builder(bundle, "bundle", PRIVATE, FINAL).build())
      constructorBuilder.addStatement("this.bundle = new \$T()", bundle)
    }

    classBuilder.addMethod(constructorBuilder.build())

    enclosedExtrasBindings
        .forEach {
          val parameter = ParameterSpec.builder(TypeName.get(it.type), "value", FINAL).build()
          classBuilder.addMethod(MethodSpec.methodBuilder(it.builderMethodName)
                                     .addModifiers(PUBLIC, FINAL)
                                     .returns(generatedClassName)
                                     .addParameter(parameter)
                                     .addStatement(getBundleStatement(it, "value").second)
                                     .addStatement("return this")
                                     .build())
        }

    val buildMethodBuilder= MethodSpec.methodBuilder("build")
        .addModifiers(PUBLIC, FINAL)
        .returns(targetClassName)
        .addAnnotation(generateHasRequiredMethodsAnnotation())

    when {
      hasBundle -> buildMethodBuilder.addStatement("return new \$T(bundle)", targetClassName)
      else -> buildMethodBuilder.addStatement("return new \$T()", targetClassName)
    }

    classBuilder.addMethod(buildMethodBuilder.build())
    val routerTransaction = ClassName.get("com.bluelinelabs.conductor", "RouterTransaction")
    classBuilder.addMethod(MethodSpec.methodBuilder("asTransaction")
                               .addAnnotation(generateHasRequiredMethodsAnnotation())
                               .addModifiers(PUBLIC, FINAL)
                               .returns(routerTransaction)
                               .addStatement("return \$T.with(build())", routerTransaction)
                               .build())
    return classBuilder.build()
  }

  private fun generateHasRequiredMethodsAnnotation(): AnnotationSpec {
    val hasRequiredMethodAnnotation = ClassName.get("io.dwak.freight.internal.annotation",
                                                    "HasRequiredMethods")
    val hasRequiredAnnotationBuilder = AnnotationSpec.builder(hasRequiredMethodAnnotation)
    var arrayString = "{"
    if (hasBundle) {
      enclosedExtrasBindings
          .filter { it.isRequired }
          .take(enclosedExtrasBindings.size - 1)
          .forEach {
            arrayString += "\"${it.builderMethodName}\""
            arrayString += ",\n"
          }
      val last = enclosedExtrasBindings.last()
      if (last.isRequired && !arrayString.contains(last.builderMethodName)) {
        arrayString += "\"${last.builderMethodName}\""
      }
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