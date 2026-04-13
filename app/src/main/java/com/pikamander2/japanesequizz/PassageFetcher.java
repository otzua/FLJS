package com.pikamander2.japanesequizz;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Fetches full-length Japanese reading passages from two public sources:
 *
 *   1. NHK Web Easy (https://www3.nhk.or.jp/news/easy/)
 *      Simplified Japanese news articles published by Japan's national broadcaster.
 *      Each article is ~400–800 characters of kana + kanji with ruby furigana
 *      annotations embedded in the HTML.  No login, no API key required.
 *      Content is © NHK — displayed for educational/non-commercial personal use,
 *      consistent with NHK's stated purpose of the site as a language-learning
 *      resource.  The URL scheme is publicly documented and stable.
 *
 *   2. Aozora Bunko public-domain excerpts (https://www.aozora.gr.jp/)
 *      Classic Japanese literature whose copyright has expired.  The texts are
 *      served as plain HTML from a stable CDN.  Excerpts used are identified by
 *      their Aozora card ID and are fully in the public domain under Japanese law.
 *
 * All network calls happen on a background thread via ExecutorService.
 * Results are delivered back to the main thread via a Handler.
 */
public class PassageFetcher {

    public interface FetchCallback {
        /** Called on the main thread when fetch succeeds. */
        void onSuccess(List<ReadingPassage> passages);
        /** Called on the main thread if all sources failed. */
        void onError(String message);
    }

    private static final String TAG = "PassageFetcher";

    // NHK Web Easy article list — public JSON endpoint, no auth required
    private static final String NHK_LIST_URL =
            "https://www3.nhk.or.jp/news/easy/top-list-detail.json";
    // NHK article HTML pattern — %s = article_id (appears twice)
    private static final String NHK_ARTICLE_URL =
            "https://www3.nhk.or.jp/news/easy/%s/%s.html";

    // Aozora Bunko — public-domain works, stable card URLs
    private static final String[][] AOZORA_DIRECT = {
        {
            "https://www.aozora.gr.jp/cards/000035/files/1567_14913.html",
            "走れメロス (Run, Melos!)", "太宰治 (Dazai Osamu)",
            "ADVANCED"
        },
        {
            "https://www.aozora.gr.jp/cards/000879/files/127_15260.html",
            "羅生門 (Rashomon)", "芥川龍之介 (Akutagawa Ryunosuke)",
            "ADVANCED"
        },
        {
            "https://www.aozora.gr.jp/cards/000148/files/789_14547.html",
            "吾輩は猫である (I Am a Cat)", "夏目漱石 (Natsume Soseki)",
            "ADVANCED"
        },
        {
            "https://www.aozora.gr.jp/cards/000121/files/628_14895.html",
            "ごん狐 (Gon, the Little Fox)", "新美南吉 (Niimi Nankichi)",
            "INTERMEDIATE"
        }
    };

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final OkHttpClient client;
    private final Random random = new Random();

    public PassageFetcher() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                .build();
    }

    /**
     * Fetch passages for the given script type.
     * For KANJI/MIXED this fetches NHK Web Easy articles (real news, full kanji + kana).
     * For HIRAGANA/KATAKANA it fetches Aozora or NHK and converts/transcribes.
     * The callback is always called exactly once on the main thread.
     */
    public void fetch(ReadingPassage.Script script, FetchCallback callback) {
        executor.execute(() -> {
            List<ReadingPassage> result = new ArrayList<>();

            // NHK Web Easy is currently requiring JWT tokens for their JSON API.
            // We'll skip it for now and rely on Aozora Bunko which is more stable.
            
            try {
                // Fetch multiple unique works from Aozora
                List<Integer> indices = new ArrayList<>();
                for (int i = 0; i < AOZORA_DIRECT.length; i++) indices.add(i);
                Collections.shuffle(indices);
                
                int count = 0;
                for (int idx : indices) {
                    if (count >= 3) break;
                    ReadingPassage aozora = fetchAozoraPassage(idx);
                    if (aozora != null) {
                        result.add(aozora);
                        count++;
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "Aozora fetch failed: " + e.getMessage());
            }

            if (result.isEmpty()) {
                mainHandler.post(() -> callback.onError(
                        "Could not connect to the internet. Using offline passages."));
            } else {
                Collections.shuffle(result, random);
                final List<ReadingPassage> finalResult = result;
                mainHandler.post(() -> callback.onSuccess(finalResult));
            }
        });
    }

    // ── NHK Web Easy ──────────────────────────────────────────────────────────

    private List<ReadingPassage> fetchNhkPassages(ReadingPassage.Script script)
            throws IOException {

        // 1. Grab the article list JSON
        String listJson = get(NHK_LIST_URL);
        List<String> articleIds;
        try {
            articleIds = parseNhkArticleIds(listJson);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing NHK list JSON", e);
            return new ArrayList<>();
        }

        // Shuffle and take up to 5 articles so we don't hammer the server
        Collections.shuffle(articleIds, random);
        int limit = Math.min(5, articleIds.size());

        List<ReadingPassage> passages = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            try {
                String id = articleIds.get(i);
                String url = String.format(NHK_ARTICLE_URL, id, id);
                String html = get(url);
                ReadingPassage p = parseNhkArticle(html, id, script);
                if (p != null) passages.add(p);
            } catch (Exception e) {
                Log.d(TAG, "Skipping NHK article: " + e.getMessage());
            }
        }
        return passages;
    }

    /**
     * Parses the NHK list JSON.  The actual response is a JSON array of objects,
     * each containing "news_id" as a string.
     */
    private List<String> parseNhkArticleIds(String json) throws Exception {
        List<String> ids = new ArrayList<>();
        // NHK returns a top-level array
        JSONArray arr = new JSONArray(json);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.has("news_id")) {
                ids.add(obj.getString("news_id"));
            }
        }
        return ids;
    }

    /**
     * Parses one NHK Web Easy article HTML page.
     *
     * The article body is wrapped in <div class="article-body"> or similar.
     * The text includes <ruby>漢字<rt>ふりがな</rt></ruby> tags.
     * We extract:
     *   - japanese: the main text with kanji (ruby stripped, rt stripped)
     *   - romaji: the text with kanji replaced by their furigana (from <rt>)
     *   - english: "(Translation not available — NHK Web Easy article)"
     */
    private ReadingPassage parseNhkArticle(String html, String articleId,
                                           ReadingPassage.Script targetScript) {

        // Extract the article title
        String title = extractBetween(html, "<title>", "</title>");
        if (title == null) title = "NHK Web Easy Article";
        title = title.replace("NHK Web Easyニュース", "").trim();
        // Remove any remaining HTML from title
        title = stripTags(title).trim();

        // Extract the article body — NHK wraps it in <div id="js-article-body">
        // or <article ...>; try multiple selectors
        String body = extractBetween(html, "id=\"js-article-body\"", "</div>");
        if (body == null || body.length() < 100) {
            body = extractBetween(html, "<article", "</article>");
        }
        if (body == null || body.length() < 100) {
            // Fall back to taking everything between the first <p> and </body>
            body = extractBetween(html, "<p>", "</body>");
        }
        if (body == null || body.length() < 50) return null;

        // Build japanese text (kanji form) and furigana form from ruby tags
        String japanese = buildJapaneseText(body);
        String furigana  = buildFuriganaText(body);

        // Minimum length: must be at least ~200 characters (a solid paragraph)
        if (japanese.length() < 150) return null;

        // Truncate at a natural sentence boundary if too long (keep ≤ ~1200 chars)
        japanese  = truncateAtSentence(japanese,  1200);
        furigana  = truncateAtSentence(furigana,  1500);

        // Determine difficulty from article length / complexity
        ReadingPassage.Difficulty diff;
        int kanjiCount = countKanji(japanese);
        if (kanjiCount < 20)      diff = ReadingPassage.Difficulty.BEGINNER;
        else if (kanjiCount < 60) diff = ReadingPassage.Difficulty.INTERMEDIATE;
        else                      diff = ReadingPassage.Difficulty.ADVANCED;

        // Script type — NHK articles always have kanji; for HIRAGANA/KATAKANA
        // mode we still use KANJI script since we can't strip kanji reliably.
        ReadingPassage.Script script = ReadingPassage.Script.KANJI;

        String source = "NHK Web Easy (nhk.or.jp/news/easy) — "
                + "simplified Japanese news for language learners.\n"
                + "Article ID: " + articleId;

        return new ReadingPassage(
                japanese,
                furigana,
                "(English translation not included — this is a live NHK Web Easy\n"
                        + "news article. Reading comprehension challenge!)",
                source,
                script,
                diff
        );
    }

    // ── Aozora Bunko ──────────────────────────────────────────────────────────

    private ReadingPassage fetchAozoraPassage(int index) throws IOException {
        String[] work = AOZORA_DIRECT[index];
        String url    = work[0];
        String title  = work[1];
        String author = work[2];
        boolean isAdvanced = "ADVANCED".equals(work[3]);

        // Aozora Bunko uses Shift_JIS encoding
        String html = get(url, "Shift_JIS");

        // Aozora body is inside <div class="main_text"> or <div id="honbun">
        String body = extractBetween(html, "class=\"main_text\"", "</div>");
        if (body == null || body.length() < 200) {
            body = extractBetween(html, "id=\"honbun\"", "</div>");
        }
        if (body == null || body.length() < 200) {
            // Fall back: take everything between the first <br> block and the
            // colophon marker 底本
            int start = html.indexOf("<br />");
            int end   = html.indexOf("底本");
            if (start > 0 && end > start) {
                body = html.substring(start, Math.min(end, start + 8000));
            }
        }
        if (body == null || body.length() < 100) return null;

        String japanese = buildJapaneseText(body);
        String furigana  = buildFuriganaText(body);

        // Aozora texts can be very long — take the first substantial chunk (~1500 chars)
        // starting after any preamble
        japanese = truncateAtSentence(japanese, 1500);
        furigana = truncateAtSentence(furigana, 2000);

        if (japanese.length() < 150) return null;

        ReadingPassage.Difficulty diff = isAdvanced
                ? ReadingPassage.Difficulty.ADVANCED
                : ReadingPassage.Difficulty.INTERMEDIATE;

        String source = "Aozora Bunko (aozora.gr.jp) — public domain Japanese literature.\n"
                + title + " / " + author;

        return new ReadingPassage(
                japanese,
                furigana,
                "(English translation not included for this classic text.\n"
                        + "Work: " + title + " by " + author + ")",
                source,
                ReadingPassage.Script.KANJI,
                diff
        );
    }

    // ── HTML parsing utilities ────────────────────────────────────────────────

    /**
     * Builds plain Japanese text with kanji from HTML containing ruby annotations.
     * Strips all HTML tags; keeps kanji form of ruby elements.
     * e.g. <ruby>東京<rt>とうきょう</rt></ruby>  →  東京
     */
    private String buildJapaneseText(String html) {
        // Remove <rt>...</rt> blocks (the furigana)
        String s = html.replaceAll("(?i)<rt[^>]*>.*?</rt>", "");
        // Remove <rp> blocks (parentheses for non-ruby browsers)
        s = s.replaceAll("(?i)<rp[^>]*>.*?</rp>", "");
        // Remove ruby wrapper tags (keep inner kanji text)
        s = s.replaceAll("(?i)</?ruby[^>]*>", "");
        s = s.replaceAll("(?i)</?rb[^>]*>", "");
        // Convert <br> variants to newlines
        s = s.replaceAll("(?i)<br\\s*/?>", "\n");
        // Replace common HTML entities
        s = unescapeHtml(s);
        // Strip remaining tags
        s = stripTags(s);
        // Collapse excess whitespace / blank lines
        s = s.replaceAll("[ \t]+", " ")
             .replaceAll("\n{3,}", "\n\n")
             .trim();
        return s;
    }

    /**
     * Builds a furigana (kana) form of the text by replacing kanji with their
     * ruby readings where available, leaving already-kana text unchanged.
     * e.g. <ruby>東京<rt>とうきょう</rt></ruby>  →  とうきょう
     */
    private String buildFuriganaText(String html) {
        // Remove <rp> blocks first
        String s = html.replaceAll("(?i)<rp[^>]*>.*?</rp>", "");
        
        // Replace <ruby>...<rt>ふりがな</rt>...</ruby> with ふりがな
        Pattern rubyPattern = Pattern.compile(
                "(?i)<ruby[^>]*>.*?<rt[^>]*>(.*?)</rt>.*?</ruby>",
                Pattern.DOTALL);
        StringBuffer sb = new StringBuffer();
        Matcher m = rubyPattern.matcher(s);
        while (m.find()) {
            String furigana = m.group(1);
            // Sanitise the furigana replacement
            furigana = stripTags(furigana).trim();
            m.appendReplacement(sb, Matcher.quoteReplacement(furigana));
        }
        m.appendTail(sb);
        s = sb.toString();
        // Remove any remaining ruby/rt/rb tags
        s = s.replaceAll("(?i)</?ruby[^>]*>", "");
        s = s.replaceAll("(?i)</?rb[^>]*>", "");
        s = s.replaceAll("(?i)<rt[^>]*>.*?</rt>", "");
        s = s.replaceAll("(?i)<br\\s*/?>", "\n");
        s = unescapeHtml(s);
        s = stripTags(s);
        s = s.replaceAll("[ \t]+", " ")
             .replaceAll("\n{3,}", "\n\n")
             .trim();
        return s;
    }

    /** Strip all HTML tags from a string. */
    private String stripTags(String html) {
        return html.replaceAll("<[^>]+>", "");
    }

    /** Extract text between the first occurrence of startMarker and the next endMarker. */
    private String extractBetween(String html, String startMarker, String endMarker) {
        int start = html.indexOf(startMarker);
        if (start < 0) return null;
        start += startMarker.length();
        // Skip ahead to the first '>' if the start marker is an opening tag fragment
        if (!startMarker.endsWith(">") && !startMarker.endsWith("}")) {
            int gt = html.indexOf('>', start);
            if (gt > start) start = gt + 1;
        }
        int end = html.indexOf(endMarker, start);
        if (end <= start) return null;
        return html.substring(start, end);
    }

    /** Unescape common HTML entities. */
    private String unescapeHtml(String s) {
        return s.replace("&amp;",  "&")
                .replace("&lt;",   "<")
                .replace("&gt;",   ">")
                .replace("&quot;", "\"")
                .replace("&#39;",  "'")
                .replace("&nbsp;", " ")
                .replace("&#8194;", " ")   // en-space
                .replace("&#12288;", "　"); // ideographic space
    }

    /**
     * Truncate at the last sentence-ending punctuation (。！？\n) before maxLen,
     * to avoid cutting mid-sentence.
     */
    private String truncateAtSentence(String text, int maxLen) {
        if (text.length() <= maxLen) return text;
        String sub = text.substring(0, maxLen);
        // Find the last sentence boundary
        int lastBoundary = -1;
        for (int i = sub.length() - 1; i >= 0; i--) {
            char c = sub.charAt(i);
            if (c == '。' || c == '！' || c == '？' || c == '\n') {
                lastBoundary = i + 1;
                break;
            }
        }
        if (lastBoundary > maxLen / 2) {
            return sub.substring(0, lastBoundary).trim();
        }
        return sub.trim();
    }

    /** Count CJK unified ideographs (kanji) in text as a complexity proxy. */
    private int countKanji(String text) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c >= 0x4E00 && c <= 0x9FFF) count++;
        }
        return count;
    }

    // ── HTTP ─────────────────────────────────────────────────────────────────

    /** Synchronous GET — must be called off the main thread. */
    private String get(String url, String charset) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                        + "AppleWebKit/537.36 (KHTML, like Gecko) "
                        + "Chrome/120.0.0.0 Safari/537.36")
                .header("Accept-Language", "ja,en;q=0.9")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("HTTP " + response.code() + " for " + url);
            }
            if (response.body() == null) throw new IOException("Empty body for " + url);
            
            if (charset != null) {
                return new String(response.body().bytes(), charset);
            } else {
                return response.body().string();
            }
        }
    }

    private String get(String url) throws IOException {
        return get(url, null);
    }

    /** Cancel any pending fetches (call from onDestroy). */
    public void shutdown() {
        executor.shutdownNow();
    }
}
