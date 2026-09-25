package com.hearingtrainer.app.core

/**
 * The two ways to answer a melody on the Practice screen (docs/SPEC.md, "Answer modes"): scored,
 * where every key press is final, or free play, where the answer can be edited before Check.
 * [jsonName] is the value written to the attempt log.
 */
enum class AnswerMode(val jsonName: String) {
    SCORED("scored"),
    FREE("free"),
}
