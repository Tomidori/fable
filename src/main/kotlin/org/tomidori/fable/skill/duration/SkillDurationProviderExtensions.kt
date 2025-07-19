package org.tomidori.fable.skill.duration

import org.tomidori.fable.skill.SkillPreContext

public inline fun SkillDurationProvider(crossinline block: SkillPreContext.() -> Int): SkillDurationProvider =
    SkillDurationProvider { context -> context.block() }