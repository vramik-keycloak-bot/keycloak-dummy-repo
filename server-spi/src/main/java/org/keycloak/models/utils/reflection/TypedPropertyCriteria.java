package org.keycloak.models.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A criteria that matches a property based on its type
 *
 * @see PropertyCriteria
 */
public class TypedPropertyCriteria implements PropertyCriteria {

    /**
     * <p> Different options can be used to match a specific property based on its type. Regardless of the option
     * chosen, if the property type equals the <code>propertyClass</code> it will be selected. <p/> <ul> <li>SUB_TYPE:
     * Also consider properties where its type is a subtype of <code>propertyClass</code>. .</li> <li>SUPER_TYPE: Also
     * consider properties where its type is a superclass or superinterface of <code>propertyClass</code>. .</li> </ul>
     * </p>
     */
    public static enum MatchOption {
        SUB_TYPE, SUPER_TYPE, ALL
    }

    private final Class<?> propertyClass;
    private final MatchOption matchOption;

    public TypedPropertyCriteria(Class<?> propertyClass) {
        this(propertyClass, null);
    }

    public TypedPropertyCriteria(Class<?> propertyClass, MatchOption matchOption) {
        if (propertyClass == null) {
            throw new IllegalArgumentException("Property class can not be null.");
        }
        this.propertyClass = propertyClass;
        this.matchOption = matchOption;
    }

    public boolean fieldMatches(Field f) {
        return match(f.getType());
    }

    public boolean methodMatches(Method m) {
        return match(m.getReturnType());
    }

    private boolean match(Class<?> type) {
        if (propertyClass.equals(type)) {
            return true;
        } else {
            boolean matchSubType = propertyClass.isAssignableFrom(type);

            if (MatchOption.SUB_TYPE == this.matchOption) {
                return matchSubType;
            }

            boolean matchSuperType = type.isAssignableFrom(propertyClass);

            if (MatchOption.SUPER_TYPE == this.matchOption) {
                return matchSuperType;
            }

            if (MatchOption.ALL == this.matchOption) {
                return matchSubType || matchSuperType;
            }
        }

        return false;
    }
}
