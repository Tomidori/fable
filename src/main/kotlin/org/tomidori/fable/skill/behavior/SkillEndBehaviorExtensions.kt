package org.tomidori.fable.skill.behavior

import org.tomidori.fable.skill.SkillPostContext

public inline fun <S> SkillEndBehavior(crossinline block: SkillPostContext<S>.() -> Unit): SkillEndBehavior<S> =
    SkillEndBehavior { context -> context.block() }

public inline fun <S> SkillEndBehavior<S>.andThen(crossinline block: SkillPostContext<S>.() -> Unit): SkillEndBehavior<S> =
    andThen(SkillEndBehavior(block))

public operator fun <S> SkillEndBehavior<S>.plus(block: SkillPostContext<S>.() -> Unit): SkillEndBehavior<S> =
    andThen(block)