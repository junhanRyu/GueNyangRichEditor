package com.luckyhan.studio.mokaeditor.span.paragraph

import android.text.NoCopySpan
import android.text.style.ParagraphStyle
import android.text.style.StrikethroughSpan
import com.luckyhan.studio.mokaeditor.span.MokaCopyable
import com.luckyhan.studio.mokaeditor.span.MokaSpan

class MokaStrikeThroughParagraphSpan : StrikethroughSpan(), MokaSpan, NoCopySpan, ParagraphStyle {
    override fun copy(): MokaCopyable {
        return MokaStrikeThroughParagraphSpan()
    }

    override fun getOpeningTag(): String {
        return "<annotation type=\"strikethrough_paragraph\">"
    }
}