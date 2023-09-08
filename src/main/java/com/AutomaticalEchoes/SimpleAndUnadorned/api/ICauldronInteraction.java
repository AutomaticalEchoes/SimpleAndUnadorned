package com.AutomaticalEchoes.SimpleAndUnadorned.api;

import com.AutomaticalEchoes.SimpleAndUnadorned.register.BlockRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.PotionRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;

public interface ICauldronInteraction extends CauldronInteraction {
    Map<Item, CauldronInteraction> ACIDITY = CauldronInteraction.newInteractionMap();
    Map<Item, CauldronInteraction> MUCUS = CauldronInteraction.newInteractionMap();
    Map<Item, CauldronInteraction> SUS_WATER = CauldronInteraction.newInteractionMap();
    CauldronInteraction FILL_ACIDITY = (p_175683_, p_175684_, p_175685_, p_175686_, p_175687_, p_175688_) -> CauldronInteraction.emptyBucket(p_175684_, p_175685_, p_175686_, p_175687_, p_175688_, BlockRegister.ACIDITY_CAULDRON_BLOCK.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY);
    CauldronInteraction FILL_MUCUS = (p_175683_, p_175684_, p_175685_, p_175686_, p_175687_, p_175688_) -> CauldronInteraction.emptyBucket(p_175684_, p_175685_, p_175686_, p_175687_, p_175688_, BlockRegister.MUCUS_CAULDRON_BLOCK.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY);
    CauldronInteraction FILL_SUS_WATER = (p_175683_, p_175684_, p_175685_, p_175686_, p_175687_, p_175688_) -> CauldronInteraction.emptyBucket(p_175684_, p_175685_, p_175686_, p_175687_, p_175688_, BlockRegister.SUS_WATER_CAULDRON_BLOCK.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY);
    static void Init(){
        ACIDITY.put(Items.BUCKET, (p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_) -> p_175697_.getValue(LayeredCauldronBlock.LEVEL) !=3? InteractionResult.PASS : CauldronInteraction.fillBucket(p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_, new ItemStack(ItemsRegister.ACIDITY_BUCKET.get()), (p_175651_) -> true, SoundEvents.BUCKET_FILL));
        MUCUS.put(Items.BUCKET, (p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_) -> p_175697_.getValue(LayeredCauldronBlock.LEVEL) !=3? InteractionResult.PASS : CauldronInteraction.fillBucket(p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_, new ItemStack(ItemsRegister.MUCUS_BUCKET.get()), (p_175651_) -> true, SoundEvents.BUCKET_FILL));
        SUS_WATER.put(Items.BUCKET, (p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_) -> p_175697_.getValue(LayeredCauldronBlock.LEVEL) !=3? InteractionResult.PASS : CauldronInteraction.fillBucket(p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_, new ItemStack(ItemsRegister.SUSPICIOUS_WATER_BUCKET.get()), (p_175651_) -> true, SoundEvents.BUCKET_FILL));

        ACIDITY.put(Items.GLASS_BOTTLE, (p_175718_, p_175719_, p_175720_, p_175721_, p_175722_, p_175723_) -> {
            if (!p_175719_.isClientSide) {
                Interaction(p_175718_, p_175719_, p_175720_, p_175721_, p_175722_, p_175723_,  p_175723_.getItem(),PotionRegister.ACIDITY.get());
            }
            return InteractionResult.sidedSuccess(p_175719_.isClientSide);
        });

        MUCUS.put(Items.GLASS_BOTTLE, (p_175718_, p_175719_, p_175720_, p_175721_, p_175722_, p_175723_) -> {
            if (!p_175719_.isClientSide) {
                Interaction(p_175718_, p_175719_, p_175720_, p_175721_, p_175722_, p_175723_,  p_175723_.getItem(),PotionRegister.MUCUS.get());
            }
            return InteractionResult.sidedSuccess(p_175719_.isClientSide);
        });

        SUS_WATER.put(Items.GLASS_BOTTLE, (p_175718_, p_175719_, p_175720_, p_175721_, p_175722_, p_175723_) -> {
            if (!p_175719_.isClientSide) {
                Interaction(p_175718_, p_175719_, p_175720_, p_175721_, p_175722_, p_175723_,  p_175723_.getItem(),PotionRegister.SUS_WATER.get());
            }
            return InteractionResult.sidedSuccess(p_175719_.isClientSide);
        });


        MUCUS.put(Items.POTION, (p_175718_, p_175719_, p_175720_, p_175721_, p_175722_, p_175723_) -> {
            if (!p_175719_.isClientSide && PotionUtils.getPotion(p_175723_) == Potions.WATER){
                Interaction(p_175718_, p_175719_, p_175720_, p_175721_, p_175722_, p_175723_,  p_175723_.getItem(),PotionRegister.SUS_WATER.get());
            }
            return InteractionResult.sidedSuccess(p_175719_.isClientSide);
        });

        WATER.put(Items.POTION, (p_175718_, p_175719_, p_175720_, p_175721_, p_175722_, p_175723_) -> {
            if (!p_175719_.isClientSide && PotionUtils.getPotion(p_175723_) == PotionRegister.MUCUS.get()){
                Interaction(p_175718_, p_175719_, p_175720_, p_175721_, p_175722_, p_175723_,  p_175723_.getItem(),PotionRegister.SUS_WATER.get());
            }
            return InteractionResult.sidedSuccess(p_175719_.isClientSide);
        });

        CauldronInteraction.addDefaultInteractions(MUCUS);
        CauldronInteraction.addDefaultInteractions(ACIDITY);
        CauldronInteraction.addDefaultInteractions(SUS_WATER);
        Expand(ACIDITY);
        Expand(MUCUS);
        Expand(SUS_WATER);
        Expand(WATER);
        Expand(EMPTY);
        Expand(LAVA);
        Expand(POWDER_SNOW);
    }

    static void Interaction(BlockState p_175718_, Level p_175719_, BlockPos p_175720_, Player p_175721_, Item item) {
        p_175721_.awardStat(Stats.USE_CAULDRON);
        p_175721_.awardStat(Stats.ITEM_USED.get(item));
        LayeredCauldronBlock.lowerFillLevel(p_175718_, p_175719_, p_175720_);
        p_175719_.playSound(null, p_175720_, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        p_175719_.gameEvent(null, GameEvent.FLUID_PICKUP, p_175720_);
    }

    static void Interaction(BlockState p_175718_, Level p_175719_, BlockPos p_175720_, Player p_175721_, InteractionHand p_175722_, ItemStack p_175723_, Item Item, Potion potion) {
        p_175721_.setItemInHand(p_175722_, ItemUtils.createFilledResult(p_175723_, p_175721_, PotionUtils.setPotion(new ItemStack(Items.POTION), potion )));
        Interaction(p_175718_, p_175719_, p_175720_, p_175721_, Item);
    }

    static void Expand(Map<Item, CauldronInteraction> map){
        map.put(ItemsRegister.ACIDITY_BUCKET.get(),FILL_ACIDITY);
        map.put(ItemsRegister.MUCUS_BUCKET.get(),FILL_MUCUS);
        map.put(ItemsRegister.SUSPICIOUS_WATER_BUCKET.get(),FILL_SUS_WATER);

    }

}
