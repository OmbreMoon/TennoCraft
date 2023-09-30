package com.ombremoon.tennocraft.common.network.packet.client;

import com.ombremoon.tennocraft.common.network.packet.IAbstractMessage;
import com.ombremoon.tennocraft.util.EntityUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundMovementPacket implements IAbstractMessage {
    private final float x;
    private final float y;
    private final float z;
    private final MovementOperation movementOperation;

    public ClientboundMovementPacket(float x, float y, float z, MovementOperation movementOperation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.movementOperation = movementOperation;
    }

    public ClientboundMovementPacket(FriendlyByteBuf friendlyByteBuf) {
        this.x = friendlyByteBuf.readFloat();
        this.y = friendlyByteBuf.readFloat();
        this.z = friendlyByteBuf.readFloat();
        this.movementOperation = MovementOperation.fromOrdinal(friendlyByteBuf.readInt());
    }

    @Override
    public void toBytes(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeFloat(x);
        friendlyByteBuf.writeFloat(y);
        friendlyByteBuf.writeFloat(z);
        friendlyByteBuf.writeInt(movementOperation.getOrdinal());
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            EntityUtil.sendMovementPacket(x, y, z, movementOperation);
        });
        return true;
    }

    public enum MovementOperation {
        SET(0),
        ADD(1),
        MULTIPLY(2);

        private final int ordinal;

        MovementOperation(int ordinal) {
            this.ordinal = ordinal;
        }

        public static MovementOperation fromOrdinal(int ordinal) {
            return switch (ordinal) {
                case 1 -> ADD;
                case 2 -> MULTIPLY;
                default -> SET;
            };
        }

        public int getOrdinal() {
            return this.ordinal;
        }
    }
}
