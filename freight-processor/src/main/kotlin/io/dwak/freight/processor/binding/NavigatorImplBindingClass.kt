package io.dwak.freight.processor.binding

import com.squareup.javapoet.*
import io.dwak.freight.processor.extension.capitalizeFirst
import io.dwak.freight.processor.extension.getTypeMirror
import io.dwak.freight.processor.model.ClassBinding
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.*
import javax.lang.model.element.TypeElement
import kotlin.properties.Delegates


class NavigatorImplBindingClass(classPackage: String,
                                className: String,
                                targetClass: String,
                                processingEnvironment: ProcessingEnvironment)
  : AbstractBindingClass(classPackage, className, targetClass, processingEnvironment) {

  companion object {
    val CLASS_PREFIX = "Freight_"
    val CLASS_SUFFIX = "Navigator"
  }

  private val builderClasses = hashMapOf<String, ClassName>()

  override fun createAndAddBinding(element: Element) {
    val binding = ClassBinding(element)
    bindings.put(binding.screenName, binding)
  }

  fun createAndAddBinding(element: TypeElement, builderClass: ClassName) {
    this.createAndAddBinding(element)
    builderClasses.put(builderClass.simpleName(), builderClass)
  }

  override fun generate(): TypeSpec {
    val routerClassName = ClassName.get("com.bluelinelabs.conductor",
                                        "Router")
    val constructor = MethodSpec.constructorBuilder()
        .addModifiers(PUBLIC)
        .addParameter(ParameterSpec.builder(routerClassName,
                                            "router")
                          .build())
        .addStatement("super(\$N)", "router")
        .build()

    val navigator = TypeSpec.classBuilder(className).addModifiers(PUBLIC)
        .superclass(ClassName.get("io.dwak.freight.internal", "FreightNavigator"))
        .addSuperinterface(ClassName.get(classPackage, className.removePrefix(CLASS_PREFIX)))
        .addMethod(constructor)


    bindings.values
        .map { it as ClassBinding }
        .forEach { binding: ClassBinding ->
          val methodBuilder = MethodSpec.methodBuilder("goTo${binding.screenName.capitalizeFirst()}")
              .addAnnotation(Override::class.java)
              .addModifiers(PUBLIC)
              .returns(TypeName.VOID)

          val builderClassName = builderClasses["${binding.name}Builder"]
          methodBuilder.addStatement("final \$T builder = new \$T()",
                                     builderClassName,
                                     builderClassName)

          binding.enclosedExtras.forEach {
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

          val routerTransaction = ClassName.get("com.bluelinelabs.conductor",
                                                "RouterTransaction")
          methodBuilder.addStatement("\$T rt = builder.asTransaction()\n.tag(\"\$L\")",
                                     routerTransaction,
                                     binding.screenName)
          val controllerChangeHandler = processingEnvironment
              .getTypeMirror("com.bluelinelabs.conductor.ControllerChangeHandler")

          val voidType = processingEnvironment
              .getTypeMirror("java.lang.Void")
          binding.popChangeHandler?.let {
            if (typeUtils.isSameType(it, voidType)) {
              return@let
            }
            if(!typeUtils.isSubtype(it, controllerChangeHandler)) {
              error("PopChangeHandler must be of type ControllerChangeHandler. Is $it")
            }
            methodBuilder.addStatement("rt.popChangeHandler(new \$T())", it)
          }

          binding.pushChangeHandler?.let {
            if (typeUtils.isSameType(it, voidType)) {
              return@let
            }
            if(!typeUtils.isSubtype(it, controllerChangeHandler)) {
              error("PushChangeHandler must be of type ControllerChangeHandler. Is $it")
            }
            methodBuilder.addStatement("rt.pushChangeHandler(new \$T())", it)
          }

          methodBuilder.addStatement("router.pushController(rt)")
          navigator.addMethod(methodBuilder.build())
        }

    return navigator.build()
  }

}