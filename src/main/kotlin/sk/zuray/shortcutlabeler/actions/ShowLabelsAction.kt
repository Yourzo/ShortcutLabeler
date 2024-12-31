package sk.zuray.shortcutlabeler.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import java.awt.Component
import java.awt.Container
import java.util.TreeMap
import javax.swing.AbstractButton
import javax.swing.JComponent
import javax.swing.JMenuItem


class ShowLabelsAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val rp = ApplicationManager.getApplication().getService(WindowManager::class.java).getIdeFrame(e.project!!)?.component
        if (rp != null) {
            recursShowLabelsOnLeaves(rp)
        }
    }

    private fun showLabelsOnComponent(component: Component) {
        if (component is JComponent) {
            component.isOpaque = true
        }
        component.background = JBColor.YELLOW
    }

    private fun componentIsClickable(component: Component): Boolean {
        if (component is AbstractButton || component is JMenuItem) {
            return true;
        }
        if (component is JComponent) {
            val accessibleContext = component.accessibleContext
            if (accessibleContext != null) {
                val role = accessibleContext.accessibleRole
                if (role != null && role.toString().contains("button", ignoreCase = true)) {
                    return true
                }
            }
        }
        if (component.mouseListeners.isNotEmpty()) {
            return true;
        }
        return false;
    }

    private fun recursShowLabelsOnLeaves(component: Container) {
        val children = component.components
        for (childComponent in children) {
            if (childComponent is Container) {
                recursShowLabelsOnLeaves(childComponent)
            }
        }
        if (componentIsClickable(component)) {
            showLabelsOnComponent(component)
            val id = component.name
            println("Component:$id")
        }
    }
}