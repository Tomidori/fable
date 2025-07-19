package org.tomidori.fable.skill;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;

public interface SkillContext {
    RegistryEntry<? extends Skill<?>> getSkill();

    LivingEntity getSource();
}
