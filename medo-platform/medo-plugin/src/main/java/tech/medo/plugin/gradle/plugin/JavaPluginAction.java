///*
// * Copyright 2012-2022 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package tech.medo.plugin.gradle.plugin;
//
//import org.gradle.api.Action;
//import org.gradle.api.Plugin;
//import org.gradle.api.Project;
//import org.gradle.api.Task;
//import org.gradle.api.artifacts.Configuration;
//import org.gradle.api.attributes.AttributeContainer;
//import org.gradle.api.attributes.Bundling;
//import org.gradle.api.attributes.LibraryElements;
//import org.gradle.api.attributes.Usage;
//import org.gradle.api.model.ObjectFactory;
//import org.gradle.api.plugins.BasePlugin;
//import org.gradle.api.plugins.JavaPlugin;
//import org.gradle.api.tasks.SourceSetContainer;
//import org.gradle.api.tasks.bundling.Jar;
//import org.gradle.api.tasks.compile.JavaCompile;
//import org.springframework.util.StringUtils;
//
//import java.io.File;
//import java.util.List;
//import java.util.Set;
//
///**
// * {@link Action} that is executed in response to the {@link JavaPlugin} being applied.
// *
// * @author Andy Wilkinson
// * @author Scott Frederick
// */
//final class JavaPluginAction implements PluginApplicationAction {
//
//	private static final String PARAMETERS_COMPILER_ARG = "-parameters";
//
//	private final SinglePublishedArtifact singlePublishedArtifact;
//
//	JavaPluginAction(SinglePublishedArtifact singlePublishedArtifact) {
//		this.singlePublishedArtifact = singlePublishedArtifact;
//	}
//
//	@Override
//	public Class<? extends Plugin<? extends Project>> getPluginClass() {
//		return JavaPlugin.class;
//	}
//
//	@Override
//	public void execute(Project project) {
//		classifyJarTask(project);
//		configureBuildTask(project);
//		configureDevelopmentOnlyConfiguration(project);
////		TaskProvider<BootJar> bootJar = configureBootJarTask(project);
////		configureBootBuildImageTask(project, bootJar);
////		configureArtifactPublication(bootJar);
////		configureBootRunTask(project);
//		project.afterEvaluate(this::configureUtf8Encoding);
//		configureParametersCompilerArg(project);
//		configureAdditionalMetadataLocations(project);
//	}
//
//	private void classifyJarTask(Project project) {
//		project.getTasks().named(JavaPlugin.JAR_TASK_NAME, Jar.class)
//				.configure((task) -> task.getArchiveClassifier().convention("plain"));
//	}
//
//	private void configureBuildTask(Project project) {
//		project.getTasks().named(BasePlugin.ASSEMBLE_TASK_NAME)
//				.configure((task) -> task.dependsOn(this.singlePublishedArtifact));
//	}
////
////	private TaskProvider<BootJar> configureBootJarTask(Project project) {
////		SourceSet mainSourceSet = sourceSets(project).getByName(SourceSet.MAIN_SOURCE_SET_NAME);
////		Configuration developmentOnly = project.getConfigurations()
////				.getByName(SpringBootPlugin.DEVELOPMENT_ONLY_CONFIGURATION_NAME);
////		Configuration productionRuntimeClasspath = project.getConfigurations()
////				.getByName(SpringBootPlugin.PRODUCTION_RUNTIME_CLASSPATH_CONFIGURATION_NAME);
////		Callable<FileCollection> classpath = () -> mainSourceSet.getRuntimeClasspath()
////				.minus((developmentOnly.minus(productionRuntimeClasspath))).filter(new JarTypeFileSpec());
////		TaskProvider<ResolveMainClassName> resolveMainClassName = ResolveMainClassName
////				.registerForTask(SpringBootPlugin.BOOT_JAR_TASK_NAME, project, classpath);
////		return project.getTasks().register(SpringBootPlugin.BOOT_JAR_TASK_NAME, BootJar.class, (bootJar) -> {
////			bootJar.setDescription(
////					"Assembles an executable jar archive containing the main classes and their dependencies.");
////			bootJar.setGroup(BasePlugin.BUILD_GROUP);
////			bootJar.classpath(classpath);
////			Provider<String> manifestStartClass = project
////					.provider(() -> (String) bootJar.getManifest().getAttributes().get("Start-Class"));
////			bootJar.getMainClass().convention(resolveMainClassName.flatMap((resolver) -> manifestStartClass.isPresent()
////					? manifestStartClass : resolveMainClassName.get().readMainClassName()));
////		});
////	}
////
////	private void configureBootBuildImageTask(Project project, TaskProvider<BootJar> bootJar) {
////		project.getTasks().register(SpringBootPlugin.BOOT_BUILD_IMAGE_TASK_NAME, BootBuildImage.class, (buildImage) -> {
////			buildImage.setDescription("Builds an OCI image of the application using the output of the bootJar task");
////			buildImage.setGroup(BasePlugin.BUILD_GROUP);
////			buildImage.getArchiveFile().set(bootJar.get().getArchiveFile());
////			buildImage.getTargetJavaVersion().set(project.provider(
////					() -> project.getExtensions().getByType(JavaPluginExtension.class).getTargetCompatibility()));
////		});
////	}
////
////	private void configureArtifactPublication(TaskProvider<BootJar> bootJar) {
////		this.singlePublishedArtifact.addJarCandidate(bootJar);
////	}
////
////	private void configureBootRunTask(Project project) {
////		Callable<FileCollection> classpath = () -> sourceSets(project).findByName(SourceSet.MAIN_SOURCE_SET_NAME)
////				.getRuntimeClasspath().filter(new JarTypeFileSpec());
////		TaskProvider<ResolveMainClassName> resolveProvider = ResolveMainClassName.registerForTask("bootRun", project,
////				classpath);
////		project.getTasks().register("bootRun", BootRun.class, (run) -> {
////			run.setDescription("Runs this project as a Spring Boot application.");
////			run.setGroup(ApplicationPlugin.APPLICATION_GROUP);
////			run.classpath(classpath);
////			run.getConventionMapping().map("jvmArgs", () -> {
////				if (project.hasProperty("applicationDefaultJvmArgs")) {
////					return project.property("applicationDefaultJvmArgs");
////				}
////				return Collections.emptyList();
////			});
////			run.getMainClass().convention(resolveProvider.flatMap(ResolveMainClassName::readMainClassName));
////			configureToolchainConvention(project, run);
////		});
////	}
////
////	private void configureToolchainConvention(Project project, BootRun run) {
////		JavaToolchainSpec toolchain = project.getExtensions().getByType(JavaPluginExtension.class).getToolchain();
////		JavaToolchainService toolchainService = project.getExtensions().getByType(JavaToolchainService.class);
////		run.getJavaLauncher().convention(toolchainService.launcherFor(toolchain));
////	}
//
//	@SuppressWarnings("deprecation")
//	private SourceSetContainer sourceSets(Project project) {
//		return project.getConvention().getPlugin(org.gradle.api.plugins.JavaPluginConvention.class).getSourceSets();
//	}
//
//	private void configureUtf8Encoding(Project evaluatedProject) {
//		evaluatedProject.getTasks().withType(JavaCompile.class).configureEach(this::configureUtf8Encoding);
//	}
//
//	private void configureUtf8Encoding(JavaCompile compile) {
//		if (compile.getOptions().getEncoding() == null) {
//			compile.getOptions().setEncoding("UTF-8");
//		}
//	}
//
//	private void configureParametersCompilerArg(Project project) {
//		project.getTasks().withType(JavaCompile.class).configureEach((compile) -> {
//			List<String> compilerArgs = compile.getOptions().getCompilerArgs();
//			if (!compilerArgs.contains(PARAMETERS_COMPILER_ARG)) {
//				compilerArgs.add(PARAMETERS_COMPILER_ARG);
//			}
//		});
//	}
//
//	private void configureAdditionalMetadataLocations(Project project) {
//		project.afterEvaluate((evaluated) -> evaluated.getTasks().withType(JavaCompile.class)
//				.configureEach(this::configureAdditionalMetadataLocations));
//	}
//
//	private void configureAdditionalMetadataLocations(JavaCompile compile) {
//		sourceSets(compile.getProject()).stream()
//				.filter((candidate) -> candidate.getCompileJavaTaskName().equals(compile.getName()))
//				.map((match) -> match.getResources().getSrcDirs()).findFirst()
//				.ifPresent((locations) -> compile.doFirst(new AdditionalMetadataLocationsConfigurer(locations)));
//	}
//
//	private void configureDevelopmentOnlyConfiguration(Project project) {
//		Configuration developmentOnly = project.getConfigurations()
//				.create(MedoPlugin.DEVELOPMENT_ONLY_CONFIGURATION_NAME);
//		developmentOnly
//				.setDescription("Configuration for development-only dependencies such as Spring Boot's DevTools.");
//		Configuration runtimeClasspath = project.getConfigurations()
//				.getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME);
//		Configuration productionRuntimeClasspath = project.getConfigurations()
//				.create(MedoPlugin.PRODUCTION_RUNTIME_CLASSPATH_CONFIGURATION_NAME);
//		AttributeContainer attributes = productionRuntimeClasspath.getAttributes();
//		ObjectFactory objectFactory = project.getObjects();
//		attributes.attribute(Usage.USAGE_ATTRIBUTE, objectFactory.named(Usage.class, Usage.JAVA_RUNTIME));
//		attributes.attribute(Bundling.BUNDLING_ATTRIBUTE, objectFactory.named(Bundling.class, Bundling.EXTERNAL));
//		attributes.attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE,
//				objectFactory.named(LibraryElements.class, LibraryElements.JAR));
//		productionRuntimeClasspath.setVisible(false);
//		productionRuntimeClasspath.setExtendsFrom(runtimeClasspath.getExtendsFrom());
//		productionRuntimeClasspath.setCanBeResolved(runtimeClasspath.isCanBeResolved());
//		productionRuntimeClasspath.setCanBeConsumed(runtimeClasspath.isCanBeConsumed());
//		runtimeClasspath.extendsFrom(developmentOnly);
//	}
//
//	/**
//	 * Task {@link Action} to add additional meta-data locations. We need to use an
//	 * inner-class rather than a lambda due to
//	 * https://github.com/gradle/gradle/issues/5510.
//	 */
//	private static final class AdditionalMetadataLocationsConfigurer implements Action<Task> {
//
//		private final Set<File> locations;
//
//		private AdditionalMetadataLocationsConfigurer(Set<File> locations) {
//			this.locations = locations;
//		}
//
//		@Override
//		public void execute(Task task) {
//			if (!(task instanceof JavaCompile)) {
//				return;
//			}
//			JavaCompile compile = (JavaCompile) task;
//			if (hasConfigurationProcessorOnClasspath(compile)) {
//				configureAdditionalMetadataLocations(compile);
//			}
//		}
//
//		private boolean hasConfigurationProcessorOnClasspath(JavaCompile compile) {
//			Set<File> files = (compile.getOptions().getAnnotationProcessorPath() != null)
//					? compile.getOptions().getAnnotationProcessorPath().getFiles() : compile.getClasspath().getFiles();
//			return files.stream().map(File::getName)
//					.anyMatch((name) -> name.startsWith("spring-boot-configuration-processor"));
//		}
//
//		private void configureAdditionalMetadataLocations(JavaCompile compile) {
//			compile.getOptions().getCompilerArgs()
//					.add("-Aorg.springframework.boot.configurationprocessor.additionalMetadataLocations="
//							+ StringUtils.collectionToCommaDelimitedString(this.locations));
//		}
//
//	}
//
//}