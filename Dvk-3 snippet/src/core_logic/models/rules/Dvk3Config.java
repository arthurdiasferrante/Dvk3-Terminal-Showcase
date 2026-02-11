package core_logic.models.rules;

public class Dvk3Config {
    // variaveis do bunker
    public static int BUNKER_TEMPERATURE_LOST_BY_LEAKING_GAS = 1;

    // game loop e ritmo
    public static final long GAME_TICK_SPEED = 6000;
    public static final long ANIMATION_FRAME_DELAY = 100;

    // velocidade dos logs
    public static final int LOG_INSTANT = 5;
    public static final int LOG_FAST = 10;
    public static final int LOG_NORMAL = 20;
    public static final int LOG_SLOW = 30;
    public static final int LOG_SUPER_SLOW = 40;
    
    // tempos de tarefas
    public static final int TIME_INSTANT = 10;
    public static final int TIME_SUPERFAST = 30;
    public static final int TIME_FAST = 50;
    public static final int TIME_STANDARD = 100;
    public static final int TIME_MODERATE = 200;
    public static final int TIME_LONG_PROCESS = 300;
    public static final int TIME_INFINITE = -1;

}
