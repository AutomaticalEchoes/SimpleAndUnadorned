package com.AutomaticalEchoes.SimpleAndUnadorned.mixin;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.Function.IPredicate;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(BowItem.class)
public class BowItemMixin {
    @Inject(method = "getAllSupportedProjectiles",at ={@At("RETURN")},cancellable = true)
    public void getAllSupportedProjectiles(CallbackInfoReturnable<Predicate<ItemStack>> callbackInfoReturnable){
        callbackInfoReturnable.setReturnValue(IPredicate.ARROW_ITEM_LIKE);
    }
}
