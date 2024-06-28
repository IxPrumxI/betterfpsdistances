package com.betterfpsdist.mixin;

import com.betterfpsdist.event.ClientEventHandler;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoSettingsScreen.class)
public abstract class VideoSettingsScreenMixin extends OptionsSubScreen
{

    public VideoSettingsScreenMixin(final Screen screen, final Options options, final Component component)
    {
        super(screen, options, component);
    }

    @Inject(method = "addOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/OptionsList;addSmall([Lnet/minecraft/client/OptionInstance;)V"))
    public void on(final CallbackInfo ci)
    {
        list.addBig(ClientEventHandler.chunkrenderdist);
    }
}