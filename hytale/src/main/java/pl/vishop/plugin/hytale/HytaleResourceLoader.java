package pl.vishop.plugin.hytale;

import java.nio.file.Path;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import pl.vishop.plugin.resource.ResourceLoader;
import pl.vishop.plugin.resource.ResourceLoaderException;
import pl.vishop.plugin.resource.ResourceLoaderException.Reason;

public class HytaleResourceLoader extends ResourceLoader<ConfigurationNode> {

    public HytaleResourceLoader(final Class<?> loadingClass, final Path dataDirectory) {
        super(loadingClass, dataDirectory);
    }

    @Override
    protected ConfigurationNode loadResource(final Path resourcePath) throws ResourceLoaderException {
        try {
            return YamlConfigurationLoader.builder().path(resourcePath).build().load();
        } catch (final Exception exception) {
            throw new ResourceLoaderException(Reason.FILE_NOT_LOADED, exception);
        }
    }

}