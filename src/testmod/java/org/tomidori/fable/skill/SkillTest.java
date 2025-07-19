package org.tomidori.fable.skill;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomidori.fable.registry.FableRegistries;
import org.tomidori.fable.skill.state.SkillStateCreationResult;

public final class SkillTest implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkillTest.class);

    private final Skill<Vec3d> testSkill = Skill.<Vec3d>builder()
            .setStateFactory(context -> {
                LOGGER.info("Skill state created");

                // state test
                return SkillStateCreationResult.success(
                        context.getSource().getRotationVector()
                );
            })
            .setDurationProvider(context -> {
                LOGGER.info("Skill duration set");

                // duration set
                return 50;
            })
            .setStartBehavior(context ->
                    LOGGER.info("Skill started")
            )
            .setTickBehavior(context ->
                    LOGGER.info("Skill ticked")
            )
            .setCompleteBehavior(context -> {
                LOGGER.info("Skill completed");

                // state test
                context.getSource().setVelocity(context.getState());
                context.getSource().velocityModified = true;

                // cooldown test
                context.getSource().getSkillCooldownManager().setCooldown(context.getSkill(), 50);
            })
            .setEndBehavior(context ->
                    LOGGER.info("Skill ended")
            )
            .setCancelHandler(context -> {
                LOGGER.info("Skill canceled");

                // allow cancel
                return true;
            })
            .setInterruptHandler(context -> {
                LOGGER.info("Skill interrupted");

                // allow interrupt
                return true;
            })
            .addModifier(
                    EntityAttributes.MOVEMENT_SPEED,
                    -0.7,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )
            .setCooldownGroup(null)
            .build();

    @Override
    public void onInitialize() {
        Registry.register(
                FableRegistries.SKILL,
                Identifier.of("fable-testmod", "skill_test"),
                testSkill
        );
    }
}
