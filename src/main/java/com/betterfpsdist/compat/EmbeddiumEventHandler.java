package com.betterfpsdist.compat;

import com.betterfpsdist.BetterfpsdistMod;
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
                 .setName(Component.literal("Render Distance y-stretch"))
                 .setTooltip(Component.literal("Reduces the distance at which chunks beneath/above are shown"))
                 .setControl(option -> new SliderControl(option, 50, 500, 1, ControlValueFormatter.percentage()))
                 .setId(ResourceLocation.fromNamespaceAndPath("betterfpsdist", "videosetting"))
                 .setBinding(
                   (options, value) -> {
                       BetterfpsdistMod.config.getCommonConfig().stretch = value / 100d;
                       BetterfpsdistMod.config.save();
                   },
                   options -> (int) (BetterfpsdistMod.config.getCommonConfig().stretch * 100)
                 )
                 .setImpact(OptionImpact.LOW)
                 .setFlags(new OptionFlag[] {OptionFlag.REQUIRES_RENDERER_RELOAD})
                 .build())
            ).build());
        }
    }

    public static void distanceFilterEvent(RenderSectionDistanceFilterEvent event)
    {
        event.setFilter(new RenderSectionDistanceFilter()
        {
            @Override
            public boolean isWithinDistance(final float dx, final float dy, final float dz, final float maxDist)
            {
                return (dx * dx + BetterfpsdistMod.config.getCommonConfig().stretch * (dy * dy) + dz * dz) < maxDist * maxDist;
            }
        });
    }
}
