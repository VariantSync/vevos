package org.variantsync.vevos.common;

import org.prop4j.*;

import java.util.Map;

public class PropositionalFormula {
    private final Node node;

    private PropositionalFormula(Node node) {
        this.node = node;
    }

    public static PropositionalFormula fromLiteral(boolean value) {
        if (value) {
            return new PropositionalFormula(new True());
        } else {
            return new PropositionalFormula(new False());
        }
    }

    public PropositionalFormula asNegated() {
        return new PropositionalFormula(new Not(this.node));
    }

    public PropositionalFormula and(PropositionalFormula other) {
        return new PropositionalFormula(new And(this.node, other.node));
    }

    public PropositionalFormula or(PropositionalFormula other) {
        return new PropositionalFormula(new Or(this.node, other.node));
    }

    public static PropositionalFormula fromString(String formula) {
        final NodeReader nodeReader = new NodeReader();
        nodeReader.activateJavaSymbols(); // select the symbols used for parsing conjunction (&&), disjunction (||), ...
        return new PropositionalFormula(nodeReader.stringToNode(formula));
    }

    public boolean evaluate(Map<Object, Boolean> featureAssignment) {
        return this.node.getValue(featureAssignment);
    }

    @Override
    public String toString() {
        return this.node.toString();
    }
}
