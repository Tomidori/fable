package org.tomidori.fable.entity;

import org.tomidori.fable.skill.manager.SkillManager;

public interface LivingEntityHook {
    default SkillManager getSkillManager() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }
}
