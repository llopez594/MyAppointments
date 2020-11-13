package Extensions

import Utils.EncryptUtil
import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri

import android.text.Editable
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.BoolRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

val PARAM1 = "param1"
val PARAM2 = "param2"
val PARAM3 = "param3"
val PARAM4 = "param4"

fun View.displaySnack(message:String, duration: Int = Snackbar.LENGTH_LONG, func: Snackbar.()-> Unit) {

    val snackbar = Snackbar.make(this, message, duration)

    snackbar.func()

    snackbar.show()
}

fun Snackbar.action(message:String, listener: (View) -> Unit) = setAction(message, listener)

fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT)
        = Toast.makeText(this, message, duration).show()

val View.context: Context get() = context

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

fun String.decode(): String {
    return Base64.decode(this, Base64.DEFAULT).toString(charset("UTF-8"))
}

fun String.encode(): String {
    return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.DEFAULT)
}


/********************************************************************************
 *
 * ********************************NUEVOS NUEVOS NUEVOS**************************
 *
 * ******************************************************************************/



/**
 * Extension method to provide simpler access to {@link View#getResources()#getString(int)}.
 */
fun View.getString(stringResId: Int): String = resources.getString(stringResId)

/**
 * Extension method to show a keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

/**
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) { }
    return false
}

/**
 * Show the view  (visibility = View.VISIBLE)
 */
fun View.show() : View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.hide() : View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

/**
 * Gone the view (visibility = View.GONE)
 */
fun View.gone() : View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

/**
 * Extension method to Get Boolean resource for Context.
 */
fun Context.getBoolean(@BoolRes id: Int) = resources.getBoolean(id)

/**
 * Extension method to Get Color for resource for Context.
 */
fun Context.getColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

/**
 * Extension method to Get Drawable for resource for Context.
 */
fun Context.getDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

/**
 * Extension method to show notification for Context.
 */
inline fun Context.notification(body: NotificationCompat.Builder.() -> Unit): Notification {
    val builder = NotificationCompat.Builder(this)
    builder.body()
    return builder.build()
}

/**
 * Extension method to display notification text for Fragment.
 */
inline fun Fragment.notification(body: NotificationCompat.Builder.() -> Unit) = activity!!.notification(body)


/**
 * Extension method to make call for Context.
 */
@SuppressLint("MissingPermission")
fun Context.makeCall(number: String): Boolean {
    try {
        val intent = Intent(ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
        return true
    } catch (e: Exception) {
        return false
    }
}

/**
 * Extension method to make call for Fragment.
 */
fun Fragment.makeCall(number: String) = activity!!.makeCall(number)

/**
 * Extension method to dail telephone number for Context.
 */
fun Context.dial(tel: String?) = startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel)))

/**
 * Extension method to get a new Intent for an Activity class
 */
inline fun <reified T : Any> Context.intent() = Intent(this, T::class.java)

/**
 * Extension method to send sms for Context.
 */
fun Context.sms(phone: String?, body: String = "") {
    val smsToUri = Uri.parse("smsto:" + phone)
    val intent = Intent(Intent.ACTION_SENDTO, smsToUri)
    intent.putExtra("sms_body", body)
    startActivity(intent)
}

/**
 * Extension method to send sms for Fragment.
 */
fun Fragment.sendSms(number: String, text: String = "") = activity!!.sms(number, text)

/**
 * Extension method to check if String is Email.
 */
fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

/**
 * Extension method to check if String is Number.
 */
fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}


/**
 * Extension method to get md5 string.
 */
fun String.md5() = EncryptUtil.encrypt(this, "MD5")

/**
 * Extension method to get sha1 string.
 */
fun String.sha1() = EncryptUtil.encrypt(this, "SHA-1")
