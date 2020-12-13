package com.capstone.project.Controller;
/**
 * @author Rohan Patel
 */


import com.capstone.project.Bean.Holders.ReturnData;
import com.capstone.project.Bean.*;
import com.capstone.project.Repo.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurant/{id}/table")
@AllArgsConstructor
public class RestaurantTableController {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantTableRepository tableRepository;
    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final UserRepository userRepository;
    private final SeatingArrangementRepository seatingArrangementRepository;

    // Get all tables of restaurant
    @GetMapping("")
    public ReturnData getTable(@PathVariable("id") Long id) {
        ReturnData returnData = new ReturnData();
        Restaurant foundRestaurant = restaurantRepository.findById(id).get();
        returnData.setCode(0);
        returnData.setMessage("Restaurant tables fetched");
        returnData.setObject(foundRestaurant.getTables());
        return returnData;
    }


    /**
     * <p>
     *     This method returns the table information for a particular table ID to the front end
     * </p>
     * @param id
     * @param tid
     * @return
     */
    //Get a particular table info
    @GetMapping("/{tid}")
    public ReturnData getTableInfo(@PathVariable("id") Long id, @PathVariable("tid") Long tid) {
        ReturnData returnData = new ReturnData();
        Restaurant foundRestaurant = new Restaurant();
        try {
            foundRestaurant = restaurantRepository.findById(id).get();
        } catch (Exception e) {
            returnData.setCode(1);
            returnData.setMessage("Restaurant Not Found");
            return returnData;
        }
        List<RestaurantTable> allTables = foundRestaurant.getTables();

        for (RestaurantTable t : allTables) {
            if (t.getId() == tid) {
                returnData.setCode(0);
                returnData.setMessage("Table details fetched");
                returnData.setObject(t);
                return returnData;
            }
        }

        returnData.setCode(1);
        returnData.setMessage("Table not found");
        return returnData;
    }


    /**
     * This method adds a new table to the database by checking if there is already a table with same number or not
     * @param id
     * @param table
     */
    // Add new table to restaurant
    @CrossOrigin(origins = "*")
    @PutMapping(value = "", consumes = "application/json")
    public ReturnData addNewTable(@PathVariable("id") Long id, @RequestBody RestaurantTable table) {
        ReturnData returnData = new ReturnData();
        Restaurant foundRestaurant = restaurantRepository.findById(id).get();
        RestaurantTable foundTable = null;

        List<RestaurantTable> tableList = foundRestaurant.getTables();

        for (RestaurantTable t : tableList) {
            if (t.getNumber() == table.getNumber()) {
                foundTable = t;
                break;
            }
        }

        if (foundTable == null) {
            RestaurantTable savedTable = tableRepository.save(table);
            foundRestaurant.getTables().add(savedTable);
            restaurantRepository.save(foundRestaurant);
            returnData.setCode(0);
            returnData.setMessage("New table saved successfully");
            returnData.setObject(savedTable);
        } else {
            returnData.setCode(1);
            returnData.setMessage("Table with the same number already exists");
        }

        return returnData;
    }


    // Delete saved table
    @CrossOrigin(origins = "*")
    @DeleteMapping(value = "/{tid}")
    public ReturnData deleteTable(@PathVariable("id") Long id, @PathVariable("tid") Long tableId) {
        ReturnData returnData = new ReturnData();

        Restaurant foundRestaurant = restaurantRepository.findById(id).get();
        List<RestaurantTable> tables = foundRestaurant.getTables();

        for (int i = 0; i < tables.size(); i++) {
            if (tables.get(i).getId() == tableId) {
                tables.remove(i);
                foundRestaurant.setTables(tables);
                break;
            }
        }

        restaurantRepository.save(foundRestaurant);
        tableRepository.deleteById(tableId);

        returnData.setCode(0);
        returnData.setMessage("Table deleted successfully");

        return returnData;
    }


