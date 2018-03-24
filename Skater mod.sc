
// UM Geek
SCRIPT_START
{
    NOP 

    WAIT 1000

    LVAR_INT Female

    IF (Female = 7)
        PRINT_HELP_STRING "SKATER MOD START"
    ENDIF

    IF NOT READ_INT_FROM_INI_FILE "CLEO\Skater Mod.ini" "Configs" "Female" Female
        Female = 0
        WRITE_INT_TO_INI_FILE Female "CLEO\Skater Mod.ini" "Configs" "Female"
    ENDIF

    WHILE IS_PC_VERSION

        //-----------------------------------------------------------------------------------------------
        IF TEST_CHEAT SKRL
            STREAM_CUSTOM_SCRIPT "SKATER MOD.CS" 7
            BREAK
        ENDIF
        //-----------------------------------------------------------------------------------------------
        IF TEST_CHEAT SKTR
            CLEO_CALL Player_Skater 0 ( Female )
        ENDIF
        //-----------------------------------------------------------------------------------------------

        WAIT 0    

    ENDWHILE
    TERMINATE_THIS_CUSTOM_SCRIPT
}

// ------------------------------------------------------------------------------------------------------
{
    LVAR_INT Female //-- in

    LVAR_INT scplayer tempVar_A skinModel
    LVAR_FLOAT angle

    Player_Skater:
        WHILE TEST_CHEAT SKTR
            WAIT 0
        ENDWHILE

        IF (Female = 1 )
            skinModel = 92
        ELSE
            skinModel = 99
        ENDIF

        GET_PLAYER_CHAR 0 scplayer 
        IF NOT HAS_MODEL_LOADED skinModel
            REQUEST_MODEL skinModel 
            LOAD_ALL_MODELS_NOW
        ENDIF
        CLEAR_CHAR_TASKS_IMMEDIATELY scplayer
        SET_PLAYER_MODEL 0 skinModel
        MARK_MODEL_AS_NO_LONGER_NEEDED skinModel  

        WHILE NOT TEST_CHEAT SKTR
            WAIT 0
            IF  CLEO_CALL No_Player_in_Vehice 0 ()
                IF IS_KEY_PRESSED VK_SPACE
                    IF NOT HAS_ANIMATION_LOADED SKATE
                        REQUEST_ANIMATION SKATE
                        LOAD_ALL_MODELS_NOW
                    ENDIF            
                    SET_PLAYER_CONTROL 0 FALSE
                    SET_CAMERA_BEHIND_PLAYER
                    WHILE IS_KEY_PRESSED VK_SPACE
                        WAIT 0
                        GET_CHAR_HEADING scplayer angle
                        IF IS_KEY_PRESSED VK_LEFT 
                            angle += 5.0
                            SET_CHAR_HEADING scplayer angle
                        ENDIF
                        IF IS_KEY_PRESSED VK_RIGHT
                            angle -= 5.0
                            SET_CHAR_HEADING scplayer angle
                        ENDIF
                        IF NOT IS_KEY_PRESSED VK_DOWN
                            IF NOT CLEO_CALL Check_Ramp_Level 0 (scplayer)
                                IF NOT IS_KEY_PRESSED VK_UP                      
                                    TASK_PLAY_ANIM scplayer "skate_run" "SKATE" 4.0 FALSE TRUE TRUE FALSE -1  
                                ELSE
                                    TASK_PLAY_ANIM scplayer "skate_sprint" "SKATE" 5.0 FALSE TRUE TRUE FALSE -1      
                                ENDIF
                            ELSE
                                TASK_PLAY_ANIM scplayer "skate_sprint" "SKATE" 5.0 FALSE TRUE TRUE FALSE -1
                            ENDIF
                        ELSE
                            TASK_PLAY_ANIM scplayer "skate_idle" "SKATE" 4.0 FALSE TRUE TRUE FALSE -1  
                        ENDIF
                    ENDWHILE
                    TASK_PLAY_ANIM scplayer "Run_stop" "PED" 4.0 FALSE TRUE TRUE FALSE -1
                    RESTORE_CAMERA
                    SET_PLAYER_CONTROL 0 TRUE
                    REMOVE_ANIMATION SKATE
                ENDIF
            ENDIF
        ENDWHILE

        CLEAR_CHAR_TASKS_IMMEDIATELY scplayer
        SET_PLAYER_MODEL 0 0

    CLEO_RETURN 0
}

// ------------------------------------------------------------------------------------------------------
{
    LVAR_INT tempVar_A

    No_Player_in_Vehice:
        IF NOT IS_PLAYER_USING_JETPACK 0
            GET_PLAYER_CHAR 0 tempVar_A
            GET_CAR_CHAR_IS_USING tempVar_A tempVar_A
            IF NOT DOES_VEHICLE_EXIST tempVar_A
                IS_PC_VERSION
                CLEO_RETURN 0
            ENDIF
        ENDIF
        IS_AUSTRALIAN_GAME
    CLEO_RETURN 0
}

// ------------------------------------------------------------------------------------------------------
{
    LVAR_INT charId //-- in 
    LVAR_FLOAT posX_A posY_A posZ_A
    LVAR_FLOAT posX_B posY_B posZ_B

    Check_Ramp_Level:
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS charId 0.0 -1.0 0.0 posX_A posY_A posZ_A
        GET_GROUND_Z_FOR_3D_COORD posX_A posY_A posZ_A posZ_A 
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS charId 0.0 1.0 0.0 posX_B posY_B posZ_B
        GET_GROUND_Z_FOR_3D_COORD posX_B posY_B posZ_B posZ_B
        IF (posZ_B > posZ_A)
            IS_PC_VERSION
            CLEO_RETURN 0
        ENDIF
        IS_AUSTRALIAN_GAME
    CLEO_RETURN 0
}

// ------------------------------------------------------------------------------------------------------
/*
{
    LVAR_INT char // in
    LVAR_INT char_model // in
    LVAR_INT cped cpedsound

    ChangeCharModel:
        GET_PED_POINTER char cped
        CALL_METHOD 0x5E4880 cped 1 0 (char_model) // CPed::setModelIndex
        cpedsound = cped + 0x294
        CALL_METHOD 0x4E68D0 cpedsound 1 0 (cped) // CPedSound::setPedSound
    CLEO_RETURN 0
}
*/
SCRIPT_END