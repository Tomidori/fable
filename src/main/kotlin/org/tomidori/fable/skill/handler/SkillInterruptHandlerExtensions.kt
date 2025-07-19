package org.tomidori.fable.skill.handler

import org.tomidori.fable.skill.SkillPostContext

public inline fun <S> SkillInterruptHandler(crossinline block: SkillPostContext<S>.() -> Boolean): SkillInterruptHandler<S> =
    SkillInterruptHandler { context -> context.block() }

public inline fun <S> SkillInterruptHandler<S>.and(crossinline block: SkillPostContext<S>.() -> Boolean): SkillInterruptHandler<S> =
    and(SkillInterruptHandler(block))

public inline fun <S> SkillInterruptHandler<S>.or(crossinline block: SkillPostContext<S>.() -> Boolean): SkillInterruptHandler<S> =
    or(SkillInterruptHandler(block))

public operator fun <S> SkillInterruptHandler<S>.plus(block: SkillPostContext<S>.() -> Boolean): SkillInterruptHandler<S> =
    and(block)