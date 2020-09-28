package com.capstone.project.Controller;

import com.capstone.project.Bean.*;
import com.capstone.project.Bean.Holders.ReturnData;
import com.capstone.project.Repo.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurant/{id}/table")
@AllArgsConstructor
public class RestaurantTableController {
    private RestaurantRepository restaurantRepository;
    private RestaurantTableRepository tableRepository;
    private ReservationRepository reservationRepository;
    private TimeSlotRepository timeSlotRepository;
    private UserRepository userRepository;
    private SeatingArrangementRepository seatingArrangementRepository;

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


    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{uid}/book/{partySize}")
    public ReturnData bookTable(@PathVariable("id") Long id,@PathVariable("uid") Long uid, @PathVariable("partySize") Integer partySize, @RequestBody TimeSlot timeSlot) {
        ReturnData returnData = new ReturnData();

        Restaurant restaurant = restaurantRepository.findById(id).get();
        List<RestaurantTable> restaurantTables = restaurant.getTables();
        Integer minTableCap = 2;
        List<SeatingArrangement> holdedBookingList = new ArrayList<>();

        List<SeatingArrangement> bookingsList = seatingArrangementRepository.findByTimeSlot(timeSlot);

        if (bookingsList.isEmpty()) {
            // No reservations for the time slot selected is made
            List<RestaurantTable> tableGreaterThanParty = getTablesGreaterThenPartySize(restaurantTables, partySize);

            if (tableGreaterThanParty.isEmpty()) {
                for (RestaurantTable t : tableGreaterThanParty) {
                    if ((t.getCapacity() - partySize) <= minTableCap) {
                        SeatingArrangement holdedBooking = makeNewBooking(timeSlot, t, partySize);
                        holdedBookingList.add(holdedBooking);
                    }
                }
                //Join the tables
                List<SeatingArrangement> assignedTables = joinTables(
                        restaurantTables, timeSlot, partySize
                );

                returnData.setObject(makeFinalReservation(assignedTables,userRepository.findById(uid).get()));

            } else {
                // Join the tables
                List<SeatingArrangement> assignedTables = joinTables(
                        restaurantTables, timeSlot, partySize
                );
                returnData.setObject(makeFinalReservation(assignedTables,userRepository.findById(uid).get()));
            }

        } else {
            // There is at least one reservation made for the selected time slot
            List<RestaurantTable> availableTables = restaurantTables;
            for (SeatingArrangement s : bookingsList) {
                availableTables = availableTables.stream().filter(t -> t.getId() != s.getTable().getId()).
                        collect(Collectors.toList());
            }

            List<SeatingArrangement> assignedTables = joinTables(
                    availableTables, timeSlot, partySize
            );
            returnData.setObject(makeFinalReservation(assignedTables,userRepository.findById(uid).get()));

        }

        return returnData;
    }


    private List<RestaurantTable> getTablesGreaterThenPartySize(List<RestaurantTable> tableList, Integer partySize) {
        return tableList.stream().filter(t -> t.getCapacity() >= partySize).collect(Collectors.toList());
    }

    ;

    private List<RestaurantTable> getTablesLessThenPartySize(List<RestaurantTable> tableList, Integer partySize) {
        return tableList.stream().filter(t -> t.getCapacity() < partySize).collect(Collectors.toList());
    }

    ;

    private SeatingArrangement makeNewBooking(TimeSlot timeSlot, RestaurantTable table, Integer partySize) {
        SeatingArrangement seating = new SeatingArrangement();
        seating.setTimeSlot(timeSlot);
        seating.setTable(table);
        seating.setPartySize(partySize);
        return seating;
    }

    private Reservation makeFinalReservation(List<SeatingArrangement> seating,User user){
        List<SeatingArrangement> savedSeatings = seatingArrangementRepository.saveAll(seating);
        Reservation newReservation = new Reservation();
        newReservation.setDate(LocalDate.now());
        newReservation.setTime(seating.get(0).getTimeSlot().getTime());
        newReservation.setUser(user);
        newReservation.setSeatingArrangement(savedSeatings);

        return reservationRepository.save(newReservation);
    }
    private List<SeatingArrangement> joinTables(List<RestaurantTable> availableTables, TimeSlot timeSlot, Integer partySize) {
        List<SeatingArrangement> holdSeating = new ArrayList<>();
        List<RestaurantTable> tablesLessThanPartySize = getTablesLessThenPartySize(availableTables, partySize);
        List<RestaurantTable> holdTable = new ArrayList<>();

        SeatingArrangement holdBooking = makeNewBooking(timeSlot, tablesLessThanPartySize.get(0), partySize);
        holdTable.add(tablesLessThanPartySize.get(0));
        holdSeating.add(holdBooking);

        partySize = partySize - tablesLessThanPartySize.get(0).getCapacity();

        while (partySize >= 0) {
            RestaurantTable leaseDifference = tablesLessThanPartySize.get(1);
            for (int i = 1; i < tablesLessThanPartySize.size(); i++) {
                RestaurantTable currentTable = tablesLessThanPartySize.get(i);
                if ((currentTable.getCapacity() - partySize) < leaseDifference.getCapacity()) {
                    if (!holdTable.contains(currentTable)) {
                        leaseDifference = currentTable;
                    }
                }
            }
            partySize = partySize - leaseDifference.getCapacity();
            holdBooking = makeNewBooking(timeSlot, leaseDifference, partySize);
            holdTable.add(leaseDifference);
            holdSeating.add(holdBooking);
        }
        return holdSeating;
    }
}
