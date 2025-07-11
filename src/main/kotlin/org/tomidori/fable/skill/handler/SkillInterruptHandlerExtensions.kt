package org.tomidori.fable.skill.handler

import org.tomidori.fable.skill.SkillExecutionContext

public inline fun SkillInterruptHandler(crossinline block: SkillExecutionContext.() -> Boolean): SkillInterruptHandler =
    SkillInterruptHandler { context -> context.block() }

public inline fun SkillInterruptHandler.and(crossinline block: SkillExecutionContext.() -> Boolean): SkillInterruptHandler =
    and(SkillInterruptHandler(block))

public inline fun SkillInterruptHandler.or(crossinline block: SkillExecutionContext.() -> Boolean): SkillInterruptHandler =
    or(SkillInterruptHandler(block))