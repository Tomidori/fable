package org.tomidori.fable.skill.duration;

import org.tomidori.fable.skill.SkillPreContext;

@FunctionalInterface
public interface SkillDurationProvider {
    static SkillDurationProvider constant(int duration) {
        return context -> duration;
    }

    int get(SkillPreContext context);
}
