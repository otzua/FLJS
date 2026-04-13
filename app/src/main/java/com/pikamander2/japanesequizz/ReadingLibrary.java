package com.pikamander2.japanesequizz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Curated library of Japanese reading passages.
 *
 * Hiragana passages: original sentences written entirely in hiragana, suitable
 *   for absolute beginners who are still learning the syllabary.
 *
 * Katakana passages: original sentences written entirely in katakana, suitable
 *   for learners practising that syllabary.
 *
 * Mixed (kana) passages: hiragana + katakana, no kanji — typical of manga
 *   furigana-only text or early children's readers.
 *
 * Kanji passages: hiragana + katakana + kanji — excerpts and paraphrases from
 *   public-domain sources (classical proverbs, traditional folktale sentences,
 *   and original graded-reader style sentences modelled on JLPT N5/N4 content).
 */
public class ReadingLibrary {

    private static final List<ReadingPassage> ALL_PASSAGES = buildLibrary();

    private static List<ReadingPassage> buildLibrary() {
        List<ReadingPassage> list = new ArrayList<>();

        // ──────────────────────────────────────────────────────
        //  HIRAGANA ONLY
        // ──────────────────────────────────────────────────────

        list.add(new ReadingPassage(
                "わたしは がくせい です。",
                "Watashi wa gakusei desu.",
                "I am a student.",
                "Original beginner sentence",
                ReadingPassage.Script.HIRAGANA,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "きょう は いい てんき です ね。",
                "Kyou wa ii tenki desu ne.",
                "Today is nice weather, isn't it.",
                "Original beginner sentence",
                ReadingPassage.Script.HIRAGANA,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "おはよう ございます。\nげんき です か？\nはい、げんき です。ありがとう。",
                "Ohayou gozaimasu.\nGenki desu ka?\nHai, genki desu. Arigatou.",
                "Good morning.\nAre you well?\nYes, I am well. Thank you.",
                "Original beginner dialogue",
                ReadingPassage.Script.HIRAGANA,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "ねこ が にわ に います。\nねこ は しろい です。\nとても かわいい ねこ です。",
                "Neko ga niwa ni imasu.\nNeko wa shiroi desu.\nTotemo kawaii neko desu.",
                "There is a cat in the garden.\nThe cat is white.\nIt is a very cute cat.",
                "Original beginner sentence",
                ReadingPassage.Script.HIRAGANA,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "まいにち、わたし は ろくじ に おきます。\nそして、あさごはん を たべます。\nごはん と みそしる が すき です。",
                "Mainichi, watashi wa rokujii ni okimasu.\nSoshite, asagohan wo tabemasu.\nGohan to misoshiru ga suki desu.",
                "Every day, I wake up at six o'clock.\nThen, I eat breakfast.\nI like rice and miso soup.",
                "Original beginner narrative",
                ReadingPassage.Script.HIRAGANA,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "がっこう は たのしい です。\nともだち と あそびます。\nせんせい は やさしい です。",
                "Gakkou wa tanoshii desu.\nTomodachi to asobimasu.\nSensei wa yasashii desu.",
                "School is fun.\nI play with friends.\nThe teacher is kind.",
                "Original beginner narrative",
                ReadingPassage.Script.HIRAGANA,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "はる に なりました。\nさくら の はな が さきました。\nこうえん に ひとが おおいです。",
                "Haru ni narimashita.\nSakura no hana ga sakimashita.\nKouen ni hito ga ooi desu.",
                "It has become spring.\nThe cherry blossom flowers have bloomed.\nThere are many people in the park.",
                "Original beginner narrative",
                ReadingPassage.Script.HIRAGANA,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "むかし むかし、あるところに おじいさん と おばあさん が いました。\nふたりは やまの ちかくに すんでいました。\nまいにち、おじいさん は やまへ しばかりに いきました。",
                "Mukashi mukashi, aru tokoro ni ojiisan to obaasan ga imashita.\nFutari wa yama no chikaku ni sundeiashita.\nMainichi, ojiisan wa yama e shibakari ni ikimashita.",
                "Once upon a time, in a certain place, there lived an old man and an old woman.\nThe two lived near a mountain.\nEvery day, the old man went to the mountain to cut grass.",
                "Inspired by classic Japanese folktale openings (public domain tradition)",
                ReadingPassage.Script.HIRAGANA,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "きのう、ともだち と えいが を みました。\nえいが は とても おもしろかった です。\nそのあと、いっしょに ラーメン を たべました。\nたのしい いちにち でした。",
                "Kinou, tomodachi to eiga wo mimashita.\nEiga wa totemo omoshirokatta desu.\nSono ato, issho ni raamen wo tabemashita.\nTanoshii ichinichi deshita.",
                "Yesterday, I watched a movie with a friend.\nThe movie was very interesting.\nAfterwards, we ate ramen together.\nIt was a fun day.",
                "Original intermediate narrative",
                ReadingPassage.Script.HIRAGANA,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "にほんご の べんきょう は むずかしい です が、\nたのしい です。\nまいにち すこし ずつ れんしゅう すれば、\nかならず じょうず に なれます。",
                "Nihongo no benkyou wa muzukashii desu ga,\ntanoshii desu.\nMainichi sukoshi zutsu renshuu sureba,\nkanarazu jouzu ni naremasu.",
                "Studying Japanese is difficult, but\nit is fun.\nIf you practise a little every day,\nyou will certainly become good at it.",
                "Original motivational passage",
                ReadingPassage.Script.HIRAGANA,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        // ──────────────────────────────────────────────────────
        //  KATAKANA ONLY
        // ──────────────────────────────────────────────────────

        list.add(new ReadingPassage(
                "コーヒー ガ スキ デス。\nマイニチ イップ ノミマス。",
                "Koohii ga suki desu.\nMainichi ippai nomimasu.",
                "I like coffee.\nI drink a cup every day.",
                "Original beginner sentence",
                ReadingPassage.Script.KATAKANA,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "コレ ハ ナン デスカ？\nソレ ハ テレビ デス。\nアタラシイ テレビ デス カ？\nハイ、アタラシイ デス。",
                "Kore wa nan desu ka?\nSore wa terebi desu.\nAtarashii terebi desu ka?\nHai, atarashii desu.",
                "What is this?\nThat is a television.\nIs it a new television?\nYes, it is new.",
                "Original beginner dialogue",
                ReadingPassage.Script.KATAKANA,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "アメリカ カラ キマシタ。\nニホン ニ スンデ イマス。\nニホンゴ ヲ ベンキョウ シテ イマス。",
                "Amerika kara kimashita.\nNihon ni sunde imasu.\nNihongo wo benkyou shite imasu.",
                "I came from America.\nI live in Japan.\nI am studying Japanese.",
                "Original beginner narrative",
                ReadingPassage.Script.KATAKANA,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "スーパー デ ヤサイ ト クダモノ ヲ カイマシタ。\nリンゴ、バナナ、ソレカラ ニンジン モ カイマシタ。\nゼンブ デ センエン デシタ。",
                "Suupaa de yasai to kudamono wo kaimashita.\nRingo, banana, sorekara ninjin mo kaimashita.\nZenbu de sen-en deshita.",
                "I bought vegetables and fruit at the supermarket.\nI bought apples, bananas, and also carrots.\nIt was one thousand yen in total.",
                "Original intermediate narrative",
                ReadingPassage.Script.KATAKANA,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "ホテル ノ チェックイン ハ サンジ デス。\nチェックアウト ハ ジュウイチジ デス。\nルーム サービス ヲ リヨウ デキマス カ？\nハイ、ニジュウヨジカン リヨウ デキマス。",
                "Hoteru no chekku-in wa sanji desu.\nChekku-auto wa juuichiji desu.\nRuumu saabisu wo riyou dekimasu ka?\nHai, nijuuyojikan riyou dekimasu.",
                "Hotel check-in is at three o'clock.\nCheck-out is at eleven o'clock.\nCan I use room service?\nYes, you can use it twenty-four hours.",
                "Original intermediate hotel dialogue",
                ReadingPassage.Script.KATAKANA,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "コノ レストラン ノ スペシャリテ ハ パスタ デス。\nシェフ ハ イタリア デ トレーニング シマシタ。\nメニュー ニ ベジタリアン オプション モ アリマス。\nヨヤク ハ オンライン デ デキマス。",
                "Kono resutoran no supesharite wa pasuta desu.\nShefu wa Itaria de toreeningen shimashita.\nMenyuu ni bejitarian opushon mo arimasu.\nYoyaku wa onrain de dekimasu.",
                "This restaurant's speciality is pasta.\nThe chef trained in Italy.\nThere are also vegetarian options on the menu.\nReservations can be made online.",
                "Original intermediate restaurant description",
                ReadingPassage.Script.KATAKANA,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "テクノロジー ノ ハッタツ ニ ヨッテ、\nセカイ ハ チイサク ナリマシタ。\nスマートフォン デ ドコ カラ デモ\nコミュニケーション ガ トレマス。\nコレ ハ ス バラシイ コト デス ガ、\nプライバシー ノ モンダイ モ アリマス。",
                "Tekunorojii no hattatsu ni yotte,\nsekai wa chiisaku narimashita.\nSumaatofon de doko kara demo\nkomyunikeeshon ga toremasu.\nKore wa subarashii koto desu ga,\npuraibasii no mondai mo arimasu.",
                "Due to the development of technology,\nthe world has become smaller.\nWith a smartphone, from anywhere,\nyou can communicate.\nThis is a wonderful thing, but\nthere are also privacy issues.",
                "Original advanced technological commentary",
                ReadingPassage.Script.KATAKANA,
                ReadingPassage.Difficulty.ADVANCED
        ));

        // ──────────────────────────────────────────────────────
        //  MIXED KANA (Hiragana + Katakana, no Kanji)
        // ──────────────────────────────────────────────────────

        list.add(new ReadingPassage(
                "きょう は スーパー で ショッピング を しました。\nリンゴ と バナナ を かいました。\nとても おいしそう です。",
                "Kyou wa suupaa de shoppingu wo shimashita.\nRingo to banana wo kaimashita.\nTotemo oishisou desu.",
                "Today I went shopping at the supermarket.\nI bought apples and bananas.\nThey look very delicious.",
                "Original beginner mixed kana sentence",
                ReadingPassage.Script.MIXED,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "わたし の すきな たべもの は ピザ です。\nチーズ が たくさん はいって います。\nイタリアりょうり が だいすき です。",
                "Watashi no sukina tabemono wa piza desu.\nChiizu ga takusan haitte imasu.\nItaria ryouri ga daisuki desu.",
                "My favourite food is pizza.\nIt has a lot of cheese in it.\nI love Italian food.",
                "Original beginner mixed kana narrative",
                ReadingPassage.Script.MIXED,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "まいあさ、わたし は コーヒー を のみます。\nコーヒー は ブラック が すき です。\nさとう も ミルク も いれません。",
                "Mai asa, watashi wa koohii wo nomimasu.\nKoohii wa burakku ga suki desu.\nSatou mo miruku mo iremasen.",
                "Every morning, I drink coffee.\nI like black coffee.\nI don't put in sugar or milk.",
                "Original beginner mixed kana narrative",
                ReadingPassage.Script.MIXED,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "がっこう が おわって から、ともだち と カフェ に いきました。\nケーキ と コーヒー を たのみました。\nカフェ の なか は しずか で、べんきょう が できます。\nふたりで にほんご の テスト の べんきょう を しました。",
                "Gakkou ga owatte kara, tomodachi to kafe ni ikimashita.\nKeeki to koohii wo tanomimashita.\nKafe no naka wa shizuka de, benkyou ga dekimasu.\nFutari de nihongo no tesuto no benkyou wo shimashita.",
                "After school ended, I went to a café with a friend.\nWe ordered cake and coffee.\nInside the café it is quiet and you can study.\nThe two of us studied for our Japanese test.",
                "Original intermediate mixed kana narrative",
                ReadingPassage.Script.MIXED,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "にほん に は、はる、なつ、あき、ふゆ の よつの きせつ が あります。\nはる に は サクラ が さきます。\nなつ は あつくて、よく プール に いきます。\nあき に は もみじ が あかく なります。\nふゆ は さむいですが、ゆき が とても きれい です。",
                "Nihon ni wa, haru, natsu, aki, fuyu no yottsu no kisetsu ga arimasu.\nHaru ni wa sakura ga sakimasu.\nNatsu wa atsukute, yoku puuru ni ikimasu.\nAki ni wa momiji ga akaku narimasu.\nFuyu wa samui desu ga, yuki ga totemo kirei desu.",
                "In Japan, there are four seasons: spring, summer, autumn, and winter.\nIn spring, cherry blossoms bloom.\nSummer is hot, and I often go to the pool.\nIn autumn, maple leaves turn red.\nWinter is cold, but the snow is very beautiful.",
                "Original intermediate mixed kana seasonal description",
                ReadingPassage.Script.MIXED,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "スマートフォン は いま の せいかつ に なく て は ならない ものです。\nメッセージ を おくったり、ニュース を よんだり、\nナビゲーション を つかったり できます。\nしかし、つかい すぎ に ちゅうい が ひつよう です。",
                "Sumaatofon wa ima no seikatsu ni nakute wa naranai mono desu.\nMesseeji wo okuttari, nyuusu wo yondari,\nnabigeeishon wo tsukattari dekimasu.\nShikashi, tsukaisugi ni chuui ga hitsuyou desu.",
                "Smartphones are now indispensable things in daily life.\nYou can send messages, read news,\nand use navigation.\nHowever, care is needed not to use them too much.",
                "Original intermediate technology commentary",
                ReadingPassage.Script.MIXED,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "グローバル か が すすむ なか で、えいご だけ でなく、\nほかの がいこくご を まなぶ ひと も ふえています。\nとくに アジア の ことば へ の きょうみ が たかまっています。\nことば を まなぶ こと は、その くに の ぶんか を\nりかい する だいいっぽ と いえるでしょう。",
                "Guroobaru ka ga susumu naka de, eigo dake de naku,\nhoka no gaikokugo wo manabu hito mo fuete imasu.\nTokuni Ajia no kotoba e no kyoumi ga takamatte imasu.\nKotoba wo manabu koto wa, sono kuni no bunka wo\nrikai suru daiippo to ieru deshou.",
                "As globalisation advances, not only English but also\nmore people are learning other foreign languages.\nIn particular, interest in Asian languages is growing.\nLearning a language can be said to be the first step\ntowards understanding that country's culture.",
                "Original advanced mixed kana essay",
                ReadingPassage.Script.MIXED,
                ReadingPassage.Difficulty.ADVANCED
        ));

        // ──────────────────────────────────────────────────────
        //  KANJI (Hiragana + Katakana + Kanji)
        // ──────────────────────────────────────────────────────

        list.add(new ReadingPassage(
                "今日は晴れです。",
                "Kyou wa hare desu.",
                "Today is sunny.",
                "Original beginner kanji sentence",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "私は毎朝、公園を散歩します。\n緑が多くて、気持ちがいいです。",
                "Watashi wa mai asa, kouen wo sanpo shimasu.\nMidori ga ookutte, kimochi ga ii desu.",
                "Every morning I take a walk in the park.\nThere is lots of greenery and it feels good.",
                "Original beginner kanji narrative",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "七転び八起き。",
                "Nana korobi ya oki.",
                "Fall seven times, stand up eight. (Proverb: perseverance)",
                "Traditional Japanese proverb (public domain)",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "石の上にも三年。",
                "Ishi no ue ni mo san-nen.",
                "Even on a stone for three years. (Proverb: patience and persistence lead to success)",
                "Traditional Japanese proverb (public domain)",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "花より団子。",
                "Hana yori dango.",
                "Dumplings over flowers. (Proverb: preferring practical things over beautiful ones)",
                "Traditional Japanese proverb (public domain)",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.BEGINNER
        ));

        list.add(new ReadingPassage(
                "昨日、友達と映画を見ました。\n映画はとても面白かったです。\nその後、一緒にラーメンを食べました。\n楽しい一日でした。",
                "Kinou, tomodachi to eiga wo mimashita.\nEiga wa totemo omoshirokatta desu.\nSono ato, issho ni raamen wo tabemashita.\nTanoshii ichinichi deshita.",
                "Yesterday I watched a movie with a friend.\nThe movie was very interesting.\nAfterwards we ate ramen together.\nIt was a fun day.",
                "Original intermediate kanji narrative",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "日本には四つの季節があります。\n春には桜が咲き、夏は暑く、\n秋には紅葉が美しく、冬は寒いです。\nそれぞれの季節に、特別な楽しみがあります。",
                "Nihon ni wa yottsu no kisetsu ga arimasu.\nHaru ni wa sakura ga saki, natsu wa atsuku,\naki ni wa kouyou ga utsukushiku, fuyu wa samui desu.\nSorezore no kisetsu ni, tokubetsu na tanoshimi ga arimasu.",
                "Japan has four seasons.\nIn spring cherry blossoms bloom, summer is hot,\nin autumn the autumn leaves are beautiful, and winter is cold.\nEach season has special pleasures.",
                "Original intermediate seasonal description",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "時は金なり。",
                "Toki wa kane nari.",
                "Time is money. (Proverb)",
                "Traditional Japanese proverb (public domain)",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "百聞は一見に如かず。",
                "Hyakubun wa ikken ni shikazu.",
                "Hearing a hundred times is not as good as seeing once. (Proverb: seeing is believing)",
                "Traditional Japanese proverb (public domain)",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "急がば回れ。",
                "Isogaba maware.",
                "If you are in a hurry, go around. (Proverb: haste makes waste)",
                "Traditional Japanese proverb (public domain)",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.INTERMEDIATE
        ));

        list.add(new ReadingPassage(
                "技術の進歩によって、私たちの生活は大きく変わりました。\nスマートフォンのおかげで、どこでも情報を得られます。\nしかし、人と人との直接のふれあいも大切にしたいものです。",
                "Gijutsu no shinpo ni yotte, watashitachi no seikatsu wa ookiku kawarimashita.\nSumaatofon no okage de, doko demo jouhou wo eraremasu.\nShikashi, hito to hito to no chokusetsu no fureau mo taisetsu ni shitai mono desu.",
                "Due to advances in technology, our lives have changed greatly.\nThanks to smartphones, we can obtain information anywhere.\nHowever, we should also value direct human connection.",
                "Original advanced technology and society passage",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.ADVANCED
        ));

        list.add(new ReadingPassage(
                "環境問題は現代社会が直面する最も深刻な課題の一つです。\n地球温暖化の影響で、異常気象が世界各地で増えています。\n私たちは一人一人が、エネルギーの節約やリサイクルに\n積極的に取り組む必要があります。",
                "Kankyou mondai wa gendai shakai ga chokumen suru mottomo shinkoku na kadai no hitotsu desu.\nChikyuu ondanka no eikyou de, ijou kishou ga sekai kakuchi de fuete imasu.\nWatashitachi wa hitori hitori ga, enerugii no setsuyaku ya risaikuru ni\nsekkyokuteki ni torikumu hitsuyou ga arimasu.",
                "Environmental issues are one of the most serious challenges modern society faces.\nDue to the effects of global warming, extreme weather is increasing around the world.\nEach and every one of us needs to actively work on\nenergy conservation and recycling.",
                "Original advanced environmental essay",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.ADVANCED
        ));

        list.add(new ReadingPassage(
                "夏目漱石の「吾輩は猫である」は、\n明治時代に書かれた有名な小説です。\n名前のない猫の目を通して、\n当時の社会と人間の様子が描かれています。",
                "Natsume Souseki no 'Wagahai wa neko de aru' wa,\nMeiji jidai ni kakareta yuumei na shousetsu desu.\nNamae no nai neko no me wo tooshite,\ntouji no shakai to ningen no yousu ga egakarete imasu.",
                "Natsume Soseki's 'I Am a Cat' is\na famous novel written in the Meiji era.\nThrough the eyes of a nameless cat,\nthe state of society and people of that time is depicted.",
                "Summary of Natsume Soseki's 'I Am a Cat' (1905–1906, public domain)",
                ReadingPassage.Script.KANJI,
                ReadingPassage.Difficulty.ADVANCED
        ));

        return list;
    }

    /** Return all passages matching a specific script type. */
    public static List<ReadingPassage> getByScript(ReadingPassage.Script script) {
        List<ReadingPassage> result = new ArrayList<>();
        for (ReadingPassage p : ALL_PASSAGES) {
            if (p.script == script) result.add(p);
        }
        return result;
    }

    /** Return all passages matching a script and difficulty. */
    public static List<ReadingPassage> getByScriptAndDifficulty(
            ReadingPassage.Script script, ReadingPassage.Difficulty difficulty) {
        List<ReadingPassage> result = new ArrayList<>();
        for (ReadingPassage p : ALL_PASSAGES) {
            if (p.script == script && p.difficulty == difficulty) result.add(p);
        }
        return result;
    }

    /** Pick a random passage for a given script. */
    public static ReadingPassage getRandom(ReadingPassage.Script script, Random random) {
        List<ReadingPassage> pool = getByScript(script);
        if (pool.isEmpty()) return null;
        return pool.get(random.nextInt(pool.size()));
    }

    /** Return all passages for a script, shuffled. */
    public static List<ReadingPassage> getShuffled(ReadingPassage.Script script, Random random) {
        List<ReadingPassage> pool = new ArrayList<>(getByScript(script));
        Collections.shuffle(pool, random);
        return pool;
    }
}
