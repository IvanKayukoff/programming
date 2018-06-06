package xyz.sky731.programming.lab7

import xyz.sky731.programming.lab3.Bredlam
import java.util.*
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeSelectionModel

class BredlamTree(queue: Queue<Bredlam>): JTree(rootNode) {
  companion object {
    val rootNode = DefaultMutableTreeNode("Bredlams").apply {
      add(DefaultMutableTreeNode(Bredlam("ConstBredlam")))
    }
  }

  init {
    selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
    rootNode.apply {
      queue.forEach {
        add(DefaultMutableTreeNode(it))
      }
    }
  }

}