package uk.gov.justice.maven.generator.io.files.parser.test.utils;

import java.io.File;
import java.util.Arrays;

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
 * This includes: <li> local repo, repo sessions and managers configured <li> maven default remote
 * repos installed (NB: this does not use your ~/.m2 local settings) <li> system properties are
 * copies <p> No changes to subclass code is needed; this simply intercepts the {@link
 * #newMavenSession(MavenProject)} method used by the various {@link #lookupMojo(String, File)}
 * methods. <p> This also provides new methods, {@link #newMavenSession()} to conveniently create a
 * maven session, and {@link #lookupConfiguredMojo(File, String)} so you don't have to always build
 * the project yourself.
 *
 * TODO: Determine how to unit test this class
 * The only real way to test this class is to extend and create a concrete version
 * and then build a MOJO to test, however this cannot be done without pulling in
 * the extra dependencies into the project as MAIN. We are already doing this effectively
 * in the raml-maven module and in the lint checker rules (in microservices framework).
 * Originally this class was part of TEST in raml-maven-plugin and was moved into MAIN
 * so it could be used across maven projects to test MOJOs.
 *
 */
public abstract class BetterAbstractMojoTestCase extends AbstractMojoTestCase {

    protected MavenProject project;

    protected MavenSession newMavenSession() {
        try {
            MavenExecutionRequest request = new DefaultMavenExecutionRequest();
            MavenExecutionResult result = new DefaultMavenExecutionResult();

            // populate sensible defaults, including repository basedir and remote repos
            MavenExecutionRequestPopulator populator;
            populator = getContainer().lookup(MavenExecutionRequestPopulator.class);
            populator.populateDefaults(request);

            // this is needed to allow java profiles to get resolved; i.e. avoid during project builds:
            // [ERROR] Failed to determine Java version for profile java-1.5-detected @ org.apache.commons:commons-parent:22, /Users/alex/.m2/repository/org/apache/commons/commons-parent/22/commons-parent-22.pom, line 909, column 14
            request.setSystemProperties(System.getProperties());

            // and this is needed so that the repo session in the maven session
            // has a repo manager, and it points at the local repo
            // (cf MavenRepositorySystemUtils.newSession() which is what is otherwise done)
            DefaultMaven maven = (DefaultMaven) getContainer().lookup(Maven.class);
            DefaultRepositorySystemSession repoSession =
                    (DefaultRepositorySystemSession) maven.newRepositorySession(request);
            repoSession.setLocalRepositoryManager(
                    new SimpleLocalRepositoryManagerFactory().newInstance(repoSession,
                            new LocalRepository(request.getLocalRepository().getBasedir())));

            @SuppressWarnings("deprecation")
            MavenSession session = new MavenSession(getContainer(),
                    repoSession,
                    request, result);
            return session;
        } catch (Exception e) {
            throw new MojoTestCaseException(e);
        }
    }

    /**
     * Extends the super to use the new {@link #newMavenSession()} introduced here which sets the
     * defaults one expects from maven; the standard test case leaves a lot of things blank
     */
    @Override
    protected MavenSession newMavenSession(MavenProject project) {
        MavenSession session = newMavenSession();
        session.setCurrentProject(project);
        session.setProjects(Arrays.asList(project));
        return session;
    }

    /**
     * As {@link #lookupConfiguredMojo(MavenProject, String)} but taking the pom file and creating
     * the {@link MavenProject}.
     */
    protected Mojo lookupConfiguredMojo(File pom, String goal) throws Exception {
        assertNotNull(pom);

        assertTrue(pom.exists());

        ProjectBuildingRequest buildingRequest = newMavenSession().getProjectBuildingRequest();
        ProjectBuilder projectBuilder = lookup(ProjectBuilder.class);
        project = projectBuilder.build(pom, buildingRequest).getProject();

        return lookupConfiguredMojo(project, goal);
    }

}
