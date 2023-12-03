import java.io.File

fun Any.resource(name: String): File = File(javaClass.classLoader.getResource(name)!!.file)
