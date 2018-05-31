package com.beeplay.core.utils;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenlongfei
 * @date 2017-02-15
 * 对象比较工具类
 */
public class DomainEquals {

    /**
     * 日志操作类
     */
    private static Logger logger = Logger.getLogger(DomainEquals.class);

    public DomainEquals() {
    }

    /**
     * 比较两个BEAN或MAP对象的值是否相等
     * 如果是BEAN与MAP对象比较时MAP中的key值应与BEAN的属性值名称相同且字段数目要一致
     * @param source
     * @param target
     * @return
     */
    public static boolean domainEquals(Object source, Object target) {
        if (source == null || target == null) {
            return false;
        }
        boolean rv = true;
        if (source instanceof Map) {
            rv = mapOfSrc(source, target, rv);
        } else {
            rv = classOfSrc(source, target, rv);
        }
        logger.debug("THE EQUALS RESULT IS " + rv);
        return rv;
    }

    /**
     * 只比较传入的值
     * @param source 源对象
     * @param target 目标对象
     * @param fileds 对象的字段名的数组
     * @return
     */
    public static boolean domainEqualsContain(Object source, Object target,String[] fileds) {
        if (source == null || target == null) {
            return false;
        }
        boolean rv = true;
        if (source instanceof Map) {
            rv = mapOfSrcContain(source, target, rv,fileds);
        } else {
            rv = classOfSrcContain(source, target, rv,fileds);
        }
        logger.debug("THE EQUALS RESULT IS " + rv);
        return rv;
    }

    /**
     * 排除传入的值其他的进行比较
     * @param source 源对象
     * @param target 目标对象
     * @param fileds 对象的字段名的数组
     * @return
     */
    public static boolean domainEqualsNotContain(Object source, Object target,String[] fileds) {
        if (source == null || target == null) {
            return false;
        }
        boolean rv = true;
        if (source instanceof Map) {
            rv = mapOfSrcNotContain(source, target, rv,fileds);
        } else {
            rv = classOfSrcNotContain(source, target, rv,fileds);
        }
        logger.debug("THE EQUALS RESULT IS " + rv);
        return rv;
    }
    /**
     * 源目标为MAP类型时
     * @param source
     * @param target
     * @param rv
     * @return
     */
    private static boolean mapOfSrc(Object source, Object target, boolean rv) {
        HashMap<String, String> map = new HashMap<String, String>();
        map = (HashMap) source;
        for (String key : map.keySet()) {
            if (target instanceof Map) {
                HashMap<String, String> tarMap = new HashMap<String, String>();
                tarMap = (HashMap) target;
                if(tarMap.get(key)==null){
                    rv = false;
                    break;
                }
                if (!map.get(key).equals(tarMap.get(key))) {
                    rv = false;
                    break;
                }
            } else {
                String tarValue = getClassValue(target, key) == null ? "" : getClassValue(target, key).toString();
                if (!tarValue.equals(map.get(key))) {
                    rv = false;
                    break;
                }
            }
        }
        return rv;
    }
    private static boolean mapOfSrcContain(Object source, Object target, boolean rv,String[] fileds) {
        HashMap<String, String> map = new HashMap<String, String>();
        map = (HashMap) source;
        for (String key : map.keySet()) {
            if(contain(fileds,key)) {
                if (target instanceof Map) {
                    HashMap<String, String> tarMap = new HashMap<String, String>();
                    tarMap = (HashMap) target;
                    if (tarMap.get(key) == null) {
                        rv = false;
                        break;
                    }
                    if (!map.get(key).equals(tarMap.get(key))) {
                        rv = false;
                        break;
                    }
                } else {
                    String tarValue = getClassValue(target, key) == null ? "" : getClassValue(target, key).toString();
                    if (!tarValue.equals(map.get(key))) {
                        rv = false;
                        break;
                    }
                }
            }
        }
        return rv;
    }
    private static boolean mapOfSrcNotContain(Object source, Object target, boolean rv,String[] fileds) {
        HashMap<String, String> map = new HashMap<String, String>();
        map = (HashMap) source;
        for (String key : map.keySet()) {
            if(!contain(fileds,key)) {
                if (target instanceof Map) {
                    HashMap<String, String> tarMap = new HashMap<String, String>();
                    tarMap = (HashMap) target;
                    if (tarMap.get(key) == null) {
                        rv = false;
                        break;
                    }
                    if (!map.get(key).equals(tarMap.get(key))) {
                        rv = false;
                        break;
                    }
                } else {
                    String tarValue = getClassValue(target, key) == null ? "" : getClassValue(target, key).toString();
                    if (!tarValue.equals(map.get(key))) {
                        rv = false;
                        break;
                    }
                }
            }
        }
        return rv;
    }
    /**
     * 源目标为非MAP类型时
     * @param source
     * @param target
     * @param rv
     * @return
     */
    private static boolean classOfSrcContain(Object source, Object target, boolean rv,String[] fileds) {
        Class<?> srcClass = source.getClass();
        Field[] fields = srcClass.getDeclaredFields();
        for (Field field : fields) {
            String nameKey = field.getName();
            if(contain(fileds,nameKey)){
                if (target instanceof Map) {
                    HashMap<String, String> tarMap = new HashMap<String, String>();
                    tarMap = (HashMap) target;
                    String srcValue = getClassValue(source, nameKey) == null ? "" : getClassValue(source, nameKey)
                            .toString();
                    if(tarMap.get(nameKey)==null){
                        rv = false;
                        break;
                    }
                    if (!tarMap.get(nameKey).equals(srcValue)) {
                        rv = false;
                        break;
                    }
                } else {
                    String srcValue = getClassValue(source, nameKey) == null ? "" : getClassValue(source, nameKey)
                            .toString();
                    String tarValue = getClassValue(target, nameKey) == null ? "" : getClassValue(target, nameKey)
                            .toString();
                    if (!srcValue.equals(tarValue)) {
                        rv = false;
                        break;
                    }
                }
            }

        }
        return rv;
    }
    private static boolean classOfSrcNotContain(Object source, Object target, boolean rv,String[] fileds) {
        Class<?> srcClass = source.getClass();
        Field[] fields = srcClass.getDeclaredFields();
        for (Field field : fields) {
            String nameKey = field.getName();
            if(!contain(fileds,nameKey)){
                if (target instanceof Map) {
                    HashMap<String, String> tarMap = new HashMap<String, String>();
                    tarMap = (HashMap) target;
                    String srcValue = getClassValue(source, nameKey) == null ? "" : getClassValue(source, nameKey)
                            .toString();
                    if(tarMap.get(nameKey)==null){
                        rv = false;
                        break;
                    }
                    if (!tarMap.get(nameKey).equals(srcValue)) {
                        rv = false;
                        break;
                    }
                } else {
                    String srcValue = getClassValue(source, nameKey) == null ? "" : getClassValue(source, nameKey)
                            .toString();
                    String tarValue = getClassValue(target, nameKey) == null ? "" : getClassValue(target, nameKey)
                            .toString();
                    if (!srcValue.equals(tarValue)) {
                        rv = false;
                        break;
                    }
                }
            }

        }
        return rv;
    }
    private static boolean classOfSrc(Object source, Object target, boolean rv) {
        Class<?> srcClass = source.getClass();
        Field[] fields = srcClass.getDeclaredFields();
        for (Field field : fields) {
            String nameKey = field.getName();
            if (target instanceof Map) {
                HashMap<String, String> tarMap = new HashMap<String, String>();
                tarMap = (HashMap) target;
                String srcValue = getClassValue(source, nameKey) == null ? "" : getClassValue(source, nameKey)
                        .toString();
                if(tarMap.get(nameKey)==null){
                    rv = false;
                    break;
                }
                if (!tarMap.get(nameKey).equals(srcValue)) {
                    rv = false;
                    break;
                }
            } else {
                String srcValue = getClassValue(source, nameKey) == null ? "" : getClassValue(source, nameKey)
                        .toString();
                String tarValue = getClassValue(target, nameKey) == null ? "" : getClassValue(target, nameKey)
                        .toString();
                if (!srcValue.equals(tarValue)) {
                    rv = false;
                    break;
                }
            }
        }
        return rv;
    }
    /**
     * 根据字段名称取值
     * @param obj
     * @param fieldName
     * @return
     */
    private static Object getClassValue(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }
        try {
            Class beanClass = obj.getClass();
            Method[] ms = beanClass.getMethods();
            for (int i = 0; i < ms.length; i++) {
                // 非get方法不取
                if (!ms[i].getName().startsWith("get")) {
                    continue;
                }
                Object objValue = null;
                try {
                    objValue = ms[i].invoke(obj, new Object[] {});
                } catch (Exception e) {
                    logger.error("反射取值出错：" + e.toString());
                    continue;
                }
                if (objValue == null) {
                    continue;
                }
                if (ms[i].getName().toUpperCase().equals(fieldName.toUpperCase())
                        || ms[i].getName().substring(3).toUpperCase().equals(fieldName.toUpperCase())) {
                    return objValue;
                } else if (fieldName.toUpperCase().equals("SID")
                        && (ms[i].getName().toUpperCase().equals("ID") || ms[i].getName().substring(3).toUpperCase()
                        .equals("ID"))) {
                    return objValue;
                }
            }
        } catch (Exception e) {
             logger.error("取方法出错！" + e.toString());
        }
        return null;
    }
    /**
     * 包含
     * @param args
     */
    private static boolean contain(String [] fields,String fieldName){
        for(int i=0;i<fields.length;i++){
            if(fieldName.equals(fields[i])){
                return true;
            }
        }
        return false;
    }

    public static void main(String args[]) {

    }

}