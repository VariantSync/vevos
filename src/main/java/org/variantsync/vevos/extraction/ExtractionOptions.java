package org.variantsync.vevos.extraction;

import org.variantsync.diffdetective.datasets.PatchDiffParseOptions;
import org.variantsync.diffdetective.diff.git.DiffFilter;
import org.variantsync.diffdetective.feature.CPPAnnotationParser;
import org.variantsync.diffdetective.variation.diff.parse.VariationDiffParseOptions;

public class ExtractionOptions {
    final PatchDiffParseOptions parseOptions;
    final DiffFilter diffFilter;
    final ExtractionMode extractionMode;

    public static final PatchDiffParseOptions DEFAULT_PARSE_OPTIONS = new PatchDiffParseOptions(
            PatchDiffParseOptions.DiffStoragePolicy.DO_NOT_REMEMBER,
            new VariationDiffParseOptions(
                    new CPPAnnotationParser(),
                    false,
                    false
            )
    );

    public static final DiffFilter DEFAULT_DIFF_FILTER = new DiffFilter.Builder()
            .allowMerge(true)
            .allowedFileExtensions("h", "hpp", "c", "cpp")
            .build();

    public static final ExtractionMode DEFAULT_EXTRACTION_MODE = ExtractionMode.Full;

    public ExtractionOptions(ExtractionMode extractionMode) {
        this.parseOptions = DEFAULT_PARSE_OPTIONS;
        this.diffFilter = DEFAULT_DIFF_FILTER;
        this.extractionMode = extractionMode;
    }

    public ExtractionOptions(PatchDiffParseOptions parseOptions, DiffFilter diffFilter, ExtractionMode extractionMode) {
        this.parseOptions = parseOptions;
        this.diffFilter = diffFilter;
        this.extractionMode = extractionMode;
    }

    public ExtractionOptions() {
        this.parseOptions = DEFAULT_PARSE_OPTIONS;
        this.diffFilter = DEFAULT_DIFF_FILTER;
        this.extractionMode = DEFAULT_EXTRACTION_MODE;
    }
}
