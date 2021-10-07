package uk.gov.justice.maven.test.utils;

import static java.util.Arrays.asList;

import java.io.File;

import org.apache.maven.DefaultMaven;
import org.apache.maven.Maven;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequestPopulator;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.internal.impl.SimpleLocalRepositoryManagerFactory;
import org.eclipse.aether.repository.LocalRepository;

/**
 * Use this as you would {@link AbstractMojoTestCase}, where you want more of the standard maven
 * defaults to be set (and where the {@link AbstractMojoTestCase} leaves them as null or empty).
 * This includes:  local repo, repo sessions and managers configured maven default remote
 * repos installed (NB: this does not use your ~/.m2 local settings)  system properties are
 * copies <p> No changes to subclass code is needed; this simply intercepts the {@link
 * #newMavenSession(MavenProject)} method used by the various {@link #lookupMojo(String, File)}
 * methods. <p> This also provides new methods, {@link #newMavenSession()} to conveniently create a
 * maven session, and {@link #lookupConfiguredMojo(File, String)} so you don't have to always build
 * the project yourself.
 * <p>
 * TODO: Determine how to unit test this class
 * The only real way to test this class is to extend and create a concrete version
 * and then build a MOJO to test, however this cannot be done without pulling in
 * the extra dependencies into the project as MAIN. We are already doing this effectively
 * in the raml-maven module and in the lint checker rules (in microservices framework).
 * Originally this class was part of TEST in raml-maven-plugin and was moved into MAIN
 * so it could be used across maven projects to test MOJOs.
 */
public abstract class BetterAbstractMojoTestCase extends AbstractMojoTestCase {

    protected MavenProject project;

    protected MavenSession newMavenSession() {
        try {
            final MavenExecutionRequest request = new DefaultMavenExecutionRequest();
            final MavenExecutionResult result = new DefaultMavenExecutionResult();

            // populate sensible defaults, including repository basedir and remote repos
            final MavenExecutionRequestPopulator populator = getContainer().lookup(MavenExecutionRequestPopulator.class);
            populator.populateDefaults(request);

            // this is needed to allow java profiles to get resolved; i.e. avoid during project builds:
            // [ERROR] Failed to determine Java version for profile java-1.5-detected @ org.apache.commons:commons-parent:22, /Users/alex/.m2/repository/org/apache/commons/commons-parent/22/commons-parent-22.pom, line 909, column 14
            request.setSystemProperties(System.getProperties());

            // and this is needed so that the repo session in the maven session
            // has a repo manager, and it points at the local repo
            // (cf MavenRepositorySystemUtils.newSession() which is what is otherwise done)
            final DefaultMaven maven = (DefaultMaven) getContainer().lookup(Maven.class);
            final DefaultRepositorySystemSession repoSession =
                    (DefaultRepositorySystemSession) maven.newRepositorySession(request);
            repoSession.setLocalRepositoryManager(
                    new SimpleLocalRepositoryManagerFactory().newInstance(repoSession,
                            new LocalRepository(request.getLocalRepository().getBasedir())));

            return new MavenSession(getContainer(),
                    repoSession,
                    request, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extends the super to use the new {@link #newMavenSession()} introduced here which sets the
     * defaults one expects from maven; the standard test case leaves a lot of things blank
     */
    @Override
    protected MavenSession newMavenSession(final MavenProject project) {
        final MavenSession session = newMavenSession();
        session.setCurrentProject(project);
        session.setProjects(asList(project));
        return session;
    }

    /**
     * As {@link #lookupConfiguredMojo(MavenProject, String)} but taking the pom file and creating
     * the {@link MavenProject}.
     * @param pom The name of the pom file
     * @param goal The Maven goal
     * @return The configured Mojo
     */
    protected Mojo lookupConfiguredMojo(final File pom, final String goal) throws Exception {
        assertNotNull(pom);
        assertTrue(pom.exists());

        final ProjectBuildingRequest buildingRequest = newMavenSession().getProjectBuildingRequest();
        final ProjectBuilder projectBuilder = lookup(ProjectBuilder.class);
        project = projectBuilder.build(pom, buildingRequest).getProject();

        return lookupConfiguredMojo(project, goal);
    }

}
