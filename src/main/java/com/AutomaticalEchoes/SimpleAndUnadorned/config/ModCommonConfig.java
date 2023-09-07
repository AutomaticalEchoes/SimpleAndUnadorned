package com.AutomaticalEchoes.SimpleAndUnadorned.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER=new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Integer> RAGE_TARGET_EFFECT_DURATION_TICK;
    public static final ForgeConfigSpec.ConfigValue<Integer> RAGE_TARGET_EFFECT_SPEEDUP_LEVEL;

    public static final ForgeConfigSpec.ConfigValue<Integer> SUSPICIOUS_CREEPER_RAGE_EFFECT_DURATION_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> SUSPICIOUS_CREEPER_INJECT_TIME;
    public static final ForgeConfigSpec.ConfigValue<Double> SUSPICIOUS_CREEPER_EXPLODE_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Double> SUSPICIOUS_CREEPER_AREA_EFFECT_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> SUSPICIOUS_CREEPER_AREA_EFFECT_DURATION_TIME;

    public static final ForgeConfigSpec.ConfigValue<Integer> WARNING_TICK;

    public static final ForgeConfigSpec.ConfigValue<Integer> SUSPICIOUS_SLIME_ACIDITY_AREA_EFFECT_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> SUSPICIOUS_SLIME_ACIDITY_AREA_EFFECT_DURATION_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> SUSPICIOUS_SLIME_ACIDITY_DISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> SUSPICIOUS_SLIME_WANT_COLLECT_TICK;
    public static final ForgeConfigSpec.ConfigValue<Integer> SUSPICIOUS_SLIME_TRANSLATE_TICK;
    public static final ForgeConfigSpec.ConfigValue<Integer> SUSPICIOUS_SLIME_BASE_CREATE_SLIME_TICK;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_JOKE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_JOKE_ANGRY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_JOKE_CATCH;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_JOKE_FRAME_UP;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_JOKE_STEAL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_JOKE_CAT_RIDE_CREEPER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_JOKE_WOLF_RIDE_SKELETON;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_JOKE_PLAYER_RIDE_LIVING;
    public static final ForgeConfigSpec.ConfigValue<Integer> ENDER_SCARE;


    static {
        BUILDER.push("myMod config");
        BUILDER.push("rage effect");
        RAGE_TARGET_EFFECT_DURATION_TICK=BUILDER.defineInRange("rage effect duration tick",35,1,60);
        RAGE_TARGET_EFFECT_SPEEDUP_LEVEL=BUILDER.comment("monster speed up level while range").define("speed up leve",2);
        BUILDER.pop();

        BUILDER.push("creeper");
        SUSPICIOUS_CREEPER_RAGE_EFFECT_DURATION_TIME=BUILDER.defineInRange("rage effect duration time",300,120,300);
        SUSPICIOUS_CREEPER_INJECT_TIME=BUILDER.define("SUSPICIOUS_CREEPER inject time",4);
        SUSPICIOUS_CREEPER_EXPLODE_RADIUS=BUILDER.defineInRange("SUSPICIOUS_CREEPER explode radius",5,0.5,11);
        SUSPICIOUS_CREEPER_AREA_EFFECT_RADIUS=BUILDER.defineInRange("SUSPICIOUS_CREEPER area effect radius",0.5,0.5,11);
        SUSPICIOUS_CREEPER_AREA_EFFECT_DURATION_TIME=BUILDER.defineInRange("SUSPICIOUS_CREEPER area effect duration time",10,1,10);
        BUILDER.pop();

        BUILDER.push("mini creeper");
        WARNING_TICK = BUILDER.defineInRange("mini creeper warning time ",4,2,10);
        BUILDER.pop();

        BUILDER.push("slime");
        SUSPICIOUS_SLIME_ACIDITY_AREA_EFFECT_RADIUS=BUILDER.defineInRange("SUSPICIOUS_SLIME acidity area effect radius",5,3,7);
        SUSPICIOUS_SLIME_ACIDITY_AREA_EFFECT_DURATION_TIME=BUILDER.defineInRange("SUSPICIOUS_SLIME acidity area effect duration time",20,15,30);
        SUSPICIOUS_SLIME_ACIDITY_DISTANCE=BUILDER.defineInRange("SUSPICIOUS_SLIME acidity distance",5,5,11);
        SUSPICIOUS_SLIME_WANT_COLLECT_TICK=BUILDER.defineInRange("SUSPICIOUS_SLIME acidity prepare tick",4,3,5);
        SUSPICIOUS_SLIME_TRANSLATE_TICK=BUILDER.defineInRange("SUSPICIOUS_SLIME translate tick",30,15,45);
        SUSPICIOUS_SLIME_BASE_CREATE_SLIME_TICK = BUILDER.defineInRange("SUSPICIOUS_SLIME_BASE create slime tick",450,300,600);
        BUILDER.pop();

        BUILDER.push("enderman");
        ENABLE_JOKE_ANGRY = BUILDER.define("enable joke : angry",true);
        ENABLE_JOKE_CATCH = BUILDER.define("enable joke : catch",true);
        ENABLE_JOKE_FRAME_UP = BUILDER.define("enable joke : frame up",true);
        ENABLE_JOKE_STEAL = BUILDER.define("enable joke : steal",true);
        ENABLE_JOKE_CAT_RIDE_CREEPER = BUILDER.define("enable joke : cat ride creeper",true);
        ENABLE_JOKE_WOLF_RIDE_SKELETON = BUILDER.define("enable joke : wolf ride skeleton",true);
        ENABLE_JOKE_PLAYER_RIDE_LIVING = BUILDER.define("enable joke : player ride living",true);
        ENABLE_JOKE = BUILDER.define("enable joke",true);
        ENDER_SCARE = BUILDER.defineInRange("ender sacre time ",60,60,3000);
        BUILDER.pop();

        BUILDER.pop();
        SPEC=BUILDER.build();
    }
}
