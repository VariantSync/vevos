package org.variantsync.vevos.extraction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.variantsync.vevos.VEVOS;
import org.variantsync.vevos.common.FileGT;
import org.variantsync.vevos.common.PropositionalFormula;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ExtractionTest {

        private void checkPCs(final Function<Integer, PropositionalFormula> formulaGetter, final Map<Integer, PropositionalFormula> expectedValues) {
            for(int lineNumber : expectedValues.keySet()) {
                PropositionalFormula expected = expectedValues.get(lineNumber);
                PropositionalFormula actual = formulaGetter.apply(lineNumber);
                Assertions.assertEquals(expected, actual,
                        "incorrect PC for line " + lineNumber + ": (" + expected + ") vs. (" + actual + ")");
            }
        }

        @Test
        public void linePCIfElse() {
            FileGT fileGT = VEVOS.extractFileGT(IF_ELSE_PATH);
            checkPCs(fileGT::presenceCondition, lineMapping(IF_ELSE_PCS));
        }

    @Test
    public void linePCNestedIf() {
        FileGT fileGT = VEVOS.extractFileGT(NESTED_IF_PATH);
        checkPCs(fileGT::presenceCondition, lineMapping(NESTED_IF_PCS));
    }


    @Test
        public void lineFMIfElse() {
            FileGT fileGT = VEVOS.extractFileGT(IF_ELSE_PATH);
            checkPCs(fileGT::featureMapping, lineMapping(IF_ELSE_FM));
        }

    @Test
    public void lineFMNestedIf() {
        FileGT fileGT = VEVOS.extractFileGT(NESTED_IF_PATH);
        checkPCs(fileGT::featureMapping, lineMapping(NESTED_IF_FM));
    }

        @Test
        public void lineCodeMatching() {

        }

        @Test
        public void filePresenceConditions() {

        }

        @Test
        public void fileFeatureMappings() {

        }

        @Test
        public void fileCodeMatching() {

        }

        @Test
        public void fileGroundTruth() {

        }

        @Test
        public void fileFeatures() {

        }

        @Test
        public void commitFeatures() {

        }

        @Test
        public void commitGroundTruth() {

        }

        @Test
        public void commitChangedFiles() {

    }

    private static PropositionalFormula formula(String formula) {
        return PropositionalFormula.fromString(formula);
    }
    
    private static PropositionalFormula formula() {
        return PropositionalFormula.fromLiteral(true);
    }
    
    private static Map<Integer, PropositionalFormula> lineMapping(List<PropositionalFormula> formulas) {
        Map<Integer, PropositionalFormula> map = new HashMap<>();
        for (int i = 0; i < formulas.size(); i++) {
            map.put(i, formulas.get(i));
        }
        return map;
    }


    private static final Path IF_ELSE_PATH = Path.of("src/test/resources/extraction/ifelse.source");
    private static final List<PropositionalFormula> IF_ELSE_PCS;
    static {
        IF_ELSE_PCS = new ArrayList<>();
        IF_ELSE_PCS.add(formula());
        IF_ELSE_PCS.add(formula());
        IF_ELSE_PCS.add(formula());
        IF_ELSE_PCS.add(formula());
        IF_ELSE_PCS.add(formula());
        IF_ELSE_PCS.add(formula("A"));
        IF_ELSE_PCS.add(formula("A"));
        IF_ELSE_PCS.add(formula("!A"));
        IF_ELSE_PCS.add(formula("!A"));
        IF_ELSE_PCS.add(formula("A"));
        IF_ELSE_PCS.add(formula());
        IF_ELSE_PCS.add(formula());
    }
    private static final List<PropositionalFormula> IF_ELSE_FM = IF_ELSE_PCS;

    private static final Path NESTED_IF_PATH = Path.of("src/test/resources/extraction/nestedif.source");
    private static final List<PropositionalFormula> NESTED_IF_PCS;
    private static final List<PropositionalFormula> NESTED_IF_FM;

    static {
        NESTED_IF_PCS = new ArrayList<>();
        NESTED_IF_PCS.add(formula());
        NESTED_IF_PCS.add(formula("A"));
        NESTED_IF_PCS.add(formula("A"));
        NESTED_IF_PCS.add(formula("A && B"));
        NESTED_IF_PCS.add(formula("A && B"));
        NESTED_IF_PCS.add(formula("A && B"));
        NESTED_IF_PCS.add(formula("A"));
        NESTED_IF_PCS.add(formula("A"));
        NESTED_IF_PCS.add(formula());

        NESTED_IF_FM = new ArrayList<>();
        NESTED_IF_FM.add(formula());
        NESTED_IF_FM.add(formula("A"));
        NESTED_IF_FM.add(formula("A"));
        NESTED_IF_FM.add(formula("B"));
        NESTED_IF_FM.add(formula("B"));
        NESTED_IF_FM.add(formula("B"));
        NESTED_IF_FM.add(formula("A"));
        NESTED_IF_FM.add(formula("A"));
        NESTED_IF_FM.add(formula());
    }
}
