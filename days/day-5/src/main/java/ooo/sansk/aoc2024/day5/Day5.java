package ooo.sansk.aoc2024.day5;

import java.util.ArrayList;
import java.util.List;

import static java.util.function.Predicate.not;

class Day5 {
    int runPartOne(String input) {
        final var data = readData(input);

        return data.updates().stream().filter(update -> update.orderIsCorrect(data.rules())).mapToInt(Update::middlePage).sum();
    }

    int runPartTwo(String input) {
        final var data = readData(input);

        return data.updates().stream().filter(update -> !update.orderIsCorrect(data.rules)).map(update -> update.reorder(data.rules())).mapToInt(Update::middlePage).sum();
    }

    PrintQueue readData(String input) {
        final var parts = input.split("\r?\n\r?\n");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Got more than 2 parts of input data. There should only be an order rule section and an update list. The sections must be separated with two newlines.");
        }

        final var orderRules = readOrderRules(parts[0]);
        final var update = readUpdates(parts[1]);

        return new PrintQueue(orderRules, update);
    }

    List<OrderRule> readOrderRules(String input) {
        final var orderRules = new ArrayList<OrderRule>();
        for (String s : input.split("\r?\n")) {
            final var parts = s.split("\\|");
            orderRules.add(new OrderRule(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }
        return orderRules;
    }

    List<Update> readUpdates(String input) {
        final var updates = new ArrayList<Update>();
        for (String s : input.split("\r?\n")) {
            final var parts = s.split(",");
            final var pages = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                pages[i] = Integer.parseInt(parts[i]);
            }
            updates.add(new Update(pages));
        }
        return updates;
    }

    record PrintQueue(List<OrderRule> rules, List<Update> updates) {
    }

    record OrderRule(int before, int after) {
    }

    record Update(int[] pages) {
        boolean orderIsCorrect(List<OrderRule> rules) {
            final var validatedRules = rules.stream().map(ValidatedRule::new).toList();

            for (int i = 0; i < pages.length; i++) {
                for (final var rule : validatedRules) {
                    rule.register(pages[i], i);
                }
            }

            return validatedRules.stream().allMatch(ValidatedRule::isValid);
        }

        int middlePage() {
            return pages[pages.length / 2];
        }

        Update reorder(List<OrderRule> rules) {
            if (orderIsCorrect(rules)) {
                return this;
            }

            final var orderedPages = pages().clone();
            final var validatedRules = rules.stream().map(ValidatedRule::new).toList();

            for (int i = 0; i < orderedPages.length; i++) {
                for (final var rule : validatedRules) {
                    rule.register(orderedPages[i], i);
                }
            }

            final var errors = validatedRules.stream().filter(not(ValidatedRule::isValid)).toList();
            final var rule = errors.getFirst();
            final var temp = orderedPages[rule.startIndex()];
            orderedPages[rule.startIndex()] = orderedPages[rule.endIndex()];
            orderedPages[rule.endIndex()] = temp;

            return new Update(orderedPages).reorder(rules);
        }
    }

    static class ValidatedRule {
        private final OrderRule rule;
        private int startIndex = -1;
        private int endIndex = -1;

        public ValidatedRule(OrderRule rule) {
            this.rule = rule;
        }

        public void register(int pageNumber, int index) {
            if (pageNumber == rule.before()) {
                startIndex = index;
            }

            if (pageNumber == rule.after()) {
                endIndex = index;
            }
        }

        public boolean isValid() {
            if (startIndex == -1 || endIndex == -1) {
                return true;
            }

            if (startIndex >= endIndex) {
                return false;
            }

            return true;
        }

        int startIndex() {
            return startIndex;
        }

        int endIndex() {
            return endIndex;
        }
    }
}
