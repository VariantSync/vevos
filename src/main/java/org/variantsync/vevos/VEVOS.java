package org.variantsync.vevos;

import org.variantsync.diffdetective.datasets.PatchDiffParseOptions;
import org.variantsync.diffdetective.diff.git.DiffFilter;
import org.variantsync.diffdetective.feature.CPPAnnotationParser;
import org.variantsync.diffdetective.variation.diff.parse.VariationDiffParseOptions;
import org.variantsync.vevos.common.CommitGroundTruth;
import org.variantsync.vevos.common.CommitId;
import org.variantsync.vevos.common.FileGroundTruth;
import org.variantsync.vevos.common.RepositoryLocation;
import org.variantsync.vevos.error.NotImplementedException;

import java.nio.file.Path;

public class VEVOS {
    private static final PatchDiffParseOptions DEFAULT_PARSE_OPTIONS = new PatchDiffParseOptions(
            PatchDiffParseOptions.DiffStoragePolicy.DO_NOT_REMEMBER,
            new VariationDiffParseOptions(
                    new CPPAnnotationParser(),
                    false,
                    false
            )
    );
    private static final DiffFilter DEFAULT_DIFF_FILTER = new DiffFilter.Builder()
            .allowMerge(true)
            // TODO: make configurable
            .allowedFileExtensions("h", "hpp", "c", "cpp")
            .build();

    public static FileGroundTruth extractFileGT(final Path path) {
        throw new NotImplementedException();
    }

    public static CommitGroundTruth extractCommitGroundTruth(RepositoryLocation testRepoLocation, CommitId commit) {
        throw new NotImplementedException();
    }

    public static FileGroundTruth extractFileGT(RepositoryLocation testRepoLocation, CommitId commitId, Path filePath) {
        throw new NotImplementedException();
    }
}
