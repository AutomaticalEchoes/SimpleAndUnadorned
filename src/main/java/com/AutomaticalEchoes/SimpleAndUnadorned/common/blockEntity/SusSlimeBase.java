package com.AutomaticalEchoes.SimpleAndUnadorned.common.blockEntity;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.Function.FluidFunction;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.SusSlimeSummonEvent;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.BlockRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.core.jmx.Server;

public class SusSlimeBase extends BlockEntity {
    private final int size = 16;
    private final SimpleContainer ConsumeContainer = new SimpleContainer(16);
    private final SimpleContainer CollectContainer = new SimpleContainer(16);
    private int power = 0 ;
    public static SusSlimeBase Create(BlockPos pos,BlockState state){
        return new SusSlimeBase(BlockRegister.BlockEntityRegister.SUS_SLIME_BASE.get(),pos,state);
    }

    public SusSlimeBase(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    public void load(CompoundTag p_155349_) {
        super.load(p_155349_);
        this.ConsumeContainer.fromTag(p_155349_.getList("consume",10));
        this.CollectContainer.fromTag(p_155349_.getList("collect",10));
    }

    protected void saveAdditional(CompoundTag p_187489_) {
        super.saveAdditional(p_187489_);
        p_187489_.put("consume",ConsumeContainer.createTag());
        p_187489_.put("collect",CollectContainer.createTag());
    }

    public ItemStack addItemStack(ItemStack itemStack){
        boolean shouldCollect = itemStack.getItem() instanceof ArmorItem || itemStack.getItem() instanceof TieredItem;
        return shouldCollect ? CollectContainer.addItem(itemStack) : ConsumeContainer.addItem(itemStack);
    }

    public void powerGrow(Level level) {
        int i = 0;
        while (ConsumeContainer.getItem(i).isEmpty() && i < ConsumeContainer.getContainerSize() -1 ){
            i++;
        }
        if(!ConsumeContainer.removeItem(i,1).isEmpty()) ++power;
    }

    public void dropContainer(){
        if(level instanceof ServerLevel) {
            Containers.dropContents(level, this.getBlockPos(), ConsumeContainer);
            Containers.dropContents(level, this.getBlockPos(), CollectContainer);
        }
    }

    public void spawnOre(){
        if(level instanceof ServerLevel){
            FluidFunction.SpawnOrb(power,level,getBlockPos());
        }
    }

    public static void serverTick(Level p_155145_, BlockPos p_155146_, BlockState p_155147_, SusSlimeBase p_155148_) {
        p_155148_.powerGrow(p_155145_);
    }

    public static void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if (p_60515_.hasBlockEntity() && (!p_60515_.is(p_60518_.getBlock()) || !p_60518_.hasBlockEntity())) {
            if(p_60516_.getBlockEntity(p_60517_) instanceof SusSlimeBase susSlimeBase){
                susSlimeBase.dropContainer();
                susSlimeBase.spawnOre();
            }
            p_60516_.removeBlockEntity(p_60517_);
        }
    }

}
