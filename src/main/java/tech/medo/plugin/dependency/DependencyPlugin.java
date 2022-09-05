package tech.medo.plugin.dependency;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaLibraryPlugin;

/**
 * @author bryce
 * @date 2022/9/5
 */
public class DependencyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPlugins().apply(JavaLibraryPlugin.class);
        project.getDependencies().add("implementation", "com.diffplug.guava:guava-core:19.0.0");
    }
}
