package org.variantsync.vevos.extraction;

import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.variantsync.diffdetective.datasets.RepositoryLocationType;
import org.variantsync.diffdetective.util.Assert;
import org.variantsync.diffdetective.variation.diff.VariationDiff;
import org.variantsync.diffdetective.variation.tree.VariationTree;
import org.variantsync.vevos.VEVOS;
import org.variantsync.vevos.common.*;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ExtractionTest {
    public static final CommitId COMMIT_WITH_CHANGES = new CommitId("2cad77f2628ba792734c24e2034b47690a3976c6");
    private static final VEVOS vevos = new VEVOS();
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
        FileGroundTruth fileGroundTruth = vevos.extractFileGT(IF_ELSE_PATH);
        checkPCs(fileGroundTruth::presenceCondition, lineMapping(IF_ELSE_PCS));
    }

    @Test
    public void filePCDirectNestedIf() {
        FileGroundTruth fileGroundTruth = vevos.extractFileGT(NESTED_IF_PATH);
        checkPCs(fileGroundTruth::presenceCondition, lineMapping(NESTED_IF_PCS));
    }

    @Test
    public void fileFMDirectIfElse() {
        FileGroundTruth fileGroundTruth = vevos.extractFileGT(IF_ELSE_PATH);
        checkPCs(fileGroundTruth::featureMapping, lineMapping(IF_ELSE_FM));
    }

    @Test
    public void fileFMDirectNestedIf() {
        FileGroundTruth fileGroundTruth = vevos.extractFileGT(NESTED_IF_PATH);
        checkPCs(fileGroundTruth::featureMapping, lineMapping(NESTED_IF_FM));
    }

    @Test
    public void fileFeaturesDirectIfElse() {
        FileGroundTruth fileGroundTruth = vevos.extractFileGT(IF_ELSE_PATH);
        Set<Feature> features = fileGroundTruth.features();
        check_if_else_features(features);
    }

    @Test
    public void fileFeaturesDirectNestedIf() {
        FileGroundTruth fileGroundTruth = vevos.extractFileGT(NESTED_IF_PATH);
        Set<Feature> features = fileGroundTruth.features();
        check_nested_if_path_features(features);
    }

    @Test
    public void filePCFromRepoIfElse() {
        // TODO
        FileGroundTruth fileGroundTruth = vevos.extractFileGT(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES, IF_ELSE_PATH);
        checkPCs(fileGroundTruth::presenceCondition, lineMapping(IF_ELSE_PCS));
    }

    @Test
    public void filePCFromRepoNestedIf() {
        // TODO
        FileGroundTruth fileGroundTruth = vevos.extractFileGT(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES, NESTED_IF_PATH);
        checkPCs(fileGroundTruth::presenceCondition, lineMapping(NESTED_IF_PCS));
    }

    @Test
    public void fileFMFromRepoIfElse() {
        // TODO
        FileGroundTruth fileGroundTruth = vevos.extractFileGT(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES, IF_ELSE_PATH);
        checkPCs(fileGroundTruth::featureMapping, lineMapping(IF_ELSE_FM));
    }

    @Test
    public void fileFMFromRepoNestedIf() {
        // TODO
        FileGroundTruth fileGroundTruth = vevos.extractFileGT(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES, NESTED_IF_PATH);
        checkPCs(fileGroundTruth::featureMapping, lineMapping(NESTED_IF_FM));
    }

    @Test
    public void fileFeaturesFromRepoIfElse() {
        // TODO
        FileGroundTruth fileGroundTruth = vevos.extractFileGT(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES, IF_ELSE_PATH);
        Set<Feature> features = fileGroundTruth.features();
        check_if_else_features(features);
    }

    @Test
    public void fileFeaturesFromRepoNestedIf() {
        // TODO
        FileGroundTruth fileGroundTruth = vevos.extractFileGT(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES, NESTED_IF_PATH);
        Set<Feature> features = fileGroundTruth.features();
        check_nested_if_path_features(features);
    }

    @Test
    public void filePCFromCommitGTIfElse() {
        // TODO
        CommitGroundTruth groundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        FileGroundTruth fileGT = groundTruth.fileGroundTruth(IF_ELSE_PATH);
        checkPCs(fileGT::presenceCondition, lineMapping(IF_ELSE_PCS));
    }

    @Test
    public void filePCFromCommitGTNestedIf() {
        // TODO
        CommitGroundTruth groundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        FileGroundTruth fileGT = groundTruth.fileGroundTruth(NESTED_IF_PATH);
        checkPCs(fileGT::presenceCondition, lineMapping(NESTED_IF_PCS));
    }

    @Test
    public void fileFMFromCommitGTIfElse() {
        // TODO
        CommitGroundTruth groundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        FileGroundTruth fileGT = groundTruth.fileGroundTruth(IF_ELSE_PATH);
        checkPCs(fileGT::featureMapping, lineMapping(IF_ELSE_FM));
    }

    @Test
    public void fileFMFromCommitGTNestedIf() {
        // TODO
        CommitGroundTruth groundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        FileGroundTruth fileGT = groundTruth.fileGroundTruth(NESTED_IF_PATH);
        checkPCs(fileGT::featureMapping, lineMapping(NESTED_IF_FM));
    }

    @Test
    public void fileFeaturesFromCommitGTIfElse() {
        // TODO
        CommitGroundTruth groundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        Set<Feature> features = groundTruth.fileGroundTruth(IF_ELSE_PATH).features();
        check_if_else_features(features);
    }

    @Test
    public void fileFeaturesFromCommitGTNestedIf() {
        // TODO
        CommitGroundTruth groundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        Set<Feature> features = groundTruth.fileGroundTruth(NESTED_IF_PATH).features();
        check_nested_if_path_features(features);
    }

    @Test
    public void commitFeatures() {

    }

    @Test
    public void changedFiles() {
        CommitGroundTruth commitGroundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        List<Path> changedFiles = commitGroundTruth.changedFiles();
        // TODO
    }

    @Test
    public void unchangedFiles() {
        CommitGroundTruth commitGroundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        List<Path> unchangedFiles = commitGroundTruth.unchangedFiles();
        // TODO
    }

    @Test
    public void variationDiff() {
        CommitGroundTruth commitGroundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        VariationDiff<MatchedNode> variationDiff = commitGroundTruth.variationDiff(IF_ELSE_PATH.getFileName());
        // TODO
    }

    @Test
    public void variationTree() {
        CommitGroundTruth commitGroundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        VariationTree<MatchedNode> variationDiff = commitGroundTruth.variationTree(IF_ELSE_PATH.getFileName());
        // TODO
    }

    @Test
    public void matchingBefore() {
        // TODO
        CommitGroundTruth commitGroundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        CodeMatching matching = commitGroundTruth.codeMatching();
        // TODO
        checkMatching(matching::beforeToAfterMatch, null, IF_ELSE_PATH.getFileName());
    }

    @Test
    public void matchingAfter() {
        // TODO
        CommitGroundTruth commitGroundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        CodeMatching matching = commitGroundTruth.codeMatching();
        // TODO
        checkMatching(matching::afterToBeforeMatch, null, IF_ELSE_PATH.getFileName());
    }

    @Test
    public void matchingOfUnchangedFile() {
        // TODO
        CommitGroundTruth commitGroundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        CodeMatching matching = commitGroundTruth.codeMatching();
        // TODO
        checkMatching(matching::afterToBeforeMatch, null, Path.of("unchanged.c"));
    }

    @Test
    public void commitMessage() {
        CommitGroundTruth commitGroundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        RevCommit commit = commitGroundTruth.commit();
        Assert.assertEquals("changes", commit.getFullMessage().trim());
    }

    @Test
    public void jGitCommit() {
        CommitGroundTruth commitGroundTruth = vevos.extractCommitGroundTruth(TEST_REPO_LOCATION, COMMIT_WITH_CHANGES);
        RevCommit commit = commitGroundTruth.commit();
        Assert.assertEquals(COMMIT_WITH_CHANGES.id(), commit.getName());
    }
}
