package org.tomidori.fable.skill

import org.tomidori.fable.skill.behavior.SkillCompleteBehavior
import org.tomidori.fable.skill.behavior.SkillEndBehavior
import org.tomidori.fable.skill.behavior.SkillStartBehavior
import org.tomidori.fable.skill.behavior.SkillTickBehavior
import org.tomidori.fable.skill.condition.SkillCondition
import org.tomidori.fable.skill.handler.SkillCancelHandler
import org.tomidori.fable.skill.handler.SkillInterruptHandler

public inline fun Skill(builderAction: Skill.Builder.() -> Unit): Skill = Skill.builder().apply(builderAction).build()

public inline fun Skill.Builder.completeBehavior(crossinline block: SkillExecutionContext.() -> Unit): Skill.Builder =
    setCompleteBehavior(SkillCompleteBehavior(block))

public inline fun Skill.Builder.endBehavior(crossinline block: SkillExecutionContext.() -> Unit): Skill.Builder =
    setEndBehavior(SkillEndBehavior(block))

public inline fun Skill.Builder.startBehavior(crossinline block: SkillExecutionContext.() -> Unit): Skill.Builder =
    setStartBehavior(SkillStartBehavior(block))

public inline fun Skill.Builder.tickBehavior(crossinline block: SkillExecutionContext.() -> Unit): Skill.Builder =
    setTickBehavior(SkillTickBehavior(block))

public inline fun Skill.Builder.condition(crossinline block: SkillContext.() -> SkillResponse): Skill.Builder =
    setCondition(SkillCondition(block))

public inline fun Skill.Builder.cancelHandler(crossinline block: SkillExecutionContext.() -> Boolean): Skill.Builder =
    setCancelHandler(SkillCancelHandler(block))

public inline fun Skill.Builder.interruptHandler(crossinline block: SkillExecutionContext.() -> Boolean): Skill.Builder =
    setInterruptHandler(SkillInterruptHandler(block))