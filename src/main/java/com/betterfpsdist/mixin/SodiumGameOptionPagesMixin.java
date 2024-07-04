package com.betterfpsdist.mixin;

import com.betterfpsdist.BetterfpsdistMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.api.options.control.ControlValueFormatter;
import org.embeddedt.embeddium.api.options.control.SliderControl;
import org.embeddedt.embeddium.api.options.storage.MinecraftOptionsStorage;
import org.embeddedt.embeddium.api.options.structure.*;
import org.embeddedt.embeddium.impl.gui.EmbeddiumGameOptionPages;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Disabled, not needed for embeddium may be needed for sodium
@Mixin(EmbeddiumGameOptionPages.class)
public class SodiumGameOptionPagesMixin
{
    @Shadow(remap = false)
    @Final
    private static MinecraftOptionsStorage vanillaOpts;

    @Redirect(method = "general", at = @At(value = "INVOKE", target = "Lorg/embeddedt/embeddium/api/options/structure/OptionGroup$Builder;add(Lorg/embeddedt/embeddium/api/options/structure/Option;)Lorg/embeddedt/embeddium/api/options/structure/OptionGroup$Builder;", ordinal = 2), remap = false, require = 0)
    private static OptionGroup.Builder initCompat(final OptionGroup.Builder instance, final Option<?> optionparam)
    {
        instance.add(optionparam);

        instance.add(OptionImpl.createBuilder(Integer.TYPE, vanillaOpts)
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
                       .build());
        return instance;
    }
}
