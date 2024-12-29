package sk.zuray.shortcutlabeler.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.wm.WindowManager
import java.awt.Component
import java.awt.Container
import javax.swing.JComponent
import javax.swing.JLabel


class ShowLabelsAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val rp = ApplicationManager.getApplication().getService(WindowManager::class.java).getIdeFrame(e.project!!)?.component
        if (rp != null) {
            recursShowLabelsOnLeaves(rp)
        }
    }

    private fun showLabelsOnComponent(component: Container) {

    }

    private fun recursShowLabelsOnLeaves(component: Container) {
        val children = component.components.filterIsInstance<Container>()
        for (childComponent in children) {
            recursShowLabelsOnLeaves(childComponent)
        }
        if (children.isEmpty()) {
            val id = component.toString()
            println("Component:$id")
        }
    }
}