package com.betterfpsdist.mixin;

import com.betterfpsdist.BetterfpsdistMod;
import com.betterfpsdist.event.ClientEventHandler;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.occlusion.OcclusionCuller;
import me.jellysquid.mods.sodium.client.render.viewport.CameraTransform;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OcclusionCuller.class)
public class Sodiummixin
{
    @Inject(method = "isWithinRenderDistance", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void betterfps$renderdistance(
      final CameraTransform camera,
      final RenderSection section,
      final float maxDistance, final CallbackInfoReturnable<Boolean> cir)
    {
        if (Minecraft.getInstance().player != null)
        {
            if (ClientEventHandler.adjustedDistance(section.getOriginX(),
              section.getOriginY(),
              section.getOriginZ(),
              Minecraft.getInstance().player.getX(),
              Minecraft.getInstance().player.getY(),
              Minecraft.getInstance().player.getZ())
                  > ClientEventHandler.maxSqDist)
            {
                if (BetterfpsdistMod.config.getCommonConfig().debugMode)
                {
                    ClientEventHandler.hiddenSections.add(new BlockPos(section.getOriginX(), section.getOriginY(), section.getOriginZ()));
                }
                cir.setReturnValue(false);
            }
        }
    }
}
