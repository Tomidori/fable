package org.tomidori.fable.entity.attribute;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

public final class AttributeModifiers {
    private final Set<Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> entries;

    private AttributeModifiers(
            Set<Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> entries
    ) {
        this.entries = Set.copyOf(
                Objects.requireNonNull(entries)
        );
    }

    public static AttributeModifiers empty() {
        return new AttributeModifiers(Set.of());
    }

    public AttributeModifiers addModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
        Objects.requireNonNull(attribute);
        Objects.requireNonNull(modifier);

        Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier> entry =
                Map.entry(attribute, modifier);

        if (entries.contains(entry)) {
            return this;
        }

        Set<Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> newSet =
                new HashSet<>(entries.size() + 1);

        newSet.addAll(entries);
        newSet.add(entry);
        return new AttributeModifiers(newSet);
    }

    public AttributeModifiers addModifier(
            RegistryEntry<EntityAttribute> attribute,
            Identifier id,
            double value,
            EntityAttributeModifier.Operation operation
    ) {
        Objects.requireNonNull(attribute);
        Objects.requireNonNull(id);
        Objects.requireNonNull(operation);

        return addModifier(attribute, new EntityAttributeModifier(id, value, operation));
    }

    public AttributeModifiers addModifier(
            RegistryEntry<EntityAttribute> attribute,
            double value,
            EntityAttributeModifier.Operation operation
    ) {
        Objects.requireNonNull(attribute);
        Objects.requireNonNull(operation);

        Identifier id = Identifier.of("fable", UUID.randomUUID().toString());
        return addModifier(attribute, id, value, operation);
    }

    public AttributeModifiers removeModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
        Objects.requireNonNull(attribute);
        Objects.requireNonNull(modifier);

        Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier> entry =
                Map.entry(attribute, modifier);

        if (!entries.contains(entry)) {
            return this;
        }

        Set<Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> newSet =
                new HashSet<>(entries);

        newSet.remove(entry);
        return new AttributeModifiers(newSet);
    }

    @ApiStatus.Internal
    public void addModifiers(AttributeContainer container) {
        Objects.requireNonNull(container);

        for (Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier> entry : entries) {
            EntityAttributeInstance instance = container.getCustomInstance(entry.getKey());
            if (instance != null) {
                instance.removeModifier(entry.getValue());
                instance.addTemporaryModifier(entry.getValue());
            }
        }
    }

    @ApiStatus.Internal
    public void removeModifiers(AttributeContainer container) {
        Objects.requireNonNull(container);

        for (Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier> entry : entries) {
            EntityAttributeInstance instance = container.getCustomInstance(entry.getKey());
            if (instance != null) {
                instance.removeModifier(entry.getValue());
            }
        }
    }
}
