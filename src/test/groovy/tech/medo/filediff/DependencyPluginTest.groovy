//package tech.medo.filediff
//
//import org.gradle.testkit.runner.GradleRunner
//import org.gradle.testkit.runner.TaskOutcome
//import org.junit.Rule
//import org.junit.rules.TemporaryFolder
//import spock.lang.Specification
//
///**
// * @author bryce
// * @date 2022/9/4
// */
//class DependencyPluginTest{
//  @Rule
//  TemporaryFolder testTemporaryFolder = new TemporaryFolder()
//  File buildFile
//
//  void setup() {
//    buildFile = testTemporaryFolder.newFile('build.gradle')
//    buildFile << """
//            plugins {
//              id 'tech.medo.dependency'
//            }
//    """
//  }
//
//  def "compile successfully" () {
//        def result = GradleRunner.create()
//        .withProjectDir(testTemporaryFolder.root)
//        .withArguments("dependency")
//        .withPluginClasspath()
//        .build()
//  }
//}
