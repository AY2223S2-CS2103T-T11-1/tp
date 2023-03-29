package seedu.wife;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.wife.commons.core.Config;
import seedu.wife.commons.core.LogsCenter;
import seedu.wife.commons.core.Version;
import seedu.wife.commons.exceptions.DataConversionException;
import seedu.wife.commons.util.ConfigUtil;
import seedu.wife.commons.util.StringUtil;
import seedu.wife.logic.Logic;
import seedu.wife.logic.LogicManager;
import seedu.wife.model.Model;
import seedu.wife.model.ModelManager;
import seedu.wife.model.ReadOnlyUserPrefs;
import seedu.wife.model.ReadOnlyWife;
import seedu.wife.model.UserPrefs;
import seedu.wife.model.Wife;
import seedu.wife.model.util.SampleDataUtil;
import seedu.wife.storage.JsonUserPrefsStorage;
import seedu.wife.storage.JsonWifeStorage;
import seedu.wife.storage.Storage;
import seedu.wife.storage.StorageManager;
import seedu.wife.storage.UserPrefsStorage;
import seedu.wife.storage.WifeStorage;
import seedu.wife.ui.Ui;
import seedu.wife.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 2, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    private String initialMessage = "Welcome to WIFE!";

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing WIFE ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        WifeStorage wifeStorage = new JsonWifeStorage(userPrefs.getWifeFilePath());
        storage = new StorageManager(wifeStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s WIFE and {@code userPrefs}. <br>
     * The data from the sample WIFE will be used instead if {@code storage}'s WIFE is not found,
     * or an empty WIFE will be used instead if errors occur when reading {@code storage}'s WIFE.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        Optional<ReadOnlyWife> wifeOptional;
        ReadOnlyWife initialData;
        try {
            wifeOptional = storage.readWife();
            if (!wifeOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample inventory");
            }
            initialData = wifeOptional.orElseGet(SampleDataUtil::getSampleWife);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty inventory");
            initialData = new Wife();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty inventory");
            initialData = new Wife();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty inventory");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting WIFE " + MainApp.VERSION);
        ui.start(primaryStage, initialMessage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping WIFE ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
