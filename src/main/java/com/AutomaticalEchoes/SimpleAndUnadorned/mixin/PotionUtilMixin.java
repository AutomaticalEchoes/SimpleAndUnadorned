package com.AutomaticalEchoes.SimpleAndUnadorned.mixin;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.ColorPotion;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionUtils.class)
public class PotionUtilMixin {

    @Inject(method = "setPotion",at = {@At("RETURN")})
    private static void setPotion(ItemStack p_43550_, Potion p_43551_, CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
        if(p_43551_ instanceof ColorPotion colorPotion){
            p_43550_.getOrCreateTag().putInt("CustomPotionColor",colorPotion.getColor());
        }
    }
}
