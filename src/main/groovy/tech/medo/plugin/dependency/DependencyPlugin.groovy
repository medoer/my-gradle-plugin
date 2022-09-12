package tech.medo.plugin.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author bryce
 * @date 2022/9/5
 */
class DependencyPlugin implements Plugin<Project> {
  @Override
  void apply(Project project) {
    project.plugins.apply(JavaLibraryPlugin.class)
//    project.plugins.apply(JavaPlatformPlugin.class)
//    project.extensions.configure(JavaPlatformExtension.class,
//        {x ->
//          x.allowDependencies()
//        }
//    )
    project.getDependencies().platform('tech.medo:medo-platform-dependencies:0.0.1-SNAPSHOT')
//    project.dependencies {
//      api platform('tech.medo:medo-platform-dependencies:0.0.1-SNAPSHOT')
//    }
//    project.getDependencies().add("api platform", ");
  }
}
