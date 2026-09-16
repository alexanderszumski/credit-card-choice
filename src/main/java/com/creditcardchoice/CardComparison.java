package com.creditcardchoice;

import java.util.Locale;

public record CardComparison(String name, double monthlyPoints, boolean custom) {
    public CardComparison {
        monthlyPoints = Math.floor(monthlyPoints);
    }

    public CardComparison(String name, double monthlyPoints) {
        this(name, monthlyPoints, false);
    }

    private String cardKey() {
        return name.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
    }

    public String imagePath() {
        if(custom) return null;
        return switch(cardKey()) {
            case "americanexpressgold", "americanexpressgoldcard", "amexgold" -> "/images/cards/amex-gold.png";
            case "americanexpressplatinum", "americanexpressplatinumcard", "amexplatinum" -> "/images/cards/amex-platinum.png";
            case "americanexpressgreen", "americanexpressgreencard", "amexgreen" -> "/images/cards/amex-green.png";
            case "chasefreedomunlimited", "freedomunlimited" -> "/images/cards/chase-freedom-unlimited.png";
            case "chasesapphirepreferred", "sapphirepreferred" -> "/images/cards/chase-sapphire-preferred.png";
            case "capitaloneventurex", "capitaloneventurexrewards", "venturex" -> "/images/cards/capital-one-venture-x.png";
            default -> null;
        };
    }

    public String learnMoreUrl() {
        if(custom) return null;
        return switch(cardKey()) {
            case "americanexpressgold", "americanexpressgoldcard", "amexgold" -> "https://www.americanexpress.com/us/credit-cards/card/gold-card/";
            case "americanexpressplatinum", "americanexpressplatinumcard", "amexplatinum" -> "https://www.americanexpress.com/us/credit-cards/card/platinum/";
            case "americanexpressgreen", "americanexpressgreencard", "amexgreen" -> "https://www.americanexpress.com/en-us/account/get-started/green/set-up-and-payments";
            case "chasefreedomunlimited", "freedomunlimited" -> "https://creditcards.chase.com/cash-back-credit-cards/freedom/unlimited";
            case "chasesapphirepreferred", "sapphirepreferred" -> "https://creditcards.chase.com/rewards-credit-cards/sapphire/preferred";
            case "capitaloneventurex", "capitaloneventurexrewards", "venturex" -> "https://www.capitalone.com/credit-cards/venture-x/";
            default -> null;
        };
    }
}
