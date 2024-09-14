package com.betterfpsdist.compat;

import com.betterfpsdist.BetterfpsdistMod;
import com.betterfpsdist.event.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.api.OptionPageConstructionEvent;
import org.embeddedt.embeddium.api.options.control.ControlValueFormatter;
import org.embeddedt.embeddium.api.options.control.SliderControl;
import org.embeddedt.embeddium.api.options.storage.MinecraftOptionsStorage;
import org.embeddedt.embeddium.api.options.structure.*;
import org.embeddedt.embeddium.api.render.chunk.RenderSectionDistanceFilter;
import org.embeddedt.embeddium.api.render.chunk.RenderSectionDistanceFilterEvent;

public class EmbeddiumEventHandler
{
    public static void on(OptionPageConstructionEvent event)
    {
        if (event.getId() == StandardOptions.Pages.GENERAL)
        {
            event.addGroup(OptionGroup.createBuilder().setId(StandardOptions.Group.RENDERING).add(
                (OptionImpl.createBuilder(Integer.class, MinecraftOptionsStorage.INSTANCE)
                   .setName(Component.translatable("options.verticalstretch"))
                   .setTooltip(Component.literal("Reduces the distance at which chunks beneath/above are shown"))
                   .setImpact(OptionImpact.VARIES)
                   .setControl(option -> {
                       option.setValue((int) (BetterfpsdistMod.config.getCommonConfig().verticalScaling * 100));
                       return new SliderControl(option, 50, 500, 1, ControlValueFormatter.percentage());
                   })
                   .setId(ResourceLocation.fromNamespaceAndPath("betterfpsdist", "videosetting"))
                   .setBinding(
                     (options, value) -> {
                         BetterfpsdistMod.config.getCommonConfig().verticalScaling = value / 100d;
                         BetterfpsdistMod.config.save();
                     },
                     options -> (int) (BetterfpsdistMod.config.getCommonConfig().verticalScaling * 100)
                   )
                   .setFlags(new OptionFlag[] {OptionFlag.REQUIRES_RENDERER_RELOAD})
                   .build())
              ).add(
                (OptionImpl.createBuilder(Integer.class, MinecraftOptionsStorage.INSTANCE)
                   .setName(Component.translatable("options.horizontalstretch"))
                   .setTooltip(Component.literal("Reduces the distance at which chunks left/right are shown"))
                   .setControl(option -> {
                       option.setValue((int) ((BetterfpsdistMod.config.getCommonConfig().horizontalScaling - 1) * 100));
                       return new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage());
                   })
                   .setId(ResourceLocation.fromNamespaceAndPath("betterfpsdist", "videosetting"))
                   .setBinding(
                     (options, value) -> {
                         BetterfpsdistMod.config.getCommonConfig().horizontalScaling = 1 + value / 100d;
                         BetterfpsdistMod.config.save();
                     },
                     options -> (int) ((BetterfpsdistMod.config.getCommonConfig().horizontalScaling - 1) * 100)
                   )
                   .setImpact(OptionImpact.VARIES)
                   .setFlags(new OptionFlag[] {OptionFlag.REQUIRES_RENDERER_RELOAD})
                   .build())
              )
                             .build());
        }
    }

    public static void distanceFilterEvent(RenderSectionDistanceFilterEvent event)
    {
        event.setFilter(new RenderSectionDistanceFilter()
        {
            @Override
            public boolean isWithinDistance(final float dx, final float dy, final float dz, final float maxDist)
            {
                var player = Minecraft.getInstance().player;
                return ClientEventHandler.adjustedDistance((int) (dx + player.getX()),
                  (int) (dy + player.getY()),
                  (int) (dz + player.getZ()),
                  player.getX(),
                  player.getY(),
                  player.getZ()) < maxDist * maxDist;
            }
        });
    }
}
