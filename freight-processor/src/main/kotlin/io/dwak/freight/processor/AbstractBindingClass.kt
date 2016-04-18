package io.dwak.freight.processor

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import java.io.IOException
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.tools.Diagnostic

abstract class AbstractBindingClass(val classPackage: String,
                                    val className: String,
                                    val targetClass: String,
                                    val processingEnvironment: ProcessingEnvironment) {
  val targetClassName = ClassName.get(classPackage, targetClass)
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

  private val messager: Messager by lazy { processingEnvironment.messager }
  protected val bindings = hashMapOf<String, FieldBinding>()

  abstract fun createAndAddBinding(element: Element)
  abstract fun generate(): TypeSpec

  @Throws(IOException::class)
  fun writeToFiler(filer: Filer) {
    JavaFile.builder(classPackage, generate()).build().writeTo(filer)
  }

  fun note(note: String) {
    messager.printMessage(Diagnostic.Kind.NOTE, note)
  }

  fun error(message: String) {
    messager.printMessage(Diagnostic.Kind.ERROR, message)
  }
}