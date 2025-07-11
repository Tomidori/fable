package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillExecutionContext

inline fun SkillCompleteBehavior(crossinline block: SkillExecutionContext.() -> Unit): SkillCompleteBehavior =
    SkillCompleteBehavior { context -> context.block() }

inline fun SkillCompleteBehavior.andThen(crossinline block: SkillExecutionContext.() -> Unit): SkillCompleteBehavior =
    andThen(SkillCompleteBehavior(block))