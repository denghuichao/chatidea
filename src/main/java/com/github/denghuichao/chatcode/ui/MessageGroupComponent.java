package com.github.denghuichao.chatcode.ui;

import com.github.denghuichao.chatcode.util.Constant;
import com.github.denghuichao.chatcode.settings.OpenAISettingsState;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.NullableComponent;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.labels.LinkLabel;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.ui.JBFont;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.jetbrains.annotations.NotNull;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


public class MessageGroupComponent extends JBPanel<MessageGroupComponent> implements NullableComponent {
    private final JPanel myList = new JPanel(new VerticalLayout(JBUI.scale(10)));
    private final MyScrollPane myScrollPane = new MyScrollPane(myList, javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private final MyAdjustmentListener scrollListener = new MyAdjustmentListener();
    private final MessageComponent gpt35TurboModelExplanation =
            new MessageComponent(Constant.getChatGptInfoContent(), false, "md");

    private int myScrollValue = 0;
    private List<ChatMessage> messages = Lists.newArrayList();

    public MessageGroupComponent(@NotNull Project project) {
        setBorder(JBUI.Borders.empty(10, 10, 10, 0));
        setLayout(new BorderLayout(JBUI.scale(7), 0));
        setBackground(UIUtil.getListBackground());

        JPanel mainPanel = new JPanel(new BorderLayout(0, JBUI.scale(8)));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(JBUI.Borders.emptyLeft(8));

        add(mainPanel, BorderLayout.CENTER);

        JBLabel myTitle = new JBLabel("Conversation");
        myTitle.setForeground(JBColor.namedColor("Label.infoForeground", new JBColor(Gray.x80, Gray.x8C)));
        myTitle.setFont(JBFont.label());

        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.setOpaque(false);
        jPanel.setBorder(JBUI.Borders.empty(0, 10, 10, 0));

        jPanel.add(myTitle, BorderLayout.WEST);

        LinkLabel<String> clearContext = new LinkLabel<>("Clear Context", null);
        clearContext.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                myList.removeAll();
                add(gpt35TurboModelExplanation);
                messages = Lists.newArrayList();
            }
        });

        clearContext.setFont(JBFont.label());
        clearContext.setBorder(JBUI.Borders.emptyRight(20));
        jPanel.add(clearContext, BorderLayout.EAST);
        mainPanel.add(jPanel, BorderLayout.NORTH);

        myList.setOpaque(true);
        myList.setBackground(UIUtil.getListBackground());
        myList.setBorder(JBUI.Borders.emptyRight(10));

        myScrollPane.setBorder(JBUI.Borders.empty());
        mainPanel.add(myScrollPane);
        myScrollPane.getVerticalScrollBar().setAutoscrolls(true);
        myScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            int value = e.getValue();
            if (myScrollValue == 0 && value > 0 || myScrollValue > 0 && value == 0) {
                myScrollValue = value;
                repaint();
            } else {
                myScrollValue = value;
            }
        });

        add(gpt35TurboModelExplanation);
    }

    public void add(MessageComponent messageComponent) {
        // The component should be immediately added to the
        // container and displayed in the UI

        // SwingUtilities.invokeLater(() -> {
        myList.add(messageComponent);
        updateLayout();
        scrollToBottom();
        updateUI();
        // });
    }

    public void scrollToBottom() {
        JScrollBar verticalScrollBar = myScrollPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }

    public void updateLayout() {
        LayoutManager layout = myList.getLayout();
        int componentCount = myList.getComponentCount();
        for (int i = 0; i < componentCount; i++) {
            layout.removeLayoutComponent(myList.getComponent(i));
            layout.addLayoutComponent(null, myList.getComponent(i));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (myScrollValue > 0) {
            g.setColor(JBColor.border());
            int y = myScrollPane.getY() - 1;
            g.drawLine(0, y, getWidth(), y);
        }
    }


    @Override
    public boolean isVisible() {
        if (super.isVisible()) {
            int count = myList.getComponentCount();
            for (int i = 0; i < count; i++) {
                if (myList.getComponent(i).isVisible()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isNull() {
        return !isVisible();
    }

    public void addScrollListener() {
        myScrollPane.getVerticalScrollBar().
                addAdjustmentListener(scrollListener);
    }

    public void removeScrollListener() {
        myScrollPane.getVerticalScrollBar().
                removeAdjustmentListener(scrollListener);
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public String getSystemRole() {
        return OpenAISettingsState.getInstance().roleText;
    }


    static class MyAdjustmentListener implements AdjustmentListener {

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
            JScrollBar source = (JScrollBar) e.getSource();
            if (!source.getValueIsAdjusting()) {
                source.setValue(source.getMaximum());
            }
        }
    }
}
