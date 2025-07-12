package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillContext

public inline fun SkillEndBehavior(crossinline block: SkillContext.() -> Unit): SkillEndBehavior =
    SkillEndBehavior { context -> context.block() }

public inline fun SkillEndBehavior.andThen(crossinline block: SkillContext.() -> Unit): SkillEndBehavior =
    andThen(SkillEndBehavior(block))