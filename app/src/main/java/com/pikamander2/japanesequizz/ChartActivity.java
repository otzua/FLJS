package com.pikamander2.japanesequizz;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChartActivity extends AppCompatActivity {

    public static final String EXTRA_CHART_TYPE = "com.pikamander2.japanesequizz.CHART_TYPE";
    public static final int TYPE_HIRAGANA = 1;
    public static final int TYPE_KATAKANA = 2;

    private static final String[][] HIRAGANA = {
        {"あ","a"},{"い","i"},{"う","u"},{"え","e"},{"お","o"},
        {"か","ka"},{"き","ki"},{"く","ku"},{"け","ke"},{"こ","ko"},
        {"さ","sa"},{"し","shi"},{"す","su"},{"せ","se"},{"そ","so"},
        {"た","ta"},{"ち","chi"},{"つ","tsu"},{"て","te"},{"と","to"},
        {"な","na"},{"に","ni"},{"ぬ","nu"},{"ね","ne"},{"の","no"},
        {"は","ha"},{"ひ","hi"},{"ふ","fu"},{"へ","he"},{"ほ","ho"},
        {"ま","ma"},{"み","mi"},{"む","mu"},{"め","me"},{"も","mo"},
        {"や","ya"},{"",""},{"ゆ","yu"},{"",""},{"よ","yo"},
        {"ら","ra"},{"り","ri"},{"る","ru"},{"れ","re"},{"ろ","ro"},
        {"わ","wa"},{"",""},{"",""},{"",""},{"を","wo"},
        {"ん","n"},{"",""},{"",""},{"",""},{"",""}
    };

    private static final String[][] KATAKANA = {
        {"ア","a"},{"イ","i"},{"ウ","u"},{"エ","e"},{"オ","o"},
        {"カ","ka"},{"キ","ki"},{"ク","ku"},{"ケ","ke"},{"コ","ko"},
        {"サ","sa"},{"シ","shi"},{"ス","su"},{"セ","se"},{"ソ","so"},
        {"タ","ta"},{"チ","chi"},{"ツ","tsu"},{"テ","te"},{"ト","to"},
        {"ナ","na"},{"ニ","ni"},{"ヌ","nu"},{"ネ","ne"},{"ノ","no"},
        {"ハ","ha"},{"ヒ","hi"},{"フ","fu"},{"ヘ","he"},{"ホ","ho"},
        {"マ","ma"},{"ミ","mi"},{"ム","mu"},{"メ","me"},{"モ","mo"},
        {"ヤ","ya"},{"",""},{"ユ","yu"},{"",""},{"ヨ","yo"},
        {"ラ","ra"},{"リ","ri"},{"ル","ru"},{"レ","re"},{"ロ","ro"},
        {"ワ","wa"},{"",""},{"",""},{"",""},{"ヲ","wo"},
        {"ン","n"},{"",""},{"",""},{"",""},{"",""}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int type = getIntent().getIntExtra(EXTRA_CHART_TYPE, TYPE_HIRAGANA);
        boolean isHiragana = (type == TYPE_HIRAGANA);
        String[][] data = isHiragana ? HIRAGANA : KATAKANA;
        String title = isHiragana ? "Hiragana Chart" : "Katakana Chart";

        // Root layout
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFFF5F5F7);

        // Toolbar
        Toolbar toolbar = new Toolbar(this);
        toolbar.setBackgroundColor(0xFFF5F5F7);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(0xFF1A1A2E);
        LinearLayout.LayoutParams tbParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, getActionBarSize());
        root.addView(toolbar, tbParams);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }

        // ScrollView
        ScrollView scroll = new ScrollView(this);
        LinearLayout.LayoutParams svParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        scroll.setLayoutParams(svParams);

        // Grid
        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(5);
        int padding = dp(16);
        grid.setPadding(padding, padding, padding, padding);

        for (String[] pair : data) {
            String kana = pair[0];
            String romaji = pair[1];

            LinearLayout cell = new LinearLayout(this);
            cell.setOrientation(LinearLayout.VERTICAL);
            cell.setGravity(Gravity.CENTER);
            int cellSize = dp(60);
            int cellMargin = dp(4);
            GridLayout.LayoutParams cellParams = new GridLayout.LayoutParams();
            cellParams.width = cellSize;
            cellParams.height = cellSize;
            cellParams.setMargins(cellMargin, cellMargin, cellMargin, cellMargin);
            cell.setLayoutParams(cellParams);

            if (!kana.isEmpty()) {
                cell.setBackgroundColor(0xFFFFFFFF);
                // rounded look via padding
                cell.setPadding(dp(4), dp(4), dp(4), dp(4));

                TextView kanaView = new TextView(this);
                kanaView.setText(kana);
                kanaView.setTextSize(22);
                kanaView.setTextColor(0xFF1A1A2E);
                kanaView.setGravity(Gravity.CENTER);
                cell.addView(kanaView);

                TextView romajiView = new TextView(this);
                romajiView.setText(romaji);
                romajiView.setTextSize(10);
                romajiView.setTextColor(0xFF9E9E9E);
                romajiView.setGravity(Gravity.CENTER);
                cell.addView(romajiView);
            }

            grid.addView(cell);
        }

        scroll.addView(grid);
        root.addView(scroll);

        setContentView(root);
    }

    private int dp(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private int getActionBarSize() {
        int[] attrs = new int[]{android.R.attr.actionBarSize};
        android.content.res.TypedArray ta = getTheme().obtainStyledAttributes(attrs);
        int size = ta.getDimensionPixelSize(0, dp(56));
        ta.recycle();
        return size;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
