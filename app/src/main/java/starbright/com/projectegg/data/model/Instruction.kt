/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 15/8/2018.
 */
package starbright.com.projectegg.data.model

data class Instruction(
    var name: String = "",
    var steps: List<String> = emptyList()
) {
    // Keep backward compatibility constructor
    constructor(number: Int, step: String) : this(
        name = "Step $number",
        steps = listOf(step)
    )
}
