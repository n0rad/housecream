package org.housecream.server.application;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import org.housecream.server.api.domain.config.Config;
import org.housecream.server.api.domain.config.PropertyDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import fr.norad.jaxrs.doc.api.Description;
import fr.norad.jaxrs.doc.utils.DocUtils;

@Component
public class ConfigHolder {

    private Map<Class<?>, Object> configObjects = new ConcurrentHashMap<>();
    private Map<String, String> configData = new ConcurrentHashMap<>();

    public Set<PropertyDefinition> getPropertiesDefinition() {
        Set<PropertyDefinition> propertyDefinitions = new HashSet<>();
        Map<Class<?>, Object> defaultConfigs = new HashMap<>();

        for (Object o : configObjects.values()) {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor descriptor : propertyDescriptors) {
                    if ("class".equals(descriptor.getName())) {
                        continue;
                    }
                    if (defaultConfigs.get(o.getClass()) == null) {
                        defaultConfigs.put(o.getClass(), o.getClass().newInstance());
                    }
                    propertyDefinitions.add(buildDefinition(o, defaultConfigs.get(o.getClass()), descriptor));
                }
            } catch (Exception e) {
                throw new IllegalStateException("Cannot introspect configuration class", e);
            }
        }
        return propertyDefinitions;
    }

    public PropertyDefinition buildDefinition(Object object, Object defaultObject, PropertyDescriptor descriptor) throws Exception {
        PropertyDefinition propertyDefinition = new PropertyDefinition();
        propertyDefinition.setName(descriptor.getName());
        propertyDefinition.setType(descriptor.getPropertyType());
        Field field = ReflectionUtils.findField(object.getClass(), descriptor.getName());
        String description = null;
        if (field == null) {
            Description declaredAnnotation = descriptor.getReadMethod().getDeclaredAnnotation(Description.class);
            if (declaredAnnotation != null) {
                description = DocUtils.getDescription(declaredAnnotation);
            }
        } else {
            Description declaredAnnotation = field.getDeclaredAnnotation(Description.class);
            if (declaredAnnotation != null) {
                description = DocUtils.getDescription(declaredAnnotation);
            }
        }
        propertyDefinition.setDescription(description);
        Object value = descriptor.getReadMethod().invoke(object);
        if (value != null) {
            propertyDefinition.setValue(value.toString());
        }
        Object defaultValue = descriptor.getReadMethod().invoke(defaultObject);
        if (defaultValue != null) {
            propertyDefinition.setDefaultValue(defaultValue.toString());
        }
        propertyDefinition.setGroup(object.getClass());
        return propertyDefinition;
    }

    public <T extends Config> T getConfigObject(Class<T> resultClass) {
        if (configObjects.get(resultClass) == null) {
            Object o = buildConfig(resultClass);
            setObjectValues(o, configData);
            configObjects.put(resultClass, o);
        }
        return (T) configObjects.get(resultClass);
    }

    public void setValues(Map<String, String> map) {
        configData.putAll(map);
        for (Object o : configObjects.values()) {
            setObjectValues(o, map);
        }

        for (String key : map.keySet()) {
            setValue(key, map.get(key));
        }
    }

    public void setValue(String name, String value) {
        configData.put(name, value);
        for (Object o : configObjects.values()) {
            setObjectValue(o, name, value);
        }
    }

    ///////////////////////

    private <T extends Config> Object buildConfig(Class<T> resultClass) {
        try {
            return resultClass.getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    private void setObjectValues(Object o, Map<String, String> map) {
        for (String name : map.keySet()) {
            setObjectValue(o, name, map.get(name));
        }
    }

    private void setObjectValue(Object o, String name, String value) {
        try {
            Field field = ReflectionUtils.findField(o.getClass(), name);
            if (field == null) {
                return;
            }
            Class<?> type = field.getType();
            ReflectionUtils.makeAccessible(field);
            if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
                field.setInt(o, Integer.parseInt(value));
            } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
                field.set(o, Boolean.parseBoolean(value));
            } else if (type.equals(Locale.class)) {
                String[] split = value.split("_");
                field.set(o, new Locale(split[0], split[1]));
            } else if (type.equals(TimeZone.class)) {
                field.set(o, TimeZone.getTimeZone(value));
            } else if (type.isEnum()) {
                field.set(o, Enum.valueOf((Class<? extends Enum>) type, value));
            } else {
                field.set(o, value);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
