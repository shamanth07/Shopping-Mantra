package com.example.exam;

public class GuessTheNumber {
    int randomNumber = new Random().nextInt(100) + 1;

submitGuessButton.setOnClickListener(view -> {
        String guessString = guessEditText.getText().toString();
        if (!guessString.isEmpty()) {
            int guess = Integer.parseInt(guessString);
            if (guess > randomNumber) {
                Toast.makeText(this, "Too high, try again.", Toast.LENGTH_SHORT).show();
            } else if (guess < randomNumber) {
                Toast.makeText(this, "Too low, try again.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Correct! You guessed the number!", Toast.LENGTH_SHORT).show();
                randomNumber = new Random().nextInt(100) + 1; // Reset the game
            }
        }
    };
}
