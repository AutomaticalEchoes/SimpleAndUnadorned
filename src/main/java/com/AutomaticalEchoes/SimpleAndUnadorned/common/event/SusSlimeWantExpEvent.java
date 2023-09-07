package com.AutomaticalEchoes.SimpleAndUnadorned.common.event;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.blockEntity.SusSlimeBase;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.entity.SuspiciousSlime;
import net.minecraftforge.eventbus.api.Event;

public class SusSlimeWantExpEvent extends Event {
    private final SuspiciousSlime suspiciousSlime;
    private final SusSlimeBase susSlimeBase;

    public SusSlimeWantExpEvent(SuspiciousSlime suspiciousSlime, SusSlimeBase susSlimeBase) {
        this.suspiciousSlime = suspiciousSlime;
        this.susSlimeBase = susSlimeBase;
    }

    public SuspiciousSlime getSuspiciousSlime() {
        return suspiciousSlime;
    }

    public SusSlimeBase getSusSlimeBase() {
        return susSlimeBase;
    }
}