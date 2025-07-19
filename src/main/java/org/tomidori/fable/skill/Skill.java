package org.tomidori.fable.skill;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.tomidori.fable.entity.attribute.AttributeModifiers;
import org.tomidori.fable.registry.FableRegistries;
import org.tomidori.fable.skill.behavior.SkillCompleteBehavior;
import org.tomidori.fable.skill.behavior.SkillEndBehavior;
import org.tomidori.fable.skill.behavior.SkillStartBehavior;
import org.tomidori.fable.skill.behavior.SkillTickBehavior;
import org.tomidori.fable.skill.condition.SkillCondition;
import org.tomidori.fable.skill.duration.SkillDurationProvider;
import org.tomidori.fable.skill.handler.SkillCancelHandler;
import org.tomidori.fable.skill.handler.SkillInterruptHandler;
import org.tomidori.fable.skill.state.SkillStateFactory;

import java.util.Objects;

public final class Skill<S> {
    private final SkillCondition condition;
    private final SkillStateFactory<S> stateFactory;
    private final SkillDurationProvider durationProvider;
    private final SkillStartBehavior<S> startBehavior;
    private final SkillTickBehavior<S> tickBehavior;
    private final SkillCompleteBehavior<S> completeBehavior;
    private final SkillEndBehavior<S> endBehavior;
    private final SkillCancelHandler<S> cancelHandler;
    private final SkillInterruptHandler<S> interruptHandler;
    private final AttributeModifiers attributeModifiers;
    private final @Nullable Identifier cooldownGroup;

    private @Nullable String translationKey;
    private @Nullable Text name;

    private Skill(
            SkillCondition condition,
            SkillStateFactory<S> stateFactory,
            SkillDurationProvider durationProvider,
            SkillStartBehavior<S> startBehavior,
            SkillTickBehavior<S> tickBehavior,
            SkillCompleteBehavior<S> completeBehavior,
            SkillEndBehavior<S> endBehavior,
            SkillCancelHandler<S> cancelHandler,
            SkillInterruptHandler<S> interruptHandler,
            AttributeModifiers attributeModifiers,
            @Nullable Identifier cooldownGroup
    ) {
        this.condition = Objects.requireNonNull(condition);
        this.stateFactory = Objects.requireNonNull(stateFactory);
        this.durationProvider = Objects.requireNonNull(durationProvider);
        this.startBehavior = Objects.requireNonNull(startBehavior);
        this.tickBehavior = Objects.requireNonNull(tickBehavior);
        this.completeBehavior = Objects.requireNonNull(completeBehavior);
        this.endBehavior = Objects.requireNonNull(endBehavior);
        this.cancelHandler = Objects.requireNonNull(cancelHandler);
        this.interruptHandler = Objects.requireNonNull(interruptHandler);
        this.attributeModifiers = Objects.requireNonNull(attributeModifiers);
        this.cooldownGroup = cooldownGroup;
    }

    public static <S> Builder<S> builder() {
        return new Builder<>();
    }

    public SkillCondition getCondition() {
        return condition;
    }

    public SkillStateFactory<S> getStateFactory() {
        return stateFactory;
    }

    public SkillDurationProvider getDurationProvider() {
        return durationProvider;
    }

    public SkillStartBehavior<S> getStartBehavior() {
        return startBehavior;
    }

    public SkillTickBehavior<S> getTickBehavior() {
        return tickBehavior;
    }

    public SkillCompleteBehavior<S> getCompleteBehavior() {
        return completeBehavior;
    }

    public SkillEndBehavior<S> getEndBehavior() {
        return endBehavior;
    }

    public SkillCancelHandler<S> getCancelHandler() {
        return cancelHandler;
    }

    public SkillInterruptHandler<S> getInterruptHandler() {
        return interruptHandler;
    }

    public AttributeModifiers getAttributeModifiers() {
        return attributeModifiers;
    }

