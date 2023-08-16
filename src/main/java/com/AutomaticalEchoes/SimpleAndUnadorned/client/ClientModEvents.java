package com.AutomaticalEchoes.SimpleAndUnadorned.client;


import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.Utils;
import com.AutomaticalEchoes.SimpleAndUnadorned.client.Renderer.DipolarTubeRender;
import com.AutomaticalEchoes.SimpleAndUnadorned.client.Renderer.SuspiciousSlimeAcidityRender;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.Nullable;


// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@Mod.EventBusSubscriber(modid = SimpleAndUnadorned.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public  class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        ItemBlockRenderTypes.setRenderLayer(FluidRegister.MUCUS.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FluidRegister.ACIDITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FluidRegister.ACIDITY_FLOW.get(), RenderType.translucent());
        event.enqueueWork(() -> {
            ItemProperties.register(ItemsRegister.DIPOLAR_TUBE_POTION_ITEM.get(), new ResourceLocation("power"), (ClampedItemPropertyFunction) (p_174564_, p_174565_, p_174566_, p_174567_) -> {
                if(p_174566_ != null && p_174566_.getUseItem() == p_174564_){
                    return (float)(p_174564_.getUseDuration() - p_174566_.getUseItemRemainingTicks()) / 60.0F;
                }
                return 0;
            });

            ItemProperties.register(ItemsRegister.DIPOLAR_TUBE_POTION_ITEM.get(), new ResourceLocation("handing"), new ClampedItemPropertyFunction() {
                @Override
                public float unclampedCall(ItemStack p_174564_, @Nullable ClientLevel p_174565_, @Nullable LivingEntity p_174566_, int p_174567_) {
                    return p_174566_ != null && p_174566_.isUsingItem() && p_174566_.getUseItem() == p_174564_ ? 1.0F : 0;
                }
            });
        });
    }

    @SubscribeEvent
    public static void RegisterRenders(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityRegister.ACIDITY.get(), SuspiciousSlimeAcidityRender::new);
        event.registerEntityRenderer(EntityRegister.SUSPICIOUS_THROWN_ENDERPEARL_PROJECTILE.get(), p_174010_ -> new ThrownItemRenderer<>(p_174010_, 1.0F, true));
        event.registerEntityRenderer(EntityRegister.DIPOLAR_TUBE_PROJECTILE.get(), p_174010_ -> new DipolarTubeRender(p_174010_, 1.0F, false) );
    }

    @SubscribeEvent
    public static void RegisterBlockColor(RegisterColorHandlersEvent.Block event){
        event.register((p_92621_, p_92622_, p_92623_, p_92624_) -> p_92622_ != null && p_92623_ != null ? 0xA1953472 : -1, BlockRegister.ACIDITY_CAULDRON_BLOCK.get());
        event.register((p_92621_, p_92622_, p_92623_, p_92624_) -> p_92622_ != null && p_92623_ != null ? 0xA1639C58 : -1, BlockRegister.MUCUS_CAULDRON_BLOCK.get());
        event.register((p_92621_, p_92622_, p_92623_, p_92624_) -> p_92622_ != null && p_92623_ != null ? BiomeColors.getAverageWaterColor(p_92622_, p_92623_) : -1, BlockRegister.SUS_WATER_CAULDRON_BLOCK.get());
    }

    @SubscribeEvent
    public static void RegisterItemColor(RegisterColorHandlersEvent.Item event){
        event.register((p_92699_, p_92700_) -> p_92700_ > 0 ? -1 : PotionUtils.getColor(p_92699_), ItemsRegister.DIPOLAR_TUBE_POTION_ITEM.get());
    }


}
