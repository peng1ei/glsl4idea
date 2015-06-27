package glslplugin.lang.elements.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.elements.expressions.operator.GLSLOperator;
import glslplugin.lang.elements.expressions.operator.GLSLOperators;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * Unary operator expression is an expression with one operand and one unary operator, such as "-", "!" or "~"
 *
 * @author Jan Polák
 */
public class GLSLUnaryOperatorExpression extends GLSLOperatorExpression {

    private final boolean prefix;

    public GLSLUnaryOperatorExpression(@NotNull ASTNode astNode, boolean prefix) {
        super(astNode);
        this.prefix = prefix;
    }

    /**
     * Prefix operator has the operator before operand, postfix is reversed.
     * @return True if prefix operator, false if postfix operator
     */
    public boolean isPrefix() {
        return prefix;
    }

    @Nullable
    public GLSLExpression getOperand() {
        GLSLExpression[] operands = getOperands();
        if (operands.length != 1) {
            return operands[0];
        } else {
            Logger.getLogger("GLSLUnaryOperatorExpression").warning("Unary operator expression with " + operands.length + " operand(s).");
            return null;
        }
    }

    /**
     * Overrides the operator type to provide the unary versions of '+' and '-'.
     *
     * @param type the elements type to convert to an operator.
     * @return the resulting operator.
     */
    @Override
    @Nullable
    protected GLSLOperator getOperatorFromType(IElementType type) {
        GLSLOperator op = super.getOperatorFromType(type);
        if (op == GLSLOperators.ADDITION) op = GLSLOperators.PLUS;
        if (op == GLSLOperators.SUBTRACTION) op = GLSLOperators.MINUS;
        return op;
    }

    @NotNull
    @Override
    public GLSLType getType() {
        GLSLOperator operator = getOperator();
        GLSLExpression operand = getOperand();
        if(operator == null || operand == null || !(operator instanceof GLSLOperator.GLSLUnaryOperator)){
            return GLSLTypes.UNKNOWN_TYPE;
        }else{
            GLSLOperator.GLSLUnaryOperator unaryOperator = (GLSLOperator.GLSLUnaryOperator) operator;
            return unaryOperator.getResultType(operand.getType());
        }
    }

    public String toString() {
        GLSLOperator operator = getOperator();
        if(operator != null){
            return "Unary Operator Expression '" + operator.getTextRepresentation() + "'";
        }else{
            return "Unary Operator Expression '(unknown)'";
        }
    }
}
