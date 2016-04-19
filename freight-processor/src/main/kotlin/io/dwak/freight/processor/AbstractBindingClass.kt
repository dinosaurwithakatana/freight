package io.dwak.freight.processor

import com.squareup.javapoet.*
import java.io.IOException
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

abstract class AbstractBindingClass(val classPackage: String,
                                    val className: String,
                                    val targetClass: String,
                                    val processingEnvironment: ProcessingEnvironment) {

  val targetClassName = ClassName.get(classPackage, targetClass)
  val generatedClassName = ClassName.get(classPackage, className)
  val parcelable = ClassName.get("android.os", "Parcelable")
  val parcelableTypeMirror = processingEnvironment
          .getTypeMirror("${parcelable.packageName()}.${parcelable.simpleName()}")
  val serializableTypeMirror = processingEnvironment.getTypeMirror("java.io.Serializable")
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
  protected val elementUtils: Elements by lazy { processingEnvironment.elementUtils }
  protected val typeUtils: Types by lazy { processingEnvironment.typeUtils }
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

  protected fun handleType(it: FieldBinding, f: (String) -> String)
          : Pair<Boolean, String> {

    var bundleStatement : String = ""
    when {
      TypeName.get(it.type) == string                         -> return Pair(true, f("String"))
      TypeName.get(it.type) == charsequence                   -> return Pair(true, f("CharSequence"))
      TypeName.get(it.type) == integer                        -> return Pair(true, f("Int"))
      TypeName.get(it.type) == float                          -> return Pair(true, f("Float"))
      TypeName.get(it.type) == character                      -> return Pair(true, f("Char"))
      TypeName.get(it.type) == bundle                         -> return Pair(true, f("Bundle"))
      TypeName.get(it.type) == iBinder                        -> return Pair(true, f("Binder"))
      TypeName.get(it.type) == byte                           -> return Pair(true, f("Byte"))
      TypeName.get(it.type) == short                          -> return Pair(true, f("Short"))
      TypeName.get(it.type) == boolean                        -> return Pair(true, f("Boolean"))
      TypeName.get(it.type) == ArrayTypeName.of(float)        -> return Pair(true, f("FloatArray"))
      TypeName.get(it.type) == ArrayTypeName.of(character)    -> return Pair(true, f("CharArray"))
      TypeName.get(it.type) == ArrayTypeName.of(byte)         -> return Pair(true, f("ByteArray"))
      TypeName.get(it.type) == ArrayTypeName.of(charsequence) -> return Pair(true, f("CharSequenceArray"))
      typeUtils.isAssignable(it.type,
                             parcelableTypeMirror)            -> return Pair(true, f("Parcelable"))
      typeUtils.isAssignable(it.type,
                             serializableTypeMirror)          -> return Pair(false, f("Serializable"))
      else                                                    -> return Pair(false, bundleStatement)
    }
  }

}