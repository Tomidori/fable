package org.tomidori.fable.skill.state;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillPreContext;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@FunctionalInterface
public interface SkillStateFactory<S> {
    static <S> SkillStateFactory<S> constant(S state) {
        return context -> SkillStateCreationResult.success(state);
    }

    SkillStateCreationResult<S> create(SkillPreContext context);

    @ApiStatus.NonExtendable
    default <V> SkillStateFactory<V> map(
            BiFunction<? super SkillPreContext, ? super S, ? extends V> mapper
    ) {
        Objects.requireNonNull(mapper);
        return context -> create(context)
                .map(state ->
                        mapper.apply(context, state)
                );
    }

    @ApiStatus.NonExtendable
    default <V> SkillStateFactory<V> flatMap(
            BiFunction<? super SkillPreContext, ? super S, ? extends SkillStateCreationResult<V>> mapper
    ) {
        Objects.requireNonNull(mapper);
        return context -> create(context)
                .flatMap(state ->
                        mapper.apply(context, state)
                );
    }

    @ApiStatus.NonExtendable
    default SkillStateFactory<S> peek(
            BiConsumer<? super SkillPreContext, ? super S> action
    ) {
        Objects.requireNonNull(action);
        return context -> create(context)
                .peek(state ->
                        action.accept(context, state)
                );
    }
}
