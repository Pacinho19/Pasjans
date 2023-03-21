package pl.pacinho.pasjans.controller.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.pacinho.pasjans.config.UIConfig;
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

}