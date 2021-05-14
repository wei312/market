package com.market.base.framework.utils;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;

@Slf4j
public class BeanUtil {

    /**
     * 通过反射调用对像的方法
     *
     * @param obj 对像
     * @param methodName
     * @param params
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(Object obj, String methodName, Object... params)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return MethodUtils.invokeMethod(obj, methodName, params);
    }

    /**
     * 判断对像的属性是否有对像的get方法
     *
     * @param methods
     * @param fieldName
     * @param typeName 是get或set
     * @return Method如果有则返回method对像，没有则返回null
     */
    public static Method hasFieldMethod(List<Method> methods, String fieldName, String typeName) {
        for (Method m : methods) {
            String methodName = m.getName().toLowerCase();
//    		System.out.println("methodName="+methodName);
            if (null != fieldName && methodName.equals("get" + fieldName.toLowerCase())) { //方法名与属性名加一个 get相等时从方法名中取返回值
                return m;
            }
        }
        return null;
    }

    /**
     * 通过反谢获取对像的属性值
     *
     * @param obj
     * @param fieldName
     * @return
     * @throws Exception
     */
    public static Object getPropertyValue(Object obj, String fieldName) throws Exception {
        return BeanUtils.getProperty(obj, fieldName);
//    	return FieldUtils.readField(obj, fieldName,true);
//    	return MethodUtils.invokeMethod(obj, getMethodNameByFieldName(fieldName,false),null);
    }

    /**
     * 通过反谢设置对像的属性值
     *
     * @param obj
     * @param fieldName 属性名
     * @param fieldValue 要设置的值
     * @throws Exception
     */
    public static void setPropertyValue(Object obj, String fieldName, Object fieldValue) throws Exception {
        BeanUtils.setProperty(obj, fieldName, fieldValue);
    }

