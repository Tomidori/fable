package org.tomidori.fable.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import org.tomidori.fable.skill.Skill;

public final class FableRegistries {
    public static final Registry<Skill<?>> SKILL = FabricRegistryBuilder.createSimple(FableRegistryKeys.SKILL).buildAndRegister();

    private FableRegistries() {
    }

    public static void initialize() {
    }
}
