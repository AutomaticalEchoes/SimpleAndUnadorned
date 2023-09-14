package com.AutomaticalEchoes.SimpleAndUnadorned.common;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.ExpTransformerRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.PillagerMagazineRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.item.DipolarTube;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.SusSlimeSummonEvent;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.SusSlimeWantExpEvent;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.BlockRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.EntityRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.PotionRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public static void SummonSlime(SusSlimeSummonEvent event){
        SuspiciousSlime suspiciousSlime = EntityRegister.SUSPICIOUS_SLIME.get().create(event.getLevel());
        suspiciousSlime.setSize(1,true);
        suspiciousSlime.setPos(event.getVec3());
        event.getLevel().addFreshEntity(suspiciousSlime);
    }

    @SubscribeEvent
    public static void SpawnExpOrbWentWanted(SusSlimeWantExpEvent event){
        event.getSusSlimeBase().spawnOre();
    }

    @SubscribeEvent
    public static void RegisterExpTransformer(ExpTransformerRegister event){
        event.register(Items.DIAMOND,16);
    }

    @SubscribeEvent
    public static void RegisterPillagerMagazine(PillagerMagazineRegister.Harmful event){
        event.Register(DipolarTube.ALL_POTION_TUBES.get(Potions.LONG_POISON),6);
        event.Register(DipolarTube.ALL_POTION_TUBES.get(Potions.SLOWNESS),3);
        event.Register(DipolarTube.ALL_POTION_TUBES.get(PotionRegister.ACID_EROSION.get()),1);
    }

    @SubscribeEvent
    public static void onUpdateNeighbor(BlockEvent.NeighborNotifyEvent event){
        if(!(event.getLevel() instanceof ServerLevel level)) return;
        BlockState startState = event.getState();
        BlockPos startPos = event.getPos();
        if(!(startState.getBlock() instanceof LightningRodBlock && startState.getValue(BlockStateProperties.POWERED))) return;
        Direction opposite = startState.getValue(BlockStateProperties.FACING).getOpposite();
        BlockPos blockPos1 = startPos.relative(opposite);
        BlockPos blockPos2 = blockPos1.relative(opposite);
        if(level.getBlockState(blockPos1).is(BlockRegister.TRANSPARENT_CRYSTAL_BLOCK.get())
                && level.getBlockState(blockPos1).getBlock() == level.getBlockState(blockPos2).getBlock()){
            level.setBlock(startPos, Blocks.AIR.defaultBlockState(),3);
            level.setBlock(blockPos1,Blocks.AIR.defaultBlockState(),3);
            level.setBlock(blockPos2,Blocks.AIR.defaultBlockState(),3);

            Containers.dropItemStack(level, startPos.getX() + 0.5,startPos.getY() + 0.5,startPos.getZ() + 0.5,new ItemStack(ItemsRegister.SOUL_CUTER.get()));
        }
    }

    @SubscribeEvent
    public static void onLightningHit(EntityStruckByLightningEvent event){
        if(event.getEntity() instanceof ItemEntity itemEntity && itemEntity.getItem().is(ItemsRegister.SOUL_CUTER.get())) event.setCanceled(true);
    }

}
