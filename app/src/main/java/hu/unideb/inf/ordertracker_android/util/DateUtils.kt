package hu.unideb.inf.ordertracker_android.util

import java.util.*


enum class DateFormat {
    DOTYYYYMMdd,
    USYYYYMMdd,
    DOTMMddYYYY,
    USMMddYYYY
}


fun Date.toFormattedString(format: DateFormat): String {
    val calendar = Calendar.getInstance()
    calendar.time = this

    val year = calendar.get(Calendar.YEAR)
    var month: String = Integer.toString(calendar.get(Calendar.MONTH) + 1)
    var day: String = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))

    if (Integer.parseInt(month)  < 10) month = "0$month"
    if (Integer.parseInt(day)  < 10) day = "0$day"

    when (format) {
        DateFormat.DOTMMddYYYY -> return "$month.$day.$year"
        DateFormat.USMMddYYYY -> return "$month-$day-$year"
        DateFormat.DOTYYYYMMdd -> return "$year.$month.$day"
        DateFormat.USYYYYMMdd -> return "$year-$month-$day"
    }
}