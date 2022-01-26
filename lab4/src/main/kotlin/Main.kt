import regex.RegexFactory

fun main() {
    val regexValidator = RegexFactory()
    while (true) {
        val regex = readLine()!!
        println(regexValidator.create(regex))
    }
}
