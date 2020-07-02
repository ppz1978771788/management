package com.pp.common;

import java.util.ArrayList;
import java.util.List;
/*
* 把没有层级关系的集合变成有层级关系的
* */
public class TreeNodeBuider {
    public static List<TreeNode> build(List<TreeNode> treeNodes, Integer topPid){
        List<TreeNode> nodes =new ArrayList<>();
        for (TreeNode n1:treeNodes
             ) {
            if (n1.getPid()==topPid){
                nodes.add(n1);
            }
        }
        System.out.println(nodes.toString());
        for (TreeNode n1:treeNodes){
            for (TreeNode n2 :
                    nodes) {
                if (n1.getPid() == n2.getId()) {
                    List<TreeNode> children = n2.getChildren();
                    children.add(n1);
                }
            }
        }

return nodes;
    }
}
