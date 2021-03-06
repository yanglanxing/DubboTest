package com.yanglx.dubbo.test.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.TabsListener;
import com.intellij.ui.tabs.impl.JBEditorTabs;
import com.yanglx.dubbo.test.action.CloseTabAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class TabBar extends JBEditorTabs implements TabsListener {

    public static Map<String, TabInfo> tabsMap;
    public static String activeTabId;
    private Project project;
    private TreePanel leftTree;

    public TabBar(@Nullable Project project, TreePanel leftTree) {
        super(project, ActionManager.getInstance(), IdeFocusManager.getInstance(project), project);
        this.project = project;
        this.leftTree = leftTree;
        tabsMap = new LinkedHashMap<>();
        this.addListener(this);
        this.setTabDraggingEnabled(true);
        this.addTab();
    }

    public void addTab() {
        String tabId = UUID.randomUUID().toString();

        DefaultActionGroup closeActionGroup = new DefaultActionGroup();
        closeActionGroup.add(new CloseTabAction(this, tabId));

        Tab tab = new Tab(this.project, tabId, this.leftTree);
        TabInfo tabInfo = new TabInfo(tab);
        tabInfo.setText("Tab" + (this.getTabCount() + 1));
        tabInfo.setIcon(AllIcons.General.Web);
        tabInfo.setTabLabelActions(closeActionGroup, "EditorTab");

        tabsMap.put(tabId, tabInfo);
        this.addTab(tabInfo);

        //显示tab 聚焦当前tab
        this.select(tabInfo, true);
    }

    public void closeTab(@NotNull String tabId) {
        TabInfo tabInfo = tabsMap.get(tabId);
        tabsMap.remove(tabId);
        this.removeTab(tabInfo);
    }


    @Override
    public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
        Tab tab = (Tab) newSelection.getComponent();
        activeTabId = tab.getId();
    }

    public static TabInfo getSelectionTabInfo() {
        return tabsMap.get(activeTabId);
    }
}
