# gson-scala
A scala wrapper for Gson

Features:
* Scala List support
* Scala Option support
* Generic parsing (without mapping to a class)

##Converting Objects to JSON

```scala
case class TestRecord(val a: String, val b: String, val c: Option[String], val d: List[String])

val testRecord = new TestRecord("x", "y", Option("z"), List("1", "2", "3"))
Json.toJson(testRecord)
```

creates the following JSON string:

```javascript
{"a":"x","b":"y","c":"z","d":["1","2","3"]}
```

Note that pretty-printing vs compact printing is available among many other features supported by Gson via `Json.gsonInstance` variable.

##Parsing Objects from JSON


```scala
case class TestRecord(val a: String, val b: String, val c: Option[String], val d: List[String])

val testRecordAsJson = """{"a":"x","b":"y","c":"z","d":["1","2","3"]}"""
val parsedObject = Json.fromJson(testRecordAsJson, classOf[TestRecord])
```

##Generic JSON parsing (without mapping to )

```scala
val testInput = """{ "a":"1", "b":"2013-11-12T08:00:00.000Z", "c":"dsfsfddsf"}"""
val parsedJson = Json.parse(testInput)
```

At this point you can access fields via accessors:

```scala
    assert(parsedJson("a").reqStr === "1")
    assert(parsedJson("b").date.map(_.getYear()).get === 2013)
    assert(parsedJson("non-existing").date.map(_.getYear()) === None)
    assert(parsedJson("non-existing").str === None)
```

### Generic parsing features
* Accessing attributes `json("attr")`
* Accessing nested attrbiutes `json("attr")("nestedattr")`
* Optional attribute support: almost all methods return Optionals ('json("attr").str' gives `Option[String]`, etc.)
* Date parsing support (ZonedDateTime)