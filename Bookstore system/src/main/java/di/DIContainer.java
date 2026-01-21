package di;

import di.annotation.Component;
import di.annotation.Inject;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DIContainer {
    private final Map<Class<?>, Object> singletonInstances = new HashMap<>();
    private final Map<String, Class<?>> componentClasses = new HashMap<>();

    public void registerBean(Class<?> clazz) {
        if(!clazz.isAnnotationPresent(Component.class)) {
            throw new IllegalArgumentException("Component class must exist");
        }
        Component component = clazz.getAnnotation(Component.class);
        String name = component.value().isEmpty() ? clazz.getSimpleName() : component.value();
        componentClasses.put(name, clazz);
    }

    public void registerBeans(Set<Class<?>> classes) {
        for(Class<?> clazz : classes) {
            registerBean(clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        if(singletonInstances.containsKey(clazz)) {
            return (T) singletonInstances.get(clazz);
        }

        Class<?> implClass = null;
        for(Class<?> c : componentClasses.values()) {
            if(clazz.isAssignableFrom(c)) {
                implClass = c;
                break;
            }
        }
        if(implClass == null) {
            throw new IllegalArgumentException("No implementation for " + clazz);
        }

        T instance = createInstance(clazz);
        singletonInstances.put(clazz, instance);
        if (clazz != implClass) {
            singletonInstances.put(clazz, instance);
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstance(Class<T> clazz) {
        try {
            Constructor<?> constructor = findInjectConstructor(clazz);

            Class<?>[] params = constructor.getParameterTypes();
            Object[] args = new Object[params.length];
            for(int i = 0; i < params.length; i++) {
                args[i] = getBean(params[i]);
            }

            return (T) constructor.newInstance(args);

        } catch (Exception e) {
            throw new IllegalArgumentException("No constructor found for " + clazz, e);
        }
    }

    private Constructor<?> findInjectConstructor(Class<?> clazz) {
        Constructor<?> injectConstructor = null;
        Constructor<?> defaultConstructor = null;
        Constructor<?>[] allConstructors = clazz.getDeclaredConstructors();

        for (Constructor<?> c : allConstructors) {
            if (c.isAnnotationPresent(Inject.class)) {
                injectConstructor = c;
            }
            if (c.getParameterCount() == 0) {
                defaultConstructor = c;
            }
        }

        if (injectConstructor != null) {
            injectConstructor.setAccessible(true);
            return injectConstructor;
        }
        if (defaultConstructor != null) {
            defaultConstructor.setAccessible(true);
            return defaultConstructor;
        }
        if (allConstructors.length == 1) {
            allConstructors[0].setAccessible(true);
            return allConstructors[0];
        }

        throw new RuntimeException("No suitable constructor found in " + clazz.getSimpleName() +
                ". Either:\n" +
                " - Add @Inject to one constructor, or\n" +
                " - Provide a no-args constructor, or\n" +
                " - Keep only one constructor.");
    }

}
