package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.Goal.joke;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.joke.Joke;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class Angry extends Joke<Player> {
    private @Nullable BlockPos.MutableBlockPos angryJokePos = null;
    private @Nullable Integer SelectedStructuresID = null;
    public Angry(SuspiciousEnderman suspiciousEnderman) {
        super(suspiciousEnderman);
    }

    @Override
    public Case Case() {
        return Case.EMPTY;
    }

    @Override
    public boolean canJoke() {
        return suspiciousEnderman.isAngry() && ModCommonConfig.ENABLE_JOKE_ANGRY.get();
    }

    public Angry WithStructure(@Nullable Integer selectedStructuresID){
        this.SelectedStructuresID = selectedStructuresID;
        return this;
    }


    @Override
    public void doJoke() {
        if(suspiciousEnderman.getJokingTarget() instanceof Player target){
            if(this.suspiciousEnderman.isAngry() && this.suspiciousEnderman.level instanceof ServerLevel serverLevel){
                int i = SelectedStructuresID != null ? SelectedStructuresID : suspiciousEnderman.getRandom().nextInt(SuspiciousEnderman.STRUCTURES.length + 1);
                angryJokePos = i !=0? suspiciousEnderman.PreparePos(serverLevel,SelectedStructuresID) : null;
            }
            angryJoke(target,angryJokePos);
        }
    }



    @Override
    public @Nullable Predicate<LivingEntity> TargetSelector() {
        return PLAYER;
    }

    public void angryJoke(Player player, @Nullable BlockPos.MutableBlockPos blockpos$mutableblockpos){
        if(!suspiciousEnderman.isAngry()) return;
        if (blockpos$mutableblockpos != null) {
            angryJokeStructures(player, blockpos$mutableblockpos);
        } else {
            angryJokeDimension(player);
        }
    }

    public void angryJokeDimension(Player player){
        if(suspiciousEnderman.level instanceof ServerLevel serverLevel){
            ServerLevel level = serverLevel.getServer().getLevel(Level.END);
            if(level !=null){
                suspiciousEnderman.doHurtTarget(player);
                player.changeDimension(level);
            }
        }
    }

    public void angryJokeStructures(Player player, BlockPos.MutableBlockPos blockpos$mutableblockpos){
        if(suspiciousEnderman.level instanceof ServerLevel serverLevel ){
            suspiciousEnderman.doHurtTarget(player);
            player.teleportTo(blockpos$mutableblockpos.getX(),blockpos$mutableblockpos.getY(),blockpos$mutableblockpos.getZ());
        }
    }
}
