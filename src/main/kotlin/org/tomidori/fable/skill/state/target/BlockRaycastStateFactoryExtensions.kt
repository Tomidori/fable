package org.tomidori.fable.skill.state.target

import org.tomidori.fable.skill.SkillPreContext

public inline fun BlockRaycastStateFactory(builderAction: BlockRaycastStateFactory.Builder.() -> Unit): BlockRaycastStateFactory =
    BlockRaycastStateFactory.builder().apply(builderAction).build()

public inline fun BlockRaycastStateFactory.Builder.distanceProvider(crossinline block: SkillPreContext.() -> Double): BlockRaycastStateFactory.Builder =
    setDistanceProvider { context -> context.block() }