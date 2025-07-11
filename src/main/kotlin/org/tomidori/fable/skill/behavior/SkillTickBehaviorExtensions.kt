package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillExecutionContext

public inline fun SkillTickBehavior(crossinline block: SkillExecutionContext.() -> Unit): SkillTickBehavior =
    SkillTickBehavior { context -> context.block() }

public inline fun SkillTickBehavior.andThen(crossinline block: SkillExecutionContext.() -> Unit): SkillTickBehavior =
    andThen(SkillTickBehavior(block))