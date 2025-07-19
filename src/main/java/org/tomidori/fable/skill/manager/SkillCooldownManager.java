package org.tomidori.fable.skill.manager;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.registry.FableRegistries;
import org.tomidori.fable.skill.Skill;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class SkillCooldownManager {
    private final Map<Identifier, Record> records = new HashMap<>();
    private long time;

    @ApiStatus.Internal
    public SkillCooldownManager() {
    }

    public boolean isCoolingDown(Identifier id) {
        Objects.requireNonNull(id);
        return getRemainingTime(id) > 0;
    }

    public boolean isCoolingDown(RegistryEntry<? extends Skill<?>> skill) {
        Objects.requireNonNull(skill);
        return isCoolingDown(getId(skill));
    }

    public long getRemainingTime(Identifier id) {
        Objects.requireNonNull(id);
        Record record = records.get(id);
        if (record == null) {
            return 0;
        }

        long remaining = record.endTime - time;
        return Math.max(0, remaining);
    }

    public long getRemainingTime(RegistryEntry<? extends Skill<?>> skill) {
        Objects.requireNonNull(skill);
        return getRemainingTime(getId(skill));
    }

    public float getCooldownProgress(Identifier id) {
        Objects.requireNonNull(id);
        Record record = records.get(id);
        if (record == null) {
            return 1.0f;
        }

        long elapsed = time - record.startTime;
        return Math.min(1.0f, (float) elapsed / (record.endTime - record.startTime));
    }

    public float getCooldownProgress(RegistryEntry<? extends Skill<?>> skill) {
        Objects.requireNonNull(skill);
        return getCooldownProgress(getId(skill));
    }

    public void setCooldown(Identifier id, long duration) {
        Objects.requireNonNull(id);
        if (duration <= 0) {
            records.remove(id);
            return;
        }

        records.put(id, new Record(time, time + duration));
    }

    public void setCooldown(RegistryEntry<? extends Skill<?>> skill, long duration) {
        Objects.requireNonNull(skill);
        setCooldown(getId(skill), duration);
    }

    @ApiStatus.Internal
    public void update() {
        time++;
        records.entrySet().removeIf(entry -> entry.getValue().endTime <= time);
    }

    private Identifier getId(RegistryEntry<? extends Skill<?>> skill) {
        Identifier cooldownGroup = skill.value().getCooldownGroup();
        return cooldownGroup == null ?
                FableRegistries.SKILL.getId(skill.value()) :
                cooldownGroup;
    }

    private record Record(long startTime, long endTime) {
    }
}
