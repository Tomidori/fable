package org.tomidori.fable.skill.state.target;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import org.tomidori.fable.registry.FableRegistries;
import org.tomidori.fable.skill.Skill;

public final class EntityRaycastStateFactoryTest implements ModInitializer {
    private final Skill<EntityHitResult> testSkill = Skill.<EntityHitResult>builder()
            .setStateFactory(
                    EntityRaycastStateFactory.builder()
                            .build()
            )
            .setCompleteBehavior(context -> {
                Entity entity = context.getState().getEntity();
                entity.damage(
                        (ServerWorld) entity.getWorld(),
                        entity.getDamageSources().magic(),
                        1.0f
                );
            })
            .setDuration(5)
            .build();

    @Override
    public void onInitialize() {
        Registry.register(
                FableRegistries.SKILL,
                Identifier.of("fable-testmod", "entity_raycast_state_factory_test"),
                testSkill
        );
    }
}
