package org.hs.gsonscala

import com.google.gson
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.lang.reflect.{Type => JavaType}
import java.lang.reflect.{ParameterizedType => JavaParameterizedType}

import org.hs.gsonscala.typeadapters.{ListTypeAdapter, OptionTypeAdapter}

class Json(val json:Option[gson.JsonElement]) extends Serializable {

  //works if this is a JsonArray
  def get(index:Int):Json = {
    new Json(json.map((elem: gson.JsonElement) => elem.getAsJsonArray.get(index)))
  }

  //works if this is a JsonArray
  def list:List[Json] = {
    json match {
      case None => List()
      case Some(elem) => List.tabulate(elem.getAsJsonArray().size())(get(_))
    }
  }

  //works only if this is a JsonObject
  def apply(attr:String):Json = {
    val jsonElem:Option[gson.JsonElement] = json match {
      case None => None
      case Some(elem) => Option(elem.getAsJsonObject().get(attr))
    }

    new Json(jsonElem)
  }

  //works for JsonObject
  def map:Map[String, Json] = {
    def addToMap(map:Map[String, Json], iter:java.util.Iterator[java.util.Map.Entry[String, gson.JsonElement]]): Map[String, Json] = {
      if (iter.hasNext()) {
        val entry = iter.next()
        addToMap(map + (entry.getKey() -> new Json(Option(entry.getValue()))), iter)
      } else {
        map
      }
    }

    if(json.isEmpty)
      Map()
    else
      addToMap(Map(), json.get.getAsJsonObject().entrySet().iterator())
  }


  def str:Option[String] = {
    json.map(_.getAsString())
  }

  def reqStr:String = {
    str.getOrElse(throw new NullPointerException())
  }

  def int = {
    json.map(_.getAsInt().toInt)
  }

  def date:Option[ZonedDateTime] = {
    str.map((dateStr:String) => ZonedDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME))
  }

  def first = {
    new Json(json.map(_.getAsJsonObject().entrySet().iterator().next().getValue()))
  }

  override def toString():String = json.map(_.toString).getOrElse("")

}

object Json {

  val parser = new gson.JsonParser
  val gsonInstance = new gson.GsonBuilder()
    .setFieldNamingPolicy(gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    //.setExclusionStrategies(new NoneExclusionStrategy())
    .registerTypeAdapter(classOf[Option[_]], new OptionTypeAdapter)
    .registerTypeAdapter(classOf[List[_]], new ListTypeAdapter)
    .create();

  def parse(jsonString:String) = {
    val json = parser.parse(jsonString)
    new Json(Option(json))
  }

  def toJson[T](obj:T):String = {
    gsonInstance.toJson(obj)
  }

  def fromJson[T](jsonString:String, clazz: Class[T]):T = {
    gsonInstance.fromJson(jsonString, clazz)
  }

}

