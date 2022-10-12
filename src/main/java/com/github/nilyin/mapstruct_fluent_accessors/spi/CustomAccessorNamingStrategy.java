package com.github.nilyin.mapstruct_fluent_accessors.spi;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import org.mapstruct.ap.spi.DefaultAccessorNamingStrategy;
import java.util.List;


    /**
     * An extension of the {@link DefaultAccessorNamingStrategy} that supports builder setters.
     * When a method is a builder setter method then the name of the property is the same as the method name.
     */
    public class CustomAccessorNamingStrategy extends DefaultAccessorNamingStrategy {

        @Override
        public String getPropertyName(ExecutableElement getterOrSetterMethod) {

            String methodName = getterOrSetterMethod.getSimpleName().toString();
            if ( methodName.startsWith( "is" ) || methodName.startsWith( "get" ) || methodName.startsWith( "set" ) ) {
                return super.getPropertyName( getterOrSetterMethod );
            }
            else if (isMyFluentSetter( getterOrSetterMethod ) ) {
                return methodName;
            }
            else if (isFluentGetter( getterOrSetterMethod ) ) {
                return methodName;
            }
            /* in this case method is neither fluent/builder or basic setter/getter d*/
            return super.getPropertyName( getterOrSetterMethod );
        }

        @Override
        public boolean isGetterMethod (ExecutableElement method) {
            return super.isGetterMethod( method ) || isFluentGetter( method );

        }


        /**
         * Checks if the method is a builder getter method. A builder getter method is a method
         * that has exactly zero parameters and the return type of the method is same as the type of the parameter
         * with its name.
         *
         * @param getterOrSetterMethod that needs to be checked
         *
         * @return {@code true} if the {@code getterOrSetterMethod} is a builder getter method
         */

        protected boolean isFluentGetter(ExecutableElement getterOrSetterMethod) {
            return getterOrSetterMethod.getParameters().isEmpty() &&
                    getterOrSetterMethod.getReturnType().getKind() != TypeKind.VOID &&
                            isSamePropertyNameExist(getterOrSetterMethod);
        }


        /**
         * Checks if this Bean / class has parameter with name same as provided method
         * and returns true if the parameter with same bane and of type of the method
         * exists.
         *
         * @param getterOrSetterMethod that needs to be checked
         *
         * @return {@code true} if the {@code getterOrSetterMethod} is a builder getter method
         */
        private boolean isSamePropertyNameExist (ExecutableElement getterOrSetterMethod) {
                String methodName = getterOrSetterMethod.getSimpleName().toString();
                List<? extends Element> params;
                params = getterOrSetterMethod.getEnclosingElement().getEnclosedElements();
                boolean ret;
                for (Element element : params) {
                    ret = element.asType().toString().equals(getterOrSetterMethod.getReturnType().toString()) &&
                            element.getSimpleName().toString().equals(methodName) &&
                            !element.equals(getterOrSetterMethod);
                    if (ret) return true;
                }
                return false;

            }


        @Override
        public boolean isSetterMethod(ExecutableElement method) {
            return super.isFluentSetter(method) || super.isSetterMethod( method ) || isMyFluentSetter( method );
        }

        /**
         * Checks if the getter or setter method is a builder setter method. A builder setter method is a method
         * that has exactly one parameter and the return type of the method is same as the enclosing type of the method.
         *
         * @param getterOrSetterMethod that needs to be checked
         *
         * @return {@code true} if the {@code getterOrSetterMethod} is a builder setter method
         */
        protected boolean isMyFluentSetter(ExecutableElement getterOrSetterMethod) {
            return getterOrSetterMethod.getParameters().size() == 1 &&
                    getterOrSetterMethod.getReturnType()
                            .toString()
                            .equals( getterOrSetterMethod.getEnclosingElement().asType().toString() );
        }
    }
