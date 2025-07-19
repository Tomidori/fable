package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillPostContext

public inline fun <S> SkillCompleteBehavior(crossinline block: SkillPostContext<S>.() -> Unit): SkillCompleteBehavior<S> =
    SkillCompleteBehavior { context -> context.block() }

public inline fun <S> SkillCompleteBehavior<S>.andThen(crossinline block: SkillPostContext<S>.() -> Unit): SkillCompleteBehavior<S> =
    andThen(SkillCompleteBehavior(block))

public operator fun <S> SkillCompleteBehavior<S>.plus(block: SkillPostContext<S>.() -> Unit): SkillCompleteBehavior<S> =
    andThen(block)