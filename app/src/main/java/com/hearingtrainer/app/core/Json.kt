package com.hearingtrainer.app.core

/**
 * The few pieces of JSON writing the attempt log needs, written by hand: a dozen fields don't
 * justify a serialization dependency (see docs/DECISIONS.md, "JSON-lines attempt log"). Every
 * string goes through [string], which escapes what RFC 8259 requires plus the two Unicode line
 * separators (so a line-based reader never splits a record), so any value — including quotes,
 * backslashes and control characters — round-trips as valid JSON.
 */
object Json {

    /** A JSON string literal for [value], with quotes and escaping. */
    fun string(value: String): String {
        val out = StringBuilder(value.length + 2)
        out.append('"')
        for (ch in value) {
            when (ch) {
                '"' -> out.append("\\\"")
                '\\' -> out.append("\\\\")
                '\n' -> out.append("\\n")
                '\r' -> out.append("\\r")
                '\t' -> out.append("\\t")
                '\b' -> out.append("\\b")
                '\u000C' -> out.append("\\f")
                '\u2028' -> out.append("\\u2028")
                '\u2029' -> out.append("\\u2029")
                else -> if (ch < ' ') out.append(String.format("\\u%04x", ch.code)) else out.append(ch)
            }
        }
        out.append('"')
        return out.toString()
    }

    /** A JSON array of numbers, e.g. `[60,62,64]`. */
    fun numberArray(values: List<Number>): String = values.joinToString(",", "[", "]") { it.toString() }

    /** A JSON array of string literals, e.g. `["first_try","found"]`. */
    fun stringArray(values: List<String>): String = values.joinToString(",", "[", "]") { string(it) }

    /**
     * A JSON object from already-encoded member values: `Json.obj("a" to "1", "b" to Json.string("x"))`
     * gives `{"a":1,"b":"x"}`. Keys are escaped here; values are used verbatim, so they must be
     * valid JSON (numbers and booleans via `toString()`, strings via [string], nested via [obj]).
     */
    fun obj(vararg members: Pair<String, String>): String =
        members.joinToString(",", "{", "}") { (key, encodedValue) -> "${string(key)}:$encodedValue" }
}
