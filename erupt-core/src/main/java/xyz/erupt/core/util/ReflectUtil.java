package xyz.erupt.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by liyuepeng on 11/7/18.
 */
public class ReflectUtil {

    //递归查找类字段
    public static Field findClassField(Class clazz, String fieldName) {
        Field field = null;
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
                if (clazz.getSimpleName().equals("Object")) {
                    break;
                }
            }
        }
        return field;
    }

    public static Object getFieldValue(Object obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    public static void findClassAllFields(Class clazz, Consumer<Field> fieldConsumer) {
        Class tempClass = clazz;
        while (null != tempClass) {
            for (Field field : tempClass.getDeclaredFields()) {
                fieldConsumer.accept(field);
            }
            tempClass = tempClass.getSuperclass();
        }
    }

    //获取字段泛型名
    public static List<String> getFieldGenericName(Field field) {
        List<String> names = new ArrayList<>();
        Type gType = field.getGenericType();
        if (gType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) gType).getActualTypeArguments();
            for (int i = 0; i < typeArguments.length; i++) {
                String[] gArray = typeArguments[i].getTypeName().split("\\.");
                names.add(gArray[gArray.length - 1]);
            }
        }
        return names;
    }
}
