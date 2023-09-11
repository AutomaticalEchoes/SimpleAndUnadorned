package com.AutomaticalEchoes.SimpleAndUnadorned.api.joke;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface JokeCase<T extends LivingEntity> {
    SuspiciousEnderman owner();
    Case Case();
    boolean canJoke();
    void doJoke();
    void reset();
    @Nullable Predicate<LivingEntity> TargetSelector();

    enum Case{
        EMPTY(),
        CATCHING(),
        CARRIED();
    }

}
