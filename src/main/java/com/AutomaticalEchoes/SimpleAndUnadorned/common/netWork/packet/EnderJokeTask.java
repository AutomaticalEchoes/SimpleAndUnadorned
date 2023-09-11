package com.AutomaticalEchoes.SimpleAndUnadorned.common.netWork.packet;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class EnderJokeTask {
    private @Nullable Integer StructureID;
    private @Nullable Integer TargetID;
    private int EntityID;
    private boolean IsAngry;


    public EnderJokeTask(int entityID,@Nullable Integer targetID, @Nullable Integer structureID,boolean isAngry) {
        StructureID = structureID;
        EntityID = entityID;
        TargetID = targetID;
        IsAngry = isAngry;
    }

    public static void encode(EnderJokeTask msg, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(msg.EntityID);
        packetBuffer.writeNullable(msg.TargetID,FriendlyByteBuf::writeInt);
        packetBuffer.writeNullable(msg.StructureID,FriendlyByteBuf::writeInt);
        packetBuffer.writeBoolean(msg.IsAngry);
    }
    public static EnderJokeTask decode(FriendlyByteBuf packetBuffer) {
        int jokerID = packetBuffer.readInt();
        Integer targetID = packetBuffer.readNullable(FriendlyByteBuf::readInt);
        Integer StructureID = packetBuffer.readNullable(FriendlyByteBuf::readInt);
        return new EnderJokeTask(jokerID,targetID,StructureID,packetBuffer.readBoolean());
    }

    public static void onMessage(EnderJokeTask msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> msg.handleMessage(msg,context.getSender()));
        context.setPacketHandled(true);
    }

    public void handleMessage(EnderJokeTask msg, ServerPlayer sender) {
        Entity entity = sender.level.getEntity(msg.EntityID);
        if(entity instanceof SuspiciousEnderman suspiciousEnderman){
            suspiciousEnderman.setAngry(IsAngry);
            if(TargetID !=null && sender.level.getEntity(TargetID) instanceof LivingEntity livingEntity){
                suspiciousEnderman.startJokeWith(livingEntity,msg.StructureID);
            }else {
                suspiciousEnderman.startJokeWith(sender,msg.StructureID);
            }

        }
    }
}
