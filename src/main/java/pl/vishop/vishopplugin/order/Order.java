package pl.vishop.vishopplugin.order;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

public class Order {

    private final UUID orderId;
    private final String player;
    private final boolean requireOnline;
    private final List<String> commands;

    public Order(final UUID orderId, final String player, final boolean requireOnline, final List<String> commands) {
        this.orderId = orderId;
        this.player = player;
        this.requireOnline = requireOnline;
        this.commands = commands;
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public String getPlayer() {
        return this.player;
    }

    public boolean requiresOnline() {
        return this.requireOnline;
    }

    public List<String> getCommands() {
        return this.commands.stream().map(command -> StringUtils.replace(command, "{NICK}", this.player)).collect(Collectors.toList());
    }

}
