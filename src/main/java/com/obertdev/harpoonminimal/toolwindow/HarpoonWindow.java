package com.obertdev.harpoonminimal.toolwindow;

import com.intellij.ide.*;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.table.JBTable;
import com.intellij.ui.treeStructure.Tree;
import com.obertdev.harpoonminimal.actions.AddFileAction;
import com.obertdev.harpoonminimal.services.HarpoonService;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicReference;


public class HarpoonWindow {
    private JBTable table1;
    private JPanel harpoonWindowContent;
    private Tree tree1;
    private JButton addCurrentFileButton;
    private JButton deleteButton;
    private JButton sortUpButton;
    private JButton sortDownButton;

    public HarpoonWindow(ToolWindow toolWindow) {
        addCurrentFileButton.addActionListener(e -> {
            DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(context -> {
                var action = new AddFileAction();
                action.actionPerformed(AnActionEvent.createFromDataContext("AddFile", null, context));
            });

        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table1.getSelectedRow() >= 0) {
                    DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(context -> {
                        var project = context.getData(CommonDataKeys.PROJECT);
                        HarpoonService service = HarpoonService.getInstance(project);
                        if (service != null) {
                            service.removeFile((int) table1.getModel().getValueAt(table1.getSelectedRow(), 0) - 1);
                        }
                    });
                }
            }
        });
        sortUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table1.getSelectedRow() >= 0) {
                    DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(context -> {
                        var project = context.getData(CommonDataKeys.PROJECT);
                        HarpoonService service = HarpoonService.getInstance(project);
                        if (service != null) {
                            var index = (int) table1.getModel().getValueAt(table1.getSelectedRow(), 0);
                            service.changeSorting(index - 1, -1);
                        }
                    });
                }
            }
        });
        sortDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table1.getSelectedRow() >= 0) {
                    AtomicReference<@Nullable Project> project = new AtomicReference<>();
                    DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(context -> {
                        project.set(context.getData(CommonDataKeys.PROJECT));
                        HarpoonService service = HarpoonService.getInstance((Project) project.get());
                        if (service != null) {
                            try {
                                var index = (int) table1.getModel().getValueAt(table1.getSelectedRow(), 0);
                                service.changeSorting(index - 1, 1);
                            } catch (Exception ex){}
                        }
                    });
                }
            }
        });
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                AtomicReference<@Nullable Project> project = new AtomicReference<>();
                DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(context -> {
                    project.set(context.getData(CommonDataKeys.PROJECT));
                    HarpoonService service = HarpoonService.getInstance((Project) project.get());
                    if (service != null) {
                        try {
                            var index = (int) table1.getModel().getValueAt(table1.getSelectedRow(), 0);
                            service.openFile((Project) project.get(), index - 1);
                        } catch (Exception ex){}
                    }
                });
            }
        });
    }

    private void createUIComponents() {
        this.getTable();
    }

    private void getTable() {
        String[] columnNames = {"Index", "File"};
        DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(context -> {
            var project = context.getData(CommonDataKeys.PROJECT);
            HarpoonService service = HarpoonService.getInstance(project);
            var tableModel = new DefaultTableModel(columnNames, 0);
            for (int i = 0; i < service.stateValue.size(); i++) {
                var filename = service.stateValue.get(i);
                filename = filename.replace(project.getBasePath() + "/", "");
                var row = new Object[]{i + 1, filename};
                tableModel.insertRow(i, row);
            }
            table1 = new JBTable(tableModel);
            table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table1.getColumnModel().getColumn(0).setPreferredWidth(1);
            table1.getColumnModel().getColumn(1).setPreferredWidth(400);
            table1.doLayout();
        });
    }

    public void update() {
        this.getTable();
    }

    public JPanel getContent() {
        DataManager.getInstance().getDataContextFromFocusAsync().onSuccess(context -> {
            var project = context.getData(CommonDataKeys.PROJECT);
            var service = HarpoonService.getInstance(project);
            table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            if (table1.getRowCount() > service.currentIndex){
                table1.setRowSelectionInterval(service.currentIndex, service.currentIndex);
            }
        });

        return harpoonWindowContent;
    }
}

