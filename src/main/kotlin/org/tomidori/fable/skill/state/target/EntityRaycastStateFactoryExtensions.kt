package org.tomidori.fable.skill.state.target

import net.minecraft.entity.Entity
import org.tomidori.fable.skill.SkillPreContext

public inline fun EntityRaycastStateFactory(builderAction: EntityRaycastStateFactory.Builder.() -> Unit): EntityRaycastStateFactory =
    EntityRaycastStateFactory.builder().apply(builderAction).build()

public inline fun EntityRaycastStateFactory.Builder.distanceProvider(crossinline block: SkillPreContext.() -> Double): EntityRaycastStateFactory.Builder =
    setDistanceProvider { context -> context.block() }

public inline fun EntityRaycastStateFactory.Builder.predicate(crossinline block: SkillPreContext.(Entity) -> Boolean): EntityRaycastStateFactory.Builder =
    setPredicate { context, entity -> context.block(entity) }