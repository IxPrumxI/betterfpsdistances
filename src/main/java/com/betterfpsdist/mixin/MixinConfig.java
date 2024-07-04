package com.betterfpsdist.mixin;

import com.sun.management.HotSpotDiagnosticMXBean;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Set;

public class MixinConfig implements IMixinConfigPlugin
{
    @Override
    public void onLoad(final String mixinPackage)
    {
        try
        {
            HotSpotDiagnosticMXBean bean = ManagementFactory.newPlatformMXBeanProxy(
              ManagementFactory.getPlatformMBeanServer(),
              "com.sun.management:type=HotSpotDiagnostic",
              HotSpotDiagnosticMXBean.class);

            bean.setVMOption("HeapDumpOnOutOfMemoryError", "true");
            bean.setVMOption("HeapDumpPath", FMLPaths.GAMEDIR.get().toString());
        }
        catch (Exception e)
        {
        }
    }

    @Override
    public String getRefMapperConfig()
    {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(final String targetClassName, final String mixinClassName)
    {
        if (mixinClassName.equals("com.betterfpsdist.mixin.LevelRendererMixin")
        || mixinClassName.contains("VideoSettingsScreenMixin"))
        {
            return FMLLoader.getLoadingModList().getModFileById("magnesium") == null &&
                     FMLLoader.getLoadingModList().getModFileById("rubidium") == null &&
                     FMLLoader.getLoadingModList().getModFileById("embeddium") == null &&
                     FMLLoader.getLoadingModList().getModFileById("sodium") == null;
        }

        return FMLLoader.getLoadingModList().getModFileById("magnesium") != null ||
                 FMLLoader.getLoadingModList().getModFileById("rubidium") != null ||
                 FMLLoader.getLoadingModList().getModFileById("embeddium") != null ||
                 FMLLoader.getLoadingModList().getModFileById("sodium") != null;
    }

    @Override
    public void acceptTargets(final Set<String> myTargets, final Set<String> otherTargets)
    {

    }

    @Override
    public List<String> getMixins()
    {
        return null;
    }

    @Override
    public void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo)
    {

    }

    @Override
    public void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo)
    {

    }
}
