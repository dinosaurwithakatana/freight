package io.dwak.freight.processor.binding

import com.squareup.javapoet.*
import io.dwak.freight.processor.extension.capitalizeFirst
import io.dwak.freight.processor.model.ClassBinding
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.*


class NavigatorBindingClass(classPackage : String,
                            className : String,
                            targetClass : String,
                            processingEnvironment : ProcessingEnvironment)
: AbstractBindingClass(classPackage, className, targetClass, processingEnvironment) {

  companion object {
    val CLASS_SUFFIX = "Navigator"
  }

  override fun createAndAddBinding(element : Element) {
    val binding = ClassBinding(element)
    bindings.put(binding.screenName, binding)
  }

  override fun generate() : TypeSpec {
    val navigator = TypeSpec.interfaceBuilder(className).addModifiers(PUBLIC)
            .addSuperinterface(ClassName.get("io.dwak.freight", "Navigator"))
    bindings.values
            .map { it as ClassBinding }
            .forEach {
              val methodBuilder = MethodSpec.methodBuilder("goTo${it.screenName.capitalizeFirst()}")
                      .addModifiers(PUBLIC, ABSTRACT)
                      .returns(TypeName.VOID)

              it.enclosedExtras.forEach {
                val parameterBuilder = ParameterSpec.builder(TypeName.get(it.type),
                                                             it.builderMethodName)
                        .addModifiers(FINAL)
                if (!it.isRequired) {
                  parameterBuilder.addAnnotation(ClassName.get("android.support.annotation",
                                                               "Nullable"))
                }
                methodBuilder.addParameter(parameterBuilder.build())
              }
              navigator.addMethod(methodBuilder.build())
            }

    return navigator.build()
  }
}