package tech.medo.plugin.filediff

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

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
    project.getDependencies().add("implementation", "com.diffplug.guava:guava-core:19.0.0")
  }
}
