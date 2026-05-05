package uk.gov.justice.services.test.utils.persistence;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;

@ExtendWith(MockitoExtension.class)
public class HibernateTestEntityManagerProviderTest {

    private static final String PERSISTENCE_UNIT = "test-utils-hibernate-test-unit";
    private static final Map<String, Object> PERSISTENCE_UNIT_PROPERTIES = Map.of("hibernate.jpa.compliance.query", "true");

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    private HibernateTestEntityManagerProvider hibernateTestEntityManagerProvider;

    @BeforeEach
    void setUp() {
        try (final MockedStatic<Persistence> mockedPersistence = mockStatic(Persistence.class)) {
            mockedPersistence.when(() -> Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, PERSISTENCE_UNIT_PROPERTIES)).thenReturn(entityManagerFactory);
            hibernateTestEntityManagerProvider = new HibernateTestEntityManagerProvider(PERSISTENCE_UNIT);
        }
    }

    @Test
    void shouldCreateEntityManagerFactoryFromPersistenceUnitName() {
        try (final MockedStatic<Persistence> mockedPersistence = mockStatic(Persistence.class)) {
            mockedPersistence.when(() -> Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, PERSISTENCE_UNIT_PROPERTIES))
                             .thenReturn(entityManagerFactory);

            new HibernateTestEntityManagerProvider(PERSISTENCE_UNIT);

            mockedPersistence.verify(() -> Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, PERSISTENCE_UNIT_PROPERTIES));
        }
    }

    @Test
    public void shouldOpenEntityManagerViaBeforeEachCallback() throws Exception {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);

        hibernateTestEntityManagerProvider.beforeEach(null);

        verify(entityManagerFactory).createEntityManager();
        verify(transaction).begin();
    }

    @Test
    public void shouldCloseEntityManagerViaAfterEachCallback() throws Exception {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(true);
        when(entityManager.isOpen()).thenReturn(true);

        hibernateTestEntityManagerProvider.beforeEach(null);
        hibernateTestEntityManagerProvider.afterEach(null);

        verify(transaction).rollback();
        verify(entityManager).close();
    }

    @Test
    public void shouldCloseEntityManagerFactoryViaAfterAllCallback() throws Exception {
        when(entityManagerFactory.isOpen()).thenReturn(true);

        hibernateTestEntityManagerProvider.afterAll(null);

        verify(entityManagerFactory).close();
    }

    @Test
    void shouldOpenEntityManagerAndBeginTransaction() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);

        hibernateTestEntityManagerProvider.openEntityManager();

        verify(entityManagerFactory).createEntityManager();
        verify(transaction).begin();
    }

    @Test
    void shouldReturnEntityManagerAfterOpen() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);

        hibernateTestEntityManagerProvider.openEntityManager();

        assertThat(hibernateTestEntityManagerProvider.getEntityManager(), is(entityManager));
    }

    @Test
    void shouldRollbackAndCloseEntityManagerOnClose() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(true);
        when(entityManager.isOpen()).thenReturn(true);

        hibernateTestEntityManagerProvider.openEntityManager();
        hibernateTestEntityManagerProvider.closeEntityManager();

        verify(transaction).rollback();
        verify(entityManager).close();
    }

    @Test
    void shouldNotRollbackWhenTransactionIsNotActive() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(false);
        when(entityManager.isOpen()).thenReturn(true);

        hibernateTestEntityManagerProvider.openEntityManager();
        hibernateTestEntityManagerProvider.closeEntityManager();

        verify(transaction, never()).rollback();
        verify(entityManager).close();
    }

    @Test
    void shouldNotCloseEntityManagerWhenAlreadyClosed() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(false);
        when(entityManager.isOpen()).thenReturn(false);

        hibernateTestEntityManagerProvider.openEntityManager();
        hibernateTestEntityManagerProvider.closeEntityManager();

        verify(entityManager, never()).close();
    }

    @Test
    void shouldHandleCloseEntityManagerWhenNeverOpened() {
        hibernateTestEntityManagerProvider.closeEntityManager();

        verify(entityManagerFactory, never()).createEntityManager();
    }

    @Test
    void shouldCloseEntityManagerFactoryWhenOpen() {
        when(entityManagerFactory.isOpen()).thenReturn(true);

        hibernateTestEntityManagerProvider.closeEntityManagerFactory();

        verify(entityManagerFactory).close();
    }

    @Test
    void shouldNotCloseEntityManagerFactoryWhenAlreadyClosed() {
        when(entityManagerFactory.isOpen()).thenReturn(false);

        hibernateTestEntityManagerProvider.closeEntityManagerFactory();

        verify(entityManagerFactory, never()).close();
    }

    @Test
    void shouldInjectEntityManagerIntoDefaultField() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);

        hibernateTestEntityManagerProvider.openEntityManager();

        final DummyRepository repository = new DummyRepository();
        hibernateTestEntityManagerProvider.injectEntityManagerInto(repository);

        assertThat(repository.entityManager, is(entityManager));
    }

    @Test
    void shouldInjectEntityManagerIntoNamedField() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);

        hibernateTestEntityManagerProvider.openEntityManager();

        final DummyRepository repository = new DummyRepository();
        hibernateTestEntityManagerProvider.injectEntityManagerInto(repository, "customNamedEntityManager");

        assertThat(repository.customNamedEntityManager, is(entityManager));
    }

    @Test
    void shouldOnlySetTheNamedFieldWhenFieldNameIsSupplied() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);

        hibernateTestEntityManagerProvider.openEntityManager();

        final DummyRepository repository = new DummyRepository();
        hibernateTestEntityManagerProvider.injectEntityManagerInto(repository, "customNamedEntityManager");

        assertThat(repository.entityManager, is(nullValue()));
    }

    @Test
    void shouldThrowNoEntityManagerFieldFoundExceptionWhenDefaultFieldDoesNotExist() {
        final Object noEntityManagerField = new Object();

        final NoEntityManagerFieldFoundException exception = assertThrows(
                NoEntityManagerFieldFoundException.class,
                () -> hibernateTestEntityManagerProvider.injectEntityManagerInto(noEntityManagerField));

        assertThat(exception.getMessage(), is("Failed to inject Hibernate EntityManager into java.lang.Object. No EntityManager field found with the name 'entityManager'"));
    }

    @Test
    void shouldIncludeCustomFieldNameInExceptionMessageWhenNamedFieldDoesNotExist() {
        final Object noEntityManagerField = new Object();

        final NoEntityManagerFieldFoundException exception = assertThrows(
                NoEntityManagerFieldFoundException.class,
                () -> hibernateTestEntityManagerProvider.injectEntityManagerInto(noEntityManagerField, "myEntityManager"));

        assertThat(exception.getMessage(), is("Failed to inject Hibernate EntityManager into java.lang.Object. No EntityManager field found with the name 'myEntityManager'"));
    }

    static class DummyRepository {

        EntityManager entityManager;
        EntityManager customNamedEntityManager;

        public DummyEntity findBy(final UUID id) {
            return entityManager.find(DummyEntity.class, id);
        }

        public void save(final DummyEntity entity) {
            entityManager.persist(entity);
        }

        public DummyEntity findByUsingCustomField(final UUID id) {
            return customNamedEntityManager.find(DummyEntity.class, id);
        }
    }

    @Entity
    @Table(name = "test_entity")
    static class DummyEntity {

        @Id
        @Column(name = "id")
        private UUID id;

        @Column(name = "name")
        private String name;

        public DummyEntity() {
        }

        public DummyEntity(final UUID id, final String name) {
            this.id = id;
            this.name = name;
        }

        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
