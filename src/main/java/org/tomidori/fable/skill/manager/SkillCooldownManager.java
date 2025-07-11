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
    private final Map<Identifier, Record> cooldownMap = new HashMap<>();
    private long time;

    @ApiStatus.Internal
    public SkillCooldownManager() {
    }

    public boolean isCoolingDown(Identifier id) {
        Objects.requireNonNull(id);
        return getRemainingTime(id) > 0;
    }

    public boolean isCoolingDown(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return isCoolingDown(
                FableRegistries.SKILL.getId(skill.value())
        );
    }

    public long getRemainingTime(Identifier id) {
        Objects.requireNonNull(id);
        Record record = cooldownMap.get(id);
        if (record == null) {
            return 0;
        }

        long remaining = record.endTime - time;
        return Math.max(0, remaining);
    }

    public long getRemainingTime(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return getRemainingTime(
                FableRegistries.SKILL.getId(skill.value())
        );
    }

    public float getCooldownProgress(Identifier id) {
        Objects.requireNonNull(id);
        Record record = cooldownMap.get(id);
        if (record == null) {
            return 1.0f;
        }

        long elapsed = time - record.startTime;
        return Math.min(1.0f, (float) elapsed / (record.endTime - record.startTime));
    }

    public float getCooldownProgress(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return getCooldownProgress(
                FableRegistries.SKILL.getId(skill.value())
        );
    }

    public void setCooldown(Identifier id, long duration) {
        Objects.requireNonNull(id);
        if (duration <= 0) {
            cooldownMap.remove(id);
            return;
        }

        cooldownMap.put(id, new Record(time, time + duration));
    }

    public void setCooldown(RegistryEntry<Skill> skill, long duration) {
        Objects.requireNonNull(skill);
        setCooldown(
                FableRegistries.SKILL.getId(skill.value()),
                duration
        );
    }

    @ApiStatus.Internal
    public void update() {
        time++;
        cooldownMap.entrySet().removeIf(entry -> entry.getValue().endTime <= time);
    }

    private record Record(long startTime, long endTime) {
    }
}
