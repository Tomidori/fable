package org.tomidori.fable.skill.handler

import org.tomidori.fable.skill.SkillExecutionContext

inline fun SkillInterruptHandler(crossinline block: SkillExecutionContext.() -> Boolean): SkillInterruptHandler =
    SkillInterruptHandler { context -> context.block() }

inline fun SkillInterruptHandler.and(crossinline block: SkillExecutionContext.() -> Boolean): SkillInterruptHandler =
    and(SkillInterruptHandler(block))

inline fun SkillInterruptHandler.or(crossinline block: SkillExecutionContext.() -> Boolean): SkillInterruptHandler =
    or(SkillInterruptHandler(block))