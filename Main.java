import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Vehicle {
    private String vehicleId;
    private String make;
    private String variant;
    private double dailyRate;
    private boolean available;

    public Vehicle(String vehicleId, String make, String variant, double dailyRate) {
        this.vehicleId = vehicleId;
        this.make = make;
        this.variant = variant;
        this.dailyRate = dailyRate;
        this.available = true;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getMake() {
        return make;
    }

    public String getVariant() {
        return variant;
    }

    public double computeTotal(int numDays) {
        return dailyRate * numDays;
    }

    public boolean isAvailable() {
        return available;
    }

    public void markRented() {
        available = false;
    }

    public void markReturned() {
        available = true;
    }
}

class User {
    private String userId;
    private String fullName;

    public User(String userId, String fullName) {
        this.userId = userId;
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }
}

class Booking {
    private Vehicle vehicle;
    private User user;
    private int rentalDuration;

    public Booking(Vehicle vehicle, User user, int rentalDuration) {
        this.vehicle = vehicle;
        this.user = user;
        this.rentalDuration = rentalDuration;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public User getUser() {
        return user;
    }

    public int getRentalDuration() {
        return rentalDuration;
    }
}

class RentalService {
    private List<Vehicle> vehicleList;
    private List<User> userList;
    private List<Booking> bookingList;

    public RentalService() {
        vehicleList = new ArrayList<>();
        userList = new ArrayList<>();
        bookingList = new ArrayList<>();
    }

    public void registerVehicle(Vehicle vehicle) {
        vehicleList.add(vehicle);
    }

    public void registerUser(User user) {
        userList.add(user);
    }

    public void assignVehicle(Vehicle vehicle, User user, int duration) {
        if (vehicle.isAvailable()) {
            vehicle.markRented();
            bookingList.add(new Booking(vehicle, user, duration));
        } else {
            System.out.println("Selected vehicle is currently unavailable.");
        }
    }

    public void releaseVehicle(Vehicle vehicle) {
        vehicle.markReturned();
        Booking foundBooking = null;

        for (Booking booking : bookingList) {
            if (booking.getVehicle() == vehicle) {
                foundBooking = booking;
                break;
            }
        }

        if (foundBooking != null) {
            bookingList.remove(foundBooking);
        } else {
            System.out.println("No matching booking found for this vehicle.");
        }
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Vehicle Rental Portal =====");
            System.out.println("1. Book a Vehicle");
            System.out.println("2. Return a Vehicle");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");

            int selection = scanner.nextInt();
            scanner.nextLine(); // clear newline

            if (selection == 1) {
                System.out.println("\n== Vehicle Booking ==\n");
                System.out.print("Enter your full name: ");
                String nameInput = scanner.nextLine();

                System.out.println("\nAvailable Vehicles:");
                for (Vehicle v : vehicleList) {
                    if (v.isAvailable()) {
                        System.out.println(v.getVehicleId() + " - " + v.getMake() + " " + v.getVariant());
                    }
                }

                System.out.print("\nEnter Vehicle ID to book: ");
                String selectedId = scanner.nextLine();

                System.out.print("Enter duration in days: ");
                int numDays = scanner.nextInt();
                scanner.nextLine(); // clear newline

                User newUser = new User("USR" + (userList.size() + 1), nameInput);
                registerUser(newUser);

                Vehicle chosenVehicle = null;
                for (Vehicle v : vehicleList) {
                    if (v.getVehicleId().equals(selectedId) && v.isAvailable()) {
                        chosenVehicle = v;
                        break;
                    }
                }

                if (chosenVehicle != null) {
                    double bill = chosenVehicle.computeTotal(numDays);
                    System.out.println("\n== Booking Details ==");
                    System.out.println("User ID: " + newUser.getUserId());
                    System.out.println("User Name: " + newUser.getFullName());
                    System.out.println("Vehicle: " + chosenVehicle.getMake() + " " + chosenVehicle.getVariant());
                    System.out.println("Duration: " + numDays + " days");
                    System.out.printf("Total Cost: $%.2f%n", bill);

                    System.out.print("Proceed with booking (Y/N): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        assignVehicle(chosenVehicle, newUser, numDays);
                        System.out.println("\nVehicle successfully booked!");
                    } else {
                        System.out.println("\nBooking cancelled.");
                    }
                } else {
                    System.out.println("\nInvalid or unavailable vehicle selection.");
                }

            } else if (selection == 2) {
                System.out.println("\n== Vehicle Return ==\n");
                System.out.print("Enter Vehicle ID to return: ");
                String returnId = scanner.nextLine();

                Vehicle vehicleToReturn = null;
                for (Vehicle v : vehicleList) {
                    if (v.getVehicleId().equals(returnId) && !v.isAvailable()) {
                        vehicleToReturn = v;
                        break;
                    }
                }

                if (vehicleToReturn != null) {
                    User userReturning = null;
                    for (Booking booking : bookingList) {
                        if (booking.getVehicle() == vehicleToReturn) {
                            userReturning = booking.getUser();
                            break;
                        }
                    }

                    if (userReturning != null) {
                        releaseVehicle(vehicleToReturn);
                        System.out.println("Vehicle returned by " + userReturning.getFullName());
                    } else {
                        System.out.println("No rental record found for this vehicle.");
                    }
                } else {
                    System.out.println("Invalid ID or vehicle not currently rented.");
                }

            } else if (selection == 3) {
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }

        System.out.println("\nThanks for using the Vehicle Rental Portal!");
    }
}

public class Main {
    public static void main(String[] args) {
        RentalService service = new RentalService();

        Vehicle v1 = new Vehicle("V001", "Toyota", "Camry", 60.0);
        Vehicle v2 = new Vehicle("V002", "Honda", "Accord", 70.0);
        Vehicle v3 = new Vehicle("V003", "Mahindra", "Thar", 150.0);


        service.registerVehicle(v1);
        service.registerVehicle(v2);
        service.registerVehicle(v3);

        service.showMenu();
    }
}
