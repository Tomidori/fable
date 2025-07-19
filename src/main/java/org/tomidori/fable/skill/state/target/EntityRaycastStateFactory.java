package org.tomidori.fable.skill.state.target;

import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.tomidori.fable.skill.SkillPreContext;
import org.tomidori.fable.skill.SkillResponse;
import org.tomidori.fable.skill.state.SkillStateCreationResult;
import org.tomidori.fable.skill.state.SkillStateFactory;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public final class EntityRaycastStateFactory implements SkillStateFactory<EntityHitResult> {
    private final ToDoubleFunction<SkillPreContext> distanceProvider;
    private final BiPredicate<SkillPreContext, Entity> predicate;
    private final float[] margins;

    private EntityRaycastStateFactory(
            ToDoubleFunction<SkillPreContext> distanceProvider,
            BiPredicate<SkillPreContext, Entity> predicate,
            float[] margins
    ) {
        this.distanceProvider = Objects.requireNonNull(distanceProvider);
        this.predicate = Objects.requireNonNull(predicate);
        this.margins = Objects.requireNonNull(margins);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SkillStateCreationResult<EntityHitResult> create(SkillPreContext context) {
        Objects.requireNonNull(context);

        Entity source = context.getSource();
        EntityHitResult entityHitResult = getCollision(
                source,
                source.getEyePos(),
                source.raycast(distanceProvider.applyAsDouble(context), 1.0f, false).getPos(),
                margins,
                entity -> predicate.test(context, entity)
        );

        return entityHitResult == null ?
                SkillStateCreationResult.failure(SkillResponse.noTarget()) :
                SkillStateCreationResult.success(entityHitResult);
    }

    public static @Nullable EntityHitResult getCollision(
            Entity entity,
            Vec3d min,
            Vec3d max,
            float[] margins,
            Predicate<Entity> predicate
    ) {
        Objects.requireNonNull(entity);
        Objects.requireNonNull(min);
        Objects.requireNonNull(max);
        Objects.requireNonNull(margins);
        Objects.requireNonNull(predicate);

        World world = entity.getWorld();
        Box box = entity.getBoundingBox().stretch(max.subtract(min)).expand(1.0);

        for (float margin : margins) {
            EntityHitResult entityHitResult = ProjectileUtil.getEntityCollision(
                    world,
                    entity,
                    min,
                    max,
                    box,
                    predicate,
                    margin
            );
            if (entityHitResult != null) {
                return entityHitResult;
            }
        }

        return null;
    }

    public static final class Builder {
        private ToDoubleFunction<SkillPreContext> distanceProvider = context -> context.getSource().getAttributeValue(EntityAttributes.ENTITY_INTERACTION_RANGE);
        private BiPredicate<SkillPreContext, Entity> predicate = (context, entity) -> entity.canHit() && entity.isAlive() && !entity.isTeammate(context.getSource());
        private float[] margins = {0.0f, 0.25f, 0.5f, 1.0f};

        private Builder() {
        }

        public ToDoubleFunction<SkillPreContext> getDistanceProvider() {
            return distanceProvider;
        }

        public Builder setDistanceProvider(ToDoubleFunction<SkillPreContext> distanceProvider) {
            this.distanceProvider = Objects.requireNonNull(distanceProvider);
            return this;
        }

        public Builder setDistance(double distance) {
            return setDistanceProvider(context -> distance);
        }

        public BiPredicate<SkillPreContext, Entity> getPredicate() {
            return predicate;
        }

        public Builder setPredicate(BiPredicate<SkillPreContext, Entity> predicate) {
            this.predicate = Objects.requireNonNull(predicate);
            return this;
        }

        public float[] getMargins() {
            return margins;
        }

        public Builder setMargins(float... margins) {
            this.margins = Objects.requireNonNull(margins);
            return this;
        }

        public EntityRaycastStateFactory build() {
            return new EntityRaycastStateFactory(
                    distanceProvider,
                    predicate,
                    margins
            );
        }
    }
}
