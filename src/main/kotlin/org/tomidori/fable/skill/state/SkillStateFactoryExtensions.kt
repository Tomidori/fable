package org.tomidori.fable.skill.state

import org.tomidori.fable.skill.SkillPreContext

public inline fun <S> SkillStateFactory(crossinline block: SkillPreContext.() -> SkillStateCreationResult<S>): SkillStateFactory<S> =
    SkillStateFactory { context -> context.block() }

public inline fun <S, V> SkillStateFactory<S>.map(crossinline block: SkillPreContext.(S) -> V): SkillStateFactory<V> =
    map { context, state -> context.block(state) }

public inline fun <S, V> SkillStateFactory<S>.flatMap(crossinline block: SkillPreContext.(S) -> SkillStateCreationResult<V>): SkillStateFactory<V> =
    flatMap { context, state -> context.block(state) }