package com.ombremoon.tennocraft.common.network.packet.server.data;

import com.ombremoon.tennocraft.common.network.packet.client.data.TransferenceSyncData;

public class RotationSyncData {
    private static float pitchAmount;
    private static float yawAmount;

    public static void setData(float pitchAmount, float yawAmount) {
        setPitchAmount(pitchAmount);
        setYawAmount(yawAmount);
    }

    public static void setPitchAmount(float pitchAmount) {
        RotationSyncData.pitchAmount = pitchAmount;
    }

    public static void setYawAmount(float yawAmount) {
        RotationSyncData.yawAmount = yawAmount;
    }

    public static float getPitchAmount() {
        return pitchAmount;
    }

    public static float getYawAmount() {
        return yawAmount;
    }
}
