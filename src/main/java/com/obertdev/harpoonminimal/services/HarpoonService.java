package com.obertdev.harpoonminimal.services;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.obertdev.harpoonminimal.toolwindow.HarpoonWindowFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

@State(name = "harpoon_arr", storages = {@Storage("harpoon.xml")})
public class HarpoonService implements PersistentStateComponent<HarpoonService> {

    public HarpoonService() {
        stateValue = new ArrayList<String>();
        currentIndex = 0;
    }

    public ArrayList<String> stateValue;
    public int currentIndex;

    public void addFile(VirtualFile file) {
        if (!stateValue.contains(file.getPath())) {
            stateValue.add(file.getPath());
            AtomicReference<@org.jetbrains.annotations.Nullable Project> project = new AtomicReference<>();
            DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(context -> {
                project.set(context.getData(CommonDataKeys.PROJECT));
            });
        }
        this.updateToolWindow();
    }
    public void updateFile(String oldPath, String newPath) {
        System.out.println(oldPath);
        System.out.println(newPath);
        System.out.println(this.stateValue.contains(oldPath));
        if (this.stateValue.contains(oldPath)) {
            System.out.println(this.stateValue);
            this.stateValue.set(this.stateValue.indexOf(oldPath), newPath);
            this.updateToolWindow();
        }
        System.out.println(this.stateValue);
    }

    public void openFile(Project project, int index) {
        this.currentIndex = index;
        if (!this.stateValue.isEmpty()) {
            var path = this.stateValue.get(index);
            if (path != null) {
                var localFileSystem = LocalFileSystem.getInstance();
                var file = localFileSystem.findFileByPath(path);
                FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, file), true);
            }
        }
    }

    public void removeFile(int index) {
        this.currentIndex = index;
        this.stateValue.remove(index);
        this.updateToolWindow();
    }
    public void removeFile(String path) {
        var index = this.stateValue.indexOf(path);
        if (index >= 0){
            this.removeFile(index);
        }
    }

    public void changeSorting(int index, int step) {
        var file = this.stateValue.get(index);
        var newIndex = index + step;
        if (newIndex >= 0 && newIndex <= this.stateValue.size()){
            this.currentIndex = newIndex;
            this.stateValue.remove(index);
            this.stateValue.add(newIndex, file);
        }
        this.updateToolWindow();
    }

    public ToolWindow getToolWindow() {
        var id = "Harpoon";
        AtomicReference<@org.jetbrains.annotations.Nullable Project> project = new AtomicReference<>();
        DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(context -> {
            project.set(context.getData(CommonDataKeys.PROJECT));
        });
        var toolWindowManager = ToolWindowManager.getInstance((Project) project.get());
        var toolWindow = toolWindowManager.getToolWindow(id);

        // One time registration of the tool window (does not add any content).
        if (toolWindow == null) {
            toolWindow = toolWindowManager.registerToolWindow(id, null, ToolWindowAnchor.RIGHT);
            toolWindow.setToHideOnEmptyContent(true);
        }
        return toolWindow;
    }

    public void updateToolWindow() {
        var toolWindow = this.getToolWindow();
        var fac = new HarpoonWindowFactory();
        fac.updateWindowContent(toolWindow.getProject(), toolWindow);
    }

    public HarpoonService getState() {
        return this;
    }

    public void loadState(HarpoonService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Nullable
    public static HarpoonService getInstance(Project project) {
        return project.getService(HarpoonService.class);
    }

}
