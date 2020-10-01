package cn.org.atool.fluent.mybatis.entity.base;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 字段解析工具类
 *
 * @author wudarui
 */
public class FieldColumnParser {

    /**
     * 解析字段
     *
     * @param variable
     * @return
     */
    public static FieldColumn valueOf(JCTree.JCVariableDecl variable) {
        FieldColumn field = new FieldColumn().setProperty(variable.getName().toString())
            .setJavaType(variable.getType().type);

        boolean isPrimary = false;
        JCTree.JCAnnotation jcAnnotation = null;

        for (JCTree.JCAnnotation annotation : variable.mods.annotations) {
            String type = annotation.type.toString();
            if (type.contains(TableId.class.getSimpleName())) {
                jcAnnotation = annotation;
                isPrimary = true;
                break;
            } else if (type.contains(TableField.class.getSimpleName())) {
                jcAnnotation = annotation;
                break;
            }
        }
        if (jcAnnotation == null) {
            return field;
        }

        field.setPrimary(isPrimary);
        for (JCTree.JCExpression expression : jcAnnotation.args) {
            if (!expression.getKind().equals(Tree.Kind.ASSIGNMENT)) {
                continue;
            }
            JCAssign assign = (JCAssign) expression;
            if (!assign.lhs.getKind().equals(Tree.Kind.IDENTIFIER)) {
                continue;
            }
            setValue(assign, "value", field::setColumn);
            if (isPrimary) {
                setPrimaryField(field, assign);
            } else {
                setField(field, assign);
            }
        }
        return field;
    }

    /**
     * 设置主键属性
     *
     * @param field
     * @param assign
     */
    private static void setPrimaryField(FieldColumn field, JCAssign assign) {
        setValue(assign, "auto", v -> field.setAutoIncrease(Boolean.valueOf(v)));
        setValue(assign, "seqName", field::setSeqName);
    }

    private static void setField(FieldColumn field, JCAssign assign) {
        setValue(assign, "insert", field::setInsert);
        setValue(assign, "update", field::setUpdate);
        setValue(assign, "notLarge", v -> field.setNotLarge(Boolean.valueOf(v)));
        setValue(assign, "numericScale", field::setNumericScale);
        setEnumVal(assign, "jdbcType", value -> {
            if (!Objects.equals("UNDEFINED", value)) {
                field.setJdbcType(value);
            }
        });
        setTypeArg(assign, "typeHandler", type -> {
            if (type != null && !type.toString().endsWith(UnknownTypeHandler.class.getSimpleName())) {
                field.setTypeHandler(type);
            }
        });
    }

    /**
     * 获取 @TableField 或 @TableId 上定义的属性值
     *
     * @param assign
     * @param method
     * @return
     */
    private static void setValue(JCAssign assign, String method, Consumer<String> consumer) {
        if (!Objects.equals(method(assign), method)) {
            return;
        }
        String value = String.valueOf(((JCTree.JCLiteral) assign.rhs).value);
        consumer.accept(value);
    }

    /**
     * 设置枚举值
     *
     * @param assign
     * @param method
     * @param consumer
     */
    private static void setEnumVal(JCAssign assign, String method, Consumer<String> consumer) {
        if (!Objects.equals(method(assign), method)) {
            return;
        }
        String value = ((JCTree.JCFieldAccess) assign.rhs).name.toString();
        consumer.accept(value);
    }

    /**
     * 设置 Class 属性
     *
     * @param assign
     * @param method
     * @param consumer
     */
    private static void setTypeArg(JCAssign assign, String method, Consumer<Type> consumer) {
        if (!Objects.equals(method(assign), method)) {
            return;
        }
        Type value = ((JCTree.JCFieldAccess) assign.rhs).type.getTypeArguments().get(0);
        consumer.accept(value);
    }

    /**
     * 返回主键属性名称
     *
     * @param assign
     * @return
     */
    private static String method(JCAssign assign) {
        return (((JCIdent) assign.lhs).name).toString();
    }
}
