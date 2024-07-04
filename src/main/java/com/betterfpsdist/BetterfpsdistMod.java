package com.betterfpsdist;

import com.betterfpsdist.compat.EmbeddiumCompat;
import com.betterfpsdist.config.CommonConfiguration;
import com.cupboard.config.CupboardConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BetterfpsdistMod.MODID)
public class BetterfpsdistMod
{
    public static final String                              MODID  = "betterfpsdist";
    public static final Logger                              LOGGER = LogManager.getLogger();
    public static       CupboardConfig<CommonConfiguration> config = new CupboardConfig<>(MODID, new CommonConfiguration());
    public static       Random                              rand   = new Random();

    public BetterfpsdistMod(IEventBus modEventBus, ModContainer modContainer)
    {
        if (FMLLoader.getLoadingModList().getModFileById("embeddium") != null && FMLLoader.getDist() == Dist.CLIENT)
        {
            EmbeddiumCompat.initCompat();
        }
    }
}
