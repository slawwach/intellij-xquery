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

import com.intellij.execution.CommonJavaRunConfigurationParameters;
import com.intellij.execution.ExecutionBundle;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.options.SettingsEditorGroup;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: ligasgr
 * Date: 04/08/13
 * Time: 14:56
 */
public class XQueryRunConfiguration extends ModuleBasedConfiguration<XQueryModuleBasedConfiguration> implements
        CommonJavaRunConfigurationParameters, RunConfigurationWithSuppressedDefaultDebugAction {

    public String MAIN_FILE_NAME;
    public String VM_PARAMETERS;
    public String PROGRAM_PARAMETERS;
    public String WORKING_DIRECTORY;
    public boolean ALTERNATIVE_JRE_PATH_ENABLED;
    public String ALTERNATIVE_JRE_PATH;

    public String ENV_VARIABLES;
    private Map<String, String> myEnvs = new LinkedHashMap<String, String>();
    public boolean PASS_PARENT_ENVS = true;

    public XQueryRunConfiguration(String name, XQueryModuleBasedConfiguration configurationModule, ConfigurationFactory factory) {
        super(name, configurationModule, factory);
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
        SettingsEditorGroup<XQueryRunConfiguration> group = new SettingsEditorGroup<XQueryRunConfiguration>();
        group.addEditor("Configuration", new XQueryMainModuleRunConfigurationEditorForm(getProject()));
        return group;
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
        XQueryRunProfileState state = new XQueryRunProfileState(env, (XQueryRunConfiguration) env.getRunnerAndConfigurationSettings().getConfiguration());
        XQueryModuleBasedConfiguration module = getConfigurationModule();
        state.setConsoleBuilder(TextConsoleBuilderFactory.getInstance().createBuilder(getProject(), module.getSearchScope()));
        return state;
    }

    @Override
    public void setVMParameters(String value) {
        VM_PARAMETERS = value;
    }

    @Override
    public String getVMParameters() {
        return VM_PARAMETERS;
    }

    @Override
    public boolean isAlternativeJrePathEnabled() {
        return ALTERNATIVE_JRE_PATH_ENABLED;
    }

    @Override
    public void setAlternativeJrePathEnabled(boolean enabled) {
        ALTERNATIVE_JRE_PATH_ENABLED = enabled;
    }

    @Override
    public String getAlternativeJrePath() {
        return ALTERNATIVE_JRE_PATH;
    }

    @Override
    public void setAlternativeJrePath(String path) {
        ALTERNATIVE_JRE_PATH = path;
    }

    @Nullable
    @Override
    public String getRunClass() {
        return "org.intellij.xquery.runner.xqj.XQJRunner";
    }

    @Nullable
    @Override
    public String getPackage() {
        return null;
    }

    @Override
    public void setProgramParameters(@Nullable String value) {
        PROGRAM_PARAMETERS = value;
    }

    @Nullable
    @Override
    public String getProgramParameters() {
        return PROGRAM_PARAMETERS;
    }

    @Override
    public void setWorkingDirectory(@Nullable String value) {
        WORKING_DIRECTORY = value;
    }

    @Nullable
    @Override
    public String getWorkingDirectory() {
        return WORKING_DIRECTORY;
    }

    public void setPassParentEnvs(boolean passParentEnvs) {
        PASS_PARENT_ENVS = passParentEnvs;
    }

    @NotNull
    public Map<String, String> getEnvs() {
        return myEnvs;
    }

    public void setEnvs(@NotNull final Map<String, String> envs) {
        myEnvs.clear();
        myEnvs.putAll(envs);
    }

    public boolean isPassParentEnvs() {
        return PASS_PARENT_ENVS;
    }
}
