package org.intellij.xquery.runner.ui;

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.TextAccessor;
import org.intellij.xquery.XQueryFileType;
import org.jetbrains.annotations.Nullable;

import java.awt.event.ActionListener;

/**
 * User: ligasgr
 * Date: 19/09/13
 * Time: 16:27
 */
public class XQueryEditorTextFieldWithBrowseButton extends ComponentWithBrowseButton<EditorTextField> implements TextAccessor {
    public XQueryEditorTextFieldWithBrowseButton(Project project) {
        super(createEditorTextField(project), null);
    }

    private static EditorTextField createEditorTextField(Project project) {
        return new EditorTextField("", project, XQueryFileType.INSTANCE);
    }

    @Override
    public void setText(String text) {
        if (text == null) text = "";
        getChildComponent().setText(text);
    }

    @Override
    public String getText() {
        return getChildComponent().getText();
    }
}
