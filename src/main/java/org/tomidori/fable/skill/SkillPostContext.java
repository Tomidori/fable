package org.tomidori.fable.skill;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public final class SkillPostContext<S> implements SkillContext {
    private final RegistryEntry<? extends Skill<S>> skill;
    private final LivingEntity source;
    private final S state;
    private int remainingTicks;

    @ApiStatus.Internal
    public SkillPostContext(
            RegistryEntry<? extends Skill<S>> skill,
            LivingEntity source,
            S state,
            int remainingTicks
    ) {
        this.skill = Objects.requireNonNull(skill);
        this.source = Objects.requireNonNull(source);
        this.state = Objects.requireNonNull(state);
        this.remainingTicks = remainingTicks;
    }

    @Override
    public RegistryEntry<? extends Skill<S>> getSkill() {
        return skill;
    }

    @Override
    public LivingEntity getSource() {
        return source;
    }

    public S getState() {
        return state;
    }

    public int getRemainingTicks() {
        return remainingTicks;
    }

    public void setRemainingTicks(int remainingTicks) {
        this.remainingTicks = remainingTicks;
    }
}
