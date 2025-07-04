package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillInstance;

@FunctionalInterface
public interface SkillStartBehavior {
    static SkillStartBehavior noOp() {
        return instance -> {};
    }

    void onStart(SkillInstance instance);

    @ApiStatus.NonExtendable
    default SkillStartBehavior andThen(SkillStartBehavior after) {
        return instance -> {
            onStart(instance);
            after.onStart(instance);
        };
    }
}
