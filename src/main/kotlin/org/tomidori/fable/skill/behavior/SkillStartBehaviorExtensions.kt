package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillExecutionContext

inline fun SkillStartBehavior(crossinline block: SkillExecutionContext.() -> Unit): SkillStartBehavior =
    SkillStartBehavior { context -> context.block() }

inline fun SkillStartBehavior.andThen(crossinline block: SkillExecutionContext.() -> Unit): SkillStartBehavior =
    andThen(SkillStartBehavior(block))