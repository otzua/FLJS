package com.pikamander2.japanesequizz;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.List;

/**
 * OFFLINE VERSION: Internet access removed to satisfy banking security requirements.
 */
public class PassageFetcher {

    public interface FetchCallback {
        /** Called on the main thread when fetch succeeds. */
        void onSuccess(List<ReadingPassage> passages);
        /** Called on the main thread if all sources failed. */
        void onError(String message);
    }

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public PassageFetcher() {
        // No client needed
    }

    /**
     * Fetch passages for the given script type (OFFLINE ONLY).
     */
    public void fetch(ReadingPassage.Script script, FetchCallback callback) {
        // Always return an empty list or error to fallback to offline library
        mainHandler.post(() -> callback.onError("Internet connection disabled for security. Using local library."));
    }

    /** No-op shutdown */
    public void shutdown() {
        // Nothing to shut down
    }
}