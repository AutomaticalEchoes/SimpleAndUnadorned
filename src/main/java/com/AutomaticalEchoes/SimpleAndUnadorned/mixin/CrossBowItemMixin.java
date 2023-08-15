package com.AutomaticalEchoes.SimpleAndUnadorned.mixin;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.Function.IPredicate;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(CrossbowItem.class)
public class CrossBowItemMixin {
    @Inject(method = "getAllSupportedProjectiles",at ={@At("RETURN")},cancellable = true)
    public void getAllSupportedProjectiles(CallbackInfoReturnable<Predicate<ItemStack>> callbackInfoReturnable){
        callbackInfoReturnable.setReturnValue(IPredicate.ARROW_ITEM_LIKE);
    }

    @Inject(method = "getSupportedHeldProjectiles",at ={@At("RETURN")},cancellable = true)
    public void getSupportedHeldProjectiles(CallbackInfoReturnable<Predicate<ItemStack>> callbackInfoReturnable){
        callbackInfoReturnable.setReturnValue(IPredicate.ARROW_ITEM_LIKE.or(itemStack -> itemStack.is(Items.FIREWORK_ROCKET)));
    }
}
