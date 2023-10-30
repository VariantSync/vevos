package org.variantsync.vevos.common;

import org.variantsync.diffdetective.variation.diff.Time;

import java.nio.file.Path;
import java.util.*;

public record CodeMatching(HashMap<Path, FileMatching> fileMatchingBefore, HashMap<Path, FileMatching> fileMatchingAfter) {

    public Optional<Integer> beforeToAfterMatch(Path pathToFile, int lineNumber) {
        return Optional.of(fileMatchingBefore.get(pathToFile)).map(m -> m.matching.get(lineNumber));
    }

    public Optional<Integer> afterToBeforeMatch(Path pathToFile, int lineNumber) {
        return Optional.of(fileMatchingAfter.get(pathToFile)).map(m -> m.matching.get(lineNumber));
    }

    public Optional<Integer> match(Path pathToFile, int lineNumber, Time time) {
        switch (time) {
            case BEFORE -> {
                return beforeToAfterMatch(pathToFile, lineNumber);
            }
            case AFTER -> {
                return afterToBeforeMatch(pathToFile, lineNumber);
            }
            default -> throw new IllegalStateException();
        }
    }

    private static final class FileMatching {
        private final Path filePath;
        private final ArrayList<Integer> matching;

        public FileMatching(Path filePath) {
            super();
            this.filePath = filePath;
            this.matching = new ArrayList<>();
        }

        public void add(int currentLine, int matchedLine) {
            if (this.matching.size() != currentLine) {
                throw new IllegalStateException();
            }
            this.matching.add(matchedLine);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (FileMatching) obj;
            return Objects.equals(this.filePath, that.filePath) &&
                    Objects.equals(this.matching, that.matching);
        }

        @Override
        public int hashCode() {
            return Objects.hash(filePath, matching);
        }

        @Override
        public String toString() {
            return "FileMatching[" +
                    "filePath=" + filePath + ", " +
                    "matching=" + matching + ']';
        }

    }

    private static void checkAgreement(HashMap<Path, FileMatching> before, HashMap<Path, FileMatching> after) {
        final IllegalArgumentException agreementMismatch = new IllegalArgumentException("The matchings do not agree.");
        if (before.keySet().size() != after.keySet().size()) {
            throw agreementMismatch;
        }
        for (Path path : before.keySet()) {
            FileMatching beforeMatching = before.get(path);
            FileMatching afterMatching = after.get(path);
            if (afterMatching == null) {
                throw agreementMismatch;
            }
            if (arraysDisagree(beforeMatching.matching, afterMatching.matching)
                    || arraysDisagree(afterMatching.matching, beforeMatching.matching)) {
                throw agreementMismatch;
            }
        }
    }

    private static boolean arraysDisagree(ArrayList<Integer> first, ArrayList<Integer> second) {
        for (int i = 0; i < first.size(); i++)  {
            if (first.get(i) == -1) {
                continue;
            }
            if (second.get(first.get(i)) != i) {
                return true;
            }
        }
        return false;
    }
}
