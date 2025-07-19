package org.tomidori.fable.skill;

import net.minecraft.text.Text;

import java.util.Objects;

public sealed interface SkillResponse {
    static Success success() {
        return new Success();
    }

    static Failure failure(Text reason) {
        return new Failure(reason);
    }

    static Failure onCooldown() {
        return failure(Text.translatable("skill.failure.onCooldown"));
    }

    static Failure inProgress() {
        return failure(Text.translatable("skill.failure.inProgress"));
    }

    static Failure notLearned() {
        return failure(Text.translatable("skill.failure.notLearned"));
    }

    static Failure noTarget() {
        return failure(Text.translatable("skill.failure.noTarget"));
    }

    static Failure unavailable() {
        return failure(Text.translatable("skill.failure.unavailable"));
    }

    final class Success implements SkillResponse {
        private Success() {
        }
    }

    final class Failure implements SkillResponse {
        private final Text reason;

        private Failure(Text reason) {
            this.reason = Objects.requireNonNull(reason);
        }

        public Text getReason() {
            return reason;
        }
    }
}
