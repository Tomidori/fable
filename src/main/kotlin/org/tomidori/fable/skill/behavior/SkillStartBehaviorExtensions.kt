package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillPostContext

public inline fun <S> SkillStartBehavior(crossinline block: SkillPostContext<S>.() -> Unit): SkillStartBehavior<S> =
    SkillStartBehavior { context -> context.block() }

public inline fun <S> SkillStartBehavior<S>.andThen(crossinline block: SkillPostContext<S>.() -> Unit): SkillStartBehavior<S> =
    andThen(SkillStartBehavior(block))

public operator fun <S> SkillStartBehavior<S>.plus(block: SkillPostContext<S>.() -> Unit): SkillStartBehavior<S> =
    andThen(block)