package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.Goal.joke;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.joke.Joke;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.joke.JokeCase;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class Steal extends Joke<Player> {
    public Steal(SuspiciousEnderman suspiciousEnderman) {
        super(suspiciousEnderman);
    }
    @Override
    public JokeCase.Case Case() {
        return Case.EMPTY;
    }

    @Override
    public boolean canJoke() {
        return super.canJoke() && ModCommonConfig.ENABLE_JOKE_STEAL.get();
    }

    @Override
    public void doJoke() {
        if(suspiciousEnderman.getJokingTarget() instanceof  Player target){
            Inventory playerInventory = target.getInventory();
            ItemStack selected = playerInventory.getSelected();
            if(selected != ItemStack.EMPTY){
                playerInventory.setItem(playerInventory.selected,ItemStack.EMPTY);
            }else {
                target.addEffect(new MobEffectInstance(MobEffects.DARKNESS,200));
            }
            suspiciousEnderman.setCarriedItem(selected);
        }
    }

    @Override
    public @Nullable Predicate<LivingEntity> TargetSelector() {
        return PLAYER;
    }
}
