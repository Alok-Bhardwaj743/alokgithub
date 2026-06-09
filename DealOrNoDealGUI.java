import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DealOrNoDealGUI extends JFrame {

    private int[] amounts = {1,5,10,50,100,500,1000,5000,10000,50000};
    private ArrayList<Integer> values = new ArrayList<>();

    private JButton[] caseButtons = new JButton[10];
    private JLabel statusLabel;
    private JTextArea remainingArea;

    private int playerCase = -1;
    private int openedThisRound = 0;
    private int round = 0;

    private int[] rounds = {3,2,2,1}; // cases per round

    public DealOrNoDealGUI() {

        setTitle("Deal or No Deal - Complete Version");
        setSize(700,500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Shuffle values
        for(int amt : amounts) values.add(amt);
        Collections.shuffle(values);

        // Top
        statusLabel = new JLabel("Select your case", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(statusLabel, BorderLayout.NORTH);

        // Center buttons
        JPanel panel = new JPanel(new GridLayout(2,5,10,10));
        for(int i=0;i<10;i++){
            int index = i;
            caseButtons[i] = new JButton("Case " + (i+1));
            caseButtons[i].addActionListener(e -> handleClick(index));
            panel.add(caseButtons[i]);
        }
        add(panel, BorderLayout.CENTER);

        // Right side remaining values
        remainingArea = new JTextArea();
        remainingArea.setEditable(false);
        updateRemaining();
        add(new JScrollPane(remainingArea), BorderLayout.EAST);

        setVisible(true);
    }

    private void handleClick(int index){

        // Step 1: Select player's case
        if(playerCase == -1){
            playerCase = index;
            caseButtons[index].setText("YOUR CASE");
            caseButtons[index].setEnabled(false);
            statusLabel.setText("Round 1: Open " + rounds[round] + " cases");
            return;
        }

        // Step 2: Open cases
        if(caseButtons[index].isEnabled()){
            int value = values.get(index);
            caseButtons[index].setText("₹" + value);
            caseButtons[index].setEnabled(false);

            openedThisRound++;
            updateRemaining();

            // Check round completion
            if(openedThisRound == rounds[round]){
                openedThisRound = 0;
                round++;
                makeOffer();
            }
        }
    }

    private void makeOffer(){

        int sum = 0, count = 0;

        for(int i=0;i<10;i++){
            if(caseButtons[i].isEnabled() || i == playerCase){
                sum += values.get(i);
                count++;
            }
        }

        // Improved banker logic (slightly lower than average)
        int offer = (int)(sum / count * 0.8);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Banker offers ₹" + offer + "\nDeal?",
                "Bank Offer",
                JOptionPane.YES_NO_OPTION
        );

        if(choice == JOptionPane.YES_OPTION){
            JOptionPane.showMessageDialog(this,
                    "Deal taken: ₹" + offer +
                    "\nYour case had ₹" + values.get(playerCase));
            System.exit(0);
        }

        // Continue game
        if(round < rounds.length){
            statusLabel.setText("Round " + (round+1) + ": Open " + rounds[round] + " cases");
        } else {
            finalRound();
        }
    }

    private void finalRound(){

        // Find last unopened case
        int lastCase = -1;
        for(int i=0;i<10;i++){
            if(caseButtons[i].isEnabled()){
                lastCase = i;
                break;
            }
        }

        int playerValue = values.get(playerCase);
        int otherValue = values.get(lastCase);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Final Offer!\nSwap your case?",
                "Final Decision",
                JOptionPane.YES_NO_OPTION
        );

        int finalValue = (choice == JOptionPane.YES_OPTION) ? otherValue : playerValue;

        JOptionPane.showMessageDialog(this,
                "You won ₹" + finalValue);

        System.exit(0);
    }

    private void updateRemaining(){
        ArrayList<Integer> remaining = new ArrayList<>();

        for(int i=0;i<10;i++){
            if(caseButtons[i].isEnabled() || i == playerCase){
                remaining.add(values.get(i));
            }
        }

        Collections.sort(remaining);

        StringBuilder sb = new StringBuilder("Remaining:\n");
        for(int val : remaining){
            sb.append("₹").append(val).append("\n");
        }

        remainingArea.setText(sb.toString());
    }

    public static void main(String[] args){
        new DealOrNoDealGUI();
    }
    
}