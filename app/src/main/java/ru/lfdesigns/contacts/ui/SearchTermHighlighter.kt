package ru.lfdesigns.contacts.ui

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan

/**
 * A helper class to highlight a search term in a text
 */
class SearchTermHighlighter(private val color: Int) {

    var searchTerm: String? = null

    private fun findTerm(term: String, inText: String, uninterrupted: Boolean): MatchResult? {
        var regexTerm = "("
        if (uninterrupted) {
            regexTerm += term
        } else {
            term.forEachIndexed { i, item ->
                regexTerm += item
                if (i < term.length - 1)
                    regexTerm += "[\\)\\- ]*"
            }
        }
        regexTerm += ")"
        val regex = regexTerm.toRegex(RegexOption.IGNORE_CASE)
        return regex.find(inText,0)
    }

    /**
     *
     * @param inText text to highlight the search term in
     * @param uninterrupted an optimization to use less computations if the term is guaranteed
     * to be uninterrupted by special symbols etc
     */
    fun highlight(inText: String, uninterrupted: Boolean = false): SpannableString {
        val text = SpannableString(inText)
        return searchTerm?.let { term ->
            if (term.isEmpty())
                return@let text

            val match = findTerm(term, inText, uninterrupted) ?: return@let text

            val span = ForegroundColorSpan(color)
            text.apply {
                setSpan(span,
                    match.range.first,
                    match.range.last+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }?: text
    }
}