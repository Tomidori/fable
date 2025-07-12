package org.tomidori.fable.skill.handler

import org.tomidori.fable.skill.SkillContext

public inline fun SkillCancelHandler(crossinline block: SkillContext.() -> Boolean): SkillCancelHandler =
    SkillCancelHandler { context -> context.block() }

public inline fun SkillCancelHandler.and(crossinline block: SkillContext.() -> Boolean): SkillCancelHandler =
    and(SkillCancelHandler(block))

public inline fun SkillCancelHandler.or(crossinline block: SkillContext.() -> Boolean): SkillCancelHandler =
    or(SkillCancelHandler(block))