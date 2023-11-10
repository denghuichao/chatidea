package com.github.denghuichao.chatcode.ui.action.editor;

import com.github.denghuichao.chatcode.ui.action.SendAction;
import com.github.denghuichao.chatcode.ui.MainPanel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static com.github.denghuichao.chatcode.ChatCodeToolWindowFactory.ACTIVE_CONTENT;


public abstract class AbstractEditorAction extends AnAction {

    private static final String messageWithCodeTemplate = "%s  \n" +
            "```\n" +
            "%s\n" +
            "```";

    protected String text = "";
    protected String key = "";

    public AbstractEditorAction(@NotNull Supplier<@NlsActions.ActionText String> dynamicText) {
        super(dynamicText);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        doActionPerformed(e);
    }

    public void doActionPerformed(@NotNull AnActionEvent e) {

        // Check the toolWindow is active
        ToolWindow chatCode = ToolWindowManager.getInstance(e.getProject()).getToolWindow("ChatCode");
        assert chatCode != null;
        if (!chatCode.isActive()) {
            chatCode.activate(null);
        }

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        assert editor != null;

        String selectedText = editor.getSelectionModel().getSelectedText();

        Document document = editor.getDocument();
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);

        String fileFormat = "";
        if (virtualFile != null) {
            FileType fileType = virtualFile.getFileType();
            fileFormat = fileType.getDisplayName();
        }

        String apiText = String.format(messageWithCodeTemplate, key, selectedText);

        SendAction sendAction = e.getProject().getService(SendAction.class);
        Object mainPanel = e.getProject().getUserData(ACTIVE_CONTENT);

        sendAction.doActionPerformed((MainPanel) mainPanel, apiText, fileFormat);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        assert editor != null;
        boolean hasSelection = editor.getSelectionModel().hasSelection();
        e.getPresentation().setEnabledAndVisible(hasSelection);
    }
}
