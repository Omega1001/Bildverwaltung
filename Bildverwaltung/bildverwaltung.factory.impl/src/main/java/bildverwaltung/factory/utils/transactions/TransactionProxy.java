package bildverwaltung.factory.utils.transactions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.dao.helper.DBTransactionBorder;

public class TransactionProxy implements InvocationHandler {

	public static <E> E proxyFor(E actual, Class<E> interfaceType, EntityManager em) {
		return proxyFor(actual, interfaceType, em, false, false);
	}

	@SuppressWarnings("unchecked")
	public static <E> E proxyFor(E actual, Class<E> interfaceType, EntityManager em, boolean implicitStart,
			boolean implicitEnd) {
		return (E) Proxy.newProxyInstance(actual.getClass().getClassLoader(), new Class<?>[] { interfaceType },
				new TransactionProxy(actual, em, implicitStart, implicitEnd));
	}

	private final Object actual;
	private final EntityManager em;

	private boolean implicitStart;
	private boolean implicitEnd;

	private TransactionProxy(Object actual, EntityManager em, boolean implicitStart, boolean implicitEnd) {
		this.actual = actual;
		this.em = em;
		this.implicitStart = implicitStart;
		this.implicitEnd = implicitEnd;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			return method.invoke(actual, args);
		} else {
			return invokeTransactional(proxy, method, args);
		}
	}

	private Object invokeTransactional(Object proxy, Method method, Object[] args) throws Throwable {
		boolean isBorder = actual.getClass().getMethod(method.getName(), method.getParameterTypes())
				.isAnnotationPresent(DBTransactionBorder.class);
		// method.proxy.getClass().isAnnotationPresent(DBTransactionBorder.class);
		boolean hasStarted = handleBeginTransaction(isBorder);
		try {
			Object result = method.invoke(actual, args);
			if (implicitEnd || isBorder) {
				handleEndTransaction(isBorder, hasStarted);
			} else {
				// This might cause an exception other then InvocationTargetException
				// If so, throw it, so the program can react to it, and set the rollback options
				handleFlush();
			}
			return result;
		} catch (InvocationTargetException ex) {
			throw handleRollback(ex.getTargetException());
		}
	}

	private void handleFlush() {
		if (em.getTransaction().isActive()) {
			em.flush();
		}
	}

	private Throwable handleRollback(Throwable th) {
		if (em.getTransaction().isActive()) {
			if (th instanceof FacadeException) {
				FacadeException fe = (FacadeException) th;
				if (fe.isRollback()) {
					try {
						em.getTransaction().rollback();
					} catch (PersistenceException e) {
						th.addSuppressed(e);
					}
				}
			} else if (th instanceof RuntimeException) {
				try {
					em.getTransaction().rollback();
				} catch (PersistenceException e) {
					th.addSuppressed(e);
				}
			}
		}
		return th;
	}

	private void handleEndTransaction(boolean isBorder, boolean hasStarted) {
		if (implicitEnd || (isBorder && hasStarted)) {
			if (em.getTransaction().isActive()) {
				em.flush();
				em.getTransaction().commit();
				em.clear();
				em.getEntityManagerFactory().getCache().evictAll();
			}
		}
	}

	private boolean handleBeginTransaction(boolean isBorder) {
		if (isBorder || implicitStart) {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
				return true;
			}
		}
		return false;
	}

}
