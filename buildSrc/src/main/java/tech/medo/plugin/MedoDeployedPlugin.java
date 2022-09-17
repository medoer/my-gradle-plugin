package tech.medo.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlatformPlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;

/**
 * A plugin applied to a project that should be deployed.
 *
 * @author Andy Wilkinson
 * @author spring boot
 */
public class MedoDeployedPlugin implements Plugin<Project> {

	/**
	 * Name of the task that generates the deployed pom file.
	 */
	public static final String GENERATE_POM_TASK_NAME = "generatePomFileForMavenPublication";

	@Override
	public void apply(Project project) {
		project.getPlugins().apply(MavenPublishPlugin.class);
		PublishingExtension publishing = project.getExtensions().getByType(PublishingExtension.class);
		MavenPublication mavenPublication = publishing.getPublications().create("maven", MavenPublication.class);
		project.getPlugins().withType(JavaPlugin.class)
				.all((javaPlugin) -> project.getComponents().matching((component) -> component.getName().equals("java"))
						.all((javaComponent) -> mavenPublication.from(javaComponent)));
		project.getPlugins().withType(JavaPlatformPlugin.class)
				.all((javaPlugin) -> project.getComponents()
						.matching((component) -> component.getName().equals("javaPlatform"))
						.all((javaComponent) -> mavenPublication.from(javaComponent)));
	}

}
