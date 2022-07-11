package com.example.app.utils.ext

import android.text.Html
import android.view.View
import android.widget.TextView

// [Check a Valid String]
fun String?.isValidStr(): Boolean {
    return !this.isNullOrBlank() && this != "null"
}

/*************
 * Set Provided Text if Not Empty else ''
 */
fun TextView.formatText(text: String?) {
    val newText = if(text == null || text == "null") { "" } else text.toString()
    this.text = newText
}/*************


 * Set Provided Text if Not Empty else Hide
 */
fun TextView.formatTextOrHide(text: String?) {
    if(!text.isValidStr()) {
        this.visibility = View.GONE; return
    }
    val newText = if(text == null || text == "null") { "" } else text.toString()
    this.text = newText
}

/*************
 * Set Provided Text as Html if Not Empty else Hide
 */
fun TextView.formatHtmlOrHide(text: String?) {
    if(!text.isValidStr()) {
        this.visibility = View.GONE; return
    }
    val newText = if(text == null || text == "null") { "" } else text.toString()
    this.text = Html.fromHtml(newText, Html.FROM_HTML_MODE_LEGACY)
}

