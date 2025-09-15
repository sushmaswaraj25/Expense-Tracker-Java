import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.io.*;
// Expense Class (Encapsulation + OOP)
class Expense{
    private int id;
    private String category;
    private double amount;
    private LocalDate date;
    //constructor
    Expense(int id,String category,double amount,LocalDate date){
        this.id=id;
        this.category=category;
        this.amount=amount;
        this.date=date;
    }
    //getter for amount(useful for total calculation)
    public int getId() {
        return id;
    }
    public String getCategory() {
        return category;
    }
    public double getAmount() {
        return amount;
    }
    public LocalDate getDate() {
        return date;
    }
    //optional setter (not strictly needed in your project yet)
    public void setCategory(String category) {
        this.category=category;
    }
    public void setAmount(double amount) {
        this.amount=amount;
    }
    //pretty print
    @Override
    public String toString() {
         return "ID: " + id + " | Category: " + category +
               " | Amount: \u20B9" + String.format("%.2f",amount)+ " | Date: " + date;
    }
}
public class ExpenseTracker {
    private static int idCounter=1; //auto-increment ID
    public static void main(String[] args){
        Scanner scan=new Scanner(System.in);
        ArrayList<Expense> expenses=new ArrayList<>();        
        while (true) {
            System.out.println("\n==== Expense Tracker ====");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Show Total Expenses");
            System.out.println("4. Search by Category");
            System.out.println("5. Delete Expense by ID");
            System.out.println("6. Save to File");
            System.out.println("7. Load from File");
            System.out.println("8. Sort by Date");
            System.out.println("9. Exit");
            System.out.println("Enter Choice: ");
            int choice;
            try {
                choice=Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Enter a number (1-9).");
                continue;
            }
            switch (choice) {
                case 1:
                       addExpense(scan,expenses);                       
                       break;
                case 2:
                       viewExpenses(expenses);
                       break;
                case 3:
                       showTotal(expenses);
                       break;
                case 4:
                       searchByCategory(scan,expenses);
                       break;
                case 5:
                       deleteExpense(scan,expenses);
                       break;
                case 6:
                       saveToFile(expenses);
                       break;
                case 7:
                       idCounter= loadFromFile(expenses);
                       break;
                case 8:
                       sortByDate(expenses);
                       break;
                case 9:
                       System.out.println("Exiting...");
                       scan.close();
                       return;
                default:
                       System.out.println("Invalid choice!");

            }
        }
    }
    // These methods must be OUTSIDE main but INSIDE the class
    // Add Expense
    private static void addExpense(Scanner scan, ArrayList<Expense> expenses) {
    System.out.print("Enter category: ");
    String category = scan.nextLine();

    System.out.print("Enter amount: ");
    String amountInput=scan.nextLine();
   try{
    double amount = Double.parseDouble(amountInput.trim());
    Expense e = new Expense(idCounter++, category, amount, LocalDate.now());
    expenses.add(e);
    System.out.println("Expense added successfully!");
   }catch(NumberFormatException ex) {
    System.out.println("Invalid amount! please enter a number.");
 }
}
//View Expenses
private static void viewExpenses(ArrayList<Expense> expenses) {
    if (expenses.isEmpty()) {
        System.out.println("No expenses recorded.");
    } else {
        for (Expense e : expenses) {
            System.out.println(e); // will call toString() in Expense class
        }
    }
 }
 //show Total Expenses
 private static void showTotal(ArrayList<Expense> expenses) {
    if (expenses.isEmpty()) {
        System.out.println("No expenses recorded.");
        return;
    }
    double sum=0;
    for (Expense e:expenses) {
        sum+=e.getAmount();//using getter
    }
    System.out.println("Total Expenses: \u20B9" + String.format("%.2f",sum));
 }
 //Search by category
 private static void searchByCategory(Scanner scan,ArrayList<Expense> expenses) {
    if (expenses.isEmpty()) {
        System.out.println("No expenses recorded.");
        return;
    }
    System.out.print("Enter category to search: ");
    String cat=scan.nextLine();
    boolean found = false;
    for (Expense e:expenses) {
        if (e.getCategory().equalsIgnoreCase(cat)) {
            System.out.println(e);//is shorthand for System.out.println(e.toString());since desinged custom toString() in override
            found = true;
        }
    }
    if (!found) {
        System.out.println("No expenses found in this category.");
    }
 }
 //Delete Expense
 private static void deleteExpense(Scanner scan,ArrayList<Expense> expenses) {
    if (expenses.isEmpty()) {
        System.out.println("No expenses to delete.");
        return;
    }
    System.out.print("Enter ID of expense to delete: ");
    int idToDelete;
    try {
        idToDelete=Integer.parseInt(scan.nextLine());
    }catch (NumberFormatException e) {
        System.out.println("Invalid ID!");
        return;
    }
    //removeIf is a modern java 8+ way
    boolean removed = expenses.removeIf(e-> e.getId() == idToDelete);
    if (removed) {
        System.out.println("Expense deleted successfully!");
    }else{
        System.out.println("Expense with ID "+idToDelete+" not found.");
    }
 }
 //Save to File
 private static void saveToFile(ArrayList<Expense> expenses) {
    try (PrintWriter pw = new PrintWriter("expenses.txt")) {
        for (Expense e : expenses) {
            pw.println(e.getId() + "," + e.getCategory() + "," + e.getAmount() + "," + e.getDate());
        }
        System.out.println("Expenses saved to file successfully!");
    } catch (IOException ex) {
        System.out.println("Error while saving: " + ex.getMessage());
    }
}
//Load from file
 private static int loadFromFile(ArrayList<Expense> expenses) {
    expenses.clear(); // remove old data before loading
    int maxId =0;

    try (Scanner fileScanner = new Scanner(new File("expenses.txt"))) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String category = parts[1];
            double amount = Double.parseDouble(parts[2]);
            LocalDate date = LocalDate.parse(parts[3]);

            Expense e = new Expense(id, category, amount, date);
            expenses.add(e);
            if (id>maxId) maxId=id;
        }
        System.out.println("Expenses loaded from file successfully!");
    } catch (Exception ex) {
        System.out.println("Error while loading: " + ex.getMessage());
    }
    return maxId + 1;
}
//Sort by Date
 private static void sortByDate(ArrayList<Expense> expenses) {
    if (expenses.isEmpty()) {
        System.out.println("No expenses recorded.");
        return;
    }
    Collections.sort(expenses,Comparator.comparing(Expense::getDate).reversed());
    System.out.println("Expenses sorted by date successfully!(latest first):");
        viewExpenses(expenses);
 }
}
  

