package com.obertdev.harpoonminimal.listeners;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.PsiTreeChangeListener;
import com.obertdev.harpoonminimal.services.HarpoonService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PsiFileChangeListener implements PsiTreeChangeListener {
    @Override
    public void beforeChildAddition(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void beforeChildRemoval(@NotNull PsiTreeChangeEvent event) {
        DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(context -> {
            var project = context.getData(CommonDataKeys.PROJECT);
            HarpoonService service = HarpoonService.getInstance(project);
            if (service != null) {
                var file = event.getChild().getContainingFile().getVirtualFile();
                if (file != null) {
                    service.removeFile(file.getPath());
                }
            }
        });
    }

    @Override
    public void beforeChildReplacement(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void beforeChildMovement(@NotNull PsiTreeChangeEvent event) {
    }

    @Override
    public void beforeChildrenChange(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void beforePropertyChange(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void childRemoved(@NotNull PsiTreeChangeEvent event) {
    }

    @Override
    public void childReplaced(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void childrenChanged(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void childMoved(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void propertyChanged(@NotNull PsiTreeChangeEvent event) {
        if (event.getPropertyName().equals("fileName")) {
            DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(context -> {
                var file = event.getParent();
                var file1 = file.getContainingFile().getOriginalFile().getVirtualFile();
                var project = context.getData(CommonDataKeys.PROJECT);
                HarpoonService service = HarpoonService.getInstance(project);
                if (service != null) {
                    var oldFilename = (String) event.getOldValue();
                    var newFilename = (String) event.getNewValue();
                    var path = event.getParent().getContainingFile().getVirtualFile().getPath();
                    service.updateFile(path + oldFilename, path + newFilename);
                }
            });
        }
    }
}
