
import java.util.ArrayList;
import java.util.List;

public class Player {

	List<String> playersHand = new ArrayList<String>();
	List<String> handTwoOnSplit;

	boolean isItFirstGame = true;
	boolean firstChoiceInRound = true;

	int playersHandsValue = 0;
	int playersAce11to1 = 1;
	float startingMoney = 1000;
	float moneyLeft;
	float bet;
	int handTwoValue;
	int winningHands;

	CardDeck deck = BlackJack.deck;

	public void getInitials() {
		for (int i = 0; i < 2; i++) {
			int cardIndex = (int) (Math.random() * deck.deckList.size());
			playersHand.add(deck.deckList.get(cardIndex));
			deck.deckList.remove(cardIndex);
			playersHandsValue += deck.defineValueOfCard(playersHand.get(i));

		}
		String initialCardsText = "Your initials are: ";
		for (String card : playersHand) {
			initialCardsText += card + ", ";
		}
		System.out.println(initialCardsText.substring(0, initialCardsText.length() - 2));
		if (playersHandsValue == 22) { // if initials are two Aces
			playersAce11to1--;
			playersHandsValue -= 10; // one of the aces becomes 1 instead of 11
		} else if (playersHandsValue < 21)
			System.out.println("Total value of player's hand: " + playersHandsValue + "\n");
	}

	public void makeChoice(List<String> hand, int value, int aces11To1) {
		String choiceText = "Make you decision please, type H to Hit, S to Stand";
		boolean sameNominals = deck.defineValueOfCard(playersHand.get(0)) == deck.defineValueOfCard(playersHand.get(1));

		if (playersHand.size() == 2 && sameNominals && firstChoiceInRound) { // possible only on initial cards
			choiceText += ", D for double-down, SP for split";
			System.out.println(choiceText);
			while (true) {
				String decision = BlackJack.sc.nextLine().trim().toLowerCase();
				if (decision.equals("h")) {
					hit(playersHand, playersHandsValue, playersAce11to1);
					break;
				} else if (decision.equals("s")) {
					break;
				} else if (decision.equals("d")) {
					doubleDown();
					break;
				} else if (decision.equals("sp")) {
					split();
					break;
				}
			}
		} else if (playersHand.size() == 2 && firstChoiceInRound) { // possible only on iniial cards
			choiceText += ", D for double-down";
			System.out.println(choiceText);
			while (true) {
				String decision = BlackJack.sc.nextLine().trim().toLowerCase();
				if (decision.equals("h")) {
					hit(playersHand, playersHandsValue, playersAce11to1);
					break;
				} else if (decision.equals("s")) {
					break;
				} else if (decision.equals("d")) {
					doubleDown();
					break;
				}
			}
		} else {
			System.out.println(choiceText);
			while (true) {
				String decision = BlackJack.sc.nextLine().trim().toLowerCase();
				if (decision.equals("h")) {
					hit(hand, value, aces11To1);
					break;
				} else if (decision.equals("s")) {
					break;
				}
			}
		}
	}

	public void placeBet() {
		System.out.println("Please place you bet");

		if (isItFirstGame) {
			System.out.println("Your balance: " + startingMoney);
			bet = BlackJack.sc.nextFloat();
			while (true) {
				if (bet <= startingMoney / 2) {
					moneyLeft = startingMoney - bet;
					break;
				} else {
					System.out.println("You can't place a bet, that is greater than half of your bank! Try again");
					bet = BlackJack.sc.nextFloat();
				}
			}
		} else {
			System.out.println("Your balance: " + moneyLeft);
			bet = BlackJack.sc.nextFloat();
			while (true) {
				if (bet <= moneyLeft / 2) {
					moneyLeft -= bet;
					break;
				} else {
					System.out.println("You can't place a bet, that is greater than half of your bank! Try again");
					bet = BlackJack.sc.nextFloat();
				}
			}
		}
		System.out.println("Your ante is " + bet + ". Money left on account: " + moneyLeft + "\n");
	}

	public void hit(List<String> hand, int value, int aces11To1) {
		int[] valueAndAces = takeCard(hand, value, aces11To1);
		int newValue = valueAndAces[0];
		int newAces = valueAndAces[1];
		if (newValue < 21) {
			makeChoice(hand, newValue, newAces);
		} else if (newValue > 21) {
			System.out.println("Total result for this hand is greater than 21, too many for this hand!");
		}
	}

	public void doubleDown() { 
		moneyLeft -= bet;
		System.out.println("Money left on account: " + moneyLeft);
		takeCard(playersHand, playersHandsValue, playersAce11to1);
	}

	public int[] takeCard(List<String> hand, int value, int aces11To1) {

		int cardIndex = (int) (Math.random() * deck.deckList.size());
		hand.add(deck.deckList.get(cardIndex));
		deck.deckList.remove(cardIndex);
		value = 0;
		for (int i = 0; i < hand.size(); i++) {
			value += deck.defineValueOfCard(hand.get(i));
		}
		String cardsOnHand = "You took " + hand.get(hand.size() - 1) + ". \nCards on your hand are: ";
		for (String card : hand) {
			cardsOnHand += card + ", ";
		}
		System.out.println(cardsOnHand.substring(0, cardsOnHand.length() - 2) + "\n");

		if (value > 21) {
			if (cardsOnHand.contains("Ace") && aces11To1 == 1) { // if there is an Ace that is 11, make it 1
				aces11To1--;
				value -= 10;
				System.out.println("Total value of your hand now is: " + value);
			}
		} else if (value == 21) {
			System.out.println("You got to 21, no more cards for you");
		} else if (value < 21) {
			System.out.println("Total value of your hand now is: " + value);
		}
		int[] valueAndAces = new int[2];
		valueAndAces[0] = value;
		valueAndAces[1] = aces11To1;
		return valueAndAces;
	}

	public void split() {

		moneyLeft -= bet;
		System.out.println("\nMoney left on account: " + moneyLeft);
		firstChoiceInRound = false;
		int handTwoAce11to1 = 1; // giving hand two chance to turn one ace from 11 to 1

		handTwoOnSplit = new ArrayList<String>(); // initialising hand 2
		handTwoOnSplit.add(playersHand.get(1)); // moving second card from initial hand to hand 2
		playersHand.remove(1);

		playersHandsValue = deck.defineValueOfCard(playersHand.get(0)); // value of first card on hand 1
		handTwoValue = deck.defineValueOfCard(handTwoOnSplit.get(0)); // value of first card on hand 2

		System.out.print("For hand one ");
		takeCard(playersHand, playersHandsValue, playersAce11to1); // adding card to hand 1
		// if (playersHandsValue != 21)
		makeChoice(playersHand, playersHandsValue, playersAce11to1); // working on hand 1

		System.out.print("\nFor hand two ");
		takeCard(handTwoOnSplit, handTwoValue, handTwoAce11to1); // adding card to hand 2
		// if (handTwoValue != 21)
		makeChoice(handTwoOnSplit, handTwoValue, handTwoAce11to1); // working on hand 2
	}
}
