package tech.medo.plugin.filediff

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.JavaLibraryPlugin

/**
 * @author bryce
 * @date 2022/9/4
 */
class FileDiffPlugin implements Plugin<Project>{
  @Override
  void apply(Project project) {
    project.extensions.create('fileDiff', FileDiffExtension)
    project.tasks.create('fileDiff', FileDiffTask) {
      file1 = project.fileDiff.file1
      file2 = project.fileDiff.file2
    }
//    project.getPlugins().apply(JavaLibraryPlugin.class);
//    project.dependencies {
//      api 'tech.medo:medo-platform-dependencies:0.0.1-SNAPSHOT'
//    }
//    project.getDependencies().add("api", "");
//    project.getDependencies().add("implementation", "com.diffplug.guava:guava-core:19.0.0")
  }
}