    /**
     * <p>
     *     This Method takes the UserID, Party size and the check param to book a table for the user. the check
     *     param is used to determine whether to make the table reservation final or not. If check is 0 then the
     *     program wait for the restaurant to approve the table and when the check is 1 then the table is finally
     *     reserved in the database
     * </p>
     * @param id
     * @param uid
     * @param partySize
     * @param check
     * @param timeSlot
     */
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{uid}/book/{partySize}/{check}", consumes = "application/json")
    public ReturnData bookTable(@PathVariable("id") Long id, @PathVariable("uid") Long uid, @PathVariable("partySize") Integer partySize,
                                @PathVariable("check") Integer check, @RequestBody TimeSlot timeSlot) {
        // Variable CHECK : 0= retrieve assigned tables , 1=make booking after approval
        ReturnData returnData = new ReturnData();

        Restaurant restaurant = restaurantRepository.findById(id).get();
        List<RestaurantTable> restaurantTables = restaurant.getTables();
        List<SeatingArrangement> bookingsList = seatingArrangementRepository.findByTimeSlot(timeSlot);


        List<RestaurantTable> availableTables = restaurantTables;
        for (SeatingArrangement s : bookingsList) {
            availableTables = availableTables.stream().filter(t -> t.getId() != s.getTable().getId()).
                    collect(Collectors.toList());
        }

        if (partySize > calculateMaxAvailableCapacity(availableTables)) {
            returnData.setMessage("No table available for the time slot");
            returnData.setCode(1);
            return returnData;
        }
        List<SeatingArrangement> assignedTables = joinTables(
                availableTables, timeSlot, partySize
        );
        if (check == 0) {
            returnData.setMessage("Tables assigned");
            returnData.setCode(0);
            returnData.setObject(assignedTables);
        }
        if (check == 1) {
            returnData.setMessage("Booking done");
            returnData.setCode(0);
            returnData.setObject(makeFinalReservation(assignedTables, userRepository.findById(uid).get()));
        }
        return returnData;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/check/{partySize}", consumes = "application/json")
    public ReturnData checkTableAvailability(@PathVariable("id") Long id, @PathVariable("partySize") Integer partySize, @RequestBody TimeSlot timeSlot) {
        ReturnData returnData = new ReturnData();
        Restaurant restaurant = restaurantRepository.findById(id).get();
        List<RestaurantTable> restaurantTables = restaurant.getTables();
        List<RestaurantTable> availableTables = restaurantTables;
        List<SeatingArrangement> bookingsList = seatingArrangementRepository.findByTimeSlot(timeSlot);
        for (SeatingArrangement s : bookingsList) {
            availableTables = availableTables.stream().filter(t -> t.getId() != s.getTable().getId()).
                    collect(Collectors.toList());
        }
        if (partySize > calculateMaxAvailableCapacity(availableTables)) {
            returnData.setMessage("No table available for the time slot");
            returnData.setCode(1);
            return returnData;
        }
        returnData.setMessage("Table available for the time slot");
        returnData.setCode(0);
        return returnData;
    }


    private SeatingArrangement makeNewBooking(TimeSlot timeSlot, RestaurantTable table, Integer partySize) {
        SeatingArrangement seating = new SeatingArrangement();
        seating.setTimeSlot(timeSlot);
        seating.setTable(table);
        seating.setPartySize(partySize);
        return seating;
    }

    private Reservation makeFinalReservation(List<SeatingArrangement> seating, User user) {
        for (SeatingArrangement s : seating) {
            System.out.println("ID : " + s.getId());
            System.out.println("TABLE CAP : " + s.getTable().getCapacity());
            System.out.println("PARTY SIZE : " + s.getPartySize());
        }
        List<SeatingArrangement> savedSeatings = seatingArrangementRepository.saveAll(seating);
        //List<SeatingArrangement> savedSeatings = seating;
        Reservation newReservation = new Reservation();
        newReservation.setDate(LocalDate.now());
        newReservation.setTime(seating.get(0).getTimeSlot().getTime());
        newReservation.setUser(user);
        newReservation.setSeatingArrangement(savedSeatings);

        //return newReservation;
        return reservationRepository.save(newReservation);
    }


    /**
     * <p>
     *     This method takes available table list, time slot and party size to determine the best table combination
     *     based on the party size and availability. The algorithm is based on ranking which means the table with the
     *     minimum difference with party size (Table Capacity - PartySize) wins and is assigned to the customer.
     * </p>
     * @param availableTables
     * @param timeSlot
     * @param partySize
     */
    private List<SeatingArrangement> joinTables(List<RestaurantTable> availableTables, TimeSlot timeSlot, Integer partySize) {

        // Sorted list of available tables for the particular time slot
        availableTables = availableTables.stream().sorted(Comparator.comparing(RestaurantTable::getCapacity))
                .collect(Collectors.toList());

        // Empty list of Seating arrangement that will be made
        List<SeatingArrangement> reservedSeatingArrangement = new ArrayList<>();

        while (partySize > 0) {
            int i = 0;//Index of the table from available table list
            HashMap<Integer, Integer> difference = new HashMap<>();
            for (RestaurantTable table : availableTables) {
                difference.put(Math.abs(table.getCapacity() - partySize), i);
                i++;
            }
            List<Integer> differenceList = new ArrayList<>(difference.keySet());
            Collections.sort(differenceList);
            int minimumDifference = differenceList.get(0);
            int minimumIndex = difference.get(minimumDifference);
            RestaurantTable foundTable = availableTables.get(minimumIndex); // Table with the min difference
            int partySizeForTheTable = partySize;
            if (partySize >= foundTable.getCapacity())
                partySizeForTheTable = foundTable.getCapacity();
            SeatingArrangement reservedArrangement = makeNewBooking(timeSlot, foundTable, partySizeForTheTable);
            partySize -= foundTable.getCapacity();
            reservedSeatingArrangement.add(reservedArrangement);
            availableTables.remove(minimumIndex);
        }
        return reservedSeatingArrangement;
    }

    private int calculateMaxAvailableCapacity(List<RestaurantTable> availableTables) {
        int max = 0;

        for (RestaurantTable t : availableTables) {
            max += t.getCapacity();
        }

        return max;
    }
}

