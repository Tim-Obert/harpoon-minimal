package com.obertdev.harpoonminimal.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.PsiManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.obertdev.harpoonminimal.listeners.PsiFileChangeListener;
import org.jetbrains.annotations.NotNull;

public class HarpoonWindowFactory implements ToolWindowFactory {

    public ToolWindow aToolWindow;
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        var content = this.getContent(toolWindow);
        toolWindow.getContentManager().addContent(content);
        aToolWindow = toolWindow;
        // TODO: Change on Refactor
        // PsiManager.getInstance(project).addPsiTreeChangeListener(new PsiFileChangeListener());
    }
    private Content getContent(@NotNull ToolWindow toolWindow){
        HarpoonWindow harpoonWindow = new HarpoonWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.getInstance();
        return contentFactory.createContent(harpoonWindow.getContent(), "", false);

    }

    public void updateWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        var content = this.getContent(toolWindow);
        toolWindow.getContentManager().removeAllContents(true);
        toolWindow.getContentManager().addContent(content);
    }
}
