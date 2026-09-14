package com.creditcardchoice;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidNumber(Model model) {
        model.addAttribute("error", "Only enter non-negative numerical values.");
        return "home";
    }
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleServiceUnavailable(SQLException exception, Model model){
        logger.error("Failed to load credit card comparisons", exception);
        model.addAttribute("error", "This service is currently unavailable. Please try again at a later time.");
        return "home";
    }
    @GetMapping("/")
    public String home() {
        return "home";
    }
    @PostMapping("/compare")
    public String totalSpending(@RequestParam("dining") double dining, @RequestParam("grocery") double grocery, @RequestParam("travel") double travel, @RequestParam("gas") double gas, @RequestParam("other") double other, Model model,
    @RequestParam(name = "customName", defaultValue = "") String customName, @RequestParam(name = "customDining", defaultValue = "1") double customDining, @RequestParam(name = "customGrocery", defaultValue = "1") double customGrocery,
    @RequestParam(name = "customTravel", defaultValue = "1") double customTravel, @RequestParam(name = "customGas", defaultValue = "1") double customGas, @RequestParam(name = "customOther", defaultValue = "1") double customOther)
    throws SQLException{
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        boolean validSpending = (Double.isFinite(dining) && dining >= 0) && (Double.isFinite(grocery) && grocery >= 0) && (Double.isFinite(travel) && travel >= 0) && (Double.isFinite(gas) && gas >= 0) && (Double.isFinite(other) && other >= 0);
        if(!validSpending){
            model.addAttribute("error", "Enter a finite, nonnegative amount for every category.");
            return "home";
        }
        if (!customName.isBlank()) {
        boolean finite = (Double.isFinite(customDining) && customDining >= 0) && (Double.isFinite(customGrocery) && customGrocery >= 0) && (Double.isFinite(customTravel) && customTravel >= 0) && (Double.isFinite(customGas) && customGas >= 0) && (Double.isFinite(customOther) && customOther >= 0);
        if(!finite){
            model.addAttribute("error", "Enter a finite, nonnegative amount for every multiplier.");
            return "home";
        }
        }   
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            CreditCardRepository repository = new CreditCardRepository();
            List<CreditCard> ranking = repository.findRankedBySpending(connection, dining, grocery, travel, gas, other);
            List<CardComparison> comparisons = new java.util.ArrayList<>();
            for (CreditCard card : ranking) {
                CardComparison holder = new CardComparison(card.getName(), card.calculateMonthlyPoints(dining, grocery, travel, gas, other));
                comparisons.add(holder);
            }
            if(!customName.isBlank()){
                CreditCard custom = new CreditCard(customName.trim(), customDining, customGrocery, customTravel, customGas, customOther);
                CardComparison customHolder = new CardComparison(custom.getName().trim(), custom.calculateMonthlyPoints(dining, grocery, travel, gas, other));
                comparisons.add(customHolder);
            }
            comparisons.sort(
                java.util.Comparator.comparingDouble(CardComparison::monthlyPoints)
                        .reversed()
                        .thenComparing(CardComparison::name)
            );
            model.addAttribute("comparisons", comparisons);
        }
        model.addAttribute("totalSpending", dining + grocery + travel + gas + other);
        return "results";
    }
}