package org.hs.gsonscala.typeadapters

import java.lang.reflect.{ParameterizedType => JavaParameterizedType, Type => JavaType}

import com.google.gson
import org.hs.gsonscala.util.GsonUtils

/**
  * Created by serhakan on 4/11/16.
  */
class OptionTypeAdapter extends com.google.gson.JsonSerializer[Option[_]] with gson.JsonDeserializer[Option[_]] {
  def serialize(value:Option[_], valueType:JavaType, context:gson.JsonSerializationContext): gson.JsonElement = {
    value match {
      case Some(x) => context.serialize(x)
      case None => com.google.gson.JsonNull.INSTANCE
    }
  }

  def deserialize(value:gson.JsonElement, valueType:JavaType, context:gson.JsonDeserializationContext): Option[_] = {

    def deserializeNonNull() = {
      Option(context.deserialize(value, GsonUtils.getFirstParameterizedTypeArgument(valueType)))
    }

    if (value == null) None else deserializeNonNull()
  }
}
