// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.github.denghuichao.chatcode.settings;

import com.github.denghuichao.chatcode.message.ChatCodeBundle;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.TitledSeparator;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.fields.ExpandableTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;


public class ChatCodeSettingsPanel implements Configurable, Disposable {

    private JPanel myMainPanel;
    private JPanel apiKeyOptions;
    private JPanel apiKeyTitledBorderBox;
    private JBTextField apiHostFiled;

    private JPanel apiHostOptions;
    private JPanel apiHostTiledBorderBox;
    private ExpandableTextField apiKeyField;

    private JPanel modelOptions;
    private JPanel modelTitledBorderBox;
    private JComboBox<String> modelCombobox;
    private JCheckBox enableContextCheckBox;
    private JCheckBox enableAvatarCheckBox;


    public ChatCodeSettingsPanel() {
        init();
    }

    private void init() {
        apiHostFiled.getEmptyText().setText(ChatCodeBundle.message("ui.setting.openai.host.empty_text"));
        apiKeyField.getEmptyText().setText(ChatCodeBundle.message("ui.setting.openai.apikey.empty_text"));
    }

    public ExpandableTextField getApiKeyField() {
        return apiKeyField;
    }

    public JBTextField getApiHostFiled() {
        return apiHostFiled;
    }

    @Override
    public void reset() {
        OpenAISettingsState state = OpenAISettingsState.getInstance();
        apiKeyField.setText(state.apiKey);
        apiHostFiled.setText(state.apiHost);
        modelCombobox.setSelectedItem(state.model);
        enableContextCheckBox.setSelected(state.enableContext);
        enableAvatarCheckBox.setSelected(state.enableAvatar);
    }

    @Override
    public @Nullable JComponent createComponent() {
        return myMainPanel;
    }

    @Override
    public boolean isModified() {
        OpenAISettingsState state = OpenAISettingsState.getInstance();

        return !StringUtil.equals(state.apiHost, apiHostFiled.getText()) ||
                !StringUtil.equals(state.apiKey, apiKeyField.getText()) ||
                !state.model.equals(modelCombobox.getSelectedItem()) ||
                state.enableContext != enableContextCheckBox.isSelected() ||
                state.enableAvatar != enableAvatarCheckBox.isSelected();
    }

    @Override
    public void apply() {
        OpenAISettingsState state = OpenAISettingsState.getInstance();
        state.apiHost = apiHostFiled.getText();
        state.apiKey = apiKeyField.getText();
        state.model = modelCombobox.getSelectedItem().toString();
        state.enableContext = enableContextCheckBox.isSelected();
        state.enableAvatar = enableAvatarCheckBox.isSelected();
    }

    @Override
    public void dispose() {
    }


    @Override
    public String getDisplayName() {
        return ChatCodeBundle.message("ui.setting.menu.text");
    }

    private void createUIComponents() {
        apiKeyTitledBorderBox = new JPanel(new BorderLayout());
        TitledSeparator tsKey = new TitledSeparator("API Key Settings");
        apiKeyTitledBorderBox.add(tsKey, BorderLayout.CENTER);

        apiHostTiledBorderBox = new JPanel(new BorderLayout());
        TitledSeparator mdHost = new TitledSeparator("API Host Settings");
        apiHostTiledBorderBox.add(mdHost, BorderLayout.CENTER);

        modelTitledBorderBox = new JPanel(new BorderLayout());
        TitledSeparator mdModel = new TitledSeparator("Model Settings");
        modelTitledBorderBox.add(mdModel, BorderLayout.CENTER);

    }
}
