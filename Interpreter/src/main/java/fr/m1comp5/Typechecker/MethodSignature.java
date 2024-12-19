package fr.m1comp5.Typechecker;
import fr.m1comp5.ObjectType;

import java.util.List;
import java.util.Objects;

public class MethodSignature {
    private String methodName;
    private List<ObjectType> paramTypes;

    public MethodSignature(String methodName, List<ObjectType> paramTypes) {
        this.methodName = methodName;
        this.paramTypes = paramTypes;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<ObjectType> getParamTypes() {
        return paramTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodSignature that = (MethodSignature) o;
        return Objects.equals(methodName, that.methodName) && Objects.equals(paramTypes, that.paramTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, paramTypes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(methodName);
        sb.append("(");
        for (ObjectType paramType : paramTypes) {
            sb.append(paramType.toString()).append(",");
        }
        if (!paramTypes.isEmpty()) {
            sb.setLength(sb.length() - 1); // Remove the last comma
        }
        sb.append(")");
        return sb.toString();
    }
}