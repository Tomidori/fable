package org.tomidori.fable.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.tomidori.fable.skill.Skill;

public final class FableRegistryKeys {
    public static final RegistryKey<Registry<Skill<?>>> SKILL = RegistryKey.ofRegistry(Identifier.of("fable", "skill"));

    private FableRegistryKeys() {
    }

    public static void initialize() {
    }
}
