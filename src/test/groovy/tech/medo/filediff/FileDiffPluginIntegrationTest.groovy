package tech.medo.filediff

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * @author bryce
 * @date 2022/9/4
 */
class FileDiffPluginIntegrationTest extends Specification{
  @Rule
  TemporaryFolder testTemporaryFolder = new TemporaryFolder()
  File buildFile

  def setup() {
    buildFile = testTemporaryFolder.newFile('build.gradle')
    buildFile << """
            plugins {
              id 'tech.medo.file-diff'
            }
    """
  }

  def "can successfuly diff 2 files" () {
    given:
    File testFile1 = testTemporaryFolder.newFile("testFile1.txt")
    File testFile2 = testTemporaryFolder.newFile("testFile2.txt")
    buildFile << """
        fileDiff {
          file1 = file('${testFile1.getName()}')
          file2 = file('${testFile2.getName()}')
        }
    """
    when:
    def result = GradleRunner.create()
        .withProjectDir(testTemporaryFolder.root)
        .withArguments("fileDiff")
        .withPluginClasspath()
        .build()
    then:
    result.output.contains('Files have the same size')
    result.task(':fileDiff').outcome == TaskOutcome.SUCCESS
  }
}
