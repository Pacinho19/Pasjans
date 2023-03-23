package pl.pacinho.pasjans.config;

public class UIConfig {
    public static final String PREFIX = "/pasjans";
    public static final String GAME = PREFIX + "/game";
    public static final String NEW_GAME = GAME + "/new";
    public static final String GAME_PAGE = GAME + "/{gameId}";
    public static final String GAME_BOARD = GAME_PAGE + "/board";
    public static final String GAME_BOARD_RELOAD = GAME_BOARD + "/reload";
    public static final String GAME_STACK = GAME_PAGE + "/stack";
    public static final String GAME_STACK_NEXT_CARD = GAME_STACK + "/next";

    public static final String GAME_ADD_CARD_TO_GROUP = GAME_PAGE + "/card/group/add";
}