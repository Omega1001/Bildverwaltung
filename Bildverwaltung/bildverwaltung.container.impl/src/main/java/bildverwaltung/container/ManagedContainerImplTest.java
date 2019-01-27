package bildverwaltung.container;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import java.io.Closeable;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

public class ManagedContainerImplTest {

	protected static final String ERROR_MESSAGE = "Something went wrong";

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	private ManagedContainer underTest;

	private MockFactory<Runnable> mockRunner01;
	private MockFactory<Runnable> mockRunner02;
	private MockFactory<Closeable> mockCloseble01;
	private MockFactory<Cloneable> mockError01;
	@SuppressWarnings("rawtypes")
	private MockFactory<Supplier> mockSubmaterialise01;
	@SuppressWarnings("rawtypes")
	private Factory<Map.Entry> mockCC01;

	protected ManagedContainer generateContainer() {
		return new ManagedContainerImpl();
	}

	@SuppressWarnings("rawtypes")
	@Before
	public void setUp() throws Exception {
		underTest = spy(generateContainer());
		mockRunner01 = spy(new MockFactory<>(Runnable.class));
		mockRunner02 = spy(new MockFactory<>(Runnable.class));
		mockCloseble01 = spy(new MockFactory<>(Closeable.class));
		mockError01 = spy(new MockFactory<Cloneable>(Cloneable.class) {
			@Override
			public Cloneable generate(ManagedContainer container, Scope scope) {
				throw new RuntimeException(ERROR_MESSAGE);
			}
		});
		mockSubmaterialise01 = spy(new MockFactory<Supplier>(Supplier.class) {

			Object o = null;

			@Override
			public Supplier generate(ManagedContainer container, Scope scope) {
				o = container.materialize(Closeable.class, scope);
				return () -> o;
			}
		});
		mockCC01 = new CCFactory();
		underTest.addFactory(mockRunner01);
		underTest.addFactory(mockRunner02);
		underTest.addFactory(mockCloseble01);
		underTest.addFactory(mockError01);
		underTest.addFactory(mockSubmaterialise01);
		underTest.addFactory(mockCC01);
	}

	@After
	public void tearDown() throws Exception {
		// Close later
	}

	@Test
	public void testMaterializeClassOfTScopeUUID_000() {
		// Default scenario
		Closeable obj = underTest.materialize(Closeable.class, Scope.APPLICATION, null);
		assertNotNull(obj);
		assertEquals(mockCloseble01.mock, obj);
	}

	@Test
	public void testMaterializeClassOfTScopeUUID_001() {
		// Unsatisfied
		exception.expect(ContainerException.class);
		underTest.materialize(Callable.class, Scope.APPLICATION, null);
	}

	@Test
	public void testMaterializeClassOfTScopeUUID_002() {
		// Ambiguous
		exception.expect(ContainerException.class);
		underTest.materialize(Runnable.class, Scope.APPLICATION, null);
	}

	@Test
	public void testMaterializeClassOfTScopeUUID_003() {
		// kept in scope
		Closeable obj = underTest.materialize(Closeable.class, Scope.APPLICATION, null);
		Closeable obj2 = underTest.materialize(Closeable.class, Scope.APPLICATION, null);
		assertNotNull(obj);
		assertNotNull(obj2);
		assertEquals(obj, obj);
	}

	@Test
	public void testMaterializeClassOfTScopeUUID_004() {
		// Not an interface
		exception.expect(ContainerException.class);
		underTest.materialize(ManagedContainerImplTest.class, Scope.APPLICATION, null);
	}

	@Test
	public void testMaterializeClassOfTScopeUUID_005() {
		// Exception during instantiation
		exception.expect(ContainerException.class);
		exception.expectCause(IsEqual.equalTo(new RuntimeException(ERROR_MESSAGE)));
		underTest.materialize(Cloneable.class, Scope.APPLICATION, null);
	}

	@Test
	public void testMaterializeClassOfTScopeUUID_006() {
		// Sub materialisation
		Supplier<?> s = underTest.materialize(Supplier.class, Scope.APPLICATION, null);
		assertNotNull(s);
	}

	@Test
	public void testMaterializeClassOfTScopeUUID_007() {
		// Sub creational context
		// Using CCFactory
		Entry<?, ?> e = underTest.materialize(Entry.class, Scope.APPLICATION, null);
		assertNotNull(e);
		assertEquals("The injected objects are not the same", e.getKey(), e.getValue());
	}

	@Test
	public void testMaterializeAllClassOfTScopeUUID_000() {
		// default scenario
		List<Runnable> res = underTest.materializeAll(Runnable.class, Scope.APPLICATION, null);
		assertThat(res, hasSize(2));
	}

	@Test
	public void testMaterializeAllClassOfTScopeUUID_001() {
		// support for non ambiguous
		List<Closeable> res = underTest.materializeAll(Closeable.class, Scope.APPLICATION, null);
		assertThat(res, hasSize(1));
	}

	@Test
	public void testMaterializeAllClassOfTScopeUUID_002() {
		// Unsatisfied
		exception.expect(ContainerException.class);
		underTest.materializeAll(Callable.class, Scope.APPLICATION, null);
	}

	@Test
	public void testMaterializeAllClassOfTScopeUUID_003() {
		// kept in scope
		List<Runnable> obj = underTest.materializeAll(Runnable.class, Scope.APPLICATION, null);
		List<Runnable> obj2 = underTest.materializeAll(Runnable.class, Scope.APPLICATION, null);
		assertNotNull(obj);
		assertNotNull(obj2);
		assertTrue("Results are not equivalent", obj.containsAll(obj2) && obj2.containsAll(obj));
	}

	@Test
	public void testMaterializeAllClassOfTScopeUUID_004() {
		// Not an interface
		exception.expect(ContainerException.class);
		underTest.materializeAll(ManagedContainerImplTest.class, Scope.APPLICATION, null);
	}

	@Test
	public void testMaterializeAllClassOfTScopeUUID_005() {
		// Exception during instantiation
		exception.expect(ContainerException.class);
		exception.expectCause(IsEqual.equalTo(new RuntimeException(ERROR_MESSAGE)));
		underTest.materializeAll(Cloneable.class, Scope.APPLICATION, null);
	}

