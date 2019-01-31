package bildverwaltung.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestDBEntityManagerFactory {
	
	private EntityManagerFactory factory = null;
	private EntityManager currentlyOpened = null;

	public TestDBEntityManagerFactory() {
		
		build();
	}
	
	private void build() {
		Map<String,String> properties = new HashMap<>();
		properties.put("javax.persistence.jdbc.url", "jdbc:h2:mem:");
		properties.put("javax.persistence.jdbc.driver", "org.h2.Driver");
		factory = Persistence.createEntityManagerFactory("Domain Modell", properties);
		currentlyOpened = factory.createEntityManager();
	}
	
	public EntityManager get() {
		return (EntityManager) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] {EntityManager.class}, new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if(currentlyOpened == null) {
					throw new IllegalStateException("Factory already closed");
				}
				return method.invoke(currentlyOpened, args);
			}
		});
	}
	
	public void reset() {
		close();
		build();
	}
	
	public void close() {
		if(currentlyOpened != null) {
			currentlyOpened.close();
			currentlyOpened = null;
		}
		factory.close();
		factory = null;
	}

}
