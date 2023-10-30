package org.variantsync.vevos.extraction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.variantsync.diffdetective.datasets.RepositoryLocationType;
import org.variantsync.vevos.VEVOS;
import org.variantsync.vevos.common.*;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ExtractionTest {

    private static final Path IF_ELSE_PATH = Path.of("src/test/resources/extraction/ifelse.source");
    private static final List<PropositionalFormula> IF_ELSE_PCS;
    private static final List<PropositionalFormula> IF_ELSE_FM;
    private static final Path NESTED_IF_PATH = Path.of("src/test/resources/extraction/nestedif.source");
    private static final List<PropositionalFormula> NESTED_IF_PCS;
    private static final List<PropositionalFormula> NESTED_IF_FM;
    private static final Path TEST_REPO_PATH = Path.of("src/test/resources/extraction/test_repo.zip");
    private static final RepositoryLocation TEST_REPO_LOCATION = new RepositoryLocation(RepositoryLocationType.FROM_ZIP, TEST_REPO_PATH.toString(), "TestRepo");


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
        IF_ELSE_FM = IF_ELSE_PCS;
    }

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

    private static void check_nested_if_path_features(Set<Feature> features) {
        Set<Feature> expectedFeatures = new HashSet<>();
        expectedFeatures.add(new Feature("A"));
        expectedFeatures.add(new Feature("B"));
        Assertions.assertTrue(features.containsAll(expectedFeatures));
        Assertions.assertTrue(expectedFeatures.containsAll(features));
    }

    private static void check_if_else_features(Set<Feature> features) {
        Assertions.assertEquals(1, features.size());
        features.forEach(f -> Assertions.assertEquals(new Feature("A"), f));
    }

    private void checkPCs(final Function<Integer, PropositionalFormula> formulaGetter, final Map<Integer, PropositionalFormula> expectedValues) {
        for (int lineNumber : expectedValues.keySet()) {
            PropositionalFormula expected = expectedValues.get(lineNumber);
            PropositionalFormula actual = formulaGetter.apply(lineNumber);
            Assertions.assertEquals(expected, actual,
                    "incorrect PC for line " + lineNumber + ": (" + expected + ") vs. (" + actual + ")");
        }
    }

    private void checkMatching(final BiFunction<Path, Integer, Optional<Integer>> matchingGetter, final Map<Integer, Integer> expectedValues, final Path filePath) {
        for (int lineNumber : expectedValues.keySet()) {
            int expected = expectedValues.get(lineNumber);
            int actual = matchingGetter.apply(filePath, lineNumber).get();
            Assertions.assertEquals(expected, actual,
                    "incorrect Matching for line " + lineNumber + ": (" + expected + ") vs. (" + actual + ")");
        }
    }

    @Test
    public void filePCDirectIfElse() {
        FileGroundTruth fileGroundTruth = VEVOS.extractFileGT(IF_ELSE_PATH);
        checkPCs(fileGroundTruth::presenceCondition, lineMapping(IF_ELSE_PCS));
    }

    @Test
    public void filePCDirectNestedIf() {
        FileGroundTruth fileGroundTruth = VEVOS.extractFileGT(NESTED_IF_PATH);
        checkPCs(fileGroundTruth::presenceCondition, lineMapping(NESTED_IF_PCS));
    }

    @Test
    public void fileFMDirectIfElse() {
        FileGroundTruth fileGroundTruth = VEVOS.extractFileGT(IF_ELSE_PATH);
        checkPCs(fileGroundTruth::featureMapping, lineMapping(IF_ELSE_FM));
    }

    @Test
    public void fileFMDirectNestedIf() {
        FileGroundTruth fileGroundTruth = VEVOS.extractFileGT(NESTED_IF_PATH);
        checkPCs(fileGroundTruth::featureMapping, lineMapping(NESTED_IF_FM));
    }

    @Test
    public void fileFeaturesDirectIfElse() {
        FileGroundTruth fileGroundTruth = VEVOS.extractFileGT(IF_ELSE_PATH);
        Set<Feature> features = fileGroundTruth.features();
        check_if_else_features(features);
    }

    @Test
    public void fileFeaturesDirectNestedIf() {
        FileGroundTruth fileGroundTruth = VEVOS.extractFileGT(NESTED_IF_PATH);
        Set<Feature> features = fileGroundTruth.features();
        check_nested_if_path_features(features);
    }

    @Test
    public void filePCFromRepoIfElse() {
        // TODO
        CommitId commitId = new CommitId("2cad77f2628ba792734c24e2034b47690a3976c6");
        FileGroundTruth fileGroundTruth = VEVOS.extractFileGT(TEST_REPO_LOCATION, commitId, IF_ELSE_PATH);
        checkPCs(fileGroundTruth::presenceCondition, lineMapping(IF_ELSE_PCS));
    }

    @Test
    public void filePCFromRepoNestedIf() {
        // TODO
        CommitId commitId = new CommitId("2cad77f2628ba792734c24e2034b47690a3976c6");
        FileGroundTruth fileGroundTruth = VEVOS.extractFileGT(TEST_REPO_LOCATION, commitId, NESTED_IF_PATH);
        checkPCs(fileGroundTruth::presenceCondition, lineMapping(NESTED_IF_PCS));
    }

    @Test
    public void fileFMFromRepoIfElse() {
        // TODO
        CommitId commitId = new CommitId("2cad77f2628ba792734c24e2034b47690a3976c6");
        FileGroundTruth fileGroundTruth = VEVOS.extractFileGT(TEST_REPO_LOCATION, commitId, IF_ELSE_PATH);
        checkPCs(fileGroundTruth::featureMapping, lineMapping(IF_ELSE_FM));
    }

    @Test
    public void fileFMFromRepoNestedIf() {
        // TODO
        CommitId commitId = new CommitId("2cad77f2628ba792734c24e2034b47690a3976c6");
        FileGroundTruth fileGroundTruth = VEVOS.extractFileGT(TEST_REPO_LOCATION, commitId, NESTED_IF_PATH);
        checkPCs(fileGroundTruth::featureMapping, lineMapping(NESTED_IF_FM));
    }

    @Test
    public void fileFeaturesFromRepoIfElse() {
        // TODO
        CommitId commitId = new CommitId("2cad77f2628ba792734c24e2034b47690a3976c6");
        FileGroundTruth fileGroundTruth = VEVOS.extractFileGT(TEST_REPO_LOCATION, commitId, IF_ELSE_PATH);
        Set<Feature> features = fileGroundTruth.features();
        check_if_else_features(features);
    }

    @Test
    public void fileFeaturesFromRepoNestedIf() {
        // TODO
        CommitId commitId = new CommitId("2cad77f2628ba792734c24e2034b47690a3976c6");
        FileGroundTruth fileGroundTruth = VEVOS.extractFileGT(TEST_REPO_LOCATION, commitId, NESTED_IF_PATH);
        Set<Feature> features = fileGroundTruth.features();
        check_nested_if_path_features(features);
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

    @Test
    public void matchingBefore() {
        // TODO
        CommitId commit = new CommitId("2cad77f2628ba792734c24e2034b47690a3976c6");
        CommitGroundTruth commitGroundTruth = VEVOS.extractCommitGroundTruth(TEST_REPO_LOCATION, commit);
        CodeMatching matching = commitGroundTruth.codeMatching();
        // TODO
        checkMatching(matching::beforeToAfterMatch, null, IF_ELSE_PATH.getFileName());
    }

    @Test
    public void matchingAfter() {
        // TODO
        CommitId commit = new CommitId("2cad77f2628ba792734c24e2034b47690a3976c6");
        CommitGroundTruth commitGroundTruth = VEVOS.extractCommitGroundTruth(TEST_REPO_LOCATION, commit);
        CodeMatching matching = commitGroundTruth.codeMatching();
        // TODO
        checkMatching(matching::afterToBeforeMatch, null, IF_ELSE_PATH.getFileName());
    }

    @Test
    public void matchingOfUnchangedFile() {
        // TODO
    }
}
