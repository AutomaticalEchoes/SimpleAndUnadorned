package com.AutomaticalEchoes.SimpleAndUnadorned.common.event;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Event;


public class SusSlimeSummonEvent extends Event {
    private final Level level;
    private final Vec3 vec3;

    public SusSlimeSummonEvent(Level level, Vec3 vec3) {
        this.level = level;
        this.vec3 = vec3;
    }

    public Level getLevel() {
        return level;
    }

    public Vec3 getVec3() {
        return vec3;
    }
}
