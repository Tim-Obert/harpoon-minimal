package com.obertdev.harpoonminimal.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.obertdev.harpoonminimal.services.HarpoonService;
import org.jetbrains.annotations.NotNull;

public class AddFileAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        var project = e.getRequiredData(CommonDataKeys.PROJECT);

        HarpoonService harpoonService = HarpoonService.getInstance(project);
        var fileEditor = FileEditorManager.getInstance(project).getSelectedEditor();
        if (fileEditor != null) {
            var file = fileEditor.getFile();
            harpoonService.addFile(file);
        }
    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }
}