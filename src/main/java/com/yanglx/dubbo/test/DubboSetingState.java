package com.yanglx.dubbo.test;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>Description: </p>
 *
 * @author yanglx
 * @version 1.0.0
 * @email "mailto:dev_ylx@163.com"
 * @date 2021.02.20 15:57
 * @since 1.0.0
 */
@State(
    name = "com.yanglx.dubbo.test.DubboSetingState",
    storages = {@Storage("dubbo.test.configs.xml")}
)
public class DubboSetingState implements PersistentStateComponent<DubboSetingState> {

    /** Address */
    public Map<String,CacheInfo> paramInfoCache = new TreeMap<>();

    public Map<String,CacheInfo> HistoryParamInfoCache = new TreeMap<>();

    /**
     * Gets address *
     *
     * @return the address
     * @since 1.0.0
     */
    public List<CacheInfo> getParamInfoCache(CacheType cacheType) {
        if (CacheType.COLLECTIONS.equals(cacheType)) {
            return new ArrayList<>(paramInfoCache.values());
        }else {
            return new ArrayList<>(HistoryParamInfoCache.values());
        }
    }

    /**
     * 添加
     *
     * @param address address
     * @since 1.0.0
     */
    public void add(String id, CacheInfo address, CacheType cacheType) {
        if (CacheType.COLLECTIONS.equals(cacheType)) {
            this.paramInfoCache.put(id,address);
        }else {
            this.HistoryParamInfoCache.put(id,address);
        }
    }

    /**
     * 移除
     * @param id
     */
    public void remove(String id,CacheType cacheType){
        if (CacheType.COLLECTIONS.equals(cacheType)) {
            this.paramInfoCache.remove(id);
        }else {
            this.HistoryParamInfoCache.remove(id);
        }

    }

    /**
     * Gets instance *
     *
     * @return the instance
     * @since 1.0.0
     */
    public static DubboSetingState getInstance() {
        return ServiceManager.getService(DubboSetingState.class);
    }

    /**
     * Gets state *
     *
     * @return the state
     * @since 1.0.0
     */
    @Nullable
    @Override
    public DubboSetingState getState() {
        return this;
    }

    /**
     * Load state
     *
     * @param state state
     * @since 1.0.0
     */
    @Override
    public void loadState(@NotNull DubboSetingState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public enum CacheType{
        HISTORY,
        COLLECTIONS
    }
}
