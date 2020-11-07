package Utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object sharedPreferences {

    fun defaultPrefs(context: Context): SharedPreferences
            = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPrefs(context: Context, name: String): SharedPreferences
            = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun clearPrefs(context: Context) =
        this.defaultPrefs(context).edit().clear().apply()

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [type]
     */
    operator fun SharedPreferences.set(type: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(type, value) }
            is Int ->     edit { it.putInt(type, value) }
            is Boolean -> edit { it.putBoolean(type, value) }
            is Float ->   edit { it.putFloat(type, value) }
            is Long ->    edit { it.putLong(type, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * finds value on given [type].
     * [T] is the type of value
     *
     * @param defaultValue optional default value - will take null for strings,
     * false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    inline operator fun <reified T : Any> SharedPreferences.get(type: String, defaultValue: T? = null): T {
        return when (T::class) {
            String::class ->  getString(type, defaultValue as? String) as T
            Int::class ->     getInt(type, defaultValue as? Int ?: -1) as T
            Boolean::class -> getBoolean(type, defaultValue as? Boolean ?: false) as T
            Float::class ->   getFloat(type, defaultValue as? Float ?: -1f) as T
            Long::class ->    getLong(type, defaultValue as? Long ?: -1) as T
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}