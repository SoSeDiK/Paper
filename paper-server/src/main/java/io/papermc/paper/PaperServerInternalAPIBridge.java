package io.papermc.paper;

import io.papermc.paper.world.damagesource.CombatEntry;
import io.papermc.paper.world.damagesource.PaperCombatEntryWrapper;
import io.papermc.paper.world.damagesource.PaperCombatTrackerWrapper;
import io.papermc.paper.world.damagesource.FallLocation;
import org.bukkit.craftbukkit.damage.CraftDamageEffect;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PaperServerInternalAPIBridge implements InternalAPIBridge {
    public static final PaperServerInternalAPIBridge INSTANCE = new PaperServerInternalAPIBridge();

    @Override
    public DamageEffect getDamageEffect(final String key) {
        return CraftDamageEffect.getById(key);
    }

    @Override
    public CombatEntry createCombatEntry(LivingEntity entity, DamageSource damageSource, float damage) {
        net.minecraft.world.entity.LivingEntity mob = ((CraftLivingEntity) entity).getHandle();
        net.minecraft.world.damagesource.FallLocation fallLocation = net.minecraft.world.damagesource.FallLocation.getCurrentFallLocation(mob);
        float fallDistance = mob.fallDistance;
        return new PaperCombatEntryWrapper(new net.minecraft.world.damagesource.CombatEntry(((CraftDamageSource) damageSource).getHandle(), damage, fallLocation, fallDistance));
    }

    @Override
    public CombatEntry createCombatEntry(DamageSource damageSource, float damage, @Nullable FallLocation fallLocation, float fallDistance) {
        return new PaperCombatEntryWrapper(new net.minecraft.world.damagesource.CombatEntry(((CraftDamageSource) damageSource).getHandle(), damage, fallLocation == null ? null : PaperCombatTrackerWrapper.paperToMinecraft(fallLocation), fallDistance));
    }
}
