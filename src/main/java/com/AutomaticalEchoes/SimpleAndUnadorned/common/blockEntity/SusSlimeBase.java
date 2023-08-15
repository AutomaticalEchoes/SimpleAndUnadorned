package com.AutomaticalEchoes.SimpleAndUnadorned.common.blockEntity;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.SusSlimeSummonEvent;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.BlockRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
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

public class SusSlimeBase extends BlockEntity {
    private final int size = 16;
    private final SimpleContainer simpleContainer = new SimpleContainer(16);
    private int power = 0 ;
    public static SusSlimeBase Create(BlockPos pos,BlockState state){
        return new SusSlimeBase(BlockRegister.BlockEntityRegister.SUS_SLIME_BASE.get(),pos,state);
    }

    public SusSlimeBase(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    public void load(CompoundTag p_155349_) {
        super.load(p_155349_);
        this.simpleContainer.fromTag(p_155349_.getList("container",10));
    }

    protected void saveAdditional(CompoundTag p_187489_) {
        super.saveAdditional(p_187489_);
        p_187489_.put("container", this.simpleContainer.createTag());
    }

    public void summonSusSlime(Level level){
        if(level.getServer().getTickCount() % ( 20 * ModCommonConfig.SUSPICIOUS_SLIME_BASE_CREATE_SLIME_TICK.get()) ==0 && getPower() > 16){
            MinecraftForge.EVENT_BUS.post(new SusSlimeSummonEvent(level,new Vec3(getBlockPos().getX() + 0.5, getBlockPos().getY() ,getBlockPos().getZ() + 0.5)));
        }
    }


    public void setPower(int power) {
        this.power = power;
    }

    public void powerGrow(Level level) {
        int i = 0;
        while (getContainer().getItem(i).isEmpty() && i < getContainer().getContainerSize()){
            i++;
        }
        if(i == getContainer().getContainerSize()) return;
        ItemStack itemStack = getContainer().removeItem(i, 1);
        Item item = itemStack.getItem();
        if(!(item instanceof ArmorItem || item instanceof TieredItem)){
            this.power++;
        }else {
            ItemEntity itemEntity = new ItemEntity(level, getBlockPos().getX() + 0.5, getBlockPos().getY(), getBlockPos().getZ() + 0.5, itemStack);
            itemEntity.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(itemEntity);
        }
    }

    public int getPower() {
        return power;
    }


    public SimpleContainer getContainer() {
        return simpleContainer;
    }
}
