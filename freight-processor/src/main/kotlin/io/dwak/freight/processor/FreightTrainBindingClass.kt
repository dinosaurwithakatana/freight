package io.dwak.freight.processor

import com.squareup.javapoet.*
import com.squareup.javapoet.TypeName.VOID
import com.vishnurajeevan.javapoet.dsl.classType
import com.vishnurajeevan.javapoet.dsl.model.JavaPoetValue
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PUBLIC

class FreightTrainBindingClass(classPackage: String,
                               className: String,
                               targetClass: String,
                               processingEnvironment: ProcessingEnvironment)
: AbstractBindingClass(classPackage, className, targetClass, processingEnvironment) {

  private val callbackClass by lazy { ClassName.get("io.dwak.freight", "IFreightTrain") }

  companion object {
    val CLASS_SUFFIX = "$\$FreightTrain"
  }

  override fun createAndAddBinding(element: Element) {
    val binding = FieldBinding(element)
    bindings.put(binding.name, binding)
  }

  override fun generate(): TypeSpec {
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
}