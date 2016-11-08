package io.dwak.freight.processor

import com.squareup.javapoet.ClassName
import io.dwak.freight.annotation.ControllerBuilder
import io.dwak.freight.annotation.Extra
import io.dwak.freight.processor.binding.BuilderBindingClass
import io.dwak.freight.processor.binding.FreightTrainBindingClass
import io.dwak.freight.processor.binding.NavigatorBindingClass
import io.dwak.freight.processor.binding.NavigatorImplBindingClass
import io.dwak.freight.processor.extension.className
import io.dwak.freight.processor.extension.hasAnnotationWithName
import io.dwak.freight.processor.extension.packageName
import io.dwak.freight.processor.model.FieldBinding
import java.io.IOException
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

open class FreightProcessor : AbstractProcessor() {

  private lateinit var filer : Filer
  private lateinit var messager : Messager
  private lateinit var elementUtils : Elements

  override fun init(processingEnv : ProcessingEnvironment) {
    super.init(processingEnv)
    this.filer = processingEnv.filer
    this.messager = processingEnv.messager
    this.elementUtils = processingEnv.elementUtils
  }

  override fun getSupportedAnnotationTypes()
          = mutableSetOf(Extra::class.java.canonicalName,
                         ControllerBuilder::class.java.canonicalName)

  override fun getSupportedSourceVersion() = SourceVersion.latestSupported()

  override fun process(annotations : MutableSet<out TypeElement>,
                       roundEnv : RoundEnvironment) : Boolean {

    if (annotations.isNotEmpty()) {
      val freightTrainTargetClassMap = hashMapOf<TypeElement, FreightTrainBindingClass>()
      val builderTargetClassMap = hashMapOf<TypeElement, BuilderBindingClass>()
      val navigatorTargetClassMap = hashMapOf<String, NavigatorBindingClass>()
      val navigatorImplTargetClassMap = hashMapOf<String, NavigatorImplBindingClass>()
      val erasedTargetNames = mutableSetOf<String>()
      annotations.forEach {
        typeElement : TypeElement ->
        val elements = roundEnv.getElementsAnnotatedWith(typeElement)

        elements.filter { it.hasAnnotationWithName(Extra::class.java.simpleName) }
                .forEach {
                  try {
                    val enclosingTypeElement = it.enclosingElement as TypeElement
                    val shipperClass = getOrCreateFreightTrain(freightTrainTargetClassMap,
                                                               enclosingTypeElement,
                                                               erasedTargetNames)
                    shipperClass.createAndAddBinding(it)

                  } catch (e : Exception) {

                  }
                }

        elements.filter { it.hasAnnotationWithName(ControllerBuilder::class.java.simpleName) }
                .forEach {
                  val enclosedExtrasElements = arrayListOf<FieldBinding>()

                  it.enclosedElements
                          .filter { it.hasAnnotationWithName(Extra::class.java.simpleName) }
                          .map(::FieldBinding)
                          .forEach { enclosedExtrasElements.add(it) }

                  val builderClass = getOrCreateBuilder(builderTargetClassMap,
                                                        it as TypeElement,
                                                        erasedTargetNames)
                  builderClass.createAndAddBinding(it, enclosedExtrasElements)

                  val annotationInstance = it.getAnnotation(ControllerBuilder::class.java)
                  val scopeName = annotationInstance.scope
                  val screenName = annotationInstance.value
                  note(scopeName)
                  if (screenName.isNotEmpty()) {
                    val navigatorClass = getOrCreateNavigator(navigatorTargetClassMap,
                                                              it as TypeElement,
                                                              erasedTargetNames,
                                                              scopeName)
                    navigatorClass.createAndAddBinding(it)

                    val navigatorImplClass = getOrCreateNavigatorImpl(navigatorImplTargetClassMap,
                                                                      it as TypeElement,
                                                                      erasedTargetNames,
                                                                      scopeName)
                    navigatorImplClass.createAndAddBinding(it,
                                                           ClassName.get(builderClass.classPackage,
                                                                         builderClass.className))
                  }
                }
      }

      (freightTrainTargetClassMap.values
       + builderTargetClassMap.values
       + navigatorTargetClassMap.values
       + navigatorImplTargetClassMap.values)
              .forEach {
                try {
                  it.writeToFiler(filer)
                } catch (e : IOException) {
                  messager.printMessage(Diagnostic.Kind.ERROR, e.message)
                }
              }
    }

    return true
  }

