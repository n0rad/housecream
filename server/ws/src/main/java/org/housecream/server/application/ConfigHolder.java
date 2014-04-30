package org.housecream.server.application;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import org.housecream.server.api.domain.config.Config;
import org.housecream.server.api.domain.config.configDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import fr.norad.jaxrs.doc.ModelProcessorFactory;
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.api.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.api.domain.PropertyDefinition;
import fr.norad.jaxrs.doc.parser.ModelJacksonParser;

@Component
public class ConfigHolder {

    private Map<Class<?>, Object> configObjects = new ConcurrentHashMap<>();
    private Map<String, String> configData = new ConcurrentHashMap<>();

    private ModelProcessorFactory modelFactory = new ModelProcessorFactory();
    private ModelJacksonParser modelParser = new ModelJacksonParser();

    private Object findValue(PropertyAccessor accessor, Object model) {
        Object value = null;
        try {
            value = accessor.getGetter().invoke(model);
        } catch (Exception e) {
            if (accessor.getField() != null) {
                accessor.getField().setAccessible(true);
                try {
                    value = accessor.getField().get(model);
                } catch (IllegalAccessException e1) {
                }
            }
        }
        return value;
    }

    public Map<String, Object> getConfigValues() {
        Map<String, Object> values = new HashMap<>();
        for (Object config : configObjects.values()) {
            List<PropertyAccessor> properties = modelParser.findProperties(config.getClass());
            for (PropertyAccessor accessor : properties) {
                values.put(accessor.getName(), findValue(accessor, config));
            }
        }
        return values;
    }

    public configDefinition getConfigDefinition() {
        configDefinition configDefinition = new configDefinition();

        ProjectDefinition project = new ProjectDefinition();
        for (Object configObject : configObjects.values()) {
            modelFactory.getModelProcessor().process(project, configObject.getClass());
            Map<String, PropertyDefinition> properties = project.getModels().get(configObject.getClass().getName()).getProperties();
            for (String key : properties.keySet()) {
                properties.get(key).createdExtras().put("Group", configObject.getClass());
                configDefinition.getProperties().put(key, properties.get(key));
            }
            configDefinition.getModels().putAll(project.getModels());
            configDefinition.getModels().remove(configObject.getClass().getName());
        }
        return configDefinition;
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
