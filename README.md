# Kotlin Arguments Parser

###Usage

Example:
```kotlin
fun main(args: Array<String>) {
    val argsParser = ArgsParser.Builder()
            .setArgs(args)
            .setScheme("int:id,text:name,int:age")
            .build()

    print("id: ${argsParser.getInt("id")}\nname: ${argsParser.getString("name")}\nage: ${argsParser.getInt("age")}")
}
```

To run with passed args: __-id 1 -name alex -age 20__