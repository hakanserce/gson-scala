package org.hs.gsonscala.typeadapters

import java.lang.reflect.{Type => JavaType}

import com.google.gson
import org.hs.gsonscala.util.GsonUtils

/**
  * Created by serhakan on 4/11/16.
  */
class ListTypeAdapter extends com.google.gson.JsonSerializer[List[_]] with gson.JsonDeserializer[List[_]] {
  def serialize(value:List[_], valueType:JavaType, context:gson.JsonSerializationContext): gson.JsonElement = {

    def makeJsonArray(list:List[_], array:gson.JsonArray):gson.JsonArray = {
      list match {
        case x :: xs => {
          array.add(context.serialize(x))
          makeJsonArray(xs, array)
        }
        case _ => array
      }
    }

    if (value.isEmpty) com.google.gson.JsonNull.INSTANCE else makeJsonArray(value, new gson.JsonArray())
  }

  def deserialize(value:gson.JsonElement, valueType:JavaType, context:gson.JsonDeserializationContext): List[_] = {

    def deserializeElem(elem:gson.JsonElement, elemType:JavaType): Any = {
      context.deserialize(elem, elemType)
    }

    def deserializeNonNull(): List[_] = {
      val array = value.getAsJsonArray()
      val elemType = GsonUtils.getFirstParameterizedTypeArgument(valueType)

      List.tabulate(array.size())(i => deserializeElem(array.get(i), elemType))
    }

    if (value == null) List() else deserializeNonNull()
  }

}
