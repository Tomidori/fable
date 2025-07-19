package org.tomidori.fable.skill;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public final class SkillPreContext implements SkillContext {
    private final RegistryEntry<? extends Skill<?>> skill;
    private final LivingEntity source;

    @ApiStatus.Internal
    public SkillPreContext(
            RegistryEntry<? extends Skill<?>> skill,
            LivingEntity source
    ) {
        this.skill = Objects.requireNonNull(skill);
        this.source = Objects.requireNonNull(source);
    }

    @Override
    public RegistryEntry<? extends Skill<?>> getSkill() {
        return skill;
    }

    @Override
    public LivingEntity getSource() {
        return source;
    }
}
