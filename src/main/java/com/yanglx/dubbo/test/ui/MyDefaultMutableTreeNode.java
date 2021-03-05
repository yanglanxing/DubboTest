package com.yanglx.dubbo.test.ui;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
            //添加左测菜单点击后渲染右侧数据
            tree.addTreeSelectionListener(e -> {
                DefaultMutableTreeNode lastSelectedPathComponent = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (lastSelectedPathComponent == null) {
                    return;
                }
                Object userObject = lastSelectedPathComponent.getUserObject();
                if (userObject instanceof CacheInfo) {
                    CacheInfo cacheInfo = (CacheInfo) userObject;
                    DubboMethodEntity dubboMethodEntity = new DubboMethodEntity();
                    dubboMethodEntity.setId(cacheInfo.getId());
                    dubboMethodEntity.setInterfaceName(cacheInfo.getInterfaceName());
                    dubboMethodEntity.setMethodName(cacheInfo.getMethodName());
                    dubboMethodEntity.setVersion(cacheInfo.getVersion());
                    dubboMethodEntity.setGroup(cacheInfo.getGroup());
                    List<String> stringList = JSON.parseArray(cacheInfo.getMethodTypeJson(), String.class);
                    String[] methodTypes = new String[stringList.size()];
                    for (int i = 0; i < stringList.size(); i++) {
                        methodTypes[i] = stringList.get(i);
                    }
                    dubboMethodEntity.setMethodType(methodTypes);
                    JSONArray objects = JSON.parseArray(cacheInfo.getParamObjJson());
                    dubboMethodEntity.setParamObj(objects.toArray());
                    dubboMethodEntity.setAddress(cacheInfo.getAddress());
                    TabInfo selectedInfo = TabBar.getSelectionTabInfo();
                    Tab component = (Tab)selectedInfo.getComponent();
                    DubboPanel.refreshUI(component.getDubboPanel(), dubboMethodEntity);
                }
            });

            //右键删除操作
            JPopupMenu menu=new JPopupMenu();
            JMenuItem menuItem=new JMenuItem("删除");
            menu.add(menuItem);
            menuItem.addActionListener(e -> {
                DefaultMutableTreeNode lastSelectedPathComponent = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                Object userObject = lastSelectedPathComponent.getUserObject();
                if (userObject instanceof CacheInfo) {
                    CacheInfo cacheInfo = (CacheInfo) userObject;
                    if (TreeNodeTypeEnum.COLLECTIONS.equals(nowTreeNodeTypeEnum)) {
                        DubboSetingState.getInstance().remove(cacheInfo.getId(), DubboSetingState.CacheType.COLLECTIONS);
                    }else {
                        DubboSetingState.getInstance().remove(cacheInfo.getId(), DubboSetingState.CacheType.HISTORY);
                    }
                    //删除节点
                    DefaultTreeModel model =(DefaultTreeModel) tree.getModel();
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
