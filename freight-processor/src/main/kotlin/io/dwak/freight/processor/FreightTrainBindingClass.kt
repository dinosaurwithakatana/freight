package io.dwak.freight.processor

import com.squareup.javapoet.*
import com.squareup.javapoet.TypeName.VOID
import com.vishnurajeevan.javapoet.dsl.classType
import com.vishnurajeevan.javapoet.dsl.model.JavaPoetValue
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PUBLIC

class FreightTrainBindingClass(classPackage: String,
                               className: String,
                               targetClass: String,
                               processingEnvironment: ProcessingEnvironment)
: AbstractBindingClass(classPackage, className, targetClass, processingEnvironment) {

  companion object {
    val CLASS_SUFFIX = "$\$FreightTrain"
    val METHOD_NAME = "ship"
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
      implements.add(ParameterizedTypeName.get(ClassName.get("io.dwak.freight", "IFreightTrain"),
                                               TypeVariableName.get("T")))

      method(PUBLIC, VOID, METHOD_NAME,
             setOf(JavaPoetValue(FINAL, TypeVariableName.get("T"), "target"))) {
        annotations = setOf(AnnotationSpec.builder(Override::class.java).build(),
                            AnnotationSpec.builder(SuppressWarnings::class.java)
                                    .addMember("value", "\$S", "unused")
                                    .build())
        statement("final \$T bundle = target.getArgs()", bundle)


        bindings.values.forEach {
          val bundlePair = getBundleStatement(it)
          if(it.kind == ElementKind.FIELD) {
            if (!bundlePair.first) {
              statement("target.\$L = (\$L) bundle.getSerializable(\"${it.key}\")", it.name, it.type)
            }
            else {
              statement("target.\$L = \$L", it.name, getBundleStatement(it).second)
            }
          }
          else {
            if (!bundlePair.first) {
              statement("target.\$L((\$L) bundle.getSerializable(\"${it.key}\"))", it.name, it.type)
            }
            else {
              statement("target.\$L(\$L)", it.name, getBundleStatement(it).second)
            }
          }
        }
      }
    }
  }

  private fun getBundleStatement(fieldBinding: FieldBinding): Pair<Boolean, String> {
    return handleType(fieldBinding, {
      "bundle.get$it(\"${fieldBinding.key}\")"
    })
  }
}

