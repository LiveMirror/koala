package org.openkoala.businesslog.common;

import org.apache.commons.lang3.time.DateUtils;
import org.openkoala.businesslog.BusinessLogBaseException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * User: zjzhai
 * Date: 12/13/13
 * Time: 1:01 PM
 */
public enum ClassEnum {

    _int(int.class, "int") {
        @Override
        public Object convert(String value) {
            return Integer.valueOf(value);
        }
    },
    _Integer(Integer.class, "Integer", "java.lang.Integer") {
        @Override
        public Object convert(String value) {
            return _int.convert(value);
        }
    },
    _long(long.class, "long") {
        @Override
        public Object convert(String value) {
            return Long.valueOf(value);
        }
    },
    _Long(Long.class, "Long", "java.lang.Long") {
        @Override
        public Object convert(String value) {
            return _long.convert(value);
        }
    },
    _float(float.class, "float") {
        @Override
        public Object convert(String value) {
            return Float.valueOf(value);
        }
    },
    _Float(Float.class, "Float", "java.lang.Float") {
        @Override
        public Object convert(String value) {
            return _float.convert(value);
        }
    },
    _double(double.class, "double") {
        @Override
        public Object convert(String value) {
            return Double.valueOf(value);
        }
    },
    _Double(Double.class, "Double", "java.lang.Double") {
        @Override
        public Object convert(String value) {
            return _double.convert(value);
        }
    },
    _boolean(boolean.class, "boolean") {
        @Override
        public Object convert(String value) {
            return Boolean.valueOf(value);
        }
    },
    _Boolean(Boolean.class, "Boolean", "java.lang.Boolean") {
        @Override
        public Object convert(String value) {
            return _boolean.convert(value);
        }
    },
    _char(char.class, "char") {
        @Override
        public Object convert(String value) {
            return value.charAt(0);
        }
    },
    _Character(Character.class, "Character", "java.lang.Character") {
        @Override
        public Object convert(String value) {

            return _char.convert(value);
        }
    },
    _byte(byte.class, "byte") {
        @Override
        public Object convert(String value) {
            return Byte.valueOf(value);
        }
    },
    _Byte(Byte.class, "Byte", "java.lang.Byte") {
        @Override
        public Object convert(String value) {
            return _byte.convert(value);
        }
    },
    _Date(Date.class, "Date", "java.util.Date") {
        @Override
        public Object convert(String value) {
            try {
                return DateUtils.parseDate(value, parsePatterns);
            } catch (ParseException e) {
                new BusinessLogBaseException("parseDate failure", e);
            }
            return null;
        }
    },
    _String(String.class, "String", "java.lang.String") {
        @Override
        public Object convert(String value) {
            return value;

        }
    },
    _BigDecimal(BigDecimal.class, "BigDecimal", "java.math.BigDecimal") {
        @Override
        public Object convert(String value) {
            return new BigDecimal(value);
        }
    },
    _List(List.class, "List", "java.util.List") {
        @Deprecated
        @Override
        public Object convert(String value) {
            return null;
        }
    },
    _Map(Map.class, "Map", "java.util.Map") {
        @Deprecated
        @Override
        public Object convert(String value) {
            return null;
        }
    },
    _Set(Set.class, "Set", "java.util.Set") {
        @Deprecated
        @Override
        public Object convert(String value) {
            return null;
        }
    };

    private final static String[] parsePatterns = new String[]{
            "yyyy.MM.dd G \'at\' hh:mm:ss z",
            "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy年MM月dd日"

    };


    private Set<String> names = new HashSet<String>();
    private Class classz;

    private ClassEnum(Class clasz, String... stringName) {
        if (null != stringName) {
            names.addAll(Arrays.asList(stringName));
        }
        this.classz = clasz;
    }

    public Class getClassz() {
        return classz;
    }


    public abstract Object convert(String value);

    public static ClassEnum getSimpleClassEnumOf(String className) {
        for (ClassEnum each : ClassEnum.values()) {
            if (each.names.contains(className)) {
                return each;
            }
        }
        return null;
    }

    public static ClassEnum getSimpleClassEnumOf(Class clazz) {
        for (ClassEnum each : ClassEnum.values()) {
            if (each.classz.equals(clazz)) {
                return each;
            }
        }
        return null;
    }

    public static boolean isSimpleClass(String className) {
        for (ClassEnum each : ClassEnum.values()) {
            if (each.names.contains(className))   return true;
        }
        return false;
    }

    public static boolean isSimpleClass(Class clazz) {
        for (ClassEnum each : ClassEnum.values()) {
            if (each.classz.equals(clazz))  return true;
        }
        return false;
    }
}