	@Test
	public void testMaterializeAllClassOfTScopeUUID_006() {
		// Sub materialisation
		@SuppressWarnings("rawtypes")
		List<Supplier> s = underTest.materializeAll(Supplier.class, Scope.APPLICATION, null);
		assertNotNull(s);
		assertThat(s, hasSize(1));
		assertNotNull(s.get(0).get());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testMaterializeAllClassOfTScopeUUID_007() {
		// Sub creational context
		// Using CCFactory
		List<Entry> en = underTest.materializeAll(Entry.class, Scope.APPLICATION, null);
		assertNotNull(en);
		for (Entry e : en) {
			assertEquals("The injected objects are not the same", e.getKey(), e.getValue());
		}
	}
	
	@Test
	public void testMaterializeAnyClassOfTScopeUUID_000() {
		// Default scenario
		Closeable obj = underTest.materialize(Closeable.class, Scope.APPLICATION, null);
		assertNotNull(obj);
		assertEquals(mockCloseble01.mock, obj);
	}

	@Test
	public void testMaterializeAnyClassOfTScopeUUID_001() {
		// Unsatisfied
		exception.expect(ContainerException.class);
		underTest.materializeAny(Callable.class, Scope.APPLICATION, null);
	}

	@Test
	public void testMaterializeAnyClassOfTScopeUUID_002() {
		// Ambiguous
		Runnable r = underTest.materializeAny(Runnable.class, Scope.APPLICATION, null);
		assertThat(r, isOneOf(mockRunner01.mock,mockRunner02.mock));
	}

	@Test
	public void testMaterializeAnyClassOfTScopeUUID_003() {
		// kept in scope
		Closeable obj = underTest.materializeAny(Closeable.class, Scope.APPLICATION, null);
		Closeable obj2 = underTest.materializeAny(Closeable.class, Scope.APPLICATION, null);
		assertNotNull(obj);
		assertNotNull(obj2);
		assertEquals(obj, obj);
	}

	@Test
	public void testMaterializeAnyClassOfTScopeUUID_004() {
		// Not an interface
		exception.expect(ContainerException.class);
		underTest.materializeAny(ManagedContainerImplTest.class, Scope.APPLICATION, null);
	}

	@Test
	public void testMaterializeAnyClassOfTScopeUUID_005() {
		// Exception during instantiation
		exception.expect(ContainerException.class);
		exception.expectCause(IsEqual.equalTo(new RuntimeException(ERROR_MESSAGE)));
		underTest.materializeAny(Cloneable.class, Scope.APPLICATION, null);
	}

	@Test
	public void testMaterializeAnyClassOfTScopeUUID_006() {
		// Sub materialisation
		Supplier<?> s = underTest.materializeAny(Supplier.class, Scope.APPLICATION, null);
		assertNotNull(s);
	}

	@Test
	public void testMaterializeAnyClassOfTScopeUUID_007() {
		// Sub creational context
		// Using CCFactory
		Entry<?, ?> e = underTest.materializeAny(Entry.class, Scope.APPLICATION, null);
		assertNotNull(e);
		assertEquals("The injected objects are not the same", e.getKey(), e.getValue());
	}

	@Test
	@Ignore // At this time, there is no scope, that allows subScopes
	public void testBeginCustomScope() {
		//TODO implement, if a scope is created, that supports subscopes
		fail("Not yet implemented");
	}

	@Test
	@Ignore // At this time, there is no scope, that allows subScopes
	public void testEndCustomScope() {
		//TODO implement, if a scope is created, that supports subscopes
		fail("Not yet implemented");
	}

	/*
	 * The following tests are tests for overloaded methods They only test whether
	 * the method call is forwarding the correct parameters or not The outcome of
	 * those methods is NOT tested It is assumed, that the output is correct, if
	 * there master counterparts pass there tests
	 */

	@SuppressWarnings("unchecked")
	@Test
	public void testMaterializeClassOfTScope() {
		// Setup
		ArgumentCaptor<UUID> scopeId = ArgumentCaptor.forClass(UUID.class);
		ArgumentCaptor<Scope> scope = ArgumentCaptor.forClass(Scope.class);
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Class> cl = ArgumentCaptor.forClass(Class.class);
		// Test
		underTest.materialize(Closeable.class, Scope.APPLICATION);
		// Evaluation
		verify(underTest).materialize(cl.capture(), scope.capture(), scopeId.capture());
		assertEquals(Closeable.class, cl.getValue());
		assertEquals(Scope.APPLICATION, scope.getValue());
		assertNull(scopeId.getValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMaterializeClassOfT() {
		// Setup
		ArgumentCaptor<UUID> scopeId = ArgumentCaptor.forClass(UUID.class);
		ArgumentCaptor<Scope> scope = ArgumentCaptor.forClass(Scope.class);
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Class> cl = ArgumentCaptor.forClass(Class.class);
		// Test
		underTest.materialize(Closeable.class);
		// Evaluation
		verify(underTest).materialize(cl.capture(), scope.capture(), scopeId.capture());
		assertEquals(Closeable.class, cl.getValue());
		assertEquals(Scope.DEFAULT, scope.getValue());
		assertNull(scopeId.getValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMaterializeAnyClassOfTScope() {
		// Setup
		ArgumentCaptor<UUID> scopeId = ArgumentCaptor.forClass(UUID.class);
		ArgumentCaptor<Scope> scope = ArgumentCaptor.forClass(Scope.class);
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Class> cl = ArgumentCaptor.forClass(Class.class);
		// Test
		underTest.materializeAny(Closeable.class, Scope.APPLICATION);
		// Evaluation
		verify(underTest).materializeAny(cl.capture(), scope.capture(), scopeId.capture());
		assertEquals(Closeable.class, cl.getValue());
		assertEquals(Scope.APPLICATION, scope.getValue());
		assertNull(scopeId.getValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMaterializeAnyClassOfT() {
		// Setup
		ArgumentCaptor<UUID> scopeId = ArgumentCaptor.forClass(UUID.class);
		ArgumentCaptor<Scope> scope = ArgumentCaptor.forClass(Scope.class);
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Class> cl = ArgumentCaptor.forClass(Class.class);
		// Test
		underTest.materializeAny(Closeable.class);
		// Evaluation
		verify(underTest).materializeAny(cl.capture(), scope.capture(), scopeId.capture());
		assertEquals(Closeable.class, cl.getValue());
		assertEquals(Scope.DEFAULT, scope.getValue());
		assertNull(scopeId.getValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMaterializeAllClassOfTScope() {
		// Setup
		ArgumentCaptor<UUID> scopeId = ArgumentCaptor.forClass(UUID.class);
		ArgumentCaptor<Scope> scope = ArgumentCaptor.forClass(Scope.class);
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Class> cl = ArgumentCaptor.forClass(Class.class);
		// Test
		underTest.materializeAll(Closeable.class, Scope.APPLICATION);
		// Evaluation
		verify(underTest).materializeAll(cl.capture(), scope.capture(), scopeId.capture());
		assertEquals(Closeable.class, cl.getValue());
		assertEquals(Scope.APPLICATION, scope.getValue());
		assertNull(scopeId.getValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMaterializeAllClassOfT() {
		// Setup
		ArgumentCaptor<UUID> scopeId = ArgumentCaptor.forClass(UUID.class);
		ArgumentCaptor<Scope> scope = ArgumentCaptor.forClass(Scope.class);
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Class> cl = ArgumentCaptor.forClass(Class.class);
		// Test
		underTest.materializeAll(Closeable.class);
		// Evaluation
		verify(underTest).materializeAll(cl.capture(), scope.capture(), scopeId.capture());
		assertEquals(Closeable.class, cl.getValue());
		assertEquals(Scope.DEFAULT, scope.getValue());
		assertNull(scopeId.getValue());
	}

	protected static class MockFactory<T> implements Factory<T> {
		Class<T> cl;
		T mock;

		public MockFactory(Class<T> cl) {
			super();
			this.cl = cl;
			this.mock = mock(cl);
		}

		@Override
		public Class<T> getInterfaceType() {
			return cl;
		}

		@Override
		public T generate(ManagedContainer container, Scope scope) {
			return mock;
		}

	}

	@SuppressWarnings("rawtypes")
	protected static class CCFactory implements Factory<Map.Entry> {

		@Override
		public Class<Entry> getInterfaceType() {
			return Entry.class;
		}

		@Override
		public Entry<Object, Object> generate(ManagedContainer container, Scope scope) {
			Supplier s = container.materialize(Supplier.class, scope);
			return new AbstractMap.SimpleEntry<>(s.get(), container.materialize(Closeable.class));
		}
	}

}
