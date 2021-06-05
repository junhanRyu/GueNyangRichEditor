package com.luckyhan.studio.richeditor.span.paragraph

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.NoCopySpan
import android.text.Spannable
import android.text.Spanned
import android.text.style.LeadingMarginSpan
import android.util.Log
import androidx.core.content.ContextCompat
import com.luckyhan.studio.richeditor.util.DisplayUnitUtil
import com.luckyhan.studio.richeditor.R
import com.luckyhan.studio.richeditor.span.RichClickable
import com.luckyhan.studio.richeditor.span.RichCopyable
import com.luckyhan.studio.richeditor.span.RichSpan

class RichCheckBoxSpan(
    private val context: Context,
    private val spannable: Spannable,
    var checked: Boolean = false,
    private val margin: Int = 100
) : LeadingMarginSpan.Standard(margin), RichClickable, NoCopySpan, RichSpan {

    var checkBoxSize = DisplayUnitUtil.getPxFromDp(context, 24f)
    var unCheckedDrawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_check_box_unchecked)
    var checkedDrawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_check_box_checked)
    var paddingLeft = DisplayUnitUtil.getPxFromDp(context, 4f)
    var paddingRight = DisplayUnitUtil.getPxFromDp(context, 4f)

    override var clickableLeft: Int = 0
    override var clickableRight: Int = 0
    override var clickableTop: Int = 0
    override var clickableBottom: Int = 0

    override fun onClicked() {
        Log.d(this.javaClass.name, "onClicked")
        val spanStart = spannable.getSpanStart(this)
        val spanEnd = spannable.getSpanEnd(this)
        val other = copy() as RichCheckBoxSpan
        other.checked = !checked
        spannable.removeSpan(this)
        spannable.setSpan(other, spanStart, spanEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        if(other.checked){
            val strikeThroughSpan = RichStrikeThroughSpan()
            spannable.setSpan(strikeThroughSpan, spanStart, spanEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }else{
            val strikeThroughSpans = spannable.getSpans(spanStart, spanEnd, RichStrikeThroughSpan::class.java)
            if(strikeThroughSpans?.isNotEmpty() == true){
                spannable.removeSpan(strikeThroughSpans[0])
            }
        }
    }

    override fun getLeadingMargin(first: Boolean): Int {
        return (checkBoxSize + paddingLeft + paddingRight)
    }

    override fun drawLeadingMargin(
        canvas: Canvas?,
        paint: Paint?,
        x: Int,
        dir: Int,
        lineTop: Int,
        baseline: Int,
        lineBottom: Int,
        text: CharSequence?,
        startOffset: Int,
        endOffset: Int,
        first: Boolean,
        layout: Layout?
    ) {
        val drawable = if (checked) checkedDrawable else unCheckedDrawable
        canvas?.let {
            if ((text as Spanned).getSpanStart(this) == startOffset) {
                clickableLeft = x + paddingLeft
                clickableRight = clickableLeft + checkBoxSize
                clickableTop = lineTop + ((lineBottom - lineTop - checkBoxSize) / 2)
                clickableBottom = clickableTop + checkBoxSize
                Log.d(this.javaClass.name, "$clickableRight, line top : $lineTop, line bottom : $lineBottom")
                Log.d(this.javaClass.name, "left : $clickableLeft, right : $clickableRight, top : $clickableTop, bottom : $clickableBottom")
                drawable?.setBounds(clickableLeft, clickableTop, clickableRight, clickableBottom)
                drawable?.draw(canvas)
            }
        }

    }

    override fun copy(): RichCopyable {
        return RichCheckBoxSpan(context, spannable)
    }

    override fun getOpeningTag(): String {
        return "<annotation type=\"checkbox\" checked=\"$checked\">"
    }
}