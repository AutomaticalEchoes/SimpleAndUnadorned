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
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Magazine {
    protected static final Magazine HARMFUL = new Magazine();
    protected static final Magazine BENEFICIAL = new Magazine();
    private final ArrayList<Integer> weights = new ArrayList<>();
    private final List<ItemStack> itemStacks = new ArrayList<>();
    private Integer totalWeight = 0;

    public static void Init(){
        MinecraftForge.EVENT_BUS.post(new PillagerMagazineRegister.Harmful(HARMFUL));
        MinecraftForge.EVENT_BUS.post(new PillagerMagazineRegister.Beneficial(BENEFICIAL));
    }

    public boolean register(ItemStack itemStack ,Integer weight){
        if(itemStacks.contains(itemStack)) return false;
        itemStacks.add(itemStack);
        totalWeight += weight;
        weights.add(totalWeight);
        return true;
    }

    public ItemStack getItemStack(Integer i){
        return itemStacks.get(i);
    }

    public Integer getTotalWeight() {
        return totalWeight;
    }

    public Integer[] getWeights() {
        return weights.toArray(new Integer[0]);
    }

    protected static DipolarTubeProjectile RandomTube(Level level, @Nullable LivingEntity owner ,Magazine magazine){
        int random = Utils.RANDOM.nextInt(magazine.getTotalWeight());
        int index = Math.abs(BinarySearch(magazine.getWeights(), random));
        return DipolarUtils.makeProjectile(level, magazine.getItemStack(index), owner);
    }

    public static boolean ValidItemStack(ItemStack itemStack){
        return itemStack.getItem() instanceof DipolarTube && itemStack.getOrCreateTag().contains("Potion");
    }

    static Integer BinarySearch(Integer[] weight , Integer randomValue){
        int low = 0;
        int high = weight.length;
        while (low < high){
            int mid = (low + high) >>> 1;
            int i = randomValue.compareTo(weight[mid]);
            switch (i){
                case -1 -> high = mid;
                case 1 -> low = mid + 1;
                default -> {
                    return mid + 1;
                }
            }
        }
        return low;
    }


}
