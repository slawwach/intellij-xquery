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

import com.intellij.execution.Location;
import com.intellij.execution.RunManagerEx;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.configurations.ModuleBasedConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.junit.RuntimeConfigurationProducer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.intellij.xquery.psi.XQueryFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: ligasgr
 * Date: 04/08/13
 * Time: 18:57
 */
public class XQueryRunConfigurationProducer extends RuntimeConfigurationProducer implements Cloneable {
    private XQueryFile containingFile;

    public XQueryRunConfigurationProducer() {
        super(XQueryRunConfigurationType.getInstance());
    }

    @Override
    public PsiElement getSourceElement() {
        return containingFile;
    }

    @Nullable
    @Override
    protected RunnerAndConfigurationSettings createConfigurationByElement(Location location, ConfigurationContext
            context) {
        PsiElement psiElement = location.getPsiElement();
        PsiFile psiFile = psiElement.getContainingFile();

        if (isUnsupportedFile(psiFile)) return null;
        containingFile = (XQueryFile) psiFile;
        if (containingFile.isLibraryModule()) return null;
        final VirtualFile vFile = containingFile.getVirtualFile();
        if (vFile == null) return null;
        RunnerAndConfigurationSettings settings = prepareSettings(context, psiElement.getProject(), vFile);
        return settings;
    }

    private boolean isUnsupportedFile(PsiFile psiFile) {
        return !(psiFile instanceof XQueryFile);
    }

    private RunnerAndConfigurationSettings prepareSettings(ConfigurationContext context, Project project, VirtualFile vFile) {
        RunnerAndConfigurationSettings settings = cloneTemplateConfiguration(project, context);
        XQueryRunConfiguration configuration = (XQueryRunConfiguration) settings.getConfiguration();
        configuration.MAIN_FILE_NAME = vFile.getCanonicalPath();
        configuration.setName(vFile.getNameWithoutExtension());
        setupConfigurationModule(context, configuration);
        settings.setEditBeforeRun(true);
        return settings;
    }

    @Override
    public int compareTo(Object o) {
        return PREFERED;
    }

    private void setupConfigurationModule(@Nullable ConfigurationContext context, ModuleBasedConfiguration configuration) {
        if (context != null) {
            final RunnerAndConfigurationSettings template =
                    ((RunManagerImpl) context.getRunManager()).getConfigurationTemplate(getConfigurationFactory());
            final Module contextModule = context.getModule();
            final Module predefinedModule = ((ModuleBasedConfiguration) template.getConfiguration()).getConfigurationModule().getModule();
            if (predefinedModule != null) {
                configuration.setModule(predefinedModule);
                return;
            }
            final Module module = findModule(configuration, contextModule);
            if (module != null) {
                configuration.setModule(module);
                return;
            }
        }
        return;
    }

    protected Module findModule(ModuleBasedConfiguration configuration, Module contextModule) {
        if (configuration.getConfigurationModule().getModule() == null && contextModule != null) {
            return contextModule;
        }
        return null;
    }

    @Override
    protected RunnerAndConfigurationSettings findExistingByElement(Location location,
                                                                   @NotNull RunnerAndConfigurationSettings[] existingConfigurations,
                                                                   ConfigurationContext context) {
        PsiFile psiFile = location.getPsiElement().getContainingFile();
        if (!(psiFile instanceof XQueryFile)) return null;
        if (((XQueryFile) psiFile).isLibraryModule()) {
            return null;
        }
        final Module predefinedModule =
                ((XQueryRunConfiguration) ((RunManagerImpl) RunManagerEx.getInstanceEx(location.getProject()))
                        .getConfigurationTemplate(getConfigurationFactory())
                        .getConfiguration()).getConfigurationModule().getModule();
        for (RunnerAndConfigurationSettings existingConfiguration : existingConfigurations) {
            final XQueryRunConfiguration appConfiguration = (XQueryRunConfiguration) existingConfiguration.getConfiguration();
            if (Comparing.equal(psiFile.getVirtualFile().getCanonicalPath(), appConfiguration.MAIN_FILE_NAME)) {
                final Module configurationModule = appConfiguration.getConfigurationModule().getModule();
                if (Comparing.equal(location.getModule(), configurationModule)) {
                    return existingConfiguration;
                }
                if (Comparing.equal(predefinedModule, configurationModule)) {
                    return existingConfiguration;
                }
            }
        }
        return null;
    }
}
