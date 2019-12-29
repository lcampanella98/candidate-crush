package com.gmail.enzocampanella98.candidatecrush.sound;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CCSoundBank implements Disposable {
    // singleton class to keep track of all sounds

    private static final String[] SOUNDBYTE_NAMES = new String[]{
            "biden_3_brought_people_together.ogg",
            "biden_3_change_culture_of_how_women_are_treated.ogg",
            "biden_3_defeat_donald_trump.ogg",
            "biden_3_directing_justice_dept.ogg",
            "biden_3_trump_doesnt_want_me_to_be_the_nominee.ogg",
            "biden_3_we_have_to_restore_the_soul_of_this_country.ogg",
            "biden_4_barak_america.ogg",
            "biden_4_for_a_socialist_corporate_america.ogg",
            "biden_4_free_community_college.ogg",
            "biden_4_smart_guns.ogg",
            "biden_4_tired_of_woe_is_me.ogg",
            "biden_5_decriminalize_marijuana.ogg",
            "biden_5_getting_troops_out_of_iraq.ogg",
            "biden_5_job_more_than_paycheck.ogg",
            "biden_5_your_moms_still_alive.ogg",
            "biden_t_kim_jung_un_rabid_dog.ogg",
            "biden_t_poor_kids_white_kids.ogg",
            "biden_t_president_has_big_stick.ogg",
            "biden_t_tweeting.ogg",
            "buttigieg_3_born_to_make_myself_useful.ogg",
            "buttigieg_3_down_south_they_call_me_mayor_pete.ogg",
            "buttigieg_3_i_dont_even_golf.ogg",
            "buttigieg_3_president_doesnt_care_about_farmers.ogg",
            "buttigieg_3_unify_a_nation.ogg",
            "buttigieg_4_need_something_very_different.ogg",
            "buttigieg_4_only_chance_youll_ever_get.ogg",
            "buttigieg_4_solutions_lie_with_american_farmers.ogg",
            "buttigieg_4_what_your_ideas_are.ogg",
            "buttigieg_5_can_read_through_article_languages.ogg",
            "buttigieg_5_dont_have_21st_century_security_strategy.ogg",
            "buttigieg_5_moat_full_of_alligators.ogg",
            "buttigieg_t_capitol_hill_looks_small.ogg",
            "buttigieg_t_is_anyone_proposing_invading_mexico.ogg",
            "buttigieg_t_labels_becoming_less_useful.ogg",
            "buttigieg_t_least_wealthy_person_on_stage.ogg",
            "clinton_3_And I thank all of you.ogg",
            "clinton_3_Im exhausted.ogg",
            "clinton_4_I remember when my husband.ogg",
            "clinton_4_Marching orders.ogg",
            "clinton_5_This is Hillary Clinton.ogg",
            "clinton_t_As we gather here today.ogg",
            "clinton_t_Every American gets a cup cake.ogg",
            "cruz_3_halahala.ogg",
            "cruz_3_hehe.ogg",
            "cruz_3_thank you.ogg",
            "cruz_4_goose diarrhea.ogg",
            "cruz_4_i need a bogle for the glotch.ogg",
            "cruz_4_you're the devil.ogg",
            "cruz_5_barfed on the beach.ogg",
            "cruz_5_hooray for you.ogg",
            "cruz_t_garden knomes.ogg",
            "cruz_t_i just love to twerk.ogg",
            "meza_3_crush.ogg",
            "meza_4_crush.ogg",
            "meza_5_crush.ogg",
            "meza_t_crush.ogg",
            "sanders_3_black lives matter.ogg",
            "sanders_3_corrupt_political_system.ogg",
            "sanders_3_function_of_health_care_system.ogg",
            "sanders_3_last_poll_i_saw.ogg",
            "sanders_3_marijuana.ogg",
            "sanders_3_more_in_taxes_less_in_health_care.ogg",
            "sanders_3_most_people_min_wage_living_wage.ogg",
            "sanders_3_people_sleeping_on_the_street.ogg",
            "sanders_3_thank you.ogg",
            "sanders_4_free_college.ogg",
            "sanders_4_take back out government.ogg",
            "sanders_4_three_people_more_wealth.ogg",
            "sanders_4_trump_pathological_liar.ogg",
            "sanders_4_twelve_years_before_irreperable_damage.ogg",
            "sanders_4_we can do it.ogg",
            "sanders_4_wealth and income inequality.ogg",
            "sanders_4_womans_right_to_control_her_body.ogg",
            "sanders_5_congress_walk_chew_bubble_gum.ogg",
            "sanders_5_don't underestimate me.ogg",
            "sanders_5_existential_threat_climate_change.ogg",
            "sanders_5_Huge.ogg",
            "sanders_5_i_wrote_the_damn_bill.ogg",
            "sanders_5_we_encourage_diversity.ogg",
            "sanders_t_enough of the emails.ogg",
            "sanders_t_fully_paid_for.ogg",
            "sanders_t_guts_to_stand_up_to_special_interests.ogg",
            "sanders_t_middle class of this country is collapsing.ogg",
            "sanders_t_we_dont_have_decades.ogg",
            "sanders_t_What this campaign is about.ogg",
            "sanders_t_whether_the_democratic_party_has_the_guts.ogg",
            "trump_3_african_american_unemployment_rate.ogg",
            "trump_3_cheyah.ogg",
            "trump_3_dont_know_who_im_gonna_run_against.ogg",
            "trump_3_fantastic.ogg",
            "trump_3_no you're finished.ogg",
            "trump_3_not_a_nice_person.ogg",
            "trump_3_Okay.ogg",
            "trump_3_sleepy_joe.ogg",
            "trump_3_small_potatoes.ogg",
            "trump_3_weve_added_jobs.ogg",
            "trump_4_crazy_bernie.ogg",
            "trump_4_fake_pocahontas_wont_apologize.ogg",
            "trump_4_get out of here.ogg",
            "trump_4_if_ivanca_werent_my_daughter.ogg",
            "trump_4_need_to_build_wall.ogg",
            "trump_4_Thank you darling.ogg",
            "trump_4_we want deal!.ogg",
            "trump_4_we're going to make america great.ogg",
            "trump_4_you_dont_know_what_your_talking_about.ogg",
            "trump_5_get_the_baby_out_of_here.ogg",
            "trump_5_hair_getting_whiter_hes_getting_crazier.ogg",
            "trump_5_I beat China all the time.ogg",
            "trump_5_I'm really rich ver2.ogg",
            "trump_5_making_my_shoes_so_beautiful.ogg",
            "trump_5_Not that it matters but I'm much richer.ogg",
            "trump_t_I'm really rich.ogg",
            "trump_t_i've been watching you for the last couple of weeks.ogg",
            "trump_t_only_a_good_vice_president_because.ogg",
            "trump_t_they_call_her_pocahontas.ogg",
            "trump_t_thomas_the_tank.ogg",
            "trump_t_you know you're really beautiful.ogg",
            "trump_t_you used to call me.ogg",
            "trump_t_you_are_fake_news.ogg",
            "warren_3_america_that_works_for_the_people.ogg",
            "warren_3_anticorruption_plan.ogg",
            "warren_3_brothers_served_in_military.ogg",
            "warren_3_college_tuition_free.ogg",
            "warren_3_muller_report.ogg",
            "warren_4_abortion_rights_human_rights.ogg",
            "warren_4_dont_ask_to_be_an_ambassador.ogg",
            "warren_4_first_50_billion_free_and_clear.ogg",
            "warren_4_ive_got_a_plan.ogg",
            "warren_5_government_stopped_building_new_housing.ogg",
            "warren_5_trump_like_to_talk_pocahontas.ogg",
            "warren_5_want_medicare_for_all.ogg",
            "warren_5_wealth_tax_not_about_punishing.ogg",
            "warren_t_not_here_to_build_fences.ogg",
            "warren_t_pitch_in_2_cents.ogg",
            "warren_t_president_felt_free_to_break_law.ogg",
            "warren_t_stop_manmade_crisis_at_border.ogg"
    };
    public static final String SOUND_BYTE_DIR = "data/sounds/block_sounds/";
    public static final String POP_SOUND_PATH = "data/sounds/effects/pop_sound.mp3";
    public static final String BG_MUSIC_1_PATH = "data/sounds/music/star_spangled_banner.mp3";
    public static final String WIN_MUSIC_PATH = "data/sounds/music/marine_hymn.mp3";
    public static final String LOSE_MUSIC_PATH = "data/sounds/music/taps.mp3";


    private static CCSoundBank instance;

    // sound accessors
    public final List<SoundByte> allSoundBytes;
    public final Sound popSound;
    public final Music bgMusic1;
    public final Music winMusic;
    public final Music loseMusic;

    private CCSoundBank() {
        allSoundBytes = new ArrayList<>();
        for (String name : SOUNDBYTE_NAMES) {
            allSoundBytes.add(new SoundByte(SOUND_BYTE_DIR + name));
        }
        popSound = Gdx.audio.newSound(Gdx.files.internal(POP_SOUND_PATH));
        bgMusic1 = Gdx.audio.newMusic(Gdx.files.internal(BG_MUSIC_1_PATH));
        winMusic = Gdx.audio.newMusic(Gdx.files.internal(WIN_MUSIC_PATH));
        loseMusic = Gdx.audio.newMusic(Gdx.files.internal(LOSE_MUSIC_PATH));
    }

    public static CCSoundBank getInstance() {
        if (instance == null) {
            instance = new CCSoundBank();
        }
        return instance;
    }

    @Override
    public void dispose() {
        List<Disposable> toDispose = new ArrayList<>();
        toDispose.addAll(allSoundBytes);
        toDispose.addAll(Arrays.asList(popSound, bgMusic1));
        for (Disposable sound : toDispose) {
            sound.dispose();
        }
    }
}