  private fun getOrCreateFreightTrain(targetClassMap : MutableMap<TypeElement, FreightTrainBindingClass>,
                                      enclosingElement : TypeElement,
                                      erasedTargetNames : MutableSet<String>)
          : FreightTrainBindingClass {
    var freightTrainClass = targetClassMap[enclosingElement]
    if (freightTrainClass == null) {
      val targetClass = enclosingElement.qualifiedName.toString()
      val classPackage = enclosingElement.packageName(elementUtils)
      val className = enclosingElement.className(classPackage) + FreightTrainBindingClass.CLASS_SUFFIX
      freightTrainClass = FreightTrainBindingClass(classPackage,
                                                   className,
                                                   targetClass,
                                                   processingEnv)
      targetClassMap.put(enclosingElement, freightTrainClass)
      erasedTargetNames.add(enclosingElement.toString())
    }

    return freightTrainClass
  }

  private fun getOrCreateBuilder(targetClassMap : MutableMap<TypeElement, BuilderBindingClass>,
                                 element : TypeElement,
                                 erasedTargetNames : MutableSet<String>)
          : BuilderBindingClass {
    var builderClass = targetClassMap[element]
    if (builderClass == null) {
      val targetClass = element.qualifiedName.toString()
      val classPackage = element.packageName(elementUtils)
      val className = element.className(classPackage) + BuilderBindingClass.CLASS_SUFFIX
      builderClass = BuilderBindingClass(classPackage, className, targetClass, processingEnv)
      targetClassMap.put(element, builderClass)
      erasedTargetNames.add(element.toString())
    }

    return builderClass
  }

  private fun getOrCreateNavigator(targetClassMap : MutableMap<String, NavigatorBindingClass>,
                                   element : TypeElement,
                                   erasedTargetNames : MutableSet<String>,
                                   scopeName : String) : NavigatorBindingClass {
    var navigatorClass = targetClassMap[scopeName]
    if (navigatorClass == null) {
      val targetClass = element.qualifiedName.toString()
      val classPackage = "io.dwak.freight.navigator.${scopeName.toLowerCase()}"
      val className = scopeName + NavigatorBindingClass.CLASS_SUFFIX
      navigatorClass = NavigatorBindingClass(classPackage, className, targetClass, processingEnv)
      targetClassMap.put(scopeName, navigatorClass)
      erasedTargetNames.add(element.toString())
    }

    return navigatorClass
  }

  private fun getOrCreateNavigatorImpl(targetClassMap : MutableMap<String, NavigatorImplBindingClass>,
                                       element : TypeElement,
                                       erasedTargetNames : MutableSet<String>,
                                       scopeName : String) : NavigatorImplBindingClass {
    var navigatorImplClass = targetClassMap[scopeName]
    if (navigatorImplClass == null) {
      val targetClass = element.qualifiedName.toString()
      val classPackage = "io.dwak.freight.navigator.${scopeName.toLowerCase()}"
      @Suppress("RemoveSingleExpressionStringTemplate")
      val className = "${NavigatorImplBindingClass.CLASS_PREFIX}" +
                      "$scopeName" +
                      "${NavigatorImplBindingClass.CLASS_SUFFIX}"
      navigatorImplClass = NavigatorImplBindingClass(classPackage,
                                                 className,
                                                 targetClass,
                                                 processingEnv)
      targetClassMap.put(scopeName, navigatorImplClass)
      erasedTargetNames.add(element.toString())
    }

    return navigatorImplClass
  }

  @Suppress("unused")
  private fun error(element : Element, message : String, vararg args : Any) {
    messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args), element)
  }

  @Suppress("unused")
  private fun note(note : String) {
    messager.printMessage(Diagnostic.Kind.NOTE, note)
  }
}

