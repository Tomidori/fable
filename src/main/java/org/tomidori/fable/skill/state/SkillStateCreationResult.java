package org.tomidori.fable.skill.state;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillResponse;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface SkillStateCreationResult<S> {
    static <S> Success<S> success(S state) {
        return new Success<>(state);
    }

    static <S> Failure<S> failure(SkillResponse.Failure reason) {
        return new Failure<>(reason);
    }

    @ApiStatus.NonExtendable
    default <V> SkillStateCreationResult<V> map(
            Function<? super S, ? extends V> mapper
    ) {
        Objects.requireNonNull(mapper);
        return flatMap(state ->
                success(
                        mapper.apply(state)
                )
        );
    }

    @ApiStatus.NonExtendable
    default <V> SkillStateCreationResult<V> flatMap(
            Function<? super S, ? extends SkillStateCreationResult<V>> mapper
    ) {
        Objects.requireNonNull(mapper);
        switch (this) {
            case Success<S> success -> {
                return mapper.apply(success.getState());
            }
            case Failure<S> failure -> {
                return failure(failure.getReason());
            }
        }
    }

    @ApiStatus.NonExtendable
    default SkillStateCreationResult<S> peek(
            Consumer<? super S> action
    ) {
        Objects.requireNonNull(action);
        if (this instanceof SkillStateCreationResult.Success<S> success) {
            action.accept(success.getState());
        }

        return this;
    }

    final class Success<S> implements SkillStateCreationResult<S> {
        private final S state;

        private Success(S state) {
            this.state = Objects.requireNonNull(state);
        }

        public S getState() {
            return state;
        }
    }

    final class Failure<S> implements SkillStateCreationResult<S> {
        private final SkillResponse.Failure reason;

        private Failure(SkillResponse.Failure reason) {
            this.reason = Objects.requireNonNull(reason);
        }

        public SkillResponse.Failure getReason() {
            return reason;
        }
    }
}
