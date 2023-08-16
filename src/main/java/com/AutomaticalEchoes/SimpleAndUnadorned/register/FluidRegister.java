package com.AutomaticalEchoes.SimpleAndUnadorned.register;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.Function.FluidFunction;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.fluid.FluidType.BaseFluidType;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.fluid.FluidType.FunctionFluidType;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.fluid.NonNewtonianFluid;
import com.mojang.math.Vector3f;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidRegister {
    public static final DeferredRegister<Fluid> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.FLUIDS, SimpleAndUnadorned.MODID);
    public static final RegistryObject<FlowingFluid> ACIDITY = DEFERRED_REGISTER.register("acidity", () -> new ForgeFlowingFluid.Source(FluidRegister.ACIDITY_PROPERTIES));
    public static final RegistryObject<FlowingFluid> ACIDITY_FLOW = DEFERRED_REGISTER.register("acidity_flow",() -> new ForgeFlowingFluid.Flowing(FluidRegister.ACIDITY_PROPERTIES));
    public static final RegistryObject<NonNewtonianFluid> MUCUS = DEFERRED_REGISTER.register("mucus", () -> new NonNewtonianFluid().LegacyBlock(BlockRegister.SUSPICIOUS_SLIME_BLOCK).FluidType(Type.MUCUS));
    public static final RegistryObject<NonNewtonianFluid> SUSPICIOUS_WATER = DEFERRED_REGISTER.register("suspicious_water",  () -> new NonNewtonianFluid().LegacyBlock(BlockRegister.SUSPICIOUS_WATER).BucketItem(ItemsRegister.SUSPICIOUS_WATER_BUCKET));

    public static final ForgeFlowingFluid.Properties ACIDITY_PROPERTIES = new ForgeFlowingFluid.Properties(Type.ACIDITY,ACIDITY,ACIDITY_FLOW)
            .bucket(ItemsRegister.ACIDITY_BUCKET)
            .block(BlockRegister.ACIDITY)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(2);
   public static class Type{
        public static final DeferredRegister<FluidType> TYPE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, SimpleAndUnadorned.MODID);
        public static final RegistryObject<FluidType> MUCUS = TYPE_DEFERRED_REGISTER.register("mucus", () ->
                new FunctionFluidType(FluidType.Properties.create()
                        .fallDistanceModifier(0F)
                        .canExtinguish(true)
                        .canSwim(true)
                        .canConvertToSource(false)
                        .supportsBoating(true)
                        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                        .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                        .canHydrate(true), FluidFunction::MucusMove,FluidFunction::MucusItem).TintColor(0xA1639C58).FogColor(new Vector3f(99/255f,156/255f,88/255f))
                        .Texture(BaseFluidType.WATER_OVERLAY).UnderTexture(BaseFluidType.UNDERWATER_LOCATION));

       public static final RegistryObject<FluidType> ACIDITY = TYPE_DEFERRED_REGISTER.register("acidity", () ->
               new FunctionFluidType(FluidType.Properties.create()
                       .fallDistanceModifier(0F)
                       .canExtinguish(true)
                       .canSwim(true)
                       .canConvertToSource(false)
                       .supportsBoating(true)
                       .lightLevel(15)
                       .viscosity(400)
                       .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                       .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                       .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                       .canHydrate(true), FluidFunction::HurtArmor, FluidFunction::Transform).TintColor(0xA1953472).FogColor(new Vector3f(149/255f,52/255f,114/255f)));
    }
}
