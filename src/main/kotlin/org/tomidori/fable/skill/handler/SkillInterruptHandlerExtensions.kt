package org.tomidori.fable.skill.handler

import org.tomidori.fable.skill.SkillContext

public inline fun SkillInterruptHandler(crossinline block: SkillContext.() -> Boolean): SkillInterruptHandler =
    SkillInterruptHandler { context -> context.block() }

public inline fun SkillInterruptHandler.and(crossinline block: SkillContext.() -> Boolean): SkillInterruptHandler =
    and(SkillInterruptHandler(block))

public inline fun SkillInterruptHandler.or(crossinline block: SkillContext.() -> Boolean): SkillInterruptHandler =
    or(SkillInterruptHandler(block))