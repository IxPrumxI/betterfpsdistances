package com.betterfpsdist.mixin;

import com.betterfpsdist.BetterfpsdistMod;
import com.betterfpsdist.event.ClientEventHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin
{
    @Shadow
    @Final
    private Minecraft minecraft;

    @ModifyVariable(
            method = "compileSections",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            ordinal = 0,
            index = 8
    )
    private boolean modifyShouldCompileSection(boolean original, @Local SectionRenderDispatcher.RenderSection renderSection) {
        if (minecraft.cameraEntity == null) return false;

        BlockPos cameraPos = minecraft.cameraEntity.blockPosition();
        BlockPos sectionCenter = SectionPos.of(renderSection.getSectionNode()).center();

        double distanceSq = sectionCenter.distSqr(cameraPos);
        if (distanceSq > ClientEventHandler.maxSqDist) {
            if (BetterfpsdistMod.config.getCommonConfig().debugMode) {
                ClientEventHandler.hiddenSections.add(sectionCenter);
            }
            return false;
        }

        return original;
    }

}
