package com.pikamander2.japanesequizz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.LinearLayout;
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
        // Use XML layout — this ensures AppBarLayout handles fitsSystemWindows
        // correctly and the toolbar never overlaps the notch/status bar.
        setContentView(R.layout.activity_chart);

        int type = getIntent().getIntExtra(EXTRA_CHART_TYPE, TYPE_HIRAGANA);
        boolean isHiragana = (type == TYPE_HIRAGANA);
        String[][] data = isHiragana ? HIRAGANA : KATAKANA;
        String title = isHiragana ? "Hiragana Chart" : "Katakana Chart";

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }

        GridLayout grid = findViewById(R.id.gridLayout);
        grid.setColumnCount(5);

        // Resolve theme colors so the chart respects dark/light mode
        int colorBackground = getColor(R.color.colorSurface);
        int colorOnBackground = resolveAttrColor(android.R.attr.textColorPrimary);
        int colorSecondary = resolveAttrColor(android.R.attr.textColorSecondary);

        for (String[] pair : data) {
            String kana = pair[0];
            String romaji = pair[1];

            LinearLayout cell = new LinearLayout(this);
            cell.setOrientation(LinearLayout.VERTICAL);
            cell.setGravity(Gravity.CENTER);
            int cellSize = dp(64);
            int cellMargin = dp(4);
            GridLayout.LayoutParams cellParams = new GridLayout.LayoutParams();
            cellParams.width = cellSize;
            cellParams.height = cellSize;
            cellParams.setMargins(cellMargin, cellMargin, cellMargin, cellMargin);
            cell.setLayoutParams(cellParams);

            if (!kana.isEmpty()) {
                cell.setBackgroundColor(colorBackground);
                cell.setPadding(dp(4), dp(4), dp(4), dp(4));

                TextView kanaView = new TextView(this);
                kanaView.setText(kana);
                kanaView.setTextSize(22);
                kanaView.setTextColor(colorOnBackground);
                kanaView.setGravity(Gravity.CENTER);
                cell.addView(kanaView);

                TextView romajiView = new TextView(this);
                romajiView.setText(romaji);
                romajiView.setTextSize(10);
                romajiView.setTextColor(colorSecondary);
                romajiView.setGravity(Gravity.CENTER);
                cell.addView(romajiView);
            }

            grid.addView(cell);
        }
    }

    private int resolveAttrColor(int attr) {
        int[] attrs = new int[]{attr};
        android.content.res.TypedArray ta = getTheme().obtainStyledAttributes(attrs);
        int color = ta.getColor(0, 0xFF1A1A2E);
        ta.recycle();
        return color;
    }

    private int dp(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
