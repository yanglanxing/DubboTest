package com.yanglx.dubbo.test.ui;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.treeStructure.Tree;
import com.yanglx.dubbo.test.CacheInfo;
import com.yanglx.dubbo.test.DubboSetingState;
import com.yanglx.dubbo.test.dubbo.DubboMethodEntity;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TreePanel extends JPanel {

    private Tree tree;

    private TreeNodeTypeEnum nowTreeNodeTypeEnum;


    public TreePanel(TreeNodeTypeEnum treeNodeTypeEnum) {
        this.nowTreeNodeTypeEnum = treeNodeTypeEnum;
        tree = new Tree();
        JBScrollPane jScrollBar = new JBScrollPane(tree);
        this.setLayout(new BorderLayout());
        this.add(jScrollBar, BorderLayout.CENTER);
        this.repaint();
        this.validate();
    }

    /**
     * 刷新数据并添加事件
     */
    public void refresh(TreeNodeTypeEnum treeNodeTypeEnum) {
        this.nowTreeNodeTypeEnum = treeNodeTypeEnum;
        this.refresh();
    }

    /**
     * 刷新数据并添加事件
     */
    public void refresh() {
        //添加数据模型
        this.setModel();
        //添加监听
        this.setTreeListener();
        //添加鼠标右键事件
        this.addTreeRightButtonAction();
    }

    /**
     * 设置数据模型
     */
    private void setModel(){
        List<CacheInfo> paramInfoCache;
        DefaultMutableTreeNode root;
        if (TreeNodeTypeEnum.COLLECTIONS.equals(nowTreeNodeTypeEnum)) {
            root = new DefaultMutableTreeNode("Collections");
            DubboSetingState instance = DubboSetingState.getInstance();
            paramInfoCache = instance.getParamInfoCache(DubboSetingState.CacheType.COLLECTIONS);
        } else {
            root = new DefaultMutableTreeNode("History");
            DubboSetingState instance = DubboSetingState.getInstance();
            paramInfoCache = instance.getParamInfoCache(DubboSetingState.CacheType.HISTORY);
        }
        for (CacheInfo dubboMethodEntity : paramInfoCache) {
            DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(dubboMethodEntity, true);
            root.add(defaultMutableTreeNode);
        }
        tree.setModel(new DefaultTreeModel(root, false));
        tree.updateUI();
    }

    /**
     * 添加鼠标左键点击事件
     */
    private void setTreeListener(){
        //添加左测菜单点击后渲染右侧数据
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode lastSelectedPathComponent = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (lastSelectedPathComponent == null) {
                return;
            }
            Object userObject = lastSelectedPathComponent.getUserObject();
            if (userObject instanceof CacheInfo) {
                CacheInfo cacheInfo = (CacheInfo) userObject;
                DubboMethodEntity dubboMethodEntity = cacheInfo.getDubboMethodEntity();
                TabInfo selectedInfo = TabBar.getSelectionTabInfo();
                Tab component = (Tab) selectedInfo.getComponent();
                DubboPanel.refreshUI(component.getDubboPanel(), dubboMethodEntity);
            }
        });
    }

    /**
     * 添加鼠标右键事件
     */
    private void addTreeRightButtonAction(){
        //右键删除操作
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Delete");
        menu.add(menuItem);
        if (TreeNodeTypeEnum.HISTORY.equals(nowTreeNodeTypeEnum)) {
            JMenuItem menuItemAll = new JMenuItem("Delete all");
            menu.add(menuItemAll);
            menuItemAll.addActionListener(e -> {
                //删除所有
                DubboSetingState.getInstance().historyParamInfoCacheList.clear();
                this.refresh();
            });
        }
        menuItem.addActionListener(e -> {
            DefaultMutableTreeNode lastSelectedPathComponent = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (lastSelectedPathComponent == null) {
                return;
            }
            Object userObject = lastSelectedPathComponent.getUserObject();
            if (userObject instanceof CacheInfo) {
                CacheInfo cacheInfo = (CacheInfo) userObject;
                if (TreeNodeTypeEnum.COLLECTIONS.equals(this.nowTreeNodeTypeEnum)) {
                    DubboSetingState.getInstance().remove(cacheInfo, DubboSetingState.CacheType.COLLECTIONS);
                } else {
                    DubboSetingState.getInstance().remove(cacheInfo, DubboSetingState.CacheType.HISTORY);
                }
                this.refresh();
            }
        });
        //有限显示删除popup
        this.tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX();
                int y = e.getY();
                if (e.getButton() == MouseEvent.BUTTON3) {
                    TreePath pathForLocation = tree.getPathForLocation(x, y);
                    tree.setSelectionPath(pathForLocation);
                    menu.show(tree, x, y);
                }
            }
        });

    }

    public enum TreeNodeTypeEnum {
        HISTORY,
        COLLECTIONS
    }
}
