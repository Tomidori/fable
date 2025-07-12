package org.tomidori.fable.skill;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.core.AttributeBag;

import java.util.Objects;

public final class SkillContext {
    private final RegistryEntry<Skill> skill;
    private final LivingEntity source;
    private final AttributeBag attributes;
    private int duration;

    @ApiStatus.Internal
    public SkillContext(
            RegistryEntry<Skill> skill,
            LivingEntity source,
            AttributeBag attributes,
            int duration
    ) {
        this.skill = Objects.requireNonNull(skill);
        this.source = Objects.requireNonNull(source);
        this.attributes = Objects.requireNonNull(attributes);
        this.duration = duration;
    }

    public RegistryEntry<Skill> getSkill() {
        return skill;
    }

    public LivingEntity getSource() {
        return source;
    }

    public AttributeBag getAttributes() {
        return attributes;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
