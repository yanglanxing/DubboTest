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

    public TreePanel() {
        this.setLayout(new BorderLayout());
    }

    private TreeNodeTypeEnum nowTreeNodeTypeEnum;

    public void refresh() {
        this.createTree(nowTreeNodeTypeEnum);
    }

    public void createTree(TreeNodeTypeEnum treeNodeTypeEnum) {
        this.nowTreeNodeTypeEnum = treeNodeTypeEnum;
        this.removeAll();
        List<CacheInfo> paramInfoCache;
        DefaultMutableTreeNode root;
        if (TreeNodeTypeEnum.COLLECTIONS.equals(treeNodeTypeEnum)) {
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
        Tree tree = new Tree(root);
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

        //右键删除操作
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("delete");
        menu.add(menuItem);
        menuItem.addActionListener(e -> {
            DefaultMutableTreeNode lastSelectedPathComponent = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (lastSelectedPathComponent == null) {
                return;
            }
            Object userObject = lastSelectedPathComponent.getUserObject();
            if (userObject instanceof CacheInfo) {
                CacheInfo cacheInfo = (CacheInfo) userObject;
                if (TreeNodeTypeEnum.COLLECTIONS.equals(nowTreeNodeTypeEnum)) {
                    DubboSetingState.getInstance().remove(cacheInfo, DubboSetingState.CacheType.COLLECTIONS);
                } else {
                    DubboSetingState.getInstance().remove(cacheInfo, DubboSetingState.CacheType.HISTORY);
                }
                //删除节点
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                model.removeNodeFromParent(lastSelectedPathComponent);
                tree.updateUI();
            }
        });

        //有限显示删除popup
        tree.addMouseListener(new MouseAdapter() {
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
        JBScrollPane jScrollBar = new JBScrollPane(tree);
        this.add(jScrollBar, BorderLayout.CENTER);
        this.repaint();
        this.validate();
    }

    public enum TreeNodeTypeEnum {
        HISTORY,
        COLLECTIONS
    }
}
