package org.variantsync.vevos.extraction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.variantsync.vevos.VEVOS;
import org.variantsync.vevos.common.FileGT;
import org.variantsync.vevos.common.PropositionalFormula;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ExtractionTest {
        private static final Path IF_ELSE_PATH = Path.of("src/test/resources/extraction/ifelse.source");
        private static final Map<Integer, PropositionalFormula> IF_ELSE_PCs;

    static {
        IF_ELSE_PCs = new HashMap<>();
        IF_ELSE_PCs.put(0, PropositionalFormula.fromLiteral(true));
        IF_ELSE_PCs.put(1, PropositionalFormula.fromLiteral(true));
        IF_ELSE_PCs.put(2, PropositionalFormula.fromLiteral(true));
        IF_ELSE_PCs.put(3, PropositionalFormula.fromLiteral(true));
        IF_ELSE_PCs.put(4, PropositionalFormula.fromLiteral(true));
        IF_ELSE_PCs.put(5, PropositionalFormula.fromString("A"));
        IF_ELSE_PCs.put(6, PropositionalFormula.fromString("A"));
        IF_ELSE_PCs.put(7, PropositionalFormula.fromString("!A"));
        IF_ELSE_PCs.put(8, PropositionalFormula.fromString("!A"));
        IF_ELSE_PCs.put(9, PropositionalFormula.fromString("A"));
        IF_ELSE_PCs.put(10, PropositionalFormula.fromLiteral(true));
        IF_ELSE_PCs.put(11, PropositionalFormula.fromLiteral(true));
    }

        private void checkPCs(final Function<Integer, PropositionalFormula> formulaGetter, final Map<Integer, PropositionalFormula> expectedValues) {
            for(int lineNumber : expectedValues.keySet()) {
                PropositionalFormula expected = expectedValues.get(lineNumber);
                PropositionalFormula actual = formulaGetter.apply(lineNumber);
                Assertions.assertEquals(expected, actual,
                        "incorrect PC for line " + lineNumber + ": (" + expected + ") vs. (" + actual + ")");
            }
        }

        @Test
        public void linePresenceCondition() {
            FileGT fileGT = VEVOS.extractFileGT(IF_ELSE_PATH);
            checkPCs(fileGT::presenceCondition, IF_ELSE_PCs);
        }

        @Test
        public void lineFeatureMapping() {
            FileGT fileGT = VEVOS.extractFileGT(IF_ELSE_PATH);
            checkPCs(fileGT::featureMapping, IF_ELSE_PCs);
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
}
