package io.dwak.freight.processor

import javax.annotation.processing.ProcessingEnvironment

fun ProcessingEnvironment.getTypeMirror(qualifiedName : String)
        = elementUtils.getTypeElement(qualifiedName).asType()

