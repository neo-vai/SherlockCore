package me.pashaVoid.sherlockCore.config;

import org.bukkit.configuration.file.FileConfiguration;

public class MessagesConfig {

    public static String no_history = "§aThere is no revision history for this block.";
    public static String history_written_to_paper = "§6The results are written down on paper:";
    public static String block_history_preview = "§6Here's what we managed to see:";

    public static String not_enough_arguments = "§cNot enough arguments";
    public static String choose = "§cChoose custom or pattern";
    public static String pattern_syntax_error = "§cError in the command with the Pattern";
    public static String no_to_give = "§cThere is no one to give the item to :(";
    public static String pattern_not_found = "§cThe pattern was not found! :(";
    public static String arguments_invalid = "§cIncorrect argument!";
    public static String arguments_error = "§cError in arguments!";
    public static String successfully = "§aSuccessfully!";

    public static void loadMessagesConfig(FileConfiguration config) {
        no_history = config.getString("no_history");
        history_written_to_paper = config.getString("history_written_to_paper");
        block_history_preview = config.getString("block_history_preview");

        not_enough_arguments = config.getString("not_enough_arguments");
        choose = config.getString("choose");
        pattern_syntax_error = config.getString("pattern_syntax_error");
        no_to_give = config.getString("no_to_give");
        pattern_not_found = config.getString("pattern_not_found");
        arguments_invalid = config.getString("arguments_invalid");
        arguments_error = config.getString("arguments_error");
        successfully = config.getString("successfully");
    }

}
