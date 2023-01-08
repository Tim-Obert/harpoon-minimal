package com.obertdev.harpoonminimal.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.obertdev.harpoonminimal.services.HarpoonService;
import org.jetbrains.annotations.NotNull;

public class OpenFile3Action extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        var project = e.getRequiredData(CommonDataKeys.PROJECT);
        HarpoonService service = HarpoonService.getInstance(project);
        service.openFile(project, 2);
    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }
}
