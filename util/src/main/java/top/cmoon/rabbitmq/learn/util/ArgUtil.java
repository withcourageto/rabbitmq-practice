package top.cmoon.rabbitmq.learn.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class ArgUtil {

    public static String getSeverity(String[] args) {
        if (args.length == 0) {
            return "info";
        }
        return args[0];
    }

    public static Optional<String> getRouting(String[] args) {
        if (args.length == 0) {
            return Optional.empty();
        }
        return Optional.of(args[0]);
    }


    public static String getMessage(String[] args) {
        return getMessage(args, 0);
    }

    public static String getMessage(String[] args, int startIndex) {
        if (args.length < startIndex + 1) {
            return "Hello World, this is default message";
        }

        return joinStrings(Arrays.copyOfRange(args, startIndex, args.length), " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        if (strings.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder(strings[0]);

        Stream.of(strings)
                .skip(1)
                .forEach(s -> result.append(delimiter).append(s));

        return result.toString();
    }

}
