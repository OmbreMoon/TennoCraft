package com.ombremoon.tennocraft.client.renderer.layer;

import com.ombremoon.tennocraft.client.model.DefaultFrameModel;
import com.ombremoon.tennocraft.client.renderer.FrameRenderer;
import com.ombremoon.tennocraft.common.world.item.TransferenceKeyItem;

public class FrameLayer<T extends TransferenceKeyItem> extends FrameRenderer<T> {
    public FrameLayer() {
        super(new DefaultFrameModel<>());
    }
}
