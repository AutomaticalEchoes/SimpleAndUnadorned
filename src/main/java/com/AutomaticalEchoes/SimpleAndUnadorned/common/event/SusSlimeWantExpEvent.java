package com.AutomaticalEchoes.SimpleAndUnadorned.common.event;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.blockEntity.SusSlimeBase.SusSlimeBase;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
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
