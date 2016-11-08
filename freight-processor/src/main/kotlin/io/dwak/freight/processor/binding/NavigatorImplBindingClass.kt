package io.dwak.freight.processor.binding

import com.squareup.javapoet.*
import io.dwak.freight.processor.extension.capitalizeFirst
import io.dwak.freight.processor.model.ClassBinding
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.*
import javax.lang.model.element.TypeElement
import kotlin.properties.Delegates


class NavigatorImplBindingClass(classPackage : String,
                                className : String,
                                targetClass : String,
                                processingEnvironment : ProcessingEnvironment)
: AbstractBindingClass(classPackage, className, targetClass, processingEnvironment) {

  companion object {
    val CLASS_PREFIX = "Freight_"
    val CLASS_SUFFIX = "Navigator"
  }

  private var builderClasses = hashMapOf<String, ClassName>()

  override fun createAndAddBinding(element : Element) {
    val binding = ClassBinding(element)
    bindings.put(binding.screenName, binding)
  }

  fun createAndAddBinding(element : TypeElement, builderClass : ClassName) {
    this.createAndAddBinding(element)
    this.builderClasses.put(builderClass.simpleName(), builderClass)
  }

  override fun generate() : TypeSpec {
    val routerClassName = ClassName.get("com.bluelinelabs.conductor",
                                        "Router")
    val constructor = MethodSpec.constructorBuilder()
            .addModifiers(PUBLIC)
            .addParameter(ParameterSpec.builder(routerClassName,
                                                "router")
                                  .build())
            .addStatement("this.\$N = \$N", "router", "router")
            .build()

    val navigator = TypeSpec.classBuilder(className).addModifiers(PUBLIC)
            .addSuperinterface(ClassName.get(classPackage, className.removePrefix(CLASS_PREFIX)))
            .addField(FieldSpec.builder(routerClassName, "router")
                              .addModifiers(PRIVATE, FINAL)
                              .build())
            .addMethod(constructor)

    note("bindings ${bindings.values}")
    bindings.values
            .map { it as ClassBinding }
            .forEach {
              val methodBuilder = MethodSpec.methodBuilder("goTo${it.screenName.capitalizeFirst()}")
                      .addModifiers(PUBLIC)
                      .returns(TypeName.VOID)

              val builderClassName = builderClasses["${it.name}Builder"]
              methodBuilder.addStatement("final \$T builder = new \$T()",
                                         builderClassName,
                                         builderClassName)

              it.enclosedExtras.forEach {
                val parameterBuilder = ParameterSpec.builder(TypeName.get(it.type),
                                                             it.builderMethodName)
                        .addModifiers(FINAL)
                if (!it.isRequired) {
                  parameterBuilder.addAnnotation(ClassName.get("android.support.annotation",
                                                               "Nullable"))
                }
                methodBuilder.addParameter(parameterBuilder.build())

                methodBuilder.addStatement("builder.\$L(\$L)",
                                           it.builderMethodName,
                                           it.builderMethodName)
              }

              methodBuilder.addStatement("router.pushController(builder.asTransaction())")
              navigator.addMethod(methodBuilder.build())
            }

    return navigator.build()
  }

}