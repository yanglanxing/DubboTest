package com.yanglx.dubbo.test;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.yanglx.dubbo.test.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

    /** 存放收藏 */
    public LinkedList<CacheInfo> paramInfoCacheList = new LinkedList<>();
    /** 存放历史 */
    public LinkedList<CacheInfo> historyParamInfoCacheList = new LinkedList<>();

    public List<CacheInfo> dubboConfigs = new ArrayList<>();
    //限制最大历史记录条数
    private static final int MAX_HISTORY_SIZE = 200;
    /**
     * Gets address *
     *
     * @return the address
     * @since 1.0.0
     */
    public List<CacheInfo> getParamInfoCache(CacheType cacheType) {
        if (CacheType.COLLECTIONS.equals(cacheType)) {
            paramInfoCacheList.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
            return paramInfoCacheList;
        }else {
            historyParamInfoCacheList.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
            return historyParamInfoCacheList;
        }
    }

    public void setDubboConfigs(List<CacheInfo> cacheInfo){
        this.dubboConfigs.clear();
        this.dubboConfigs.addAll(cacheInfo);
    }

    public List<CacheInfo> getDubboConfigs(){
        if (this.dubboConfigs.isEmpty()) {
            CacheInfo cacheInfo = new CacheInfo();
            cacheInfo.setAddress("zookeeper://127.0.0.1:2181");
            cacheInfo.setName("Default");
            cacheInfo.setVersion("1.0.0");
            this.dubboConfigs.add(cacheInfo);
        }
        return this.dubboConfigs;
    }

    /**
     * 添加
     *
     * @param cacheInfo
     * @since 1.0.0
     */
    public void add(CacheInfo cacheInfo, CacheType cacheType) {
        if (CacheType.COLLECTIONS.equals(cacheType)) {
            this.paramInfoCacheList.remove(cacheInfo);
            this.paramInfoCacheList.add(cacheInfo);
        }else {
            if (paramInfoCacheList.size() >= MAX_HISTORY_SIZE) {
                this.historyParamInfoCacheList.addFirst(cacheInfo);
                this.historyParamInfoCacheList.removeLast();
            }else {
                this.historyParamInfoCacheList.add(cacheInfo);
            }
        }
    }

    /**
     * 移除缓存
     */
    public void remove(CacheInfo cacheInfo,CacheType cacheType){
        if (CacheType.COLLECTIONS.equals(cacheType)) {
            this.paramInfoCacheList.remove(cacheInfo);
        }else {
            this.historyParamInfoCacheList.remove(cacheInfo);
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
