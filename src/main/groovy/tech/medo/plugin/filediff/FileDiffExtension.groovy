package tech.medo.plugin.filediff

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

import javax.inject.Inject

/**
 * @author bryce
 * @date 2022/9/4
 */
class FileDiffExtension {

  final Property<File> file1
  final Property<File> file2

  @Inject
  FileDiffExtension(ObjectFactory objectFactory) {
    file1 = objectFactory.property(File);
    file2 = objectFactory.property(File);
  }

}
