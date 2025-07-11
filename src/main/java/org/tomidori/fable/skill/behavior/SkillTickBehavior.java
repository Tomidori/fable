package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillInstance;

import java.util.Objects;

@FunctionalInterface
public interface SkillTickBehavior {
    static SkillTickBehavior noOp() {
        return instance -> {};
    }

    void onTick(SkillInstance instance);

    @ApiStatus.NonExtendable
    default SkillTickBehavior andThen(SkillTickBehavior after) {
        Objects.requireNonNull(after);
        return instance -> {
            onTick(instance);
            after.onTick(instance);
        };
    }
}
