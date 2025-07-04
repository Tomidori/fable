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
            .setStartBehavior(instance -> {
                LOGGER.info("Skill started");

                // attribute test
                instance.getAttributes().set("rotationVector", instance.getSource().getRotationVector());
            })
            .setCompleteBehavior(instance -> {
                LOGGER.info("Skill completed");

                // attribute test
                instance.getSource().setVelocity(instance.getAttributes().getOrThrow("rotationVector"));
                instance.getSource().velocityModified = true;
            })
            .setTickBehavior(instance ->
                    LOGGER.info("Skill ticked")
            )
            .setEndBehavior(instance ->
                    LOGGER.info("Skill ended")
            )
            .setCancelHandler(instance -> {
                LOGGER.info("Skill canceled");
                return true;
            })
            .setInterruptHandler(instance -> {
                LOGGER.info("Skill interrupted");
                return true;
            })
            .setDuration(50)
            .build();

    @Override
    public void onInitialize() {
        Registry.register(FableRegistries.SKILL, Identifier.of("fable-test", "skill"), testSkill);
    }
}
