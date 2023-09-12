package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.Goal.joke;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.joke.Joke;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class Ride extends Joke<LivingEntity> {
    public LivingEntity Rider;
    public LivingEntity Saddle;
    private final TriFunction<Ride,LivingEntity,LivingEntity,Boolean> func;

    public Ride(SuspiciousEnderman suspiciousEnderman , TriFunction<Ride,LivingEntity,LivingEntity,Boolean> func) {
        super(suspiciousEnderman);
        this.func = func;
    }


    @Override
    public Case Case() {
        return Case.CATCHING;
    }

    @Override
    public boolean canJoke() {
        if(suspiciousEnderman.getFirstPassenger() instanceof LivingEntity catching && catching.isAlive()){
            return super.canJoke() && func.apply(this,catching,suspiciousEnderman.getJokingTarget()) && Rider !=null && Saddle != null ;
        }
        return false;
    }

    @Override
    public void doJoke() {
        Rider.startRiding(Saddle);
    }

    @Override
    public @Nullable Predicate<LivingEntity> TargetSelector() {
        return LIVING_ENTITY;
    }

    public static Boolean CatWithCreeper(Ride rideJoke,LivingEntity catching,LivingEntity target){
        if(catching instanceof Cat){
            rideJoke.Rider = catching;
            rideJoke.Saddle = target;
            return ModCommonConfig.ENABLE_JOKE_CAT_RIDE_CREEPER.get();
        }
        return false;
    }
    public static Boolean WolfWithSkeleton(Ride rideJoke,LivingEntity catching,LivingEntity target){
        if(catching instanceof Wolf){
            rideJoke.Rider = catching;
            rideJoke.Saddle = target;
            return ModCommonConfig.ENABLE_JOKE_WOLF_RIDE_SKELETON.get();
        }
        return false;
    }
    public static Boolean LivingWithPlayer(Ride rideJoke,LivingEntity catching,LivingEntity target){
        if(!(catching instanceof Monster)){
            rideJoke.Rider = target;
            rideJoke.Saddle = catching;
            return ModCommonConfig.ENABLE_JOKE_PLAYER_RIDE_LIVING.get();
        }
        return false;
    }

}
