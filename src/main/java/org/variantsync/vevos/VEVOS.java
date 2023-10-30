package org.variantsync.vevos;

import org.variantsync.vevos.common.CommitGroundTruth;
import org.variantsync.vevos.common.CommitId;
import org.variantsync.vevos.common.FileGroundTruth;
import org.variantsync.vevos.common.RepositoryLocation;
import org.variantsync.vevos.error.NotImplementedException;
import org.variantsync.vevos.extraction.ExtractionOptions;
import org.variantsync.vevos.simulation.SimulationOptions;

import java.nio.file.Path;

public class VEVOS {
    private final ExtractionOptions extractionOptions;
    private final SimulationOptions simulationOptions;

    public VEVOS() {
        this.extractionOptions = new ExtractionOptions();
        this.simulationOptions = new SimulationOptions();
    }

    public VEVOS(ExtractionOptions extractionOptions) {
        this.extractionOptions = extractionOptions;
        this.simulationOptions = new SimulationOptions();
    }

    public VEVOS(SimulationOptions simulationOptions) {
        this.extractionOptions = new ExtractionOptions();
        this.simulationOptions = simulationOptions;
    }

    public VEVOS(ExtractionOptions extractionOptions, SimulationOptions simulationOptions) {
        this.extractionOptions = extractionOptions;
        this.simulationOptions = simulationOptions;
    }

    public FileGroundTruth extractFileGT(final Path path) {
        throw new NotImplementedException();
    }

    public CommitGroundTruth extractCommitGroundTruth(RepositoryLocation testRepoLocation, CommitId commit) {
        throw new NotImplementedException();
    }

    public FileGroundTruth extractFileGT(RepositoryLocation testRepoLocation, CommitId commitId, Path filePath) {
        throw new NotImplementedException();
    }
}
