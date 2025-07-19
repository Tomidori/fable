package org.tomidori.fable.skill.state.target;

import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.tomidori.fable.skill.SkillPreContext;
import org.tomidori.fable.skill.SkillResponse;
import org.tomidori.fable.skill.state.SkillStateCreationResult;
import org.tomidori.fable.skill.state.SkillStateFactory;

import java.util.Objects;
import java.util.function.ToDoubleFunction;

public final class BlockRaycastStateFactory implements SkillStateFactory<BlockHitResult> {
    private final ToDoubleFunction<SkillPreContext> distanceProvider;
    private final RaycastContext.ShapeType shapeType;
    private final RaycastContext.FluidHandling fluidHandling;
    private final boolean failOnMiss;

    private BlockRaycastStateFactory(
            ToDoubleFunction<SkillPreContext> distanceProvider,
            RaycastContext.ShapeType shapeType,
            RaycastContext.FluidHandling fluidHandling,
            boolean failOnMiss
    ) {
        this.distanceProvider = Objects.requireNonNull(distanceProvider);
        this.shapeType = Objects.requireNonNull(shapeType);
        this.fluidHandling = Objects.requireNonNull(fluidHandling);
        this.failOnMiss = failOnMiss;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SkillStateCreationResult<BlockHitResult> create(SkillPreContext context) {
        Objects.requireNonNull(context);

        BlockHitResult blockHitResult = getCollision(
                context.getSource(),
                shapeType,
                fluidHandling,
                distanceProvider.applyAsDouble(context)
        );

        return failOnMiss && blockHitResult.getType() == HitResult.Type.MISS ?
                SkillStateCreationResult.failure(SkillResponse.noTarget()) :
                SkillStateCreationResult.success(blockHitResult);
    }

    public static BlockHitResult getCollision(
            Entity entity,
            RaycastContext.ShapeType shapeType,
            RaycastContext.FluidHandling fluidHandling,
            double distance
    ) {
        Objects.requireNonNull(entity);
        Objects.requireNonNull(shapeType);
        Objects.requireNonNull(fluidHandling);

        Vec3d min = entity.getEyePos();
        Vec3d max = min.add(entity.getRotationVector().multiply(distance));

        return entity.getWorld().raycast(
                new RaycastContext(
                        min,
                        max,
                        shapeType,
                        fluidHandling,
                        entity
                )
        );
    }

    public static final class Builder {
        private ToDoubleFunction<SkillPreContext> distanceProvider = context -> context.getSource().getAttributeValue(EntityAttributes.BLOCK_INTERACTION_RANGE);
        private RaycastContext.ShapeType shapeType = RaycastContext.ShapeType.OUTLINE;
        private RaycastContext.FluidHandling fluidHandling = RaycastContext.FluidHandling.NONE;
        private boolean failOnMiss = true;

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

        public RaycastContext.ShapeType getShapeType() {
            return shapeType;
        }

        public Builder setShapeType(RaycastContext.ShapeType shapeType) {
            this.shapeType = Objects.requireNonNull(shapeType);
            return this;
        }

        public RaycastContext.FluidHandling getFluidHandling() {
            return fluidHandling;
        }

        public Builder setFluidHandling(RaycastContext.FluidHandling fluidHandling) {
            this.fluidHandling = Objects.requireNonNull(fluidHandling);
            return this;
        }

        public boolean isFailOnMiss() {
            return failOnMiss;
        }

        public Builder setFailOnMiss(boolean failOnMiss) {
            this.failOnMiss = failOnMiss;
            return this;
        }

        public BlockRaycastStateFactory build() {
            return new BlockRaycastStateFactory(
                    distanceProvider,
                    shapeType,
                    fluidHandling,
                    failOnMiss
            );
        }
    }
}
