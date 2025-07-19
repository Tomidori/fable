package org.tomidori.fable.skill

import org.tomidori.fable.skill.behavior.SkillCompleteBehavior
import org.tomidori.fable.skill.behavior.SkillEndBehavior
import org.tomidori.fable.skill.behavior.SkillStartBehavior
import org.tomidori.fable.skill.behavior.SkillTickBehavior
import org.tomidori.fable.skill.condition.SkillCondition
import org.tomidori.fable.skill.duration.SkillDurationProvider
import org.tomidori.fable.skill.handler.SkillCancelHandler
import org.tomidori.fable.skill.handler.SkillInterruptHandler
import org.tomidori.fable.skill.state.SkillStateCreationResult
import org.tomidori.fable.skill.state.SkillStateFactory

public inline fun <S> Skill(builderAction: Skill.Builder<S>.() -> Unit): Skill<S> =
    Skill.builder<S>().apply(builderAction).build()

public inline fun <S> Skill.Builder<S>.completeBehavior(crossinline block: SkillPostContext<S>.() -> Unit): Skill.Builder<S> =
    setCompleteBehavior(SkillCompleteBehavior(block))

public inline fun <S> Skill.Builder<S>.endBehavior(crossinline block: SkillPostContext<S>.() -> Unit): Skill.Builder<S> =
    setEndBehavior(SkillEndBehavior(block))

public inline fun <S> Skill.Builder<S>.startBehavior(crossinline block: SkillPostContext<S>.() -> Unit): Skill.Builder<S> =
    setStartBehavior(SkillStartBehavior(block))

public inline fun <S> Skill.Builder<S>.tickBehavior(crossinline block: SkillPostContext<S>.() -> Unit): Skill.Builder<S> =
    setTickBehavior(SkillTickBehavior(block))

public inline fun <S> Skill.Builder<S>.condition(crossinline block: SkillPreContext.() -> SkillResponse): Skill.Builder<S> =
    setCondition(SkillCondition(block))

public inline fun <S> Skill.Builder<S>.durationProvider(crossinline block: SkillPreContext.() -> Int): Skill.Builder<S> =
    setDurationProvider(SkillDurationProvider(block))

public inline fun <S> Skill.Builder<S>.cancelHandler(crossinline block: SkillPostContext<S>.() -> Boolean): Skill.Builder<S> =
    setCancelHandler(SkillCancelHandler(block))

public inline fun <S> Skill.Builder<S>.interruptHandler(crossinline block: SkillPostContext<S>.() -> Boolean): Skill.Builder<S> =
    setInterruptHandler(SkillInterruptHandler(block))

public inline fun <S> Skill.Builder<S>.stateFactory(crossinline block: SkillPreContext.() -> SkillStateCreationResult<S>): Skill.Builder<S> =
    setStateFactory(SkillStateFactory(block))