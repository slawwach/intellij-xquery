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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: ligasgr
 * Date: 04/08/13
 * Time: 14:56
 */
public class XQueryRunConfiguration extends RunConfigurationBase implements
        RunConfigurationWithSuppressedDefaultDebugAction, LocatableConfiguration {

    private static final String UNNAMED_NAME = "unnamed";
    private String mainModuleFilename;

    public XQueryRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new XQueryMainModuleRunConfigurationEditorForm();
    }

    @Nullable
    @Override
    public JDOMExternalizable createRunnerSettings(ConfigurationInfoProvider provider) {
        return null;
    }

    @Nullable
    @Override
    public SettingsEditor<JDOMExternalizable> getRunnerSettingsEditor(ProgramRunner runner) {
        return null;
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws
            ExecutionException {
        return new XQueryRunProfileState(env);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
    }

    public String getMainModuleFilename() {
        return mainModuleFilename;
    }

    public void setMainModuleFilename(String mainModuleFilename) {
        this.mainModuleFilename = mainModuleFilename;
    }


    public void writeExternal(final Element element) throws WriteExternalException {
        super.writeExternal(element);
        XmlSerializer.serializeInto(this, element);
    }

    public void readExternal(final Element element) throws InvalidDataException {
        super.readExternal(element);
        XmlSerializer.deserializeInto(this, element);
    }

    @Override
    public boolean isGeneratedName() {
        return UNNAMED_NAME.equals(getName());
    }

    @Override
    public String suggestedName() {
        return UNNAMED_NAME;
    }
}
