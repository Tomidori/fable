package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillInstance;

@FunctionalInterface
public interface SkillEndBehavior {
    static SkillEndBehavior noOp() {
        return instance -> {};
    }

    void onEnd(SkillInstance instance);

    @ApiStatus.NonExtendable
    default SkillEndBehavior andThen(SkillEndBehavior after) {
        return instance -> {
            onEnd(instance);
            after.onEnd(instance);
        };
    }
}
