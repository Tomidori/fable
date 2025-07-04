package org.tomidori.fable.skill.manager;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.tomidori.fable.core.AttributeBag;
import org.tomidori.fable.skill.Skill;
import org.tomidori.fable.skill.SkillContext;
import org.tomidori.fable.skill.SkillInstance;
import org.tomidori.fable.skill.SkillResponse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class SkillManager {
    private final Supplier<LivingEntity> entitySupplier;

    private final Map<RegistryEntry<Skill>, Integer> skillCooldowns = new HashMap<>();
    private @Nullable SkillInstance skillInstance;

    @ApiStatus.Internal
    public SkillManager(Supplier<LivingEntity> entitySupplier) {
        this.entitySupplier = Objects.requireNonNull(entitySupplier);
    }

    public boolean hasSkillCooldown(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return getSkillCooldown(skill) > 0;
    }

    public int getSkillCooldown(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return skillCooldowns.getOrDefault(skill, 0);
    }

    public void setSkillCooldown(RegistryEntry<Skill> skill, int cooldown) {
        Objects.requireNonNull(skill);
        skillCooldowns.put(skill, cooldown);
    }

    public boolean isCastingSkill() {
        return skillInstance != null;
    }

    public @Nullable SkillInstance getSkillInstance() {
        return skillInstance;
    }

    public SkillResponse canCastSkill(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return skill.value().getCondition().check(skillContext(skill));
    }

    public SkillResponse castSkill(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        SkillResponse response = canCastSkill(skill);
        if (response instanceof SkillResponse.Failure) {
            return response;
        }

        beginCastingSkill(skillInstance(skill));
        return response;
    }

    public boolean cancelCastingSkill() {
        return isCastingSkill() &&
                stopCastingSkill(skillInstance, false);
    }

    public boolean interruptCastingSkill() {
        return isCastingSkill() &&
                stopCastingSkill(skillInstance, true);
    }

    @ApiStatus.Internal
    public void update() {
        updateSkillCooldowns();
        if (isCastingSkill()) {
            updateCastingSkill(skillInstance);
        }
    }

    private void updateSkillCooldowns() {
        Iterator<Map.Entry<RegistryEntry<Skill>, Integer>> iterator = skillCooldowns.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<RegistryEntry<Skill>, Integer> entry = iterator.next();
            int cooldown = entry.getValue() - 1;
            if (cooldown > 0) {
                entry.setValue(cooldown);
            } else {
                iterator.remove();
            }
        }
    }

    private void beginCastingSkill(SkillInstance skillInstance) {
        this.skillInstance = skillInstance;
        skillInstance.getSkill().value().getStartBehavior().onStart(skillInstance);
    }

    private void endCastingSkill(SkillInstance skillInstance) {
        skillInstance.getSkill().value().getEndBehavior().onEnd(skillInstance);
        this.skillInstance = null;
    }

    private boolean stopCastingSkill(SkillInstance skillInstance, boolean force) {
        boolean shouldStop = force ?
                skillInstance.getSkill().value().getInterruptHandler().shouldInterrupt(skillInstance) :
                skillInstance.getSkill().value().getCancelHandler().shouldCancel(skillInstance);

        if (!shouldStop) {
            return false;
        }

        endCastingSkill(skillInstance);
        return true;
    }

    private void updateCastingSkill(SkillInstance skillInstance) {
        skillInstance.setDuration(skillInstance.getDuration() - 1);
        if (skillInstance.getDuration() > 0) {
            skillInstance.getSkill().value().getTickBehavior().onTick(skillInstance);
        } else {
            skillInstance.getSkill().value().getCompleteBehavior().onComplete(skillInstance);
            endCastingSkill(skillInstance);
        }
    }

    private SkillContext skillContext(RegistryEntry<Skill> skill) {
        return new SkillContext.Impl(skill, entitySupplier.get());
    }

    private SkillInstance skillInstance(RegistryEntry<Skill> skill) {
        return new SkillInstance.Impl(skillContext(skill), AttributeBag.create(), skill.value().getDuration());
    }
}
