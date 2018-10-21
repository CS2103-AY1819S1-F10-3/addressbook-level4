package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.events.ui.ShowHelpRequestEvent;
import seedu.address.logic.ListElementPointer;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.LackOfPrivilegeException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.UserPrefs;

/**
 * The Login Window. Users have to login before they are redirected to the Main Window.
 */
public class LoginWindow extends UiPart<Stage> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "LoginWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Config config;
    private Logic logic;
    private UserPrefs prefs;
    private ListElementPointer historySnapshot;

    private MainWindow mainWindow;
    private HelpWindow helpWindow;


    @FXML
    private Label statusPlaceholder;

    @FXML
    private TextField loginCli;

    @FXML
    private Text forgetPw;

    /**
     * Instantiates the Login Window
     * @param logic Logic parsed from UiManager
     */
    public LoginWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.logic = logic;
        this.primaryStage = primaryStage;
        this.prefs = prefs;
        this.config = config;
        historySnapshot = logic.getHistorySnapshot();

        setWindowDefaultSize(prefs);
        helpWindow = new HelpWindow();
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleHelp();
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleLogin() {
        try {
            CommandResult commandResult = logic.execute(loginCli.getText());
            initHistory();
            historySnapshot.next();
            // process result of the command
            loginCli.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));
            statusPlaceholder.setText(commandResult.feedbackToUser);

            if (commandResult.feedbackToUser.equals("Successfully logged in.")) {
                mainWindow = new MainWindow(primaryStage, config, prefs, logic);
                mainWindow.show();
                mainWindow.fillInnerParts();
            }

        } catch (CommandException | ParseException | LackOfPrivilegeException e) {
            initHistory();
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + loginCli.getText());
            statusPlaceholder.setText("Invalid command: " + loginCli.getText());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    /**
     * Initializes the history snapshot.
     */
    private void initHistory() {
        historySnapshot = logic.getHistorySnapshot();
        // add an empty string to represent the most-recent end of historySnapshot, to be shown to
        // the user if she tries to navigate past the most-recent end of the historySnapshot.
        historySnapshot.add("");
    }

    /**
     * Shows password reset request page.
     * Password reset request sent to admin.
     */
    public void handleForgetPw() {
        // Send to password reset page
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        loginCli.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = loginCli.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    void hide() {
        primaryStage.hide();
    }

    void releaseResources() {
        mainWindow.releaseResources();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}