/*
 * Copyright 2013 Grzegorz Ligas <ligasgr@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.intellij.xquery.runner;

import com.intellij.execution.ui.AlternativeJREPanel;
import com.intellij.execution.ui.CommonJavaParametersPanel;
import com.intellij.execution.ui.ConfigurationModuleSelector;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.ui.PanelWithAnchor;
import com.intellij.util.ui.UIUtil;
import org.intellij.xquery.runner.ui.XQueryEditorTextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: ligasgr
 * Date: 04/08/13
 * Time: 16:39
 */
public class XQueryMainModuleRunConfigurationEditorForm extends SettingsEditor<XQueryRunConfiguration> implements PanelWithAnchor {

    private CommonJavaParametersPanel myCommonProgramParameters;
    private LabeledComponent<XQueryEditorTextFieldWithBrowseButton> myMainFile;
    private LabeledComponent<JComboBox> myModule;
    private JPanel myWholePanel;

    private final ConfigurationModuleSelector myModuleSelector;
    private AlternativeJREPanel myAlternativeJREPanel;
    private final Project myProject;
    private JComponent myAnchor;

    public XQueryMainModuleRunConfigurationEditorForm(final Project project) {
        myProject = project;
        myModuleSelector = new ConfigurationModuleSelector(project, myModule.getComponent());
        myCommonProgramParameters.setModuleContext(myModuleSelector.getModule());
        myModule.getComponent().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myCommonProgramParameters.setModuleContext(myModuleSelector.getModule());
            }
        });

        myAnchor = UIUtil.mergeComponentsWithAnchor(myMainFile, myCommonProgramParameters, myAlternativeJREPanel, myModule);
    }

    @Override
    protected void resetEditorFrom(XQueryRunConfiguration configuration) {
        myCommonProgramParameters.reset(configuration);
        myModuleSelector.reset(configuration);
        getMainFileField().setText(configuration.MAIN_FILE_NAME);
        myAlternativeJREPanel.init(configuration.ALTERNATIVE_JRE_PATH, configuration.ALTERNATIVE_JRE_PATH_ENABLED);
    }

    @Override
    protected void applyEditorTo(XQueryRunConfiguration configuration) throws ConfigurationException {
        myCommonProgramParameters.applyTo(configuration);
        myModuleSelector.applyTo(configuration);
        configuration.MAIN_FILE_NAME = getMainFileField().getText();
        configuration.ALTERNATIVE_JRE_PATH = myAlternativeJREPanel.getPath();
        configuration.ALTERNATIVE_JRE_PATH_ENABLED = myAlternativeJREPanel.isPathEnabled();
    }

    public XQueryEditorTextFieldWithBrowseButton getMainFileField() {
        return myMainFile.getComponent();
    }

    public CommonJavaParametersPanel getCommonProgramParameters() {
        return myCommonProgramParameters;
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myWholePanel;
    }

    @Override
    protected void disposeEditor() {

    }

    @Override
    public JComponent getAnchor() {
        return myAnchor;
    }

    @Override
    public void setAnchor(@Nullable JComponent anchor) {
        this.myAnchor = anchor;
        myMainFile.setAnchor(anchor);
        myCommonProgramParameters.setAnchor(anchor);
        myAlternativeJREPanel.setAnchor(anchor);
        myModule.setAnchor(anchor);
    }

    private void createUIComponents() {
        myMainFile = new LabeledComponent<XQueryEditorTextFieldWithBrowseButton>();
        myMainFile.setComponent(new XQueryEditorTextFieldWithBrowseButton(myProject));
    }
}
