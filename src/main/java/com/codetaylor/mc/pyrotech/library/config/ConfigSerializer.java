package com.codetaylor.mc.pyrotech.library.config;

import com.codetaylor.mc.athenaeum.util.FileHelper;
import net.minecraftforge.common.config.Config;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ConfigSerializer {

  public static final ConfigSerializer INSTANCE = new ConfigSerializer();

  private static final boolean DEBUG = true;

  public void deserialize(Map<String, Map<String, byte[]>> configMap, Logger logger) {

    for (Map.Entry<String, Map<String, byte[]>> entry : configMap.entrySet()) {

      String className = entry.getKey();
      Map<String, byte[]> fieldMap = entry.getValue();

      Class<?> configClass;

      try {
        configClass = Class.forName(className);

        if (DEBUG) {
          System.out.println("De-serializing class " + configClass.getName());
        }

      } catch (ClassNotFoundException e) {
        logger.error("Error retrieving config class for name: " + className, e);
        continue;
      }

      Field[] declaredFields = configClass.getDeclaredFields();

      for (Map.Entry<String, byte[]> fieldEntry : fieldMap.entrySet()) {

        String fieldName = "_" + fieldEntry.getKey();
        byte[] bytes = fieldEntry.getValue();

        Field declaredField = this.findDeclaredField(declaredFields, fieldName);

        if (declaredField == null) {

          if (DEBUG) {
            System.out.println("Unable to locate field for " + fieldName);
          }
          continue;
        }

        if (Modifier.isStatic(declaredField.getModifiers())) {
          throw new RuntimeException("Field is not static: " + fieldName);
        }

        Object o = this.deserializeObject(bytes, logger);

        if (o != null) {

          try {
            declaredField.set(null, o);

            if (DEBUG) {
              System.out.println("Assigned field " + fieldName);
            }

          } catch (IllegalAccessException e) {
            logger.error("Error assigning config declared field", e);
          }
        }

        break;
      }
    }

  }

  @Nullable
  private Field findDeclaredField(Field[] declaredFields, String fieldName) {

    for (Field declaredField : declaredFields) {

      if (declaredField.getName().equals(fieldName)) {
        return declaredField;
      }
    }

    return null;
  }

  public Map<String, Map<String, byte[]>> serialize(Class<?>[] configClasses, Logger logger) {

    Map<String, Map<String, byte[]>> serializedConfigs = new HashMap<>();

    for (Class<?> configClass : configClasses) {
      Map<String, byte[]> serializedConfig = this.serializeConfig(configClass, logger);

      if (!serializedConfig.isEmpty()) {
        serializedConfigs.put(configClass.getName(), serializedConfig);
      }
    }

    return serializedConfigs;
  }

  private Map<String, byte[]> serializeConfig(Class<?> configClass, Logger logger) {

    Field[] declaredFields = configClass.getDeclaredFields();
    Map<String, byte[]> fieldBytesMap = new HashMap<>();

    for (Field declaredField : declaredFields) {

      if (declaredField.getAnnotation(Config.Ignore.class) != null) {
        // Skip any fields annotated with the config ignore directive

        if (DEBUG) {
          System.out.println("Has Config.Ignore annotation, skipping: " + declaredField.getName());
        }
        continue;
      }

      SyncConfigField annotation = declaredField.getAnnotation(SyncConfigField.class);

      if (annotation == null) {
        // Skip any fields not annotated with sync intent

        if (DEBUG) {
          System.out.println("Doesn't have SyncConfigField annotation, skipping: " + declaredField.getName());
        }
        continue;
      }

      // Field must be static
      if (Modifier.isStatic(declaredField.getModifiers())) {

        byte[] bytes = this.serializeStaticField(declaredField, logger);

        if (bytes.length > 0) {
          fieldBytesMap.put(annotation.value(), bytes);

          if (DEBUG) {
            System.out.println("Serialized " + declaredField.getName() + " to " + bytes.length + " bytes");
          }
        }
      }
    }

    return fieldBytesMap;
  }

  private byte[] serializeStaticField(Field field, Logger logger) {

    if (Modifier.isStatic(field.getModifiers())) {

      try {
        return this.serializeObject(field.get(null), logger);

      } catch (IllegalAccessException e) {
        logger.error("Error serializing config field: " + field.getDeclaringClass() + "." + field.getName(), e);
      }
    }

    return new byte[0];
  }

  public byte[] serializeObject(Object o, Logger logger) {

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out;

    try {
      out = new ObjectOutputStream(bos);
      out.writeObject(o);
      out.flush();
      return bos.toByteArray();

    } catch (IOException e) {
      logger.error("Error serializing object", e);

    } finally {
      FileHelper.closeSilently(bos);
    }

    return new byte[0];
  }

  public <T> T deserializeObject(byte[] bytes, Logger logger) {

    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    ObjectInput in;

    try {
      in = new ObjectInputStream(bis);
      //noinspection unchecked
      return (T) in.readObject();

    } catch (IOException | ClassNotFoundException e) {
      logger.error("Error de-serializing object", e);

    } finally {
      FileHelper.closeSilently(bis);
    }

    return null;
  }

  private ConfigSerializer() {
    //
  }
}
