package app.task.pinterestsample.util

import java.text.SimpleDateFormat
import java.util.*

/*
 * This class is used to perform date and time related operations.
 * Created by venkatesh on 14-Apr-19.
 */

object DateUtil {

    fun getDateFromString(inputFormat: String, inputDate: String): Date? {

        var parsed: Date? = null
        val dfInput = SimpleDateFormat(inputFormat, Locale.getDefault())
        try {
            parsed = dfInput.parse(inputDate)
        } catch (ignored: Exception) {
        }
        return parsed

    }

    fun formattedDateFromDate(inputDate: Date, outputFormat: String): String {

        var outputDate = "Date N/A"
        val dfOutput = SimpleDateFormat(outputFormat, Locale.getDefault())
        try {
            outputDate = dfOutput.format(inputDate)
        } catch (ignored: Exception) {
        }
        return outputDate

    }
}

