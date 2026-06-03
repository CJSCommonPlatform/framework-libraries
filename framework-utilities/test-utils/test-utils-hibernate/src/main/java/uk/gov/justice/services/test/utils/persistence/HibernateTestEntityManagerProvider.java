package uk.gov.justice.services.test.utils.persistence;

import static java.lang.String.format;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import uk.gov.justice.services.test.utils.core.reflection.ReflectionException;

import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Test utility that bootstraps a standalone Hibernate {@link EntityManager} from a named
 * persistence unit.  Intended to be used in unit/integration tests for repository classes
 * that inject an {@link EntityManager} via {@code @PersistenceContext}.
 *
 * <p>Typical usage pattern in a JUnit 5 test:
 * <pre>
 *     private final HibernateTestEntityManagerProvider provider =
 *             new HibernateTestEntityManagerProvider("my-persistence-unit");
 *
 *     {@literal @}BeforeEach
 *     void setUp() {
 *         provider.openEntityManager();
 *         provider.injectEntityManagerInto(myRepository);
 *     }
 *
 *     {@literal @}AfterEach
 *     void tearDown() {
 *         provider.closeEntityManager();
 *     }
 *
 *     {@literal @}AfterAll
 *     static void tearDownFactory() {
 *         provider.closeEntityManagerFactory();
 *     }
 * </pre>
 *
 * <p>Each call to {@link #openEntityManager()} begins a new transaction.  {@link
 * #closeEntityManager()} rolls back any active transaction and closes the manager, leaving
 * the database clean for the next test.
 */
public class HibernateTestEntityManagerProvider implements BeforeEachCallback, AfterEachCallback, AfterAllCallback {

    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Creates the provider and immediately builds the {@link EntityManagerFactory} for the
     * named persistence unit.  The factory is expensive to create, so a single instance
     * should be shared across all tests in a class (e.g. stored in a {@code static} field
     * and initialised once with {@code @BeforeAll}).
     *
     * @param persistenceUnitName the name of the persistence unit as declared in
     *                            {@code META-INF/persistence.xml}
     */
    public HibernateTestEntityManagerProvider(final String persistenceUnitName) {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName,
                Map.of("hibernate.jpa.compliance.query", "true"));
    }

    @Override
    public void beforeEach(final ExtensionContext context) {
        openEntityManager();
    }

    @Override
    public void afterEach(final ExtensionContext context) {
        closeEntityManager();
    }

    @Override
    public void afterAll(final ExtensionContext context) {
        closeEntityManagerFactory();
    }


    /**
     * Opens a new {@link EntityManager} and begins a transaction.  Must be called before
     * any repository interaction in a test.
     */
    public void openEntityManager() {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
    }

    /**
     * Rolls back the active transaction (if any) and closes the {@link EntityManager}.
     * Call this in {@code @AfterEach} so every test starts with a clean database state.
     */
    public void closeEntityManager() {
        if (entityManager != null) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    /**
     * Closes the underlying {@link EntityManagerFactory}.  Call this in {@code @AfterAll}
     * once all tests in the class have finished.
     */
    public void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    /**
     * Returns the current {@link EntityManager}.  Valid only between calls to
     * {@link #openEntityManager()} and {@link #closeEntityManager()}.
     *
     * @return the active {@link EntityManager}
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Injects the current {@link EntityManager} into the field named {@code entityManager}
     * on the supplied repository object.  Equivalent to calling
     * {@link #injectEntityManagerInto(Object, String)} with {@code "entityManager"}.
     *
     * @param repository the repository instance to inject into
     * @throws NoEntityManagerFieldFoundException if the field cannot be found
     */
    public void injectEntityManagerInto(final Object repository) {
        injectEntityManagerInto(repository, "entityManager");
    }

    /**
     * Injects the current {@link EntityManager} into the named field of the supplied
     * repository object.  Uses reflection so that no special interface or constructor is
     * required on the repository.
     *
     * @param repository             the repository instance to inject into
     * @param entityManagerFieldName the name of the field that holds the {@link EntityManager}
     * @throws NoEntityManagerFieldFoundException if the field cannot be found
     */
    public void injectEntityManagerInto(final Object repository, final String entityManagerFieldName) {
        try {
            setField(repository, entityManagerFieldName, entityManager);
        } catch (final ReflectionException e) {
            throw new NoEntityManagerFieldFoundException(
                    format("Failed to inject Hibernate EntityManager into %s. No EntityManager field found with the name '%s'",
                            repository.getClass().getName(), entityManagerFieldName));
        }
    }
}
