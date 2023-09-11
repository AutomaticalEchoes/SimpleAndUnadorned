package com.AutomaticalEchoes.SimpleAndUnadorned.common.blockEntity.SusSlimeBase;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.IContainerHelper;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.IExperienceOrb;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.BlockRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.EntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;

public class SusSlimeBase extends RandomizableContainerBlockEntity {
    private final int size = 16;
    private NonNullList<ItemStack> itemStacks = NonNullList.withSize(16, ItemStack.EMPTY);
    private int power = 0 ;
    private int translateTIck = 0;
    public static SusSlimeBase Create(BlockPos pos,BlockState state){
        return new SusSlimeBase(BlockRegister.BlockEntityRegister.SUS_SLIME_BASE.get(),pos,state);
    }

    public SusSlimeBase(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return itemStacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> p_59625_) {
        this.itemStacks = p_59625_;
    }

    public void load(CompoundTag p_155349_) {
        super.load(p_155349_);
        if (!this.tryLoadLootTable(p_155349_)) {
            ContainerHelper.loadAllItems(p_155349_, this.itemStacks);
        }
    }

    protected void saveAdditional(CompoundTag p_187489_) {
        super.saveAdditional(p_187489_);
        if (!this.trySaveLootTable(p_187489_)) {
            ContainerHelper.saveAllItems(p_187489_,this.itemStacks);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("sau.sus_slime_base");
    }

    @Override
    protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) {
        return null;
    }

    public ItemStack addItemStack(ItemStack itemStack){
        return IContainerHelper.addItem(this,itemStack);
    }

    public void powerGrow(Level level) {
        if(this.isEmpty() || level.hasNeighborSignal(worldPosition)) return;
        if(translateTIck > 0){
            translateTIck-- ;
            return;
        }

        int i = 0;
        while ( i < getContainerSize() && getItem(i).isEmpty() ){
            i++;
        }
        if(i >= getContainerSize()) return;

        ItemStack itemStack = removeItem(i, 1);
        int growLevel;
        if(itemStack.getItem() instanceof ArmorItem || itemStack.getItem() instanceof TieredItem){
            growLevel = itemStack.getMaxDamage() - itemStack.getDamageValue();
        }else {
            growLevel = TransformMap.TRANSFORM_MAP.getOrDefault(itemStack.getItem(), 1);
        }
        this.power += growLevel;
        this.translateTIck = growLevel;
    }

    public void dropContainer(){
        if(level instanceof ServerLevel) {
            Containers.dropContents(level, this.getBlockPos(), this);
        }
    }

    public void spawnOre(){
        if(level instanceof ServerLevel serverLevel && power > 0){
            IExperienceOrb.Award(serverLevel, getBlockPos(), power, List.of(EntityRegister.SUSPICIOUS_SLIME.get()));
            power = 0 ;
        }
    }

    @Override
    public int getContainerSize() {
        return 16;
    }

    public boolean canAddItem(ItemStack p_19184_) {
        boolean flag = false;
        for(ItemStack itemstack : this.itemStacks) {
            if (itemstack.isEmpty() || ItemStack.isSameItemSameTags(itemstack, p_19184_) && itemstack.getCount() < itemstack.getMaxStackSize()) {
                flag = true;
                break;
            }
        }
        return flag;
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

    public static void neighborChanged(BlockState p_55666_, Level p_55667_, BlockPos p_55668_, Block p_55669_, BlockPos p_55670_, boolean p_55671_){

    }


}
