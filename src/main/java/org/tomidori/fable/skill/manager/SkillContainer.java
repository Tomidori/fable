package org.tomidori.fable.skill.manager;

import net.minecraft.registry.entry.RegistryEntry;
import org.tomidori.fable.skill.Skill;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class SkillContainer {
    private final Set<RegistryEntry<? extends Skill<?>>> skills = new HashSet<>();

    public Set<? extends RegistryEntry<? extends Skill<?>>> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    public boolean hasSkill(RegistryEntry<? extends Skill<?>> skill) {
        Objects.requireNonNull(skill);
        return skills.contains(skill);
    }

    public boolean addSkill(RegistryEntry<? extends Skill<?>> skill) {
        Objects.requireNonNull(skill);
        return skills.add(skill);
    }

    public boolean removeSkill(RegistryEntry<? extends Skill<?>> skill) {
        Objects.requireNonNull(skill);
        return skills.remove(skill);
    }
}
