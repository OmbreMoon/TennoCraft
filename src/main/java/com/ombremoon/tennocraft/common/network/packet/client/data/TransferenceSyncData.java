package com.ombremoon.tennocraft.common.network.packet.client.data;

public class TransferenceSyncData {
    private static boolean hasOnFrame;

    public static void setHasOnFrame(boolean hasOnFrame) {
        TransferenceSyncData.hasOnFrame = hasOnFrame;
    }

    public static boolean getHasOnFrame() { return hasOnFrame; }
}
