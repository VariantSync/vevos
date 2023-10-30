package org.variantsync.vevos.common;

import org.variantsync.diffdetective.datasets.RepositoryLocationType;

public record RepositoryLocation(RepositoryLocationType locationType, String uri, String repoName) {
}
