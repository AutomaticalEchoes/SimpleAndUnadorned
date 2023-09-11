package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.Goal.joke;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.joke.Joke;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class Catch extends Joke<LivingEntity> {
    public Catch(SuspiciousEnderman suspiciousEnderman) {
        super(suspiciousEnderman);
    }

    @Override
    public Case Case() {
        return Case.EMPTY;
    }

    @Override
    public boolean canJoke() {
        return super.canJoke() && ModCommonConfig.ENABLE_JOKE_CATCH.get();
    }

    @Override
    public void doJoke() {
        suspiciousEnderman.getJokingTarget().startRiding(suspiciousEnderman);
    }

    @Override
    public @Nullable Predicate<LivingEntity> TargetSelector() {
        return EXCEPT_PLAYER;
    }

}
