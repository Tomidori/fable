package org.tomidori.fable.skill.condition

import org.tomidori.fable.skill.SkillPreContext
import org.tomidori.fable.skill.SkillResponse

public inline fun SkillCondition(crossinline block: SkillPreContext.() -> SkillResponse): SkillCondition =
    SkillCondition { context -> context.block() }

public inline fun SkillCondition.and(crossinline block: SkillPreContext.() -> SkillResponse): SkillCondition =
    and(SkillCondition(block))

public inline fun SkillCondition.or(crossinline block: SkillPreContext.() -> SkillResponse): SkillCondition =
    or(SkillCondition(block))

public operator fun SkillCondition.plus(block: SkillPreContext.() -> SkillResponse): SkillCondition = and(block)