package org.tomidori.fable.skill;

import net.minecraft.text.Text;
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
    private final SkillCompleteBehavior completeBehavior;
    private final SkillEndBehavior endBehavior;
    private final SkillStartBehavior startBehavior;
    private final SkillTickBehavior tickBehavior;
    private final SkillCondition condition;
    private final SkillCancelHandler cancelHandler;
    private final SkillInterruptHandler interruptHandler;
    private final int duration;

    private @Nullable String translationKey;
    private @Nullable Text name;

    private Skill(
            SkillCompleteBehavior completeBehavior,
            SkillEndBehavior endBehavior,
            SkillStartBehavior startBehavior,
            SkillTickBehavior tickBehavior,
            SkillCondition condition,
            SkillCancelHandler cancelHandler,
            SkillInterruptHandler interruptHandler,
            int duration
    ) {
        this.completeBehavior = Objects.requireNonNull(completeBehavior);
        this.endBehavior = Objects.requireNonNull(endBehavior);
        this.startBehavior = Objects.requireNonNull(startBehavior);
        this.tickBehavior = Objects.requireNonNull(tickBehavior);
        this.condition = Objects.requireNonNull(condition);
        this.cancelHandler = Objects.requireNonNull(cancelHandler);
        this.interruptHandler = Objects.requireNonNull(interruptHandler);
        this.duration = duration;
    }

    public static Builder builder() {
        return new Builder();
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

    public int getDuration() {
        return duration;
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
        private SkillCompleteBehavior completeBehavior = SkillCompleteBehavior.noOp();
        private SkillEndBehavior endBehavior = SkillEndBehavior.noOp();
        private SkillStartBehavior startBehavior = SkillStartBehavior.noOp();
        private SkillTickBehavior tickBehavior = SkillTickBehavior.noOp();
        private SkillCondition condition = SkillCondition.defaultConditions();
        private SkillCancelHandler cancelHandler = SkillCancelHandler.alwaysAllow();
        private SkillInterruptHandler interruptHandler = SkillInterruptHandler.alwaysAllow();
        private int duration = 0;

        private Builder() {
        }

        public Builder setCompleteBehavior(SkillCompleteBehavior completeBehavior) {
            this.completeBehavior = completeBehavior;
            return this;
        }

        public Builder setEndBehavior(SkillEndBehavior endBehavior) {
            this.endBehavior = endBehavior;
            return this;
        }

        public Builder setStartBehavior(SkillStartBehavior startBehavior) {
            this.startBehavior = startBehavior;
            return this;
        }

        public Builder setTickBehavior(SkillTickBehavior tickBehavior) {
            this.tickBehavior = tickBehavior;
            return this;
        }

        public Builder setCondition(SkillCondition condition) {
            this.condition = condition;
            return this;
        }

        public Builder setCancelHandler(SkillCancelHandler cancelHandler) {
            this.cancelHandler = cancelHandler;
            return this;
        }

        public Builder setInterruptHandler(SkillInterruptHandler interruptHandler) {
            this.interruptHandler = interruptHandler;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Skill build() {
            return new Skill(
                    completeBehavior,
                    endBehavior,
                    startBehavior,
                    tickBehavior,
                    condition,
                    cancelHandler,
                    interruptHandler,
                    duration
            );
        }
    }
}
