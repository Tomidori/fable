package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillContext

public inline fun SkillStartBehavior(crossinline block: SkillContext.() -> Unit): SkillStartBehavior =
    SkillStartBehavior { context -> context.block() }

public inline fun SkillStartBehavior.andThen(crossinline block: SkillContext.() -> Unit): SkillStartBehavior =
    andThen(SkillStartBehavior(block))