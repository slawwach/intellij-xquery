package org.intellij.xquery.runner;

import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * User: ligasgr
 * Date: 18/09/13
 * Time: 16:22
 */
public class XQueryModuleBasedConfiguration extends RunConfigurationModule {
    public XQueryModuleBasedConfiguration(Project project) {
        super(project);
    }

    public GlobalSearchScope getSearchScope() {
        final Module module = getModule();
        if (module != null) {
            return GlobalSearchScope.moduleWithDependenciesScope(module);
        }
        return GlobalSearchScope.projectScope(getProject());
    }
}
