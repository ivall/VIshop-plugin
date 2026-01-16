package pl.vishop.plugin.hytale;

import com.hypixel.hytale.logger.HytaleLogger;
import pl.vishop.plugin.logger.ViShopLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HytaleVishopLogger implements ViShopLogger {

    private final HytaleLogger logger;

    public HytaleVishopLogger(final HytaleLogger logger) {
        this.logger = logger;
    }

    @Override
    public void info(final String message) {
        this.logger.at(Level.INFO).log(message);
    }

    @Override
    public void error(final String message) {
        this.logger.at(Level.SEVERE).log(message);
    }

    @Override
    public void debug(final String message) {
        this.info(String.format("[DEBUG] %s", message));
    }

}