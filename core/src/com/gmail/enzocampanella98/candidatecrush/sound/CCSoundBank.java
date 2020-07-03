package com.gmail.enzocampanella98.candidatecrush.sound;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CCSoundBank implements Disposable {
    // singleton class to keep track of all sounds

    private static final String[] SOUNDBYTE_NAMES = new String[]{
            "biden_3_brought_people_together.mp3",
            "biden_3_the_thing.mp3",
//            "biden_4_barak_america.mp3",
            "biden_5_decriminalize_marijuana.mp3",
            "biden_t_kim_jung_un_rabid_dog.mp3",
            "biden_3_change_culture_of_how_women_are_treated.mp3",
            "biden_4_free_community_college.mp3",
            "biden_5_getting_troops_out_of_iraq.mp3",
            "biden_t_poor_kids_white_kids.mp3",
            "biden_3_defeat_donald_trump.mp3",
            "biden_4_for_a_socialist_corporate_america.mp3",
            "biden_5_job_more_than_paycheck.mp3",
            "biden_t_president_has_big_stick.mp3",
            "biden_3_directing_justice_dept.mp3",
            "biden_4_smart_guns.mp3",
            "biden_5_your_moms_still_alive.mp3",
            "biden_3_trump_doesnt_want_me_to_be_the_nominee.mp3",
            "biden_t_tweeting.mp3",
            "biden_3_we_have_to_restore_the_soul_of_this_country.mp3",
            "biden_4_tired_of_woe_is_me.mp3",
            "buttigieg_3_born_to_make_myself_useful.mp3",
            "buttigieg_4_need_something_very_different.mp3",
            "buttigieg_5_can_read_through_article_languages.mp3",
            "buttigieg_t_capitol_hill_looks_small.mp3",
            "buttigieg_3_down_south_they_call_me_mayor_pete.mp3",
            "buttigieg_4_only_chance_youll_ever_get.mp3",
            "buttigieg_5_dont_have_21st_century_security_strategy.mp3",
            "buttigieg_t_is_anyone_proposing_invading_mexico.mp3",
            "buttigieg_3_i_dont_even_golf.mp3",
            "buttigieg_4_solutions_lie_with_american_farmers.mp3",
            "buttigieg_5_moat_full_of_alligators.mp3",
            "buttigieg_t_labels_becoming_less_useful.mp3",
            "buttigieg_3_president_doesnt_care_about_farmers.mp3",
            "buttigieg_4_what_your_ideas_are.mp3",
            "buttigieg_t_least_wealthy_person_on_stage.mp3",
            "buttigieg_3_unify_a_nation.mp3",
            "clinton_3_And I thank all of you.mp3",
            "clinton_3_Im exhausted.mp3",
            "clinton_4_I remember when my husband.mp3",
            "clinton_4_Marching orders.mp3",
            "clinton_5_This is Hillary Clinton.mp3",
            "clinton_t_As we gather here today.mp3",
            "clinton_t_Every American gets a cup cake.mp3",
            "cruz_3_halahala.mp3",
            "cruz_3_hehe.mp3",
            "cruz_3_thank you.mp3",
            "cruz_4_goose diarrhea.mp3",
            "cruz_4_i need a bogle for the glotch.mp3",
            "cruz_4_you're the devil.mp3",
            "cruz_5_barfed on the beach.mp3",
            "cruz_5_hooray for you.mp3",
            "cruz_t_garden knomes.mp3",
            "cruz_t_i just love to twerk.mp3",
            "meza_3_crush.mp3",
            "meza_4_crush.mp3",
            "meza_5_crush.mp3",
            "meza_t_crush.mp3",
            "sanders_3_black lives matter.mp3",
            "sanders_4_free_college.mp3",
            "sanders_5_congress_walk_chew_bubble_gum.mp3",
            "sanders_t_enough of the emails.mp3",
            "sanders_3_corrupt_political_system.mp3",
            "sanders_4_take back out government.mp3",
            "sanders_5_don't underestimate me.mp3",
            "sanders_t_fully_paid_for.mp3",
            "sanders_3_function_of_health_care_system.mp3",
            "sanders_4_three_people_more_wealth.mp3",
            "sanders_5_existential_threat_climate_change.mp3",
            "sanders_t_guts_to_stand_up_to_special_interests.mp3",
            "sanders_3_last_poll_i_saw.mp3",
            "sanders_4_trump_pathological_liar.mp3",
            "sanders_5_Huge.mp3",
            "sanders_t_middle class of this country is collapsing.mp3",
            "sanders_3_marijuana.mp3",
            "sanders_4_twelve_years_before_irreperable_damage.mp3",
            "sanders_5_i_wrote_the_damn_bill.mp3",
            "sanders_t_we_dont_have_decades.mp3",
            "sanders_3_more_in_taxes_less_in_health_care.mp3",
            "sanders_4_we can do it.mp3",
            "sanders_5_we_encourage_diversity.mp3",
            "sanders_t_What this campaign is about.mp3",
            "sanders_3_most_people_min_wage_living_wage.mp3",
            "sanders_4_wealth and income inequality.mp3",
            "sanders_t_whether_the_democratic_party_has_the_guts.mp3",
            "sanders_3_people_sleeping_on_the_street.mp3",
            "sanders_4_womans_right_to_control_her_body.mp3",
            "sanders_3_thank you.mp3",
            "trump_4_we want deal!.mp3",
            "trump_t_they_call_her_pocahontas.mp3",
            "trump_3_weve_added_jobs.mp3",
            "trump_3_Okay.mp3",
            "trump_t_i've been watching you for the last couple of weeks.mp3",
            "trump_3_fantastic.mp3",
            "trump_3_dont_know_who_im_gonna_run_against.mp3",
            "trump_t_I'm really rich.mp3",
            "trump_3_small_potatoes.mp3",
            "trump_4_we're going to make america great.mp3",
            "trump_3_no you're finished.mp3",
            "trump_3_not_a_nice_person.mp3",
            "trump_5_get_the_baby_out_of_here.mp3",
            "trump_t_you used to call me.mp3",
            "trump_3_sleepy_joe.mp3",
            "trump_3_cheyah.mp3",
            "trump_4_crazy_bernie.mp3",
            "trump_4_get out of here.mp3",
            "trump_4_if_ivanca_werent_my_daughter.mp3",
            "trump_4_need_to_build_wall.mp3",
            "trump_4_fake_pocahontas_wont_apologize.mp3",
            "trump_4_Thank you darling.mp3",
            "trump_t_you_are_fake_news.mp3",
            "trump_4_you_dont_know_what_your_talking_about.mp3",
            "trump_5_I beat China all the time.mp3",
            "trump_3_african_american_unemployment_rate.mp3",
            "trump_5_I'm really rich ver2.mp3",
            "trump_5_making_my_shoes_so_beautiful.mp3",
            "trump_5_hair_getting_whiter_hes_getting_crazier.mp3",
            "trump_5_Not that it matters but I'm much richer.mp3",
            "trump_t_only_a_good_vice_president_because.mp3",
            "trump_t_thomas_the_tank.mp3",
            "trump_t_you know you're really beautiful.mp3",
            "warren_3_america_that_works_for_the_people.mp3",
            "warren_4_ive_got_a_plan.mp3",
            "warren_5_trump_like_to_talk_pocahontas.mp3",
            "warren_t_not_here_to_build_fences.mp3",
            "warren_3_anticorruption_plan.mp3",
            "warren_4_dont_ask_to_be_an_ambassador.mp3",
            "warren_5_government_stopped_building_new_housing.mp3",
            "warren_t_pitch_in_2_cents.mp3",
            "warren_3_brothers_served_in_military.mp3",
            "warren_4_abortion_rights_human_rights.mp3",
            "warren_5_want_medicare_for_all.mp3",
            "warren_t_president_felt_free_to_break_law.mp3",
            "warren_3_college_tuition_free.mp3",
            "warren_4_first_50_billion_free_and_clear.mp3",
            "warren_5_wealth_tax_not_about_punishing.mp3",
            "warren_t_stop_manmade_crisis_at_border.mp3",
            "warren_3_muller_report.mp3",
            "campanella_3_technically_bot.mp3",
            "daudelin_3_lets_go.mp3",
            "daudelin_3_spikeball_bot.mp3",
            "ghattas_3_heck.mp3",
            "ghattas_3_heck_bot.mp3",
            "ghattas_3_yeet.mp3",
            "ghattas_3_yeet_bot.mp3",
            "ghattas_3_zip_zap_zoop.mp3",
            "kozan_3_oh_gosh.mp3",
            "kozan_3_oh_gosh_bot.mp3",
            "lomuscio_3_deek_a_da_da_bot.mp3",
            "lomuscio_3_lit.mp3",
            "lomuscio_3_lit_bot.mp3",
            "meza_3_baby.mp3",
            "meza_3_baby_bot.mp3",
            "morgan_3_oh_snap.mp3",
            "morgan_3_oh_snap_bot.mp3",
            "young_3_wow.mp3",

            "pumpkin_3_halloween.mp3",
            "quesadilla_3_ramsey.mp3",
            "spikeball_3_trump_spikeball.mp3",
            "turkey_3_gobble_gobble.mp3",
    };
    public static final String SOUND_BYTE_DIR = "data/sounds/block_sounds/";
    public static final String POP_SOUND_PATH = "data/sounds/effects/pop_sound.mp3";
    public static final String BG_MUSIC_1_PATH = "data/sounds/music/star_spangled_banner.mp3";
    public static final String WIN_MUSIC_PATH = "data/sounds/music/marine_hymn.mp3";
    public static final String LOSE_MUSIC_PATH = "data/sounds/music/taps.mp3";
    public static final String STAMP_SOUND_PATH = "data/sounds/effects/stamp.mp3";

    public static final String CALABRIA_PRIMER_PATH = "data/sounds/music/calabria_primer.mp3";
    public static final String CALABRIA_PATH = "data/sounds/music/calabria.mp3";

    public static final String HELLO_DARKNESS_PATH = "data/sounds/music/hello_darkness.mp3";
    public static final String SAD_VIOLIN_PATH = "data/sounds/music/sad_violin.mp3";
    public static final String TRY_YOUR_BEST_PATH = "data/sounds/music/try_your_best.mp3";
    public static final String WATCHA_SAY_PATH = "data/sounds/music/watcha_say.mp3";
    public static final String FIVE_STAR_PATH = "data/sounds/music/five_star.mp3";


    public static final float[] CALABRIA_START_TIMES = new float[] {
            0.0f,
            15.48f,
            30.7f,
            45.9f,
            61.1f,
            84.0f,
            95.5f,
            138.3f,
            213.6f
    };

    private static CCSoundBank instance = new CCSoundBank();

    // sound accessors
    public final List<SoundByte> allSoundBytes;
    public final Sound popSound;
    public final Sound stampSound;
    public final Sound unlockOakBaesSound;
    public final Music bgMusic1;
    public final Music winMusic;
    public final Music loseMusic;
    public final Music winMusicOakBaes;
    public final float[] oakBaesWinMusicStartTimes;

    public final List<Music> lostMusicOakBaes;

    private CCSoundBank() {
        allSoundBytes = new ArrayList<>();
        for (String name : SOUNDBYTE_NAMES) {
            allSoundBytes.add(new SoundByte(SOUND_BYTE_DIR + name));
        }

        popSound = Gdx.audio.newSound(Gdx.files.internal(POP_SOUND_PATH));
        stampSound = Gdx.audio.newSound(Gdx.files.internal(STAMP_SOUND_PATH));
        bgMusic1 = Gdx.audio.newMusic(Gdx.files.internal(BG_MUSIC_1_PATH));
        winMusic = Gdx.audio.newMusic(Gdx.files.internal(WIN_MUSIC_PATH));
        loseMusic = Gdx.audio.newMusic(Gdx.files.internal(LOSE_MUSIC_PATH));

        unlockOakBaesSound = Gdx.audio.newSound(Gdx.files.internal(CALABRIA_PRIMER_PATH));
        winMusicOakBaes = Gdx.audio.newMusic(Gdx.files.internal(CALABRIA_PATH));
        oakBaesWinMusicStartTimes = CALABRIA_START_TIMES;

        lostMusicOakBaes = new ArrayList<>();
        for (String musicPath : Arrays.asList(
                HELLO_DARKNESS_PATH,
                SAD_VIOLIN_PATH,
                TRY_YOUR_BEST_PATH,
                WATCHA_SAY_PATH,
                FIVE_STAR_PATH
        )) {
            lostMusicOakBaes.add(Gdx.audio.newMusic(Gdx.files.internal(musicPath)));
        }
    }

    public List<SoundByte> soundBytesOfCandidate(BlockType candidate) {
        List<SoundByte> result = new ArrayList<>();
        for (SoundByte b : allSoundBytes) {
            if (b.getLastname().equals(candidate.getLname())) {
                result.add(b);
            }
        }
        return result;
    }

    public static CCSoundBank getInstance() {
        if (instance == null) {
            instance = new CCSoundBank();
        }
        return instance;
    }

    @Override
    public void dispose() {
        List<Disposable> toDispose = new ArrayList<>(Arrays.asList(
                popSound, stampSound, bgMusic1, winMusic, loseMusic
        ));
        for (Disposable sound : toDispose) {
            sound.dispose();
        }
    }
}
