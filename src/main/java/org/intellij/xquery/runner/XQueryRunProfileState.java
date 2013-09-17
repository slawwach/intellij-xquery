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
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * User: ligasgr
 * Date: 04/08/13
 * Time: 22:40
 */
public class XQueryRunProfileState extends CommandLineState {

    protected XQueryRunProfileState(ExecutionEnvironment environment) {
        super(environment);
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        GeneralCommandLine commandLine = null;
        try {
            commandLine = getCommand();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return new OSProcessHandler(commandLine.createProcess(), commandLine.getCommandLineString());
    }

    private GeneralCommandLine getCommand() throws IOException, URISyntaxException, CantRunException {
        XQueryRunConfiguration configuration = (XQueryRunConfiguration) getEnvironment()
                .getRunnerAndConfigurationSettings().getConfiguration();
        String filename = configuration.getMainModuleFilename();
        String directory = new File(filename).getParent();
        String java = FileUtil.toSystemDependentName("java");

        final GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.setWorkDirectory(directory);
        commandLine.setExePath(java);
        commandLine.addParameters("-cp");
//        commandLine.addParameters("/opt/dev/marklogic/*:"+getJarPath());
        commandLine.addParameters("D:/dev/lib/saxon/*" + File.pathSeparator + getJarPath());
        commandLine.addParameters("org.intellij.xquery.runner.xqj.XQJRunner");
        commandLine.addParameters(filename);

        final TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder
                (getEnvironment().getProject());
        setConsoleBuilder(consoleBuilder);
        return commandLine;
    }

    private String getJarPath() throws URISyntaxException, IOException, CantRunException {
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
