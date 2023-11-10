package com.github.denghuichao.chatcode.util;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MyUIUtil {

    public static void registerKeystrokeFocusForInput(JTextArea myTextArea) {
        myTextArea.setFocusable(true);
        Action focusAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myTextArea.requestFocus();
            }
        };

        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F, 0);
        InputMap inputMap = myTextArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(keyStroke, "focusAction");
        myTextArea.getActionMap().put("focusAction", focusAction);
    }
}
