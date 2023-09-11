package com.AutomaticalEchoes.SimpleAndUnadorned.api.joke;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public abstract class Joke<T extends LivingEntity> implements JokeCase<T> {
    protected SuspiciousEnderman suspiciousEnderman;
    protected Case JokeCase;

    protected Predicate<LivingEntity> LIVING_ENTITY  = livingEntity -> {
        if (owner().getFirstPassenger() instanceof Cat) {
            return livingEntity instanceof Creeper;
        }
        if (owner().getFirstPassenger() instanceof Wolf) {
            return livingEntity instanceof Skeleton;
        }
        if (owner().getFirstPassenger() instanceof Monster) {
            return livingEntity instanceof Player player && player.isInvulnerable();
        }

        return !(livingEntity instanceof Player player) || !player.isInvulnerable();
    };

    protected Predicate<LivingEntity> EXCEPT_PLAYER  = livingEntity -> !(livingEntity instanceof Player);

    protected Predicate<LivingEntity> PLAYER  = livingEntity -> livingEntity instanceof Player player && !player.isInvulnerable();

    @Override
    public boolean canJoke() {
        return !suspiciousEnderman.isAngry();
    }

    @Override
    public SuspiciousEnderman owner() {
        return this.suspiciousEnderman;
    }

    @Override
    public void reset() {
        suspiciousEnderman.reset();
    }

    public Joke(SuspiciousEnderman suspiciousEnderman) {
        this.suspiciousEnderman = suspiciousEnderman;
    }
}
