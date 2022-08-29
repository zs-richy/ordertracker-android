package hu.unideb.inf.ordertracker_android.util

import java.time.LocalDateTime
import java.util.*


enum class DateFormat {
    DOTYYYYMMdd,
    USYYYYMMdd,
    DOTMMddYYYY,
    USMMddYYYY
}


fun LocalDateTime.toFormattedString(format: DateFormat): String {
    val year = this.year
    var month: String = this.month.value.toString()
    var day: String = this.dayOfMonth.toString()

    if (Integer.parseInt(month)  < 10) month = "0$month"
    if (Integer.parseInt(day)  < 10) day = "0$day"

    when (format) {
        DateFormat.DOTMMddYYYY -> return "$month.$day.$year"
        DateFormat.USMMddYYYY -> return "$month-$day-$year"
        DateFormat.DOTYYYYMMdd -> return "$year.$month.$day"
        DateFormat.USYYYYMMdd -> return "$year-$month-$day"
    }
}