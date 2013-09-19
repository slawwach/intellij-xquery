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

import com.intellij.execution.CantRunException;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.JavaCommandLineState;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JdkUtil;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.ex.PathUtilEx;

import java.io.File;

/**
 * User: ligasgr
 * Date: 04/08/13
 * Time: 22:40
 */
public class XQueryRunProfileState extends JavaCommandLineState {

    private XQueryRunConfiguration myConfiguration;

    protected XQueryRunProfileState(ExecutionEnvironment environment, XQueryRunConfiguration runConfiguration) {
        super(environment);
        myConfiguration = runConfiguration;
    }

    @Override
    protected JavaParameters createJavaParameters() throws ExecutionException {
        final JavaParameters parameters = new JavaParameters();
        final RunConfigurationModule module = myConfiguration.getConfigurationModule();
        if (module == null) throw CantRunException.noModuleConfigured(module.getModuleName());

        Project project = module.getProject();
        Sdk sdk = PathUtilEx.getAnyJdk(project);
        if (sdk == null)
            throw CantRunException.noJdkConfigured();
        parameters.setJdk(sdk);
        parameters.setMainClass(myConfiguration.getRunClass());
        parameters.getProgramParametersList().add(myConfiguration.MAIN_FILE_NAME);
        parameters.getClassPath().addTail(getRtJarPath());
        parameters.setWorkingDirectory(new File(myConfiguration.WORKING_DIRECTORY).getParentFile().getAbsolutePath());
        parameters.setUseDynamicClasspath(JdkUtil.useDynamicClasspath(myConfiguration.getProject()));

        if (module.getModule() != null)
            parameters.configureByModule(module.getModule(), JavaParameters.CLASSES_ONLY, sdk);
        else
            parameters.configureByProject(module.getProject(), JavaParameters.CLASSES_ONLY, sdk);

        return parameters;
    }

    private String getRtJarPath() throws CantRunException {
        final PluginId pluginId = PluginManager.getPluginByClassName(getClass().getName());
        assert pluginId != null;
        final IdeaPluginDescriptor descriptor = PluginManager.getPlugin(pluginId);
        assert descriptor != null;

        File pluginPath = descriptor.getPath();
        final char c = File.separatorChar;
        File rtClasspath = new File(pluginPath, "lib" + c + "intellij-xquery-rt.jar");
        if (!rtClasspath.exists()) {
            if (!(rtClasspath = new File(pluginPath, "classes")).exists()) {
                if (ApplicationManagerEx.getApplicationEx().isInternal() && new File(pluginPath, "org").exists()) {
                    rtClasspath = pluginPath;
                } else {
                    throw new CantRunException("Runtime classes not found");
                }
            }
        }
        return rtClasspath.getAbsolutePath();
    }
}
