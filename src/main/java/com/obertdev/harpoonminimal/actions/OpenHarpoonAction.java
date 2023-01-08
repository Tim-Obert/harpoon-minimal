package com.obertdev.harpoonminimal.actions;

import com.intellij.openapi.actionSystem.*;
import com.obertdev.harpoonminimal.services.HarpoonService;

import java.util.Objects;

public class OpenHarpoonAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        var project = e.getRequiredData(CommonDataKeys.PROJECT);
        var toolWindow = HarpoonService.getInstance(project).getToolWindow();
        if (toolWindow != null) {
            toolWindow.show();
        }
    }
}
