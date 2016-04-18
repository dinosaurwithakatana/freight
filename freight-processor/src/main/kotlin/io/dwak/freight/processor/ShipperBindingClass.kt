package io.dwak.freight.processor

import com.squareup.javapoet.*
import com.squareup.javapoet.TypeName.VOID
import com.vishnurajeevan.javapoet.dsl.classType
import com.vishnurajeevan.javapoet.dsl.model.JavaPoetValue
import io.dwak.Extra
import java.io.IOException
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

class ShipperBindingClass(val classPackage: String,
                          val className: String,
                          val targetClass: String,
                          val processingEnvironment: ProcessingEnvironment) {

  private val messager: Messager by lazy { processingEnvironment.messager }

  companion object {
    val CLASS_SUFFIX = "$\$FreightTrain"
  }

  private val bindings = hashMapOf<String, FieldBinding>()
  private val callbackClass by lazy { ClassName.get("io.dwak.freight", "IFreightTrain") }

  fun createAndAddShippingBinding(element: Element) {
    val binding = FieldBinding(element)
    bindings.put(binding.name, binding)
  }

  fun generateShipper(): TypeSpec {
    return classType(PUBLIC, className) {
      annotations = setOf(AnnotationSpec.builder(SuppressWarnings::class.java)
                                  .addMember("value", "\$S", "unused")
                                  .build())
      parameterizedTypes.add(TypeVariableName.get("T", ClassName.get(classPackage, targetClass)))
      implements.add(ParameterizedTypeName.get(callbackClass, TypeVariableName.get("T")))

      method(PUBLIC, VOID, "ship", setOf(JavaPoetValue(FINAL, TypeVariableName.get("T"), "target"))) {
        annotations = setOf(AnnotationSpec.builder(Override::class.java).build(),
                            AnnotationSpec.builder(SuppressWarnings::class.java).addMember("value", "\$S", "unused")
                                    .build())
        val iBinder = ClassName.get("android.os", "IBinder")
        val bundle = ClassName.get("android.os", "Bundle")
        val charsequence = ClassName.get("java.lang", "CharSequence")
        val string = ClassName.get("java.lang", "String")
        val integer = TypeName.INT
        val float = TypeName.FLOAT
        val character = TypeName.CHAR
        val byte = TypeName.BYTE
        val short = TypeName.SHORT
        val boolean = TypeName.BOOLEAN

        statement("final \$T bundle = target.getArgs()", bundle)


        bindings.values.forEach {
          var bundleAccessor = ""
          when (TypeName.get(it.type)) {
            string       -> bundleAccessor = "bundle.getString(\"${it.key}\")"
            charsequence -> bundleAccessor = "bundle.getCharSequence(\"${it.key}\")"
            integer      -> bundleAccessor = "bundle.getInt(\"${it.key}\")"
            float        -> bundleAccessor = "bundle.getFloat(\"${it.key}\")"
            character    -> bundleAccessor = "bundle.getChar(\"${it.key}\")"
            bundle       -> bundleAccessor = "bundle.getBundle(\"${it.key}\")"
            iBinder      -> bundleAccessor = "bundle.getBinder(\"${it.key}\")"
            byte         -> bundleAccessor = "bundle.getByte(\"${it.key}\")"
            short        -> bundleAccessor = "bundle.getShort(\"${it.key}\")"
            boolean      -> bundleAccessor = "bundle.getBoolean(\"${it.key}\")"
          }

          statement("target.\$L = \$L", it.name, bundleAccessor)
        }
      }
    }
  }

  @Throws(IOException::class)
  fun writeToFiler(filer: Filer) {
    JavaFile.builder(classPackage, generateShipper()).build().writeTo(filer)
  }

  private class FieldBinding(element: Element) {
    internal val name: String
    internal val type: TypeMirror
    internal var key: String? = null

    init {
      val instance = element.getAnnotation(Extra::class.java)
      name = element.simpleName.toString()
      type = element.asType()
      if (instance.value.isNotEmpty()) {
        key = instance.value
      }
      else {
        key = name.toUpperCase()
      }
    }

  }

  private fun note(note: String) {
    messager.printMessage(Diagnostic.Kind.NOTE, note)
  }

  private fun error(message: String) {
    messager.printMessage(Diagnostic.Kind.ERROR, message)
  }
}