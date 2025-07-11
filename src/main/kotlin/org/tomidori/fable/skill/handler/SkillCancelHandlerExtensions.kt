package org.tomidori.fable.skill.handler

import org.tomidori.fable.skill.SkillExecutionContext

inline fun SkillCancelHandler(crossinline block: SkillExecutionContext.() -> Boolean): SkillCancelHandler =
    SkillCancelHandler { context -> context.block() }

inline fun SkillCancelHandler.and(crossinline block: SkillExecutionContext.() -> Boolean): SkillCancelHandler =
    and(SkillCancelHandler(block))

inline fun SkillCancelHandler.or(crossinline block: SkillExecutionContext.() -> Boolean): SkillCancelHandler =
    or(SkillCancelHandler(block))