import analyzer.Analyzer
import reader.CsvReader

fun main() {
    val reader = CsvReader()
    val steps = reader.readCsv()

    while (true) {
        println(Analyzer().isValid(readLine()!!, steps))
    }
}