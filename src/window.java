import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class window implements ToolWindowFactory {

    private ToolWindow toolWindow;
    private JPanel panel1;
    private JTextArea textArea1;
    private JButton saveButton;
    private JButton newButton;
    private JButton clearButton;
    private JScrollPane scrollPane;
    private JEditorPane editorPane1;
    private JButton prevButton;
    private JButton nextButton;
    public static final String FILE_PATH = "D:\\xTemp.txt";

    public window() {
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea1.setText("");
            }
        });

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String findContent = editorPane1.getText();
                if (!findContent.isEmpty()) {
                    int caretPosition = textArea1.getCaretPosition();
                    String selectedText = textArea1.getSelectedText();
                    int length = findContent.length();

                    String text = textArea1.getText().substring(0, caretPosition - length);
                    int position = text.lastIndexOf(findContent);
                    System.out.println("Prev:" + findContent + ",caret:" + caretPosition + ",selectTx:" + selectedText + ",position:" + position);
                    if (position >= 0) {
                        textArea1.requestFocus();
                        textArea1.select(position, position + length);
                    }
                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String findContent = editorPane1.getText();
                if (!findContent.isEmpty()) {
                    int caretPosition = textArea1.getCaretPosition();
                    String selectedText = textArea1.getSelectedText();
                    if (!TextUtils.isEmpty(selectedText)) {
                        caretPosition += selectedText.length();
                    }
                    int position = textArea1.getText().indexOf(findContent, caretPosition);
                    System.out.println("NExt:" + findContent + ",caret:" + caretPosition + ",selectTx:" + selectedText + ",position:" + position);
                    if (position >= 0) {
                        textArea1.requestFocus();
                        textArea1.select(position, position + findContent.length());
                    }
                }
            }
        });
        initPageAction();
    }

    private void initPageAction() {
        final DefaultEditorKit kit = new DefaultEditorKit();
        final JTextArea jta = new JTextArea();
        final Document doc = jta.getDocument();

        final Action[] actions = kit.getActions();
        for (Action action : actions) {
            String name = (String) action.getValue(Action.NAME);
            if (DefaultEditorKit.pageUpAction.equals(name))
                saveButton.addActionListener(action);
            else if (DefaultEditorKit.pageDownAction.equals(name))
                newButton.addActionListener(action);
        }

        try {
            File file = new File(FILE_PATH);
            if (file.exists() || (!file.exists() && file.createNewFile())) {
                kit.read(new FileReader(FILE_PATH), doc, 0);
            }
        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panel1, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
