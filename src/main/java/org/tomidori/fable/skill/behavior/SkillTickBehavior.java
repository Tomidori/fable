package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillTickBehavior {
    static SkillTickBehavior noOp() {
        return context -> {};
    }

    void onTick(SkillContext context);

    @ApiStatus.NonExtendable
    default SkillTickBehavior andThen(SkillTickBehavior after) {
        Objects.requireNonNull(after);
        return context -> {
            onTick(context);
            after.onTick(context);
        };
    }
}
