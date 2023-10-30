package org.variantsync.vevos.common;

import com.googlecode.javaewah.EWAHCompressedBitmap;
import org.eclipse.jgit.revwalk.RevCommit;
import org.variantsync.diffdetective.variation.diff.VariationDiff;
import org.variantsync.diffdetective.variation.tree.VariationTree;
import org.variantsync.vevos.error.NotImplementedException;
import org.variantsync.vevos.extraction.MatchedNode;

import java.nio.file.Path;
import java.util.List;

public class CommitGroundTruth {
    public CodeMatching codeMatching() {
        throw new NotImplementedException();
    }

    public VariationDiff<MatchedNode> variationDiff(Path fileName) {
        throw new NotImplementedException();
    }

    public VariationTree<MatchedNode> variationTree(Path fileName) {
        throw new NotImplementedException();
    }

    public List<Path> unchangedFiles() {
        throw new NotImplementedException();
    }

    public List<Path> changedFiles() {
        throw new NotImplementedException();
    }

    public FileGroundTruth fileGroundTruth(Path path) {
        throw new NotImplementedException();
    }

    public String commitMessage() {
        throw new NotImplementedException();
    }

    public RevCommit commit() {
        throw new NotImplementedException();
    }
}
