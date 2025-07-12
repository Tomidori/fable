package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillEndBehavior {
    static SkillEndBehavior noOp() {
        return context -> {};
    }

    void onEnd(SkillContext context);

    @ApiStatus.NonExtendable
    default SkillEndBehavior andThen(SkillEndBehavior after) {
        Objects.requireNonNull(after);
        return context -> {
            onEnd(context);
            after.onEnd(context);
        };
    }
}
