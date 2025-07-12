package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillContext

public inline fun SkillTickBehavior(crossinline block: SkillContext.() -> Unit): SkillTickBehavior =
    SkillTickBehavior { context -> context.block() }

public inline fun SkillTickBehavior.andThen(crossinline block: SkillContext.() -> Unit): SkillTickBehavior =
    andThen(SkillTickBehavior(block))