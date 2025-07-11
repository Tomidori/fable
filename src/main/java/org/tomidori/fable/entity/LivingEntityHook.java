package org.tomidori.fable.entity;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.manager.SkillContainer;
import org.tomidori.fable.skill.manager.SkillCooldownManager;
import org.tomidori.fable.skill.manager.SkillManager;

public interface LivingEntityHook {
    default SkillManager getSkillManager() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default SkillContainer getSkillContainer() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @ApiStatus.Internal
    default void setSkillContainer(SkillContainer skillContainer) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default SkillCooldownManager getSkillCooldownManager() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @ApiStatus.Internal
    default void setSkillCooldownManager(SkillCooldownManager skillCooldownManager) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }
}
