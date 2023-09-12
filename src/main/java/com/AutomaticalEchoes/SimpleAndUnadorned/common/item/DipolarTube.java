package com.AutomaticalEchoes.SimpleAndUnadorned.common.item;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils.DipolarUtils;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile.DipolarTubeProjectile;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class DipolarTube extends ArrowItem implements Vanishable {
    public static final HashMap<Potion,ItemStack> ALL_POTION_TUBES = new HashMap<>();
    public DipolarTube(Properties p_41383_) {
        super(p_41383_);
    }

    public InteractionResultHolder<ItemStack> use(Level p_43303_, Player p_43304_, InteractionHand p_43305_) {
        ItemStack itemstack = p_43304_.getItemInHand(p_43305_);
        p_43304_.startUsingItem(p_43305_);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack p_41412_, Level p_41413_, LivingEntity p_41414_, int p_41415_) {
        int i = Math.max( (getUseDuration(p_41412_) - p_41415_) / 10, 1);
        int f = Math.min(6,i);
        if(p_41413_ instanceof ServerLevel serverLevel){
            DipolarTubeProjectile dipolarTubeProjectile = DipolarUtils.makeProjectile(serverLevel,p_41412_,p_41414_);
            if(i >= 2){
                dipolarTubeProjectile.setTurn(true);
            }else {
                dipolarTubeProjectile.setVertical(false);
            }
            dipolarTubeProjectile.shootFromRotation(p_41414_, p_41414_.getXRot(), p_41414_.getYRot(), -4.0F * (6 - f) - 2.0F, 0.5F * f, 1.0F);
            serverLevel.addFreshEntity(dipolarTubeProjectile);
        }
        if (p_41414_ instanceof Player player && player.getAbilities().instabuild) return;

        p_41412_.shrink(1);
    }

    public String getDescriptionId(ItemStack p_43003_) {
        return PotionUtils.getPotion(p_43003_).getName(this.getDescriptionId() + ".effect.");
    }

    public void appendHoverText(ItemStack p_42988_, @Nullable Level p_42989_, List<Component> p_42990_, TooltipFlag p_42991_) {
        PotionUtils.addPotionTooltip(p_42988_, p_42990_, 0.25F);
        DipolarUtils.addPolarityTooltip(p_42988_,p_42990_);
    }

    public int getUseDuration(ItemStack p_43001_) {
        return 72000;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        ALL_POTION_TUBES.forEach((potion, itemStack) -> {
            if (potion.allowedInCreativeTab(this, p_41391_, this.allowedIn(p_41391_)))
                p_41392_.add(itemStack);
        });
    }

    @Override
    public AbstractArrow createArrow(Level p_40513_, ItemStack p_40514_, LivingEntity p_40515_) {
        return DipolarUtils.makeProjectile(p_40513_,p_40514_,p_40515_);
    }

    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.POISON);
    }


}
