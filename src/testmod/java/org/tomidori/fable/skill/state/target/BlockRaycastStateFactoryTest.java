package org.tomidori.fable.skill.state.target;

import net.fabricmc.api.ModInitializer;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import org.tomidori.fable.registry.FableRegistries;
import org.tomidori.fable.skill.Skill;

public final class BlockRaycastStateFactoryTest implements ModInitializer {
    private final Skill<BlockHitResult> testSkill = Skill.<BlockHitResult>builder()
            .setStateFactory(
                    BlockRaycastStateFactory.builder()
                            .build()
            )
            .setCompleteBehavior(context -> {
                Vec3d pos = context.getState().getPos();
                ((ServerWorld) context.getSource().getWorld()).spawnParticles(
                        ParticleTypes.POOF,
                        pos.x,
                        pos.y,
                        pos.z,
                        3,
                        0.0,
                        0.0,
                        0.0,
                        0.0
                );
            })
            .setDuration(5)
            .build();

    @Override
    public void onInitialize() {
        Registry.register(
                FableRegistries.SKILL,
                Identifier.of("fable-testmod", "block_raycast_state_factory_test"),
                testSkill
        );
    }
}
