package pl.pacinho.pasjans.controller.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.pacinho.pasjans.config.UIConfig;
import pl.pacinho.pasjans.model.dto.CardDto;
import pl.pacinho.pasjans.model.dto.CardMoveDto;
import pl.pacinho.pasjans.model.dto.GameDto;
import pl.pacinho.pasjans.service.GameService;

@RequiredArgsConstructor
@Controller
public class GameController {

    private final GameService gameService;

    @GetMapping(UIConfig.PREFIX)
    public String gameHome(Model model) {
        return "home";
    }

    @PostMapping(UIConfig.GAME)
    public String availableGames(Model model, Authentication authentication) {
        if (authentication != null)
            model.addAttribute("games", gameService.getAvailableGames(authentication.getName()));
        return "fragments/available-games :: availableGamesFrag";
    }

    @PostMapping(UIConfig.NEW_GAME)
    public String newGame(Model model, Authentication authentication) {
        try {
            return "redirect:" + UIConfig.GAME + "/" + gameService.newGame(authentication.getName());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return gameHome(model);
        }
    }

    @GetMapping(UIConfig.GAME_PAGE)
    public String gamePage(@PathVariable(value = "gameId") String gameId,
                           Model model,
                           Authentication authentication) {
        try {
            GameDto game = gameService.findDtoById(gameId);

            if (!game.getPlayer().equals(authentication.getName()))
                throw new IllegalStateException("Cannot play game " + gameId);

            model.addAttribute("game", game);
            return "game";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return gameHome(model);
        }
    }

    @GetMapping(UIConfig.GAME_BOARD_RELOAD)
    public String reloadBoard(Model model,
                              @PathVariable(value = "gameId") String gameId) {
        model.addAttribute("game", gameService.findDtoById(gameId));
        return "fragments/board :: boardFrag";
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping(UIConfig.GAME_STACK_NEXT_CARD)
    public void nextStackCard(@PathVariable(value = "gameId") String gameId) {
        gameService.nextStackCard(gameId);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping(UIConfig.GAME_ADD_CARD_TO_GROUP)
    public void addCardToGroup(@PathVariable(value = "gameId") String gameId, @RequestBody CardDto cardDto) {
        gameService.addCardToGroup(gameId,cardDto);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping(UIConfig.GAME_MOVE_CARDS)
    public void addCardToGroup(@PathVariable(value = "gameId") String gameId, @RequestBody CardMoveDto cardMoveDto) {
        gameService.moveCards(gameId,cardMoveDto);
    }


}