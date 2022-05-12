package com.maden.mlauncher.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    companion object {
        private val calendar: Calendar = Calendar.getInstance()

        fun getSystemHourAndMin(): String {
            val calendar = Calendar.getInstance()
            val hour24hrs = calendar[Calendar.HOUR_OF_DAY]
            //val hour12hrs = calendar[Calendar.HOUR]
            val minutes = calendar[Calendar.MINUTE]
            //val seconds = calendar[Calendar.SECOND]
            return String.format("%02d:%02d", hour24hrs, minutes)
        }

        fun getSystemDate(): String {
            val del = "/"
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val stringDate = sdf.format(Date())
            val list = stringDate.split(del)

            return list[0] + " " + getFullMonthName() + " " + list[2]
            //return sdf.format(Date())
        }

        private fun getFullMonthName() = String.format(Locale.US,"%tB",calendar)

    }

}