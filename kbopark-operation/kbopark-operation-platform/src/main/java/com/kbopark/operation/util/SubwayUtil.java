package com.kbopark.operation.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/11 11:27
 **/
@UtilityClass
public class SubwayUtil {


	/**
	 * 两层循环实现建树
	 *
	 * @param treeNodes 传入的树节点列表
	 * @return
	 */
	public <T extends TreeNode> List<T> build(List<T> treeNodes, Object root) {

		List<T> trees = new ArrayList<>();

		for (T treeNode : treeNodes) {

			if (root.equals(treeNode.getParentId())) {
				trees.add(treeNode);
			}

			for (T it : treeNodes) {
				if (it.getParentId().equals(treeNode.getId())) {
					if (treeNode.getChildren() == null) {
						treeNode.setChildren(new ArrayList<>());
					}
					treeNode.add(it);
				}
			}
		}
		return trees;
	}

	/**
	 * 使用递归方法建树
	 *
	 * @param treeNodes
	 * @return
	 */
	public <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
		List<T> trees = new ArrayList<T>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * 递归查找子节点
	 *
	 * @param treeNodes
	 * @return
	 */
	public <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
		for (T it : treeNodes) {
			if (treeNode.getId().equals(it.getParentId())) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<>());
				}
				treeNode.add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}


	/**
	 * 构建地铁线路树
	 *
	 * @param subwayLines
	 * @param root
	 * @return
	 */
	public static List<SubwaySiteTree> buildLineTree(List<SubwayLineVO> subwayLines, String root) {
		List<SubwaySiteTree> nodes = new ArrayList<>();
		for (SubwayLineVO line : subwayLines) {
			SubwaySiteTree lineTree = new SubwaySiteTree();
			lineTree.setId(line.getId());
			lineTree.setParentId(line.getParentId());
			lineTree.setText(line.getText());
			lineTree.setCode(line.getCode());
			lineTree.setLng(line.getLng());
			lineTree.setLat(line.getLat());
			nodes.add(lineTree);
		}
		return build(nodes, root);
	}

}
