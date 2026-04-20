package com.pikamander2.japanesequizz;

import java.util.List;

/**
 * No-op stub — app is now fully local via LocalLibrary.
 * Kept to avoid any stale references that might cause compile errors.
 */
public class PassageFetcher {
    public interface FetchCallback {
        void onSuccess(List<ReadingPassage> passages);
        void onError(String message);
    }
    public PassageFetcher() {}
    public void fetch(ReadingPassage.Script script, FetchCallback cb) {
        // No-op: LocalLibrary handles everything offline
    }
    public void shutdown() {}
}
