package com.betterfpsdist.mixin;

import com.betterfpsdist.event.ClientEventHandler;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(Entity.class)
public class EntityRenderDistMixin
{
    @Redirect(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;shouldRenderAtSqrDistance(D)Z"))
    private boolean adaptRenderDist(final Entity instance, double orgSqDist, double x, double y, double z)
    {
        return instance.shouldRenderAtSqrDistance(ClientEventHandler.adjustedDistance(instance.getBlockX(), instance.getBlockY(), instance.getBlockZ(),x,y,z));
    }
}
