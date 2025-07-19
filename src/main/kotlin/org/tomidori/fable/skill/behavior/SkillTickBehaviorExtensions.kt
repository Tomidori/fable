package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillPostContext

public inline fun <S> SkillTickBehavior(crossinline block: SkillPostContext<S>.() -> Unit): SkillTickBehavior<S> =
    SkillTickBehavior { context -> context.block() }

public inline fun <S> SkillTickBehavior<S>.andThen(crossinline block: SkillPostContext<S>.() -> Unit): SkillTickBehavior<S> =
    andThen(SkillTickBehavior(block))

public operator fun <S> SkillTickBehavior<S>.plus(block: SkillPostContext<S>.() -> Unit): SkillTickBehavior<S> =
    andThen(block)