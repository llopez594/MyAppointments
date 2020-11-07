package Utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object EncryptUtil {
    /**
     * Extension method to convert byteArray to String.
     */
    fun bytes2Hex(bts: ByteArray): String {
        var des = ""
        var tmp: String
        for (i in bts.indices) {
            tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
            if (tmp.length == 1) {
                des += "0"
            }
            des += tmp
        }
        return des
    }

    /**
     * Extension method to get encrypted string.
     */
    fun encrypt(string: String?, type: String): String {
        if (string.isNullOrEmpty()) {
            return ""
        }
        val md5: MessageDigest
        return try {
            md5 = MessageDigest.getInstance(type)
            val bytes = md5.digest(string!!.toByteArray())
            bytes2Hex(bytes)
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }
}