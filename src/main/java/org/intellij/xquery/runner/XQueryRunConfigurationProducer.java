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
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.junit.RuntimeConfigurationProducer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.intellij.xquery.psi.XQueryFile;
import org.jetbrains.annotations.Nullable;

/**
 * User: ligasgr
 * Date: 04/08/13
 * Time: 18:57
 */
public class XQueryRunConfigurationProducer extends RuntimeConfigurationProducer {
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
        configuration.setMainModuleFilename(vFile.getCanonicalPath());
        configuration.setName(vFile.getNameWithoutExtension());
        settings.setEditBeforeRun(true);
        return settings;
    }

    @Override
    public int compareTo(Object o) {
        return PREFERED;
    }
}
