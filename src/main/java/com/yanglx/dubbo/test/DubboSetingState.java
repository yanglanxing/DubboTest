package com.yanglx.dubbo.test;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@State(
        name = "com.me.dubbo.test.DubboSetingState",
        storages = {@Storage("SdkSettingsPlugin.xml")}
)
public class DubboSetingState implements PersistentStateComponent<DubboSetingState> {

    public List<String> address = new ArrayList<>();

    public List<String> getAddress() {
        if (address.isEmpty()) {
            address.add("zookeeper://127.0.0.1:2181");
        }
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public static DubboSetingState getInstance() {
        return ServiceManager.getService(DubboSetingState.class);
    }

    @Nullable
    @Override
    public DubboSetingState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull DubboSetingState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}
