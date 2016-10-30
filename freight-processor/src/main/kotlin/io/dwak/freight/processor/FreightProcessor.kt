package io.dwak.freight.processor

import com.google.auto.service.AutoService
import io.dwak.freight.annotation.ControllerBuilder
import io.dwak.freight.annotation.Extra
import io.dwak.freight.processor.binding.BuilderBindingClass
import io.dwak.freight.processor.binding.FreightTrainBindingClass
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

@AutoService(value = Processor::class)
open class FreightProcessor : AbstractProcessor() {

  private lateinit var filer: Filer
  private lateinit var messager: Messager
  private lateinit var elementUtils: Elements

  override fun init(processingEnv: ProcessingEnvironment) {
    super.init(processingEnv)
    this.filer = processingEnv.filer
    this.messager = processingEnv.messager
    this.elementUtils = processingEnv.elementUtils
  }

  override fun getSupportedAnnotationTypes()
          = mutableSetOf(Extra::class.java.canonicalName,
                         ControllerBuilder::class.java.canonicalName)

  override fun getSupportedSourceVersion() = SourceVersion.latestSupported()

  override fun process(annotations: MutableSet<out TypeElement>,
                       roundEnv: RoundEnvironment): Boolean {

    if (annotations.isNotEmpty()) {
      val freightTrainTargetClassMap = hashMapOf<TypeElement, FreightTrainBindingClass>()
      val builderTargetClassMap = hashMapOf<TypeElement, BuilderBindingClass>()
      val erasedTargetNames = mutableSetOf<String>()
      annotations.forEach {
        typeElement: TypeElement ->
        val elements = roundEnv.getElementsAnnotatedWith(typeElement)

        elements.filter { it.hasAnnotationWithName(Extra::class.java.simpleName) }
                .forEach {
                  try {
                    val enclosingTypeElement = it.enclosingElement as TypeElement
                    val shipperClass = getOrCreateFreightTrain(freightTrainTargetClassMap,
                                                               enclosingTypeElement,
                                                               erasedTargetNames)
                    shipperClass.createAndAddBinding(it)

                  } catch (e: Exception) {

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
                }
      }

      freightTrainTargetClassMap.values
              .plus(builderTargetClassMap.values)
              .forEach {
                try {
                  it.writeToFiler(filer)
                } catch (e: IOException) {
                  messager.printMessage(Diagnostic.Kind.ERROR, e.message)
                }
              }
    }

    return true
  }

  private fun getOrCreateFreightTrain(targetClassMap: MutableMap<TypeElement, FreightTrainBindingClass>,
                                      enclosingElement: TypeElement,
                                      erasedTargetNames: MutableSet<String>)
          : FreightTrainBindingClass {
    var freightTrainClass = targetClassMap[enclosingElement]
    if (freightTrainClass == null) {
      val targetClass = enclosingElement.qualifiedName.toString()
      val classPackage = enclosingElement.packageName(elementUtils)
      val className = enclosingElement.className(classPackage) + FreightTrainBindingClass.CLASS_SUFFIX
      freightTrainClass = FreightTrainBindingClass(classPackage, className, targetClass, processingEnv)
      targetClassMap.put(enclosingElement, freightTrainClass)
      erasedTargetNames.add(enclosingElement.toString())
    }

    return freightTrainClass
  }

  private fun getOrCreateBuilder(targetClassMap: MutableMap<TypeElement, BuilderBindingClass>,
                                 element: TypeElement,
                                 erasedTargetNames: MutableSet<String>)
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

  @Suppress("unused")
  private fun error(element: Element, message: String, vararg args: Any) {
    messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args), element)
  }

  @Suppress("unused")
  private fun note(note: String) {
    messager.printMessage(Diagnostic.Kind.NOTE, note)
  }
}

