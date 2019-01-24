package bildverwaltung.container;

interface AddInterface {
    public int add(int a, int b);
}


class AddInterfaceImpl implements AddInterface {

    @Override
    public int add(int a, int b) {
        return a + b;
    }
}

class AddInterfaceFactory implements Factory<AddInterface> {

    @Override
    public Class<AddInterface> getInterfaceType() {
        return AddInterface.class;
    }

    @Override
    public AddInterface generate(ManagedContainer container, Scope scope) {
        return new AddInterfaceImpl();
    }
}



interface AddDoubleInterface {

    public double addDoubleAndInt(double a, int b);

    public double addDouble(double a, double b);

    public AddInterface getAddInterface();
}

class AddDoubleInterfaceImpl implements AddDoubleInterface {

    private AddInterface whyNot;

    public AddDoubleInterfaceImpl(AddInterface whyNot) {
        this.whyNot = whyNot;
    }

    @Override
    public double addDoubleAndInt(double a, int b) {
        return a + b;
    }

    @Override
    public double addDouble(double a, double b) {
        return a + b;
    }

    @Override
    public AddInterface getAddInterface() {
        return whyNot;
    }
}

class AddDoubleInterfaceFactory implements Factory<AddDoubleInterface> {

    @Override
    public Class<AddDoubleInterface> getInterfaceType() {
        return AddDoubleInterface.class;
    }

    @Override
    public AddDoubleInterface generate(ManagedContainer container, Scope scope) {
        AddInterface lul = container.materialize(AddInterface.class, scope);

        return new AddDoubleInterfaceImpl(lul);
    }

}

public class TestBaguette {

    public static void main(String [] args) {
        Factory<AddInterface> test = new AddInterfaceFactory();
        Factory<AddDoubleInterface> testDouble = new AddDoubleInterfaceFactory();
        ManagedContainer container = new ManagedContainerImpl();
        //ManagedContainer container = Container.getActiveContainer();

        container.addFactory(test,AddInterface.class);
        container.addFactory(testDouble, AddDoubleInterface.class);
        /*
        container.addFactory(new Factory<AddDoubleInterface>() {
            @Override
            public Class<AddDoubleInterface> getInterfaceType() {
                return AddDoubleInterface.class;
            }

            @Override
            public AddDoubleInterface generate(ManagedContainer container, Scope scope) {
                return new AddDoubleInterfaceImpl(new AddInterface() {
                    @Override
                    public int add(int a, int b) {
                        return a + b;
                    }
                });
            }
        }, AddDoubleInterface.class);
        */
        AddDoubleInterface test5 = container.materialize(AddDoubleInterface.class,Scope.APPLICATION);
        //AddDoubleInterface test6 = container.materialize(AddDoubleInterface.class,Scope.APPLICATION);
        AddInterface test2 = container.materialize(AddInterface.class, Scope.APPLICATION);
        AddInterface test3 = container.materialize(AddInterface.class, Scope.APPLICATION);
        AddInterface test4 = container.materialize(AddInterface.class, Scope.DEFAULT);

        System.out.println(test5.getAddInterface());
        System.out.println(test2);
        System.out.println(test3);
        System.out.println(test4);

        String equals1 = test2==test3 ? " ja":" nein";
        String equals2 = test3==test4 ? " ja":" nein";
        System.out.println("Ist test2==test3" + equals1); // Muss ja liefern
        System.out.println("Ist test3==test4?" + equals2 ); // Muss nein liefern

        //lets try giving it a non-interface

        try {
            container.addFactory(new Factory<String>() {
                @Override
                public Class<String> getInterfaceType() {
                    return String.class;
                }

                @Override
                public String generate(ManagedContainer container, Scope scope) {
                    return "haha";
                }
            }, String.class);
        } catch (ContainerException e) {
            System.out.println("Exception gefangen! hat geklappt.");
        }


    }
}
