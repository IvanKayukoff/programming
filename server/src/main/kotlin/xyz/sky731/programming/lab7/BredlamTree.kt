package xyz.sky731.programming.lab7

import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab3.Human
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeSelectionModel
import javax.swing.tree.TreePath



class BredlamTree(): JTree(makeRoot()) {
  companion object {
    fun makeRoot() = DefaultMutableTreeNode("Bredlams").apply {
      add(DefaultMutableTreeNode(Bredlam("ConstBredlam")))
    }
  }

  var selection: Any? = null

  val rootNode = model?.root as DefaultMutableTreeNode
  val treeSelectionListener = addTreeSelectionListener {
    val selected = if (selectionPath != null && selectionPath.pathCount > 1)
      (lastSelectedPathComponent as DefaultMutableTreeNode?)?.userObject
    else null

    when (selected) {
      is Bredlam -> println(selected)
      is Human -> println(selected)
    }

    selection = selected
  }
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