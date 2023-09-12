package com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils;


import com.AutomaticalEchoes.SimpleAndUnadorned.api.Function.EffectFunction;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.IExplosion;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile.DipolarTubeProjectile;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DipolarUtils{
    public static DipolarTubeProjectile makeProjectile(Level level, ItemStack itemStack,@Nullable LivingEntity owner){
        DipolarTubeProjectile dipolarTubeProjectile = new DipolarTubeProjectile(owner, level, itemStack);
        List<Polarity> polarity = getPolarity(itemStack);
        if(polarity.isEmpty()){
            dipolarTubeProjectile.AddFunc(new BaseDipolarTubeFunc().HitEntity(DipolarTubeFunc::Total));
        }else {
            polarity.forEach(polarity1 -> dipolarTubeProjectile.AddFunc(polarity1.getFunc()));
        }
        return dipolarTubeProjectile;
    }

    public static List<Polarity> getPolarity(ItemStack itemStack){
        ArrayList<Polarity> polarities = new ArrayList<>();
        if(itemStack.getOrCreateTag().contains("polarity")){
            ListTag polarity = itemStack.getTag().getList("polarity", 10);
            polarity.forEach(tag -> {
                Polarity fromName = Polarity.getFromName(((CompoundTag) tag).getString("name"));
                if(fromName != null) polarities.add(fromName);
            });
        }
        return polarities;
    }

    public static boolean isConflict(List<Polarity> polarities, Polarity polarity){
        if(polarities.isEmpty()) return false;
        Optional<Polarity> first = polarities.stream().filter(insPolarity -> (polarity.getLogicalNum() & insPolarity.getLogicalNum()) != 0).findFirst();
        return first.isPresent();
    }


    public static void addPolarityTooltip(ItemStack itemStack, List<Component> p_42990_){
        List<Polarity> polarity = getPolarity(itemStack);
        if(polarity.isEmpty()) return;
        p_42990_.add(Component.empty());
        polarity.forEach(polarity1 -> p_42990_.add(Component.translatable("polarity." + polarity1.getName()).withStyle(Style.EMPTY.withColor(polarity1.getColor()))));
    }

    public static boolean ValidItem(ItemStack itemStack){
        if(itemStack.getItem() != ItemsRegister.DIPOLAR_TUBE_POTION_ITEM.get()) return false;
        List<Polarity> polarity = getPolarity(itemStack);
        return polarity.size() < 2;
    }

    public static void CreateAreaEffectCloudOrInstantenous(ServerLevel serverLevel, Vec3 location, ItemStack itemStack , Entity entity, float radius, @Nullable LivingEntity source){
       EffectFunction.CreateAreaEffectCloudOrInstantenous(serverLevel,location,PotionUtils.getMobEffects(itemStack),entity,radius,10,0.25,source);
    }

    public static void ApplyEffectToLivingEntity( DipolarTubeProjectile dipolarTubeProjectile,LivingEntity livingEntity ,double scale , Entity source ){
        EffectFunction.applyEffect(livingEntity,PotionUtils.getMobEffects(dipolarTubeProjectile.getItem()),scale,source);
    }

    public static void Explode(DipolarTubeProjectile dipolarTubeProjectile){
        List<MobEffectInstance> collection = new ArrayList<>(PotionUtils.getMobEffects(dipolarTubeProjectile.getItem()));
        ServerLevel serverLevel = (ServerLevel) dipolarTubeProjectile.level;
        Explosion explosion = new IExplosion(serverLevel,dipolarTubeProjectile,null,null, dipolarTubeProjectile.getX(), dipolarTubeProjectile.getY(), dipolarTubeProjectile.getZ(), 5 ,false, Explosion.BlockInteraction.NONE)
                .BaseDamage(1.0F)
                .ShouldBlow(false)
                .Effects(collection)
                .ShouldCalculateDamage(false);

        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(serverLevel, explosion)) return ;
        explosion.explode();
        explosion.finalizeExplosion(false);
        explosion.clearToBlow();

        for(ServerPlayer serverplayer : serverLevel.players()) {
            if (serverplayer.distanceToSqr(dipolarTubeProjectile.getX(), dipolarTubeProjectile.getY(), dipolarTubeProjectile.getZ()) < 4096.0D) {
                serverplayer.connection.send(new ClientboundExplodePacket(dipolarTubeProjectile.getX(), dipolarTubeProjectile.getY(), dipolarTubeProjectile.getZ(),5, explosion.getToBlow(), explosion.getHitPlayers().get(serverplayer)));
            }
        }
    }
}
