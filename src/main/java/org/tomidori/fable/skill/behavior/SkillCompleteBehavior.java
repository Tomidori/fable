package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillInstance;

import java.util.Objects;

@FunctionalInterface
public interface SkillCompleteBehavior {
    static SkillCompleteBehavior noOp() {
        return instance -> {};
    }

    void onComplete(SkillInstance instance);

    @ApiStatus.NonExtendable
    default SkillCompleteBehavior andThen(SkillCompleteBehavior after) {
        Objects.requireNonNull(after);
        return instance -> {
            onComplete(instance);
            after.onComplete(instance);
        };
    }
}
