package org.firstinspires.ftc.teamcode.state;

import androidx.annotation.NonNull;

import java.util.EnumMap;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class StateMachine<E extends Enum<E>> {
    private static class State<E extends Enum<E>> {
        Runnable opener;
        Consumer<StateMachine<E>.Ctx> update;
        Runnable closer;

        State(Runnable opener, Consumer<StateMachine<E>.Ctx> update, Runnable closer) {
            this.opener = opener;
            this.update = update;
            this.closer = closer;
        }
    }

    public static class Builder<E extends Enum<E>> {
        public class StateBuilder {
            private Runnable opener;
            private Consumer<StateMachine<E>.Ctx> update;
            private Runnable closer;

            private StateBuilder() {
                this.opener = null;
                this.update = null;
                this.closer = null;
            }

            public StateBuilder onStart(Runnable runnable) {
                if (this.opener == null) this.opener = runnable;
                else this.opener = () -> { this.opener.run(); opener.run(); };

                return this;
            }

            public StateBuilder onUpdate(Consumer<StateMachine<E>.Ctx> updater) {
                if (this.update == null) this.update = updater;
                else this.update = this.update.andThen(update);

                return this;
            }

            public StateBuilder onEnd(Runnable runnable) {
                if (this.closer == null) this.closer = runnable;
                else this.closer = () -> { this.closer.run(); closer.run(); };

                return this;
            }

            private State<E> finish() {
                if (this.opener == null) this.opener = () -> {};
                if (this.update == null) this.update = _ctx -> {};
                if (this.closer == null) this.closer = () -> {};

                return new State<>(this.opener, this.update, this.closer);
            }
        }

        private final Class<E> keyType;
        private final EnumMap<E, State<E>> map;

        private Builder(Class<E> keyType) {
            this.keyType = keyType;
            this.map = new EnumMap<>(keyType);
        }

        public Builder<E> addState(E label, UnaryOperator<StateBuilder> consumer) {
            map.put(
                    Objects.requireNonNull(label),
                    consumer.apply(new StateBuilder()).finish()
            );

            return this;
        }

        public StateMachine<E> finishAndBegin(E initialState) {
            return new StateMachine<>(keyType, map, initialState);
        }
    }

    public static <E extends Enum<E>> Builder<E> builder(Class<E> keyType) {
        return new Builder<>(keyType);
    }

    public class Ctx {
        public E netNewState;

        private Ctx() {
            this.netNewState = null;
        }

        public void changeState(E newState) {
            this.netNewState = Objects.requireNonNull(newState);
        }
    }

    private final EnumMap<E, State<E>> map;
    private E current;

    private StateMachine(Class<E> keyType, EnumMap<E, State<E>> map, E initial) {
        this.map = map;
        this.current = initial;

        @NonNull
        @SuppressWarnings(value = "DataFlowIssue")
        E[] constants = keyType.getEnumConstants();
        for (E k : constants) { // FUCK OFF (if i accidentally commit this oops)
            if (!map.containsKey(k)) throw new IllegalArgumentException(String.format(
                    "missing descriptor for state `%s`",
                    k.name()
            ));
        }

        this.getState(initial).opener.run();
    }

    @NonNull
    @SuppressWarnings(value = "DataFlowIssue")
    private State<E> getState(E key) {
        return this.map.get(key);
    }

    public void update() {
        State<E> currentState = this.getState(this.current);

        Ctx ctx = this.new Ctx();
        currentState.update.accept(ctx);

        if (ctx.netNewState != null) {
            currentState.closer.run();
            this.current = ctx.netNewState;
            this.getState(ctx.netNewState).opener.run();
        }
    }
}
