package org.bedracket.command;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("SameParameterValue")
public class ModInfoCommand {

    public static final Map<String, ModMetadata> MODS = new HashMap<>();
    public static final Map<String, String> OUTSTANDING_CONTACT_TYPES = ImmutableMap.of("homepage", "Homepage", "sources", "Sources", "issues", "Issues");


    public static final LiteralArgumentBuilder<ServerCommandSource> COMMAND = CommandManager.literal("mod")
            .then(CommandManager.literal("list")
                    .executes(context -> listMods(context.getSource()))
            )
            .then(CommandManager.literal("info")
                    .then(CommandManager.argument("mod", StringArgumentType.string())
                            .suggests(ModInfoCommand::modSuggestions)
                            .executes(context -> printModInfo(context.getSource(), getModId(context, "mod")))
                    )
            );


    private static CompletableFuture<Suggestions> modSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        for (String modId : MODS.keySet())
            builder.suggest(modId);
        return builder.buildFuture();
    }


    private static final DynamicCommandExceptionType INVALID_MOD = new DynamicCommandExceptionType(id -> Text.literal("Unknown mod '" + id + "'"));
    private static String getModId(CommandContext<ServerCommandSource> context, String argument) throws CommandSyntaxException {
        String modId = StringArgumentType.getString(context, argument);
        if (!MODS.containsKey(modId))
            throw INVALID_MOD.create(modId);
        return modId;
    }


    private static int printModInfo(ServerCommandSource source, String modId) {
        ModMetadata mod = MODS.get(modId);
        InfoPrinter printer = new InfoPrinter(source);
        printer.line(Text.literal(mod.getName()).styled(style -> style.withBold(true).withFormatting(Formatting.UNDERLINE)).append(InfoPrinter.notStyled(" (" + modId + "):")));
        printer.value("Version", mod.getVersion().getFriendlyString());
        printer.value("Description", mod.getDescription());
        if (mod.getLicense().size() == 1)
            printer.value("License", Iterables.get(mod.getLicense(), 0));
        else if (mod.getLicense().size() > 1)
            printer.list("Licenses", mod.getLicense());
        for (Map.Entry<String, String> outstandingContactEntry : OUTSTANDING_CONTACT_TYPES.entrySet())
            printer.link(outstandingContactEntry.getValue(), mod.getContact().get(outstandingContactEntry.getKey()));
        printer.listTexts("Contact", mod.getContact().asMap().entrySet(), contactEntry -> {
            if (!OUTSTANDING_CONTACT_TYPES.containsKey(contactEntry.getKey())) {
                try {
                    new URL(contactEntry.getValue()); // Will throw 'MalformedURLException' if the string is not a proper URL.
                    // FIXME this URL detection doesn't work with URLs that have custom schemes like 'irc'
                    return Text.literal(contactEntry.getKey() + ": ").append(Text.literal(contactEntry.getValue()).setStyle(InfoPrinter.linkStyle(contactEntry.getValue())));
                } catch (MalformedURLException exception) {
                    return Text.literal(contactEntry.getKey() + ": ").append(Text.literal(contactEntry.getValue()).setStyle(InfoPrinter.clipboardStyle(contactEntry.getValue()).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to copy to clipboard")))));
                }
            }
            return null;
        });
        printer.list("Authors", mod.getAuthors(), Person::getName);
        printer.list("Contributors", mod.getContributors(), Person::getName);
        return 1;
    }

    private static int listMods(ServerCommandSource source) {
        if (MODS.isEmpty()) {
            source.sendFeedback(Text.literal("There are no mods loaded"), false);
            return 0;
        }
        source.sendFeedback(Text.literal("There are " + MODS.size() + " mods: ").append(Texts.join(MODS.keySet(), modId -> Text.literal(modId).setStyle(InfoPrinter.commandStyle("mod info " + modId).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click for more information")))))), false);
        return MODS.size();
    }
}