package sk.zuray.shortcutlabeler.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.wm.WindowManager
import java.awt.Color
import java.awt.Component
import java.awt.Container
import javax.swing.AbstractButton
import javax.swing.JComponent
import javax.swing.JMenuItem


class ShowLabelsAction: AnAction() {
    private val originalComponentColor = mutableMapOf<Component, Color>()
    override fun actionPerformed(e: AnActionEvent) {
        if (originalComponentColor.isNotEmpty()) {
            rollbackOriginalColors()
            return
        }
        val rp = ApplicationManager.getApplication().getService(WindowManager::class.java).getIdeFrame(e.project!!)?.component
        if (rp != null) {
            recursShowLabelsOnLeaves(rp)
        }
    }

    private fun rollbackOriginalColors() {
        for (component in originalComponentColor.keys) {
            if (component is JComponent) {
                component.isOpaque = true
            }
            component.background = originalComponentColor[component]
        }
        originalComponentColor.clear()
    }

    private fun showLabelsOnComponent(component: Component) {
        originalComponentColor[component] = component.background
        if (component is JComponent) {
            component.isOpaque = true
        }
        component.background = Color.YELLOW
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
        }
    }
}