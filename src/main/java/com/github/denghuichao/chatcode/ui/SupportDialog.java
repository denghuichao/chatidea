package com.github.denghuichao.chatcode.ui;

import com.github.denghuichao.chatcode.icons.ChatCodeIcons;
import com.intellij.CommonBundle;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.ActionLink;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

@SuppressWarnings("all")
public class SupportDialog extends DialogWrapper {

    private JPanel panel;

    public SupportDialog(@Nullable Project project) {
        super(project);
        setTitle("Support / Donate");
        setResizable(false);
        setOKButtonText("Thanks for Your Supporting!");
        init();
        setOKActionEnabled(true);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        panel = new JPanel();
        panel.setLayout(new VerticalLayout(JBUIScale.scale(8)));
        panel.setBorder(JBUI.Borders.empty(20));
        panel.setBackground(UIManager.getColor("TextArea.background"));

        panel.add(new JBLabel("You can support or contribute or this project through the following ways:"));
        panel.add(createSharePanel());
        panel.add(new JBLabel(
                "If this plugin help you, feel free to buy me a coffee."));
        panel.add(createDonatePanel());

        panel.add(new JBLabel(
                "Or just donate to encorage me to make this plugin better."));
        panel.add(createSupportPanel());
        return panel;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return panel;
    }

    @Override
    protected @NotNull DialogStyle getStyle() {
        return DialogStyle.COMPACT;
    }

    @Override
    protected Action @NotNull [] createActions() {
        myOKAction = new DialogWrapperAction(CommonBundle.getOkButtonText()) {
            @Override
            protected void doAction(ActionEvent e) {
                dispose();
                close(OK_EXIT_CODE);
            }
        };
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(getOKAction());
        return actions.toArray(new Action[0]);
    }

    private JPanel createSharePanel() {
        JPanel jPanel = new NonOpaquePanel();
        jPanel.setLayout(new GridLayout(5, 1));
        jPanel.add(createActionLink("1. Star this project on GitHub", "https://github.com/denghuichao/chatcode"));
        jPanel.add(createActionLink("2. Report bugs", "https://github.com/denghuichao/chatcode/issues"));
        jPanel.add(createActionLink("3. Tell me your ideas", "https://github.com/denghuichao/chatcode/discussions"));
        jPanel.add(createActionLink("4. Create pull requests", "https://github.com/denghuichao/chatcode"));
        jPanel.add(createActionLink("5. Share this plugin with you friends", "https://github.com/denghuichao/chatcode"));
        return jPanel;
    }

    private JPanel createDonatePanel() {
        JPanel jPanel = new NonOpaquePanel();
        jPanel.setLayout(new GridLayout(1, 1));
        jPanel.add(createActionLink("Buy me a coffee", "https://www.buymeacoffee.com/denghuichao"));
        return jPanel;
    }

    private ActionLink createActionLink(String text, String url) {
        ActionLink actionLink = new ActionLink(text);
        actionLink.addActionListener(e -> BrowserUtil.browse(url));
        return actionLink;
    }

    private JPanel createSupportPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));

        JPanel alipay = new JPanel(new BorderLayout());
        alipay.add(new JLabel(ChatCodeIcons.ALIPAY), BorderLayout.CENTER);
        JLabel alipayLabel = new JLabel("Alipay", SwingConstants.CENTER);
        alipayLabel.setBorder(JBUI.Borders.empty(10, 0));
        alipay.add(alipayLabel, BorderLayout.SOUTH);
        alipay.setBorder(JBUI.Borders.empty(20));

        JPanel wechat = new JPanel(new BorderLayout());
        wechat.add(new JLabel(ChatCodeIcons.WE_CHAT), BorderLayout.CENTER);
        JLabel weChatPayLabel = new JLabel("WeChat Pay", SwingConstants.CENTER);
        weChatPayLabel.setBorder(JBUI.Borders.empty(10, 0));
        wechat.add(weChatPayLabel, BorderLayout.SOUTH);
        wechat.setBorder(JBUI.Borders.empty(20));

        panel.add(alipay);
        panel.add(wechat);

        return panel;
    }
}
