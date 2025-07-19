package org.tomidori.fable.skill.handler

import org.tomidori.fable.skill.SkillPostContext

public inline fun <S> SkillCancelHandler(crossinline block: SkillPostContext<S>.() -> Boolean): SkillCancelHandler<S> =
    SkillCancelHandler { context -> context.block() }

public inline fun <S> SkillCancelHandler<S>.and(crossinline block: SkillPostContext<S>.() -> Boolean): SkillCancelHandler<S> =
    and(SkillCancelHandler(block))

public inline fun <S> SkillCancelHandler<S>.or(crossinline block: SkillPostContext<S>.() -> Boolean): SkillCancelHandler<S> =
    or(SkillCancelHandler(block))

public operator fun <S> SkillCancelHandler<S>.plus(block: SkillPostContext<S>.() -> Boolean): SkillCancelHandler<S> =
    and(block)