package org.tomidori.fable.skill.handler

import org.tomidori.fable.skill.SkillExecutionContext

public inline fun SkillCancelHandler(crossinline block: SkillExecutionContext.() -> Boolean): SkillCancelHandler =
    SkillCancelHandler { context -> context.block() }

public inline fun SkillCancelHandler.and(crossinline block: SkillExecutionContext.() -> Boolean): SkillCancelHandler =
    and(SkillCancelHandler(block))

public inline fun SkillCancelHandler.or(crossinline block: SkillExecutionContext.() -> Boolean): SkillCancelHandler =
    or(SkillCancelHandler(block))