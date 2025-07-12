package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillContext

public inline fun SkillCompleteBehavior(crossinline block: SkillContext.() -> Unit): SkillCompleteBehavior =
    SkillCompleteBehavior { context -> context.block() }

public inline fun SkillCompleteBehavior.andThen(crossinline block: SkillContext.() -> Unit): SkillCompleteBehavior =
    andThen(SkillCompleteBehavior(block))