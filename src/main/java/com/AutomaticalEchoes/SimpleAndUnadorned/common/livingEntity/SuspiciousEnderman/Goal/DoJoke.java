package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.joke.JokeCase;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DoJoke extends Goal {
    private final SuspiciousEnderman suspiciousEnderman;
    private JokeCase<? extends LivingEntity> joke;
    private int tick = 0 ;

    public DoJoke(SuspiciousEnderman suspiciousEnderman) {
        this.suspiciousEnderman = suspiciousEnderman;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return suspiciousEnderman.isJoking() && !suspiciousEnderman.isScared();
    }

    @Override
    public void start() {
        this.joke = suspiciousEnderman.getJoke();
        this.tick = 0;
    }

    @Override
    public void tick() {
        if(tick == 0) {
            this.suspiciousEnderman.teleportAim(suspiciousEnderman.getJokingTarget().position(),2);
            this.tick ++;
        }else if(tick == 1){
            LivingEntity jokingTarget = suspiciousEnderman.getJokingTarget();
            if(suspiciousEnderman.distanceTo(jokingTarget) > 4) {
                tick=0;
                return;
            }
            suspiciousEnderman.moveTo(jokingTarget.position());
            joke.doJoke();
            this.tick++;
        }else if(tick > 2){
            this.suspiciousEnderman.jokeComp();
            this.stop();
        }
    }

    @Override
    public void stop() {
        joke.reset();
    }



}
