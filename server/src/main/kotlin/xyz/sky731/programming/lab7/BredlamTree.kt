package xyz.sky731.programming.lab7

import xyz.sky731.programming.lab3.Bredlam
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeSelectionModel

class BredlamTree(): JTree(makeRoot()) {
  companion object {
    fun makeRoot() = DefaultMutableTreeNode("Bredlams").apply {
      add(DefaultMutableTreeNode(Bredlam("ConstBredlam")))
    }
  }

  val rootNode = model?.root as DefaultMutableTreeNode

  init {
    selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
  }

  override fun getModel(): DefaultTreeModel? = super.getModel() as DefaultTreeModel?

  fun applyChanges(updates: List<TreeChange>) {
    updates.forEach { (bredlam, isAdded) ->
      when (isAdded) {
        true -> {
          val curNode = DefaultMutableTreeNode(bredlam)
          model?.insertNodeInto(curNode, rootNode, rootNode.childCount)
          bredlam.humans.forEach {
            model?.insertNodeInto(DefaultMutableTreeNode(it), curNode, curNode.childCount)
          }
        }
        false -> {
          rootNode.children().iterator().asSequence()
              .find { (it as DefaultMutableTreeNode).userObject == it }
              ?.let { model?.removeNodeFromParent(it as DefaultMutableTreeNode) }
        }
      }

    }
  }
}