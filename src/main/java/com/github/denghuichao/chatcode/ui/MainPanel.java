package com.github.denghuichao.chatcode.ui;

import com.github.denghuichao.chatcode.message.ChatCodeBundle;
import com.github.denghuichao.chatcode.settings.OpenAISettingsState;
import com.github.denghuichao.chatcode.ui.listener.SendListener;
import com.intellij.find.SearchTextArea;
import com.intellij.icons.AllIcons;
import com.intellij.ide.ui.laf.darcula.ui.DarculaButtonUI;
import com.intellij.openapi.project.Project;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.components.JBTextArea;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.util.IconLoader;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainPanel {

    private final SearchTextArea searchTextArea;
    private final JButton button;
    private final JButton stopGenerating;
    private final MessageGroupComponent contentPanel;
    private final JProgressBar progressBar;
    private final OnePixelSplitter splitter;
    private final Project myProject;
    private JPanel actionPanel;
    private ExecutorService executorService;

    public MainPanel(@NotNull Project project) {
        myProject = project;
        SendListener listener = new SendListener(this);

        splitter = new OnePixelSplitter(true, .98f);
        splitter.setDividerWidth(2);

        searchTextArea = new SearchTextArea(new JBTextArea(), true);
        searchTextArea.getTextArea().addKeyListener(listener);
        searchTextArea.setMinimumSize(new Dimension(searchTextArea.getWidth(), 500));
        searchTextArea.setMultilineEnabled(OpenAISettingsState.getInstance().enableLineWarp);
        button = new JButton(ChatCodeBundle.message("ui.toolwindow.send"),
                IconLoader.getIcon("/icons/send.svg", MainPanel.class));
        button.addActionListener(listener);
        button.setUI(new DarculaButtonUI());

        stopGenerating = new JButton("Stop", AllIcons.Actions.Suspend);
        stopGenerating.addActionListener(e -> {
            executorService.shutdownNow();
            aroundRequest(false);
        });
        stopGenerating.setUI(new DarculaButtonUI());

        actionPanel = new JPanel(new BorderLayout());
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        actionPanel.add(progressBar, BorderLayout.NORTH);
        actionPanel.add(searchTextArea, BorderLayout.CENTER);
        actionPanel.add(button, BorderLayout.EAST);
        contentPanel = new MessageGroupComponent(project);

        splitter.setFirstComponent(contentPanel);
        splitter.setSecondComponent(actionPanel);
    }

    public Project getProject() {
        return myProject;
    }

    public SearchTextArea getSearchTextArea() {
        return searchTextArea;
    }

    public MessageGroupComponent getContentPanel() {
        return contentPanel;
    }

    public JPanel init() {
        return splitter;
    }

    public JButton getButton() {
        return button;
    }

    public ExecutorService getExecutorService() {
        executorService = Executors.newFixedThreadPool(1);
        return executorService;
    }

    public void aroundRequest(boolean status) {
        progressBar.setIndeterminate(status);
        progressBar.setVisible(status);
        button.setEnabled(!status);
        if (status) {
            contentPanel.addScrollListener();
            actionPanel.remove(button);
            actionPanel.add(stopGenerating, BorderLayout.EAST);
        } else {
            contentPanel.removeScrollListener();
            actionPanel.remove(stopGenerating);
            actionPanel.add(button, BorderLayout.EAST);
        }
        actionPanel.updateUI();
    }
}
