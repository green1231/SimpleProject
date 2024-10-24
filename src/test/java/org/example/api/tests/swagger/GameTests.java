package org.example.api.tests.swagger;


import org.example.api.addons.AdminUser;
import org.example.api.addons.RandomTestData;
import org.example.api.assertions.Conditions;
import org.example.api.models.swager.DlcsItem;
import org.example.api.models.swager.FullUser;
import org.example.api.models.swager.GamesItem;
import org.example.api.models.swager.UpdField;
import org.example.api.services.GameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GameTests extends BaseApiTest {
    private final GameService gameService = new GameService();

    private GamesItem addRandomGame(String jwt, boolean witDlc) {
        return gameService.addRandomGame(jwt, witDlc).as("register_data", GamesItem.class);
    }

    @AfterEach
    public void deleteTestUser() {
        try {
            String token = userService.auth(randomUser).asJwt();
            userService.deleteUser(token);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testGetGameByIdSuccess(@AdminUser FullUser user) {
        String token = userService.auth(user).asJwt();
        GamesItem game = gameService.getGame(token, 3)
                .should(Conditions.hasStatusCode(200))
                .as(GamesItem.class);
        Assertions.assertNotNull(game);
    }

    @Test
    public void testGetGameByIdNotFound(@AdminUser FullUser user) {
        String token = userService.auth(user).asJwt();
        gameService.getGame(token, 99)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Game with this id not exist"));
    }

    @Test
    public void deleteBaseGame(@AdminUser FullUser user) {
        String token = userService.auth(user).asJwt();
        gameService.deleteGame(token, 3)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Cant delete game from base users"));
    }

    @Test
    public void addGamToUserTest() {
        userService.register(randomUser);

        String token = userService.auth(randomUser).asJwt();


        List<GamesItem> gamesBefore = gameService.getGames(token).asListObject();

        GamesItem game = RandomTestData.getRandomGame();

        gameService.addRandomGame(token, game);

        List<GamesItem> gamesAfter = gameService.getGames(token).asList(GamesItem.class);

        Assertions.assertNotEquals(gamesBefore, gamesAfter);
    }

    @Test
    public void deleteGameSuccess() {
        userService.register(randomUser);

        String token = userService.auth(randomUser).asJwt();

        List<GamesItem> gamesBefore = gameService
                .getGames(token)
                .asList(GamesItem.class);

        GamesItem game = gameService
                .addRandomGame(token, false)
                .as("register_data", GamesItem.class);

        List<GamesItem> gamesAfterNewGame = gameService.getGames(token).asList(GamesItem.class);

        Assertions.assertNotEquals(gamesBefore.size(), gamesAfterNewGame.size(), "Game not added");

        gameService.deleteGame(token, game.getGameId())
                .should(Conditions.hasStatusCode(200))
                .should(Conditions.hasMessage("Game successfully deleted"));

        List<GamesItem> gamesAfterDeleteGame = gameService.getGames(token).asList(GamesItem.class);
        Assertions.assertEquals(gamesBefore.size(), gamesAfterDeleteGame.size(), "Game not deleted");
    }

    @Test
    public void deleteDlcGameNotFound() {
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();
        gameService.deleteGame(token, -1)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Game with this id not exist"));
    }

    @Test
    public void deleteDlcIfListNull() {
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();

        GamesItem game = addRandomGame(token, true);

        gameService.deleteListDlc(token, game.getGameId(), new ArrayList<>())
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("List with DLC to delete cant be empty or null"));
    }

    @Test
    public void deleteDlcSuccess() {
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();

        GamesItem game = addRandomGame(token, true);
        List<DlcsItem> toDeleteDlc = Collections.singletonList(game.getDlcs().get(0));
        gameService.deleteListDlc(token, game.getGameId(), toDeleteDlc)
                .should(Conditions.hasStatusCode(200))
                .should(Conditions.hasMessage("Game DLC successfully deleted"));

        GamesItem deletedDlc = gameService.getGame(token, game.getGameId()).as(GamesItem.class);

        Assertions.assertNotEquals(game.getDlcs().size(), deletedDlc.getDlcs().size());
    }

    @Test
    public void updateDlcInfoWithoutDlc() {
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();

        GamesItem game = addRandomGame(token, true);
        gameService.updateListDlc(token, game.getGameId(), new ArrayList<>())
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Empty body with list of dlc to modify"));
    }

    @Test
    public void updateDlcInfoSuccess() {
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();

        GamesItem game = addRandomGame(token, true);
        DlcsItem firstDlcToUpdate = game.getDlcs().get(0);
        firstDlcToUpdate.setDlcName("new value dlc");
        List<DlcsItem> dlcList = Collections.singletonList(firstDlcToUpdate);

        gameService.updateListDlc(token, game.getGameId(), dlcList)
                .should(Conditions.hasStatusCode(200))
                .should(Conditions.hasMessage("DlC successfully changed"));


        GamesItem gameUpdated = gameService.getGame(token, game.getGameId()).as(GamesItem.class);

        Assertions.assertNotEquals(game.getDlcs().get(0), gameUpdated.getDlcs().get(0));
    }

    @Test
    public void addFreeGameNoDlcWithPriceError() {
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();

        GamesItem game = RandomTestData.getRandomGame();
        game.setDlcs(new ArrayList<>());
        game.setIsFree(true);
        game.setPrice(20);

        gameService.addGame(token, game)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Free DLC or Game cant have price more than 0.0$"));
    }

    @Test
    public void addFreeGameWithDlcPriceError() {
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();

        GamesItem game = RandomTestData.getRandomGame();
        game.setIsFree(true);
        game.setPrice(0);
        game.getDlcs().get(0).setDlcFree(true);
        game.getDlcs().get(0).setPrice(20);

        gameService.addGame(token, game)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Free DLC or Game cant have price more than 0.0$"));
    }

    @Test
    public void addGameSuccess() {
        userService.register(randomUser);
        String token = userService
                .auth(randomUser)
                .asJwt();

        gameService.addRandomGame(token, true)
                .should(Conditions.hasStatusCode(201))
                .should(Conditions.hasMessage("Game created"));
    }

    @Test
    public void updateGameIdFieldError() {
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();

        GamesItem game = addRandomGame(token, true);
        UpdField updField = new UpdField("gameId", 10);
        gameService.updateField(token, game.getGameId(), updField)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Cannot edit ID field"));
    }

    @Test
    public void updateNonExistFieldError() {
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();

        GamesItem game = addRandomGame(token, true);
        UpdField updField = new UpdField("fakeField", 10);

        gameService.updateField(token, game.getGameId(), updField)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Cannot find field"));
    }

    @Test
    public void updateFieldWithIncorrectType() {
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();

        GamesItem game = addRandomGame(token, true);
        UpdField updField = new UpdField("title", 10);

        gameService.updateField(token, game.getGameId(), updField)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Cannot set new value because field has incorrect type"));
    }

    @Test
    public void updateFieldWithSameValue() {
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();

        GamesItem game = addRandomGame(token, true);
        UpdField updField = new UpdField("title", game.getTitle());

        gameService.updateField(token, game.getGameId(), updField)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("New field value is same as before"));
    }

    @Test
    public void updateFieldSuccess() {
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();

        GamesItem game = addRandomGame(token, true);
        UpdField updField = new UpdField("title", "new title");

        gameService.updateField(token, game.getGameId(), updField)
                .should(Conditions.hasStatusCode(200))
                .should(Conditions.hasMessage("New value edited successfully on field title"));
    }

}