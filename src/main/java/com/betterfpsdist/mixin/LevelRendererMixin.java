package com.betterfpsdist.mixin;

import com.betterfpsdist.BetterfpsdistMod;
import com.betterfpsdist.event.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin
{
    @Shadow
    @Final
    private Minecraft                             minecraft;
    private SectionRenderDispatcher.RenderSection current = null;

    @Redirect(method = "renderSectionLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSection;getCompiled()Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$CompiledSection;"))
    public SectionRenderDispatcher.CompiledSection on(final SectionRenderDispatcher.RenderSection instance)
    {
        current = instance;
        return instance.getCompiled();
    }

    @Redirect(method = "renderSectionLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$CompiledSection;isEmpty(Lnet/minecraft/client/renderer/RenderType;)Z"))
    public boolean on(final SectionRenderDispatcher.CompiledSection instance, final RenderType type)
    {
        if (instance.isEmpty(type))
        {
            return true;
        }

        if (minecraft.cameraEntity != null && ClientEventHandler.adjustedDistance(minecraft.cameraEntity.blockPosition(), current.getOrigin()) > ClientEventHandler.maxSqDist)
        {
            if (BetterfpsdistMod.config.getCommonConfig().debugMode)
            {
                ClientEventHandler.hiddenSections.add(current.getOrigin());
            }
            return true;
        }

        return false;
    }
}
