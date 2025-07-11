package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillExecutionContext

inline fun SkillTickBehavior(crossinline block: SkillExecutionContext.() -> Unit): SkillTickBehavior =
    SkillTickBehavior { context -> context.block() }

inline fun SkillTickBehavior.andThen(crossinline block: SkillExecutionContext.() -> Unit): SkillTickBehavior =
    andThen(SkillTickBehavior(block))