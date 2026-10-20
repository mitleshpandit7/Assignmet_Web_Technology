package com.studentportal.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Safe file-path validation. Guarantees that the returned file
 *  - lies INSIDE the configured upload folder (blocks ../ traversal, absolute paths, symlink escapes),
 *  - exists, is a regular file and is readable.
 */
public final class SafePath {

    private SafePath() {
    }

    public static Optional<Path> resolve(Path baseDir, String relative) {
        if (baseDir == null || relative == null) {
            return Optional.empty();
        }
        String rel = relative.trim();
        if (rel.isEmpty() || rel.indexOf('\0') >= 0) {
            return Optional.empty();
        }
        try {
            Path realBase = baseDir.toRealPath();                  // resolves symlinks, must exist
            Path candidate = realBase.resolve(rel).normalize();    // absolute input replaces base -> caught below
            if (!candidate.startsWith(realBase)) {
                return Optional.empty();                           // path traversal attempt
            }
            Path real = candidate.toRealPath();                    // throws if the file is missing
            if (!real.startsWith(realBase) || !Files.isRegularFile(real) || !Files.isReadable(real)) {
                return Optional.empty();                           // symlink escape / directory / unreadable
            }
            return Optional.of(real);
        } catch (IOException | InvalidPathException | SecurityException e) {
            return Optional.empty();
        }
    }
}