    /**
     * 通过反射设置类属性的值，主要被依赖注入时使用,不需要set和get方法，强制设置类对像的属性值
     *
     * @param obj
     * @param fieldName
     * @param fieldValue
     * @throws Exception
     */
    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) throws Exception {
        //首先看此类没有提供set方法名，如果有set方法则以方法名注入优先
        boolean setFlag = false;
        List<Method> methods = getAllMethods(obj);
        String methodName = "set" + fieldName;
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                //不区分大小写
                BeanUtils.setProperty(obj, fieldName, fieldValue);
                setFlag = true;
            }
        }

        //直接通过属性的反谢功能注入
        if (setFlag == false) {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, fieldValue);
        }

    }

    /**
     * 获得当前类及其父类的所有方法
     *
     * @param obj
     * @return
     */
    public static List<Method> getAllMethods(Object obj) {
        if (obj == null) {
            return null;
        }
        //获得当前类的属性
        List<Method> listMethods = new ArrayList<Method>();
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method m : methods) {
            listMethods.add(m);
        }
        //得到父类的方法
        Class<?> parentClazz = obj.getClass().getSuperclass();
        if (parentClazz != null && !parentClazz.getName().equalsIgnoreCase("java.lang.Object")) {
            methods = parentClazz.getDeclaredMethods();
            for (Method m : methods) {
                listMethods.add(m);
            }
        }
        return listMethods;
    }

    /**
     * 获得当前类及其父类的所有方法
     *
     * @param clz
     * @return
     */
    public static List<Method> getAllMethods(Class<?> clz) {
        //获得当前类的属性
        List<Method> listMethods = new ArrayList<Method>();
        Method[] methods = clz.getDeclaredMethods();
        for (Method m : methods) {
            listMethods.add(m);
        }
        //得到父类的方法
        Class<?> parentClazz = clz.getSuperclass();
        if (parentClazz != null && !parentClazz.getName().equalsIgnoreCase("java.lang.Object")) {
            methods = parentClazz.getDeclaredMethods();
            for (Method m : methods) {
                listMethods.add(m);
            }
        }
        return listMethods;
    }

    /**
     * 获取指定类中属性注解为@KeyId的字段名
     *
     * @param clazz
     * @return 返回关键字段的id
     * @throws Exception
     */
    public static String getBeanKeyId(Class<?> clazz) {
        //查找字段属性中的注解有@KeyId注解的说明也是关键字段
        List<Field> fields = getAllFields(clazz);
        for (Field field : fields) {
            TableId keyid = field.getAnnotation(TableId.class);
            if (keyid != null) {
                return field.getName();
            }
        }
        log.info("警告信息:" + clazz.getSimpleName() + " Java Bean没有使用@KeyId注解主键,将使用使用默认主键id");
        //默认id字段
        return "id";
    }

    /**
     * 根据Class中的@Table注解获取数据库表名
     *
     * @param clazz 要获取的类
     * @return
     * @throws Exception
     */
    public static String getBeanTableName(Class<?> clazz) {
        String tableName = null;
        TableName table = clazz.getAnnotation(TableName.class);
        if (table != null) {
            tableName = table.value();
        }
        return tableName;
    }

    /**
     * 根据Class中的@DbName注解获取数据库名
     *
     * @param clazz 要获取的类
     * @return
     * @throws Exception
     */
    public static String getBeanDbName(Class<?> clazz) {
        String dbName = "";
        //DBName dbNameAnn = clazz.getAnnotation(dbn.class);
        //if (dbNameAnn != null) {
        //    dbName = dbNameAnn.name();
        //}
        return dbName;
    }

    /**
     * 获得当前类及其父类的所有字段属性
     *
     * @param clazz
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        //获得当前类的属性
        List<Field> listFields = new ArrayList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            listFields.add(field);
        }
        //得到父类的属性
        Class<?> parentClazz = clazz.getSuperclass();
//    	System.out.println("parentClassName="+parentClazz.getName());
        if (parentClazz != null && !parentClazz.getName().equalsIgnoreCase("java.lang.Object")) {
            fields = parentClazz.getDeclaredFields();
            for (Field field : fields) {
                listFields.add(field);
            }
        }
        return listFields;
    }

    /*
     * 是否是自定义类型】
     *
     * false:是自定义
     */
    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }


    /**
     * 判断是否基本类型
     *
     * @param clazz
     * @return
     * @throws Exception
     */
    public static boolean isBaseDataType(Class<?> clazz) throws Exception {
        return
                (
                        clazz.equals(String.class) ||
                                clazz.equals(Integer.class) ||
                                clazz.equals(Byte.class) ||
                                clazz.equals(Long.class) ||
                                clazz.equals(Double.class) ||
                                clazz.equals(Float.class) ||
                                clazz.equals(Character.class) ||
                                clazz.equals(Short.class) ||
                                clazz.equals(BigDecimal.class) ||
                                clazz.equals(BigInteger.class) ||
                                clazz.equals(Boolean.class) ||
                                clazz.equals(Date.class) ||
                                clazz.equals(StringBuilder.class) ||
                                clazz.isPrimitive()
                );
    }

    /**
     * 判断一个class类是否在jar中加载的
     *
     * @param cls
     * @return
     */
    public static boolean classInJar(final Class<?> cls) {

        //非空判断
        if (cls == null) {
            throw new IllegalArgumentException("null input: cls");
        }

        URL result = null;
        final String clsAsResource = cls.getName().replace('.', '/').concat(".class");

        final ProtectionDomain pd = cls.getProtectionDomain();

        if (pd != null) {
            final CodeSource cs = pd.getCodeSource();
            if (cs != null) {
                result = cs.getLocation();
            }

            if (result != null) {
                if ("file".equals(result.getProtocol())) {
                    try {
                        if (result.toExternalForm().endsWith(".jar") || result.toExternalForm().endsWith(".zip")) {
                            return true;
                        } else if (new File(result.getFile()).isDirectory()) {
                            return false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (result == null) {
            final ClassLoader clsLoader = cls.getClassLoader();
            result = clsLoader != null ? clsLoader.getResource(clsAsResource)
                    : ClassLoader.getSystemResource(clsAsResource);
            if (result.toString().indexOf(".jar") != -1) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * 获取一个class类的实际位置
     *
     * @param cls
     * @return
     */
    public static URL getClassLocation(final Class<?> cls) {

        //非空判断
        if (cls == null) {
            throw new IllegalArgumentException("null input: cls");
        }

        URL result = null;
        final String clsAsResource = cls.getName().replace('.', '/').concat(".class");

        final ProtectionDomain pd = cls.getProtectionDomain();

        if (pd != null) {
            final CodeSource cs = pd.getCodeSource();
            if (cs != null) {
                result = cs.getLocation();
            }

            if (result != null) {
                if ("file".equals(result.getProtocol())) {
                    try {
                        if (result.toExternalForm().endsWith(".jar") || result.toExternalForm().endsWith(".zip")) {
                            result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(clsAsResource));
                        } else if (new File(result.getFile()).isDirectory()) {
                            result = new URL(result, clsAsResource);
                        }
                    } catch (MalformedURLException ignore) {

                    }
                }
            }
        }

        if (result == null) {
            final ClassLoader clsLoader = cls.getClassLoader();
            result = clsLoader != null ? clsLoader.getResource(clsAsResource)
                    : ClassLoader.getSystemResource(clsAsResource);
        }

        return result;
    }

}
