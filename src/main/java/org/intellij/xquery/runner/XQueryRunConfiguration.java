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
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;

/**
 * User: ligasgr
 * Date: 04/08/13
 * Time: 14:56
 */
public class XQueryRunConfiguration extends ModuleBasedConfiguration<XQueryModuleBasedConfiguration> implements
        RunConfigurationWithSuppressedDefaultDebugAction, LocatableConfiguration {

    private static final String UNNAMED_NAME = "unnamed";
    private String mainModuleFilename;

    public XQueryRunConfiguration(String name, XQueryModuleBasedConfiguration configurationModule, ConfigurationFactory factory) {
        super(name, configurationModule, factory);
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

    @Override
    protected ModuleBasedConfiguration createInstance() {
        return new XQueryRunConfiguration(getName(), new XQueryModuleBasedConfiguration(getProject()), XQueryRunConfigurationType.getInstance().getConfigurationFactories()[0]);
    }

    @Override
    public Collection<Module> getValidModules() {
        Module[] modules = ModuleManager.getInstance(getProject()).getModules();
        return Arrays.asList(modules);
    }

    public void readExternal(final Element element) throws InvalidDataException {
        super.readExternal(element);
        XmlSerializer.deserializeInto(this, element);
    }

    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new XQueryMainModuleRunConfigurationEditorForm();
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
        XQueryRunProfileState state = new XQueryRunProfileState(env, (XQueryRunConfiguration) env.getRunnerAndConfigurationSettings().getConfiguration());
        XQueryModuleBasedConfiguration module = getConfigurationModule();
        state.setConsoleBuilder(TextConsoleBuilderFactory.getInstance().createBuilder(getProject(), module.getSearchScope()));
        return state;
    }
}
