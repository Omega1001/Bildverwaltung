package bildverwaltung.container;

import java.util.*;

public class ManagedContainerImpl implements ManagedContainer {

    private Map<Scope,ScopeContainer> scopeContainer;
    Map<Class,List<Factory<?>>> factories; //save in (interface, factory) pairs

    public ManagedContainerImpl() {
        factories = new HashMap<Class, List<Factory<?>>>();
        scopeContainer = new HashMap<>();
    }

    @Override
    public <T> T materialize(Class<T> interfaceClass, Scope scope, UUID scopeId) {

        switch(scope) {
            case APPLICATION:

                if(scopeId != null) {
                    throw new ContainerException("no subscopes allowed in APPLICATION Scope");
                } else {

                    ScopeContainer container = scopeContainer.get(scope);
                    List desiredFactory = factories.get(interfaceClass);

                    if(desiredFactory == null) {
                        throw new ContainerException("given class does not have a factory");
                    }

                    if(desiredFactory.size() > 1) {
                        throw new ContainerException("ambiguous materialize: more than one factory for this interface");
                    }

                    if(!container.hasImplementationForFactory( (Factory) desiredFactory.get(0))) {
                        throw new ContainerException("Scope does not have a implementation for this interface");
                    } else {
                        return (T) container.getImplementationForFactory( (Factory) desiredFactory.get(0));
                    }
                }

            case DEFAULT:

                if(scopeId != null) {
                    throw new ContainerException("no subscopes allowed in DEFAULT Scope");
                } else {

                    List desiredFactory = factories.get(interfaceClass);

                    if (desiredFactory == null) {
                        throw new ContainerException("given class does not have a factory");
                    }else if(desiredFactory.size() > 1) {

                    	// materialize only usable when there is only one factory max for an given interface
                        throw new ContainerException("ambiguous materialize: more than one factory for this interface");

                    } else {

                        Factory factory =  (Factory) desiredFactory.get(0);
                        return (T) factory.generate(this, Scope.DEFAULT);

                    }
                }

            default:
            	return null;
        }
    }

    @Override
    public <T> T materialize(Class<T> interfaceClass, Scope scope) {
        return materialize(interfaceClass, scope, null);
    }

    @Override
    public <T> T materialize(Class<T> interfaceClass) {
        return materialize(interfaceClass, null);
    }

    @Override
    public <T> T materializeAny(Class<T> interfaceClass, Scope scope, UUID scopeId) {

        switch(scope) {
            case APPLICATION:

                if(scopeId != null) {
                    throw new ContainerException("no subscopes allowed in APPLICATION Scope");
                } else {

                    ScopeContainer container = scopeContainer.get(scope);
                    List desiredFactory = factories.get(interfaceClass);

                    if(desiredFactory == null) {
                        throw new ContainerException("given class does not have a factory");
                    }

                    if(desiredFactory.size() > 1) {
                        throw new ContainerException("ambiguous materialize: more than one factory for this interface");
                    }

                    if(!container.hasImplementationForFactory( (Factory) desiredFactory.get(0))) {
                        throw new ContainerException("Scope does not have a implementation for this interface");
                    } else {
                        return (T) container.getImplementationForFactory((Factory) desiredFactory.get(0));
                    }
                }

            case DEFAULT:

                if(scopeId != null) {
                    throw new ContainerException("no subscopes allowed in DEFAULT Scope");
                } else {

                    List desiredFactory = factories.get(interfaceClass);

                    if (desiredFactory == null) {
                    	throw new ContainerException("given class does not have a factory");
                    } else {

                        Factory factory =  (Factory) desiredFactory.get(0);
                        return (T) factory.generate(this, Scope.DEFAULT);

                    }
                }

            default:
        		return null;
        }
    }

    @Override
    public <T> T materializeAny(Class<T> interfaceClass, Scope scope) {
        return materializeAny(interfaceClass, scope, null);


    }

    @Override
    public <T> T materializeAny(Class<T> interfaceClass) {
        return materializeAny(interfaceClass, Scope.DEFAULT, null);
    }

    @Override
    public <T> List<T> materializeAll(Class<T> interfaceClass, Scope scope, UUID scopeId) {


        switch(scope) {
            case APPLICATION:

                if(scopeId != null) {

                    throw new ContainerException("no subscopes allowed in APPLICATION Scope");

                } else {

                    ScopeContainer container = scopeContainer.get(scope);

                    List desiredFactory = factories.get(interfaceClass);

                    if(desiredFactory == null) {

                        throw new ContainerException("given class does not have a factory");

                    }

                    if(desiredFactory.size() > 1) {

                        throw new ContainerException("ambigous materialize: more than one factory for this interface");

                    }

                    if(!container.hasImplementationForFactory( (Factory) desiredFactory.get(0))) {

                        throw new ContainerException("Scope does not have a implementation for this interface");

                    } else {

                        List<T> implementations = new ArrayList();

                        for(int i = 0; i < desiredFactory.size(); i++) {

                            Factory curFactory = (Factory) desiredFactory.get(i);
                            implementations.add((T) container.getImplementationForFactory(curFactory));

                        }

                        return implementations;

                    }
                }

            case DEFAULT:

                if(scopeId != null) {
                    throw new ContainerException("no subscopes allowed in DEFAULT Scope");
                } else {

                    List desiredFactory = factories.get(interfaceClass);

                    if (desiredFactory == null) {
                        throw new ContainerException("given class does not have a factory");
                    }else if(desiredFactory.size() > 1) {
                        throw new ContainerException("ambiguous materialize: more than one factory for this interface");
                    } else {

                    	List<T> implementations = new ArrayList();

                        for(int i = 0; i < desiredFactory.size(); i++) {

                            Factory curFactory = (Factory) desiredFactory.get(i);
                            implementations.add((T) curFactory.generate(this,Scope.DEFAULT));

                        }

                        return implementations;
                    }
                }

            default:
                return null;
        }
    }

    @Override
    public <T> List<T> materializeAll(Class<T> interfaceClass, Scope scope) {
        return materializeAll(interfaceClass,scope, null);
    }

    @Override
    public <T> List<T> materializeAll(Class<T> interfaceClass) {
        return materializeAll(interfaceClass,Scope.DEFAULT,null);
    }

    @Override
    public <T> void addFactory(Factory<T> factory, Class<T> targetInterface) {

        List<Factory<?>> list = factories.get(targetInterface);

        if(list == null) {
            list = new ArrayList<Factory<?>>();
        } else {
           if(!list.contains(factory)) {
               list.add(factory);
           }
        }

        factories.put(targetInterface, list);
    }
    
    @Override
    public <T> void addFactory(Factory<?> factory) {

        Class interfaceClass = factory.getInterfaceType();
        addFactory(factory, interfaceClass);

    }

    @Override
    public UUID beginCustomScope(Scope scope) {
        if(scope == Scope.APPLICATION || scope == Scope.DEFAULT) {
            throw new ContainerException("The given scope " + scope.name() + " does not support any subscope");
        } else {
            //TODO add a functioning implementation for this
            return null;
        }
    }

    @Override
    public void endCustomScope(Scope scope, UUID scopeId) {
        if(scope == Scope.APPLICATION || scope == Scope.DEFAULT) {
            throw new ContainerException("The given scope " + scope.name() + " does not support any subscope so there is nothing to close");
        } else {

		}
    }
}
