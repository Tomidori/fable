package org.tomidori.fable.skill;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.tomidori.fable.registry.FableRegistries;
import org.tomidori.fable.skill.behavior.SkillCompleteBehavior;
import org.tomidori.fable.skill.behavior.SkillEndBehavior;
import org.tomidori.fable.skill.behavior.SkillStartBehavior;
import org.tomidori.fable.skill.behavior.SkillTickBehavior;
import org.tomidori.fable.skill.condition.SkillCondition;
import org.tomidori.fable.skill.handler.SkillCancelHandler;
import org.tomidori.fable.skill.handler.SkillInterruptHandler;

import java.util.Objects;

public final class Skill {
    private final @Nullable Identifier cooldownGroup;
    private final SkillCompleteBehavior completeBehavior;
    private final SkillEndBehavior endBehavior;
    private final SkillStartBehavior startBehavior;
    private final SkillTickBehavior tickBehavior;
    private final SkillCondition condition;
    private final SkillCancelHandler cancelHandler;
    private final SkillInterruptHandler interruptHandler;
    private final int initialDuration;

    private @Nullable String translationKey;
    private @Nullable Text name;

    private Skill(
            @Nullable Identifier cooldownGroup,
            SkillCompleteBehavior completeBehavior,
            SkillEndBehavior endBehavior,
            SkillStartBehavior startBehavior,
            SkillTickBehavior tickBehavior,
            SkillCondition condition,
            SkillCancelHandler cancelHandler,
            SkillInterruptHandler interruptHandler,
            int initialDuration
    ) {
        this.cooldownGroup = cooldownGroup;
        this.completeBehavior = Objects.requireNonNull(completeBehavior);
        this.endBehavior = Objects.requireNonNull(endBehavior);
        this.startBehavior = Objects.requireNonNull(startBehavior);
        this.tickBehavior = Objects.requireNonNull(tickBehavior);
        this.condition = Objects.requireNonNull(condition);
        this.cancelHandler = Objects.requireNonNull(cancelHandler);
        this.interruptHandler = Objects.requireNonNull(interruptHandler);
        this.initialDuration = initialDuration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public @Nullable Identifier getCooldownGroup() {
        return cooldownGroup;
    }

    public SkillCompleteBehavior getCompleteBehavior() {
        return completeBehavior;
    }

    public SkillEndBehavior getEndBehavior() {
        return endBehavior;
    }

    public SkillStartBehavior getStartBehavior() {
        return startBehavior;
    }

    public SkillTickBehavior getTickBehavior() {
        return tickBehavior;
    }

    public SkillCondition getCondition() {
        return condition;
    }

    public SkillCancelHandler getCancelHandler() {
        return cancelHandler;
    }

    public SkillInterruptHandler getInterruptHandler() {
        return interruptHandler;
    }

    public int getInitialDuration() {
        return initialDuration;
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

    public static final class Builder {
        private @Nullable Identifier cooldownGroup = null;
        private SkillCompleteBehavior completeBehavior = SkillCompleteBehavior.noOp();
        private SkillEndBehavior endBehavior = SkillEndBehavior.noOp();
        private SkillStartBehavior startBehavior = SkillStartBehavior.noOp();
        private SkillTickBehavior tickBehavior = SkillTickBehavior.noOp();
        private SkillCondition condition = SkillCondition.defaultConditions();
        private SkillCancelHandler cancelHandler = SkillCancelHandler.alwaysAllow();
        private SkillInterruptHandler interruptHandler = SkillInterruptHandler.alwaysAllow();
        private int initialDuration = 0;

        private Builder() {
        }

        public @Nullable Identifier getCooldownGroup() {
            return cooldownGroup;
        }

        public Builder setCooldownGroup(@Nullable Identifier cooldownGroup) {
            this.cooldownGroup = cooldownGroup;
            return this;
        }

        public SkillCompleteBehavior getCompleteBehavior() {
            return completeBehavior;
        }

        public Builder setCompleteBehavior(SkillCompleteBehavior completeBehavior) {
            this.completeBehavior = Objects.requireNonNull(completeBehavior);
            return this;
        }

        public SkillEndBehavior getEndBehavior() {
            return endBehavior;
        }

        public Builder setEndBehavior(SkillEndBehavior endBehavior) {
            this.endBehavior = Objects.requireNonNull(endBehavior);
            return this;
        }

        public SkillStartBehavior getStartBehavior() {
            return startBehavior;
        }

        public Builder setStartBehavior(SkillStartBehavior startBehavior) {
            this.startBehavior = Objects.requireNonNull(startBehavior);
            return this;
        }

        public SkillTickBehavior getTickBehavior() {
            return tickBehavior;
        }

        public Builder setTickBehavior(SkillTickBehavior tickBehavior) {
            this.tickBehavior = Objects.requireNonNull(tickBehavior);
            return this;
        }

        public SkillCondition getCondition() {
            return condition;
        }

        public Builder setCondition(SkillCondition condition) {
            this.condition = Objects.requireNonNull(condition);
            return this;
        }

        public SkillCancelHandler getCancelHandler() {
            return cancelHandler;
        }

        public Builder setCancelHandler(SkillCancelHandler cancelHandler) {
            this.cancelHandler = Objects.requireNonNull(cancelHandler);
            return this;
        }

        public SkillInterruptHandler getInterruptHandler() {
            return interruptHandler;
        }

        public Builder setInterruptHandler(SkillInterruptHandler interruptHandler) {
            this.interruptHandler = Objects.requireNonNull(interruptHandler);
            return this;
        }

        public int getInitialDuration() {
            return initialDuration;
        }

        public Builder setInitialDuration(int initialDuration) {
            this.initialDuration = initialDuration;
            return this;
        }

        public Skill build() {
            return new Skill(
                    cooldownGroup,
                    completeBehavior,
                    endBehavior,
                    startBehavior,
                    tickBehavior,
                    condition,
                    cancelHandler,
                    interruptHandler,
                    initialDuration
            );
        }
    }
}
