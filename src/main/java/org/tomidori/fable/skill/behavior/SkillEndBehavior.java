package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillInstance;

import java.util.Objects;

@FunctionalInterface
public interface SkillEndBehavior {
    static SkillEndBehavior noOp() {
        return instance -> {};
    }

    void onEnd(SkillInstance instance);

    @ApiStatus.NonExtendable
    default SkillEndBehavior andThen(SkillEndBehavior after) {
        Objects.requireNonNull(after);
        return instance -> {
            onEnd(instance);
            after.onEnd(instance);
        };
    }
}