    public @Nullable Identifier getCooldownGroup() {
        return cooldownGroup;
    }

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey("skill", FableRegistries.SKILL.getId(this));
        }

        return translationKey;
    }

    public Text getName() {
        if (name == null) {
            name = Text.translatable(getTranslationKey());
        }

        return name;
    }

    public static final class Builder<S> {
        private SkillCondition condition = SkillCondition.defaultConditions();
        private @Nullable SkillStateFactory<S> stateFactory = null;
        private @Nullable SkillDurationProvider durationProvider = null;
        private SkillStartBehavior<S> startBehavior = SkillStartBehavior.noOp();
        private SkillTickBehavior<S> tickBehavior = SkillTickBehavior.noOp();
        private SkillCompleteBehavior<S> completeBehavior = SkillCompleteBehavior.noOp();
        private SkillEndBehavior<S> endBehavior = SkillEndBehavior.noOp();
        private SkillCancelHandler<S> cancelHandler = SkillCancelHandler.alwaysAllow();
        private SkillInterruptHandler<S> interruptHandler = SkillInterruptHandler.alwaysAllow();
        private AttributeModifiers attributeModifiers = AttributeModifiers.empty();
        private @Nullable Identifier cooldownGroup = null;

        private Builder() {
        }

        public SkillCondition getCondition() {
            return condition;
        }

        public Builder<S> setCondition(SkillCondition condition) {
            this.condition = Objects.requireNonNull(condition);
            return this;
        }

        public @Nullable SkillStateFactory<S> getStateFactory() {
            return stateFactory;
        }

        public Builder<S> setStateFactory(SkillStateFactory<S> stateFactory) {
            this.stateFactory = Objects.requireNonNull(stateFactory);
            return this;
        }

        public Builder<S> setState(S state) {
            this.stateFactory = SkillStateFactory.constant(Objects.requireNonNull(state));
            return this;
        }

        public @Nullable SkillDurationProvider getDurationProvider() {
            return durationProvider;
        }

        public Builder<S> setDurationProvider(SkillDurationProvider durationProvider) {
            this.durationProvider = Objects.requireNonNull(durationProvider);
            return this;
        }

        public Builder<S> setDuration(int duration) {
            this.durationProvider = SkillDurationProvider.constant(duration);
            return this;
        }

        public SkillStartBehavior<S> getStartBehavior() {
            return startBehavior;
        }

        public Builder<S> setStartBehavior(SkillStartBehavior<S> startBehavior) {
            this.startBehavior = Objects.requireNonNull(startBehavior);
            return this;
        }

        public SkillTickBehavior<S> getTickBehavior() {
            return tickBehavior;
        }

        public Builder<S> setTickBehavior(SkillTickBehavior<S> tickBehavior) {
            this.tickBehavior = Objects.requireNonNull(tickBehavior);
            return this;
        }

        public SkillCompleteBehavior<S> getCompleteBehavior() {
            return completeBehavior;
        }

        public Builder<S> setCompleteBehavior(SkillCompleteBehavior<S> completeBehavior) {
            this.completeBehavior = Objects.requireNonNull(completeBehavior);
            return this;
        }

        public SkillEndBehavior<S> getEndBehavior() {
            return endBehavior;
        }

        public Builder<S> setEndBehavior(SkillEndBehavior<S> endBehavior) {
            this.endBehavior = Objects.requireNonNull(endBehavior);
            return this;
        }

        public SkillCancelHandler<S> getCancelHandler() {
            return cancelHandler;
        }

        public Builder<S> setCancelHandler(SkillCancelHandler<S> cancelHandler) {
            this.cancelHandler = Objects.requireNonNull(cancelHandler);
            return this;
        }

        public SkillInterruptHandler<S> getInterruptHandler() {
            return interruptHandler;
        }

        public Builder<S> setInterruptHandler(SkillInterruptHandler<S> interruptHandler) {
            this.interruptHandler = Objects.requireNonNull(interruptHandler);
            return this;
        }

        public AttributeModifiers getAttributeModifiers() {
            return attributeModifiers;
        }

        public Builder<S> setAttributeModifiers(AttributeModifiers attributeModifiers) {
            this.attributeModifiers = Objects.requireNonNull(attributeModifiers);
            return this;
        }

        public Builder<S> addModifier(
                RegistryEntry<EntityAttribute> attribute,
                EntityAttributeModifier modifier
        ) {
            Objects.requireNonNull(attribute);
            Objects.requireNonNull(modifier);
            this.attributeModifiers = attributeModifiers.addModifier(attribute, modifier);
            return this;
        }

        public Builder<S> addModifier(
                RegistryEntry<EntityAttribute> attribute,
                Identifier id,
                double value,
                EntityAttributeModifier.Operation operation
        ) {
            Objects.requireNonNull(attribute);
            Objects.requireNonNull(id);
            Objects.requireNonNull(operation);
            this.attributeModifiers = attributeModifiers.addModifier(attribute, id, value, operation);
            return this;
        }

        public Builder<S> addModifier(
                RegistryEntry<EntityAttribute> attribute,
                double value,
                EntityAttributeModifier.Operation operation
        ) {
            Objects.requireNonNull(attribute);
            Objects.requireNonNull(operation);
            this.attributeModifiers = attributeModifiers.addModifier(attribute, value, operation);
            return this;
        }

        public Builder<S> removeModifier(
                RegistryEntry<EntityAttribute> attribute,
                EntityAttributeModifier modifier
        ) {
            Objects.requireNonNull(attribute);
            Objects.requireNonNull(modifier);
            this.attributeModifiers = attributeModifiers.removeModifier(attribute, modifier);
            return this;
        }

        public @Nullable Identifier getCooldownGroup() {
            return cooldownGroup;
        }

        public Builder<S> setCooldownGroup(@Nullable Identifier cooldownGroup) {
            this.cooldownGroup = cooldownGroup;
            return this;
        }

        public Skill<S> build() {
            return new Skill<>(
                    condition,
                    Objects.requireNonNull(stateFactory),
                    Objects.requireNonNull(durationProvider),
                    startBehavior,
                    tickBehavior,
                    completeBehavior,
                    endBehavior,
                    cancelHandler,
                    interruptHandler,
                    attributeModifiers,
                    cooldownGroup
            );
        }
    }
}
