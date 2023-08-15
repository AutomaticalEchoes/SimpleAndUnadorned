package com.AutomaticalEchoes.SimpleAndUnadorned.common.fluid.FluidType;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BaseFluidType extends FluidType {
    public static final ResourceLocation UNDERWATER_LOCATION = new ResourceLocation("textures/misc/underwater.png"),
            WATER_STILL = new ResourceLocation("block/water_still"),
            WATER_FLOW = new ResourceLocation("block/water_flow"),
            WATER_OVERLAY = new ResourceLocation("block/water_overlay");

    private ResourceLocation StillTexture = WATER_STILL;
    private ResourceLocation FlowingTexture = WATER_FLOW;
    private ResourceLocation OverlayTexture = WATER_OVERLAY;
    private ResourceLocation UnderTexture = UNDERWATER_LOCATION;
    private int TintColor =-12618012;
    private @Nullable Vector3f FogColor = null;

    /**
     * Default constructor.
     *
     * @param properties the general properties of the fluid type
     */
    public BaseFluidType(Properties properties) {
        super(properties);
    }

    public BaseFluidType FlowingTexture(ResourceLocation flowingTexture){
        this.FlowingTexture = flowingTexture;
        return this;
    }
    public BaseFluidType StillTexture(ResourceLocation stillTexture){
        this.StillTexture = stillTexture;
        return this;
    }
    public BaseFluidType OverlayTexture(ResourceLocation overlayTexture){
        this.OverlayTexture = overlayTexture;
        return this;
    }
    public BaseFluidType UnderTexture(ResourceLocation underTexture){
        this.UnderTexture = underTexture;
        return this;
    }
    public BaseFluidType Texture(ResourceLocation texture){
        this.StillTexture = this.FlowingTexture = this.OverlayTexture = this.UnderTexture = texture;
        return this;
    }
    public BaseFluidType TintColor(int tintColor){
        this.TintColor = tintColor;
        return this;
    }
    public BaseFluidType FogColor(Vector3f fogColor){
        this.FogColor = fogColor;
        return this;
    }


    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
    {
        consumer.accept(new IClientFluidTypeExtensions()
        {
            @Override
            public ResourceLocation getStillTexture()
            {
                return StillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture()
            {
                return FlowingTexture;
            }

            @Override
            public ResourceLocation getOverlayTexture()
            {
                return OverlayTexture;
            }

            @Override
            public @Nullable ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UnderTexture;
            }

            public int getTintColor() {
                return TintColor;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return FogColor != null? FogColor : fluidFogColor;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogStart(6f);
            }
        });
    }
}
