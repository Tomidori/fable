package org.tomidori.fable.skill.condition

import org.tomidori.fable.skill.SkillContext
import org.tomidori.fable.skill.SkillResponse

public inline fun SkillCondition(crossinline block: SkillContext.() -> SkillResponse): SkillCondition =
    SkillCondition { context -> context.block() }

public inline fun SkillCondition.and(crossinline block: SkillContext.() -> SkillResponse): SkillCondition =
    and(SkillCondition(block))

public inline fun SkillCondition.or(crossinline block: SkillContext.() -> SkillResponse): SkillCondition =
    or(SkillCondition(block))