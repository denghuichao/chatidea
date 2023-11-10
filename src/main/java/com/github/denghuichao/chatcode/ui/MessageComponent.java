package com.github.denghuichao.chatcode.ui;

import cn.hutool.core.img.ImgUtil;
import com.github.denghuichao.chatcode.icons.ChatCodeIcons;
import com.github.denghuichao.chatcode.settings.OpenAISettingsState;
import com.github.denghuichao.chatcode.util.ColorUtils;
import com.github.denghuichao.chatcode.util.HtmlUtils;
import com.github.denghuichao.chatcode.util.ImgUtils;
import com.github.denghuichao.chatcode.util.StringUtil;
import com.intellij.notification.impl.ui.NotificationsUtil;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.EditorColorsUtil;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.ui.JBUI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.accessibility.AccessibleContext;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class MessageComponent extends JBPanel<MessageComponent> {

    private static final Logger LOG = LoggerFactory.getLogger(MessageComponent.class);

    private JEditorPane component;

    private String question;
    private List<String> answers = new ArrayList<>();
    private String answer;
    private String fileFormat;

    public MessageComponent(String content, boolean me, String fileFormat) {
        this.component = new JEditorPane();
        this.question = content;
        this.fileFormat = fileFormat;

        Font editorFont = EditorFontType.getGlobalPlainFont();
        component.setFont(editorFont);
        setDoubleBuffered(true);

        EditorColorsScheme colorsScheme = EditorColorsUtil.getGlobalOrDefaultColorScheme();
        Color background = colorsScheme.getDefaultBackground();
        Color[] colors = ColorUtils.calculateMessageBackground(background);
        setBackground(me ? colors[0] : colors[1]);
        setBorder(JBUI.Borders.empty(10, 10, 10, 0));
        setLayout(new BorderLayout(JBUI.scale(7), 0));

        if (OpenAISettingsState.getInstance().enableAvatar) {
            JPanel iconPanel = new JPanel(new BorderLayout());
            iconPanel.setOpaque(false);
            Image imageIcon = me ? ImgUtils.iconToImage(ChatCodeIcons.ME) : ImgUtils.iconToImage(ChatCodeIcons.OPEN_AI);
            Image scale = ImgUtil.scale(imageIcon, 30, 30);
            iconPanel.add(new JBLabel(new ImageIcon(scale)), BorderLayout.NORTH);
            add(iconPanel, BorderLayout.WEST);
        }

        JPanel centerPanel = new JPanel(new VerticalLayout(JBUI.scale(8)));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(JBUI.Borders.emptyRight(10));
        centerPanel.add(createContentComponent(content, fileFormat));
        add(centerPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);
        actionPanel.setBorder(JBUI.Borders.emptyRight(10));
        add(actionPanel, BorderLayout.EAST);


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 6, 6));
        g2.dispose();
        setOpaque(false);
    }

    public Component createContentComponent(String content, String fileFormat) {
        component.setEditable(false);
        component.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        component.setOpaque(false);
        component.setBorder(null);

        NotificationsUtil.configureHtmlEditorKit(component, false);
        component.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY,
                StringUtil.unescapeXmlEntities(StringUtil.stripHtml(content, " ")));

        component.setText(StringUtils.isBlank(fileFormat) ? HtmlUtils.addLineBreaks(content) : HtmlUtils.md2Html(content));

        component.setEditable(false);
        if (component.getCaret() != null) {
            component.setCaretPosition(0);
        }

        component.revalidate();
        component.repaint();
        return component;
    }

    public void setContent(String content) {
        new MessageWorker(content).execute();
    }

    public void setSourceContent(String source) {
        answer = source;
    }

    public void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            Rectangle bounds = getBounds();
            scrollRectToVisible(bounds);
        });
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String prevAnswers() {
        StringBuilder result = new StringBuilder();
        for (String s : answers) {
            result.append(s);
        }
        return result.toString();
    }

    class MessageWorker extends SwingWorker<Void, String> {
        private final String message;

        public MessageWorker(String message) {
            this.message = message;
        }

        @Override
        protected Void doInBackground() throws Exception {
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
                component.setText(HtmlUtils.md2Html(message));
                component.updateUI();
                scrollToBottom();
            } catch (Exception e) {
                LOG.error("ChatGPT Exception in processing response: response:{} error: {}", message, e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
