package org.tomidori.fable.skill;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.core.AttributeBag;

import java.util.Objects;

public interface SkillExecutionContext extends SkillContext {
    AttributeBag getAttributes();

    int getDuration();

    void setDuration(int duration);

    @ApiStatus.Internal
    final class Impl implements SkillExecutionContext {
        private final SkillContext context;
        private final AttributeBag attributes;
        private int duration;

        public Impl(
                SkillContext context,
                AttributeBag attributes,
                int duration
        ) {
            this.context = Objects.requireNonNull(context);
            this.attributes = Objects.requireNonNull(attributes);
            this.duration = duration;
        }

        @Override
        public RegistryEntry<Skill> getSkill() {
            return context.getSkill();
        }

        @Override
        public LivingEntity getSource() {
            return context.getSource();
        }

        @Override
        public AttributeBag getAttributes() {
            return attributes;
        }

        @Override
        public int getDuration() {
            return duration;
        }

        @Override
        public void setDuration(int duration) {
            this.duration = duration;
        }
    }
}
