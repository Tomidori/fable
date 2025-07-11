package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillExecutionContext

inline fun SkillEndBehavior(crossinline block: SkillExecutionContext.() -> Unit): SkillEndBehavior =
    SkillEndBehavior { context -> context.block() }

inline fun SkillEndBehavior.andThen(crossinline block: SkillExecutionContext.() -> Unit): SkillEndBehavior =
    andThen(SkillEndBehavior(block))