package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils.DipolarUtils;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.Utils;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.PillagerMagazineRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.item.DipolarTube;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile.DipolarTubeProjectile;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class Magazine {
    protected static final LinkedHashSet<ItemStack> MAGAZINE_ATTACK = new LinkedHashSet<>();
    protected static final LinkedHashSet<ItemStack> MAGAZINE_HELP = new LinkedHashSet<>();
    private static final List<ItemStack> ATTACK = new ArrayList<>();
    private static final List<ItemStack> HELP = new ArrayList<>();
    public static void Init(){
        MinecraftForge.EVENT_BUS.post(new PillagerMagazineRegister(MAGAZINE_ATTACK,MAGAZINE_HELP));
        ATTACK.clear();
        ATTACK.addAll(MAGAZINE_ATTACK);
        HELP.clear();
        HELP.addAll(MAGAZINE_HELP);
    }

    protected static DipolarTubeProjectile RandomAttackTube(Level level, @Nullable LivingEntity owner){
        int random = Utils.RANDOM.nextInt(ATTACK.size());
        DipolarTubeProjectile dipolarTubeProjectile = DipolarUtils.makeProjectile(level, ATTACK.get(random), owner);
        dipolarTubeProjectile.setTurn(true);
        return dipolarTubeProjectile;
    }

    public static boolean ValidItemStack(ItemStack itemStack){
        return itemStack.getItem() instanceof DipolarTube && itemStack.getOrCreateTag().contains("Potion");
    }

}
