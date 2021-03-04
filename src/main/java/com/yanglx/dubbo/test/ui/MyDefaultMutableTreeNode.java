package com.yanglx.dubbo.test.ui;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.treeStructure.Tree;
import com.yanglx.dubbo.test.CacheInfo;
import com.yanglx.dubbo.test.DubboSetingState;
import com.yanglx.dubbo.test.dubbo.DubboMethodEntity;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.List;

public class MyDefaultMutableTreeNode extends JPanel {

    public MyDefaultMutableTreeNode() {
        this.setLayout(new BorderLayout());
    }

    private TreeNodeTypeEnum nowTreeNodeTypeEnum;

    public void refresh(){
        this.createTree(nowTreeNodeTypeEnum);
    }

    public void createTree(TreeNodeTypeEnum treeNodeTypeEnum) {
        this.nowTreeNodeTypeEnum = treeNodeTypeEnum;
        this.removeAll();
        if (TreeNodeTypeEnum.COLLECTIONS.equals(treeNodeTypeEnum)) {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("收藏");
            DubboSetingState instance = DubboSetingState.getInstance();
            List<CacheInfo> paramInfoCache = instance.getParamInfoCache(DubboSetingState.CacheType.COLLECTIONS);
            for (CacheInfo dubboMethodEntity : paramInfoCache) {
                DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(dubboMethodEntity, true);
                root.add(defaultMutableTreeNode);
            }
            Tree tree = new Tree(root);
            tree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    DefaultMutableTreeNode lastSelectedPathComponent = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    Object userObject = lastSelectedPathComponent.getUserObject();
                    if (userObject instanceof CacheInfo) {
                        CacheInfo cacheInfo = (CacheInfo) userObject;
                        DubboMethodEntity dubboMethodEntity = cacheInfo.getDubboMethodEntity();
                        TabInfo selectedInfo = TabBar.getSelectionTabInfo();
                        Tab component = (Tab)selectedInfo.getComponent();
                        DubboPanel.refreshUI(component.getDubboPanel(), dubboMethodEntity);
                    }
                }
            });
            JBScrollPane jScrollBar = new JBScrollPane(tree);
            this.add(jScrollBar, BorderLayout.CENTER);
        } else {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("历史");
            DubboSetingState instance = DubboSetingState.getInstance();
            List<CacheInfo> paramInfoCache = instance.getParamInfoCache(DubboSetingState.CacheType.HISTORY);
            for (CacheInfo dubboMethodEntity : paramInfoCache) {
                DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(dubboMethodEntity, true);
                root.add(defaultMutableTreeNode);
            }
            Tree tree = new Tree(root);
            JBScrollPane jScrollBar = new JBScrollPane(tree);
            this.add(jScrollBar, BorderLayout.CENTER);
        }
        this.repaint();
        this.validate();
    }

    public enum TreeNodeTypeEnum {
        HISTORY,
        COLLECTIONS
    }
}
