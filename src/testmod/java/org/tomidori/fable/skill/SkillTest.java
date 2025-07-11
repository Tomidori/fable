package org.tomidori.fable.skill;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomidori.fable.registry.FableRegistries;

public final class SkillTest implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkillTest.class);

    private final Skill testSkill = Skill.builder()
            .setStartBehavior(context -> {
                LOGGER.info("Skill started");

                // attribute test
                context.getAttributes().set("rotationVector", context.getSource().getRotationVector());
            })
            .setCompleteBehavior(context -> {
                LOGGER.info("Skill completed");

                // attribute test
                context.getSource().setVelocity(context.getAttributes().getOrThrow("rotationVector"));
                context.getSource().velocityModified = true;

                // cooldown test
                context.getSource().getSkillCooldownManager().setCooldown(context.getSkill(), 50);
            })
            .setTickBehavior(context ->
                    LOGGER.info("Skill ticked")
            )
            .setEndBehavior(context ->
                    LOGGER.info("Skill ended")
            )
            .setCancelHandler(context -> {
                LOGGER.info("Skill canceled");
                return true;
            })
            .setInterruptHandler(context -> {
                LOGGER.info("Skill interrupted");
                return true;
            })
            .setInitialDuration(50)
            .build();

    @Override
    public void onInitialize() {
        Registry.register(FableRegistries.SKILL, Identifier.of("fable-test", "skill"), testSkill);
    }
}
