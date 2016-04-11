package org.hs.gsonscala.util

import java.lang.reflect.{ParameterizedType => JavaParameterizedType, Type => JavaType}

object GsonUtils {

  def getFirstParameterizedTypeArgument(valueType:JavaType):JavaType = {
    val parameterizedType = valueType.asInstanceOf[JavaParameterizedType]
    parameterizedType.getActualTypeArguments()(0)
  }
}