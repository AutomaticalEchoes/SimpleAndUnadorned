package com.AutomaticalEchoes.SimpleAndUnadorned.common.item;

import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class FunctionWeapon extends Item {
    private TriFunction<ItemStack,LivingEntity,LivingEntity,Boolean> HurtEnemyFunction = (p_43278_, p_43279_, p_43280_) -> {
        p_43278_.hurtAndBreak(1, p_43280_, (p_43296_) -> {
            p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    };

    public FunctionWeapon(Properties p_41383_) {
        super(p_41383_);
    }

    public FunctionWeapon Function(TriFunction<ItemStack,LivingEntity,LivingEntity,Boolean> hurtEnemyFunction){
        this.HurtEnemyFunction = hurtEnemyFunction;
        return this;
    }

    @Override
    public boolean hurtEnemy(ItemStack p_43278_, LivingEntity p_43279_, LivingEntity p_43280_) {
        return HurtEnemyFunction.apply(p_43278_, p_43279_, p_43280_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        MutableComponent mutableComponent = Component.translatable("description." + p_41421_.getDescriptionId()).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC);
        p_41423_.add(mutableComponent);
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack p_41403_) {
        return p_41403_.is(ItemsRegister.TRANSPARENT_CRYSTAL_BLOCK_ITEM.get());
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if(stack.getDamageValue() == stack.getMaxDamage() -2) onBroken.accept(entity);
        return stack.getDamageValue() < stack.getMaxDamage() -1 ? 1 : 0;
    }

    public static Boolean HalfLive(ItemStack p_43278_, LivingEntity p_43279_, LivingEntity p_43280_){
        if(p_43278_.getDamageValue() == p_43278_.getMaxDamage() -1) return false;
        float dam = p_43279_.getMaxHealth() - p_43279_.getHealth();
        p_43279_.hurt(DamageSource.indirectMagic(p_43279_,p_43280_),dam * p_43278_.getDamageValue() / (p_43278_.getMaxDamage() - 1));
        p_43278_.hurtAndBreak(1, p_43280_, (p_43296_) -> {
            p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public static Boolean SoulCut(ItemStack p_43278_, LivingEntity p_43279_, LivingEntity p_43280_){
        if(!(p_43279_ instanceof Player) && p_43279_.level instanceof ServerLevel serverLevel){
            EntityType<?> type = p_43279_.getType();
            CompoundTag compoundTag = p_43279_.saveWithoutId(new CompoundTag());
//            compoundTag.remove("Items");
            Entity spawn = type.spawn(serverLevel, compoundTag, null, null, p_43279_.blockPosition(), MobSpawnType.MOB_SUMMONED, false, false);
            p_43278_.hurtAndBreak( p_43278_.getMaxDamage(), p_43280_, (p_43296_) -> {
                p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }
}
