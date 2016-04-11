package org.hs.gsonscala

import org.scalatest.FunSuite

/**
  * Created by serhakan on 4/11/16.
  */
class Json$Test extends FunSuite {
  case class TestRecord(val a: String, val b: String, val c: Option[String], val d: List[String])
  val testInput1 = """{ "a":"1", "b":"2013-11-12T08:00:00.000Z", "c":"dsfsfddsf"}"""
  val testRecord = new TestRecord("x", "y", Option("z"), List("1", "2", "3"))
  val testRecordAsJson = """{"a":"x","b":"y","c":"z","d":["1","2","3"]}"""
  val parsedJson = Json.parse(testInput1)

  test("testToJson") {
    val writtenJson = Json.toJson(testRecord)
    assert(writtenJson === testRecordAsJson)
  }

  test("testParse") {
    assert(parsedJson("a").reqStr === "1", "reqStr")
    assert(parsedJson("b").date.map(_.getYear()).get === 2013, "date")
    assert(parsedJson("non-existing").date.map(_.getYear()) === None, "non-existing date")
    assert(parsedJson("non-existing").str === None, "non-existing str")
  }

  test("test map") {
    assert(parsedJson.map.toList.map(e => e._1) === List("a", "b", "c"))
  }

  ignore("testFromJson") {
    val parsedObject = Json.fromJson(testRecordAsJson, classOf[TestRecord])
    assert(parsedObject === testRecord)
  }

}
