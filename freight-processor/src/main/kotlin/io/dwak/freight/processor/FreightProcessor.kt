package io.dwak.freight.processor

import com.google.auto.service.AutoService
import io.dwak.Extra
import java.io.IOException
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@AutoService(value = Processor::class)
public open class FreightProcessor : AbstractProcessor() {

  private lateinit var filer: Filer
  private lateinit var messager: Messager
  private lateinit var elementUtils: Elements

  override fun init(processingEnv: ProcessingEnvironment) {
    super.init(processingEnv)
    this.filer = processingEnv.filer
    this.messager = processingEnv.messager
    this.elementUtils = processingEnv.elementUtils
  }

  override fun getSupportedAnnotationTypes() = mutableSetOf(Extra::class.java.canonicalName)
  override fun getSupportedSourceVersion() = SourceVersion.latestSupported()

  override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {

    if (annotations.isNotEmpty()) {
      val targetClassMap = hashMapOf<TypeElement, ShipperBindingClass>()
      val erasedTargetNames = mutableSetOf<String>()

      annotations.forEach {
        typeElement: TypeElement ->
        roundEnv.getElementsAnnotatedWith(typeElement)
                .forEach {
                  try {
                    if (it.kind != ElementKind.FIELD) {
                      error(it, "Extra can only be applied to fields!")
                      return false
                    }

                    val enclosingElement = it.enclosingElement as TypeElement
                    val shipperClass = getOrCreateShipper(targetClassMap, enclosingElement, erasedTargetNames)
                    shipperClass.createAndAddShippingBinding(it)
                  } catch (e: Exception) {

                  }
                }
      }

      targetClassMap.values
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

  private fun getOrCreateShipper(targetClassMap: MutableMap<TypeElement, ShipperBindingClass>,
                                 enclosingElement: TypeElement,
                                 erasedTargetNames: MutableSet<String>): ShipperBindingClass {
    var shipperClass = targetClassMap[enclosingElement]
    if (shipperClass == null) {
      val targetClass = enclosingElement.qualifiedName.toString()
      val classPackage = enclosingElement.packageName(elementUtils)
      val className = enclosingElement.className(classPackage) + ShipperBindingClass.CLASS_SUFFIX
      shipperClass = ShipperBindingClass(classPackage, className, targetClass, processingEnv)
      targetClassMap.put(enclosingElement, shipperClass)
      erasedTargetNames.add(enclosingElement.toString())
    }

    return shipperClass
  }

  private fun error(element: Element, message: String, vararg args: Any) {
    messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args), element)
  }

  private fun note(note : String){
    messager.printMessage(Diagnostic.Kind.NOTE, note)
  }
}

