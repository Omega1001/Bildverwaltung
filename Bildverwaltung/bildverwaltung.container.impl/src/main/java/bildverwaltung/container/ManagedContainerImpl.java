package bildverwaltung.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ManagedContainerImpl implements ManagedContainer {

    //private Map<Scope,ScopeContainer> scopeContainer;
    private Map<Scope,ScopeContainer> scopeContainer;
    private Map<Class<?>,List<Factory<?>>> factories; //save in (interface, factory) pairs

    private UUID creationalContextScopeId;

    private static final Logger LOG = LoggerFactory.getLogger(ManagedContainerImpl.class);

    public ManagedContainerImpl() {
        factories = new HashMap<Class<?>, List<Factory<?>>>();
        scopeContainer = new HashMap<>();
        scopeContainer.put(Scope.APPLICATION, new ScopeContainerImpl(Scope.APPLICATION));
    }

    @Override
    public <T> T materialize(Class<T> interfaceClass, Scope scope, UUID scopeId) {

        List<Factory<?>> desiredFactory = factories.get(interfaceClass);

        if (desiredFactory == null) {
            LOG.error("given class does not have a factory");
            throw new ContainerException("given class does not have a factory");
        } else if (desiredFactory.size() > 1) {
            // materialize method only usable when there is only one factory max for an given interface
            LOG.error("ambiguous materialize: more than one factory for this interface");
            throw new ContainerException("ambiguous materialize: more than one factory for this interface");

            // differentiate the different scopes
        } else {
            @SuppressWarnings("unchecked")
			Factory<T> singleFactory = (Factory<T>) desiredFactory.get(0);

            if (scope == Scope.DEFAULT) {
                LOG.debug("given scope is {} so a new object is given",scope);
				T res = singleFactory.generate(this, scope);
                return res;

            } else if (scope == Scope.APPLICATION) {

                ScopeContainer container;
                boolean hadCCSId;

                // scopeId = null gives the primary scope
                if (scopeId == null) {
                    container = scopeContainer.get(Scope.APPLICATION);
                } else {
                    container = scopeContainer.get(Scope.APPLICATION).getSubScope(scopeId);
                }

                // Was CreationalContextSubScope already created?
                if (creationalContextScopeId != null) {

                    LOG.debug("- CCS was already created");
                    hadCCSId = true;

                } else {

                    LOG.debug("- CCS is not created so it will be created");
                    hadCCSId = false;
                    //creationalContextScopeId = scopeContainer.get(scopeId).beginSubScope();
                    creationalContextScopeId = beginCustomScope(scope);

                }

                // First try to get from creationalContext
				T wantedObject = container
                        .getImplementationForFactory(singleFactory, creationalContextScopeId);

                // If it does not work, try to get from primary Scope
                if (wantedObject == null) {
                    wantedObject = container.getImplementationForFactory(singleFactory);


                    // IF it also not works, generate it
                    if (wantedObject == null) {
                        wantedObject = singleFactory.generate(this, scope);
                    }
                }

                container.setImplementationForFactory(wantedObject, singleFactory);
                container.setImplementationForFactory(wantedObject, singleFactory, creationalContextScopeId);

                if (!hadCCSId) {
                    LOG.debug("+ CCS subScope will be destroyed");
                    container.endSubScope(creationalContextScopeId);
                    creationalContextScopeId = null;
                } else {
                    LOG.debug("+ CCS subScope does not need to be destroyed");
                }


                LOG.info("Object {} is materialized.",wantedObject);
                return wantedObject;

            } else {
                LOG.warn("Given scope is unknown. New added scope or why is it not implemented yet?");
                return null;
            }
        }
    }

    @Override
    public <T> T materialize(Class<T> interfaceClass, Scope scope) {
        return materialize(interfaceClass, scope, null);
    }

    @Override
    public <T> T materialize(Class<T> interfaceClass) {
        return materialize(interfaceClass, Scope.DEFAULT);
    }

	@Override
    public <T> T materializeAny(Class<T> interfaceClass, Scope scope, UUID scopeId) {

        List<Factory<?>> desiredFactory = factories.get(interfaceClass);

        if (desiredFactory == null) {
            LOG.error("given class does not have a factory");
            throw new ContainerException("given class does not have a factory");

            // differentiate the different scopes
        } else {
            @SuppressWarnings("unchecked")
            Factory<T> singleFactory = (Factory<T>) desiredFactory.get(0);

            if (scope == Scope.DEFAULT) {
                LOG.debug("given scope is {} so a new object is given",scope);
                T res = singleFactory.generate(this, scope);
                return res;

            } else if (scope == Scope.APPLICATION) {

                ScopeContainer container;
                boolean hadCCSId;

                // scopeId = null gives the primary scope
                if (scopeId == null) {
                    container = scopeContainer.get(Scope.APPLICATION);
                } else {
                    container = scopeContainer.get(Scope.APPLICATION).getSubScope(scopeId);
                }

                // Was CreationalContextSubScope already created?
                if (creationalContextScopeId != null) {

                    LOG.debug("- CCS was already created");
                    hadCCSId = true;

                } else {

                    LOG.debug("- CCS is not created so it will be created");
                    hadCCSId = false;
                    //creationalContextScopeId = scopeContainer.get(scopeId).beginSubScope();
                    creationalContextScopeId = beginCustomScope(scope);

                }

                // First try to get from creationalContext
                T wantedObject = container
                        .getImplementationForFactory(singleFactory, creationalContextScopeId);

                // If it does not work, try to get from primary Scope
                if (wantedObject == null) {
                    wantedObject = container.getImplementationForFactory(singleFactory);


                    // IF it also not works, generate it
                    if (wantedObject == null) {
                        wantedObject = singleFactory.generate(this, scope);
                    }
                }

                container.setImplementationForFactory(wantedObject, singleFactory);
                container.setImplementationForFactory(wantedObject, singleFactory, creationalContextScopeId);

                if (!hadCCSId) {
                    LOG.debug("+ CCS subScope will be destroyed");
                    container.endSubScope(creationalContextScopeId);
                    creationalContextScopeId = null;
                } else {
                    LOG.debug("+ CCS subScope does not need to be destroyed");
                }


                LOG.info("Object {} is materialized.",wantedObject);
                return wantedObject;

            } else {
                LOG.warn("Given scope is unknown. New added scope or why is it not implemented yet?");
                return null;
            }
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

        List<Factory<?>> desiredFactory = factories.get(interfaceClass);
        List<T> resList = new ArrayList<T>();

        if (desiredFactory == null) {
            LOG.error("given class does not have a factory");
            throw new ContainerException("given class does not have a factory");

            // differentiate the different scopes
        } else {


            if (scope == Scope.DEFAULT) {
                desiredFactory.forEach(singleFactory-> {
                    LOG.debug("given scope is {} so a new object is given", scope);
                    //unchecked
                    T res = (T) singleFactory.generate(this, scope);
                    resList.add(res);
                });

            } else if (scope == Scope.APPLICATION) {
                desiredFactory.forEach(singleFactory-> {
                    ScopeContainer container;
                    boolean hadCCSId;

                    // scopeId = null gives the primary scope
                    if (scopeId == null) {
                        container = scopeContainer.get(Scope.APPLICATION);
                    } else {
                        container = scopeContainer.get(Scope.APPLICATION).getSubScope(scopeId);
                    }

                    // Was CreationalContextSubScope already created?
                    if (creationalContextScopeId != null) {

                        LOG.debug("- CCS was already created");
                        hadCCSId = true;

                    } else {

                        LOG.debug("- CCS is not created so it will be created");
                        hadCCSId = false;
                        //creationalContextScopeId = scopeContainer.get(scopeId).beginSubScope();
                        creationalContextScopeId = beginCustomScope(scope);

                    }

                    // First try to get from creationalContext
                    //unchecked
                    T wantedObject = (T) container
                            .getImplementationForFactory(singleFactory, creationalContextScopeId);

                    // If it does not work, try to get from primary Scope
                    if (wantedObject == null) {
                        //unchecked
                        wantedObject = (T) container.getImplementationForFactory(singleFactory);


                        // IF it also not works, generate it
                        if (wantedObject == null) {
                            //unchecked
                            wantedObject = (T) singleFactory.generate(this, scope);
                        }
                    }

                    container.setImplementationForFactory(wantedObject, singleFactory);
                    container.setImplementationForFactory(wantedObject, singleFactory, creationalContextScopeId);

                    if (!hadCCSId) {
                        LOG.debug("+ CCS subScope will be destroyed");
                        container.endSubScope(creationalContextScopeId);
                        creationalContextScopeId = null;
                    } else {
                        LOG.debug("+ CCS subScope does not need to be destroyed");
                    }


                    LOG.info("Object {} is materialized.", wantedObject);
                    resList.add(wantedObject);
                });

            } else {
                LOG.warn("Given scope is unknown. New added scope or why is it not implemented yet?");
                return null;
            }

        }
        return resList;
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

        if(!targetInterface.isInterface()) {
            LOG.error("given interface is not actually a interface");
            throw new ContainerException("given interface is not actually a interface");
        }

        List<Factory<?>> list = factories.get(targetInterface);

        if(list == null) {
            list = new ArrayList<Factory<?>>();
        }
        if(!list.contains(factory)) {
               list.add(factory);
        }


        factories.put(targetInterface, list);

        System.out.println(factories.get(String.class));
    }
    
    @Override
    public <T> void addFactory(Factory<?> factory) {

        Class interfaceClass = factory.getInterfaceType();
        addFactory(factory, interfaceClass);

    }

    @Override
    public UUID beginCustomScope(Scope scope) {
        if(scope == Scope.DEFAULT) {
            throw new ContainerException("The given scope " + scope.name() + " does not support any subscope");
        } else if(scope == Scope.APPLICATION){
            UUID newScopeId = scopeContainer.get(scope).beginSubScope();
            return  newScopeId;
        } else {
            LOG.error("unknown scope {} or scope has currently no implementation for subScopes.",scope);
            throw new ContainerException("unknown scope");
        }
    }

    @Override
    public void endCustomScope(Scope scope, UUID scopeId) {
        if(scope == Scope.DEFAULT) {
            throw new ContainerException("The given scope " + scope.name() + " does not support any subscope so there is nothing to close");
        } else {
            scopeContainer.get(scope).endSubScope(scopeId);
		}
    }
}
