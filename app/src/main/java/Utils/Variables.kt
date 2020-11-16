package Utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import android.content.pm.PackageManager.*
import android.content.Context
import android.content.pm.PackageInfo


object Variables {

    private var url:String? = "192.168.0.3/my-appointments/public"
    var time_connect_out: Long = 10
    var time_write_out: Long = 10
    var time_read_out: Long = 10


    fun direction(): String = "http://$url/api/"

    fun addZero(valor: Int): String
    {
        return if (valor < 10) {
            "0$valor"
        } else {
            valor.toString()
        }
    }

    fun moneyConverter(): DecimalFormat
    {
        val symbol = DecimalFormatSymbols()
        symbol.decimalSeparator = ','
        symbol.groupingSeparator = '.'
        return DecimalFormat("###,###.##", symbol)
    }

    fun moneyConverterDecimalCero(): DecimalFormat
    {
        val symbol = DecimalFormatSymbols()
        symbol.decimalSeparator = ','
        symbol.groupingSeparator = '.'
        return DecimalFormat("###,###.00", symbol)
    }

    fun moneyConverterNoDecimal(): DecimalFormat
    {
        val symbol = DecimalFormatSymbols()
        symbol.decimalSeparator = ','
        symbol.groupingSeparator = '.'
        return DecimalFormat("###", symbol)
    }

    fun version_name(context: Context): String
    {
        val pInfo: PackageInfo?
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
            val version = pInfo.versionName
            return version
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        return "no info"
    }

    fun existsWhatsapp(context: Context): Boolean
    {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo("com.whatsapp", GET_ACTIVITIES)
            true
        } catch (e: NameNotFoundException) {
            try {
                pm.getPackageInfo("com.whatsapp.w4b", GET_ACTIVITIES)
                true
            } catch (e: NameNotFoundException) {
                false
            }
        }
    }

    fun namePackageWhatsapp(context: Context): String
    {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo("com.whatsapp", GET_ACTIVITIES)
            "com.whatsapp"
        } catch (e: NameNotFoundException) {
            try {
                pm.getPackageInfo("com.whatsapp.w4b", GET_ACTIVITIES)
                "com.whatsapp.w4b"
            } catch (e: NameNotFoundException) {
                ""
            }
        }
    }
}
