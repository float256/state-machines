package reader

import analyzer.AnalyzeStep
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.util.*

class CsvReader {
    companion object {
        const val PATH_TO_CSV = "src/main/resources/table.csv"
    }

    fun readCsv(): Map<Int, AnalyzeStep> {
        val analyzeSteps = TreeMap<Int, AnalyzeStep>()
        csvReader().open(PATH_TO_CSV) {
            readAllAsSequence().forEachIndexed { index, row ->
                if (index != 0) {
                    val currStep = AnalyzeStep(
                        row[0].toInt(),
                        row[1],
                        row[2].split(" ").filter { it.isNotEmpty() }.toSet(),
                        row[3].toBoolean(),
                        if (row[4].isEmpty()) null else row[4].toInt(),
                        row[5].toBoolean(),
                        row[6].toBoolean(),
                        row[7].toBoolean()
                    )
                    analyzeSteps[currStep.id] = currStep
                }
            }
        }
        return analyzeSteps
    }
}