
import java.util.ArrayList;
import java.util.Scanner;

public class BlackJack {

	static Scanner sc = new Scanner(System.in);
	static CardDeck deck = new CardDeck();
	static Dealer dealer = new Dealer();
	static Player player = new Player();
	String finalLine = "";

	public static void main(String[] args) {
		BlackJack game = new BlackJack();
		game.playGame();
	}

	public void playGame() {
		player.placeBet();
		deck.fillTheDeck();
		if (!player.isItFirstGame) {// reinitialising every variable
			player.playersHand = new ArrayList<String>();
			player.playersHandsValue = 0;
			player.playersAce11to1 = 1;
			player.bet = 0;
			dealer.dealersHand = new ArrayList<String>();
			dealer.dealersHandsValue = 0;
			dealer.dealersAce11to1 = 1;
			dealer.cardsOnHand = "";
			finalLine = "";
			player.firstChoiceInRound = true;
		}
		player.getInitials();
		dealer.getAllCards();
		player.isItFirstGame = false;
		if (player.playersHandsValue == 21) { // if 21 is on initials = blackjack
			finalLine += "You got BlackJack!\n";
			if (dealer.dealersHandsValue == 21 && dealer.dealersHand.size() == 2) { // checking BJ for dealer
				player.moneyLeft += player.bet;
				finalLine += "Dealer also has BlackJack, round ends with push ";
			} else { // if no BJ for dealer and BJ for player
				finalLine += "Dealer has no BlackJack, you won with BJ! ";
				player.moneyLeft += (player.bet * 2.5f);
			}
		} else { // if there is no BJ for player
			finalLine += "Dealers cards are " + dealer.cardsOnHand;
			if (dealer.dealersHandsValue == 21 && dealer.dealersHand.size() == 2) {
				finalLine += "Dealer has BlackJack, dealer wins";
			} else {
				finalLine += "Dealer's result is " + dealer.dealersHandsValue + "\n";
				player.makeChoice(player.playersHand, player.playersHandsValue, player.playersAce11to1);
				if (player.handTwoOnSplit != null) {
					splitWasChosen();
				} else {
					if (player.playersHandsValue > 21) {
						finalLine += "You lost this time";
					} else if ( player.playersHandsValue <= 21 && dealer.dealersHandsValue > 21) {
						finalLine += "Dealer busts, ";
					} else if (player.playersHandsValue == dealer.dealersHandsValue) {
						player.moneyLeft += player.bet;
						finalLine += "Round ends with push!";
					} else if (dealer.dealersHandsValue <= 21) {
						if (player.playersHandsValue > dealer.dealersHandsValue) {
							player.moneyLeft += 2 * player.bet;
							finalLine += "You won this round!";
						}
						if (player.playersHandsValue < dealer.dealersHandsValue) {
							finalLine += "You lose this round!";
						}
					}
				}
			}
		}
		System.out.println(finalLine);
		letsPlayAgain();
	}

	public void splitWasChosen() {
		{ // if player made split
			if (player.playersHandsValue <= 21 && player.handTwoValue <= 21) { // if both hands did not get too many
				if (dealer.dealersHandsValue > 21) { // if dealer has too many
					player.moneyLeft += 4 * player.bet;
					finalLine += "Dealer busts, both hands win!";
				} else if (dealer.dealersHandsValue <= 21) {
					if (player.playersHandsValue == dealer.dealersHandsValue) { // if hand 1 pushes
						player.moneyLeft += player.bet;
						finalLine += "Hand one pushes. ";
					}
					if (player.handTwoValue == dealer.dealersHandsValue) { // if hand 2 pushes
						player.moneyLeft += player.bet;
						finalLine += "Hand two pushes. ";
					}
					if (player.playersHandsValue > dealer.dealersHandsValue) { // if hand 1 wins
						player.moneyLeft += 2 * player.bet;
						finalLine += "Hand one wins. ";
					}
					if (player.handTwoValue > dealer.dealersHandsValue) { // if hand 2 wins
						player.moneyLeft += 2 * player.bet;
						finalLine += "Hand two wins";
					}
				}
			}
			if (player.playersHandsValue > 21 && player.handTwoValue > 21) { // if both hands got too many
				finalLine += "Both hands got too many, you lose!";
			} else if (player.playersHandsValue > 21 || player.handTwoValue > 21) { // if only one hand get too many
				finalLine += "One of the hands got too many. ";
				if (dealer.dealersHandsValue > 21) {
					if (player.playersHandsValue <= 21 && dealer.dealersHandsValue > 21) {
						player.moneyLeft += 2 * player.bet; // if hand 1 < 21 + dealer busts
						finalLine += "Dealer busts, result is push";
					}
					if (player.handTwoValue <= 21 && dealer.dealersHandsValue > 21) {
						player.moneyLeft += 2 * player.bet; // if hand 2 < 21 + dealer busts
						finalLine += "Dealer busts, result is push";
					}
				} else if (player.playersHandsValue == dealer.dealersHandsValue
						|| player.handTwoValue == dealer.dealersHandsValue) {
					player.moneyLeft += player.bet;
					finalLine += "Another hand pushes"; // if one hand pushes
				} else if (player.playersHandsValue > dealer.dealersHandsValue
						|| player.handTwoValue > dealer.dealersHandsValue) {
					player.moneyLeft += 2 * player.bet;
					finalLine += "Another hand wins"; // if one hand pushes
				}

			}
		}
	}

	public void letsPlayAgain() {
		System.out.println("Would you like to play again?");
		while (true) {
			String choice = BlackJack.sc.nextLine().trim().toLowerCase();
			if (choice.equals("y")) {
				player.startingMoney = player.moneyLeft;
				System.out.println("\n");
				playGame();
				break;
			} else if (choice.equals("n")) {
				System.out.println("Thank you for the game, see you later!");
				sc.close();
				System.exit(1);
			}
		}
	}
}
