package org.tomidori.fable.skill;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public interface SkillContext {
    RegistryEntry<Skill> getSkill();

    LivingEntity getSource();

    @ApiStatus.Internal
    final class Impl implements SkillContext {
        private final RegistryEntry<Skill> skill;
        private final LivingEntity source;

        public Impl(
                RegistryEntry<Skill> skill,
                LivingEntity source
        ) {
            this.skill = Objects.requireNonNull(skill);
            this.source = Objects.requireNonNull(source);
        }

        @Override
        public RegistryEntry<Skill> getSkill() {
            return skill;
        }

        @Override
        public LivingEntity getSource() {
            return source;
        }
    }
}
