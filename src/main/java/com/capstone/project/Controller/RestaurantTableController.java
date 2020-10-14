package com.capstone.project.Controller;

import com.capstone.project.Bean.*;
import com.capstone.project.Bean.Holders.ReturnData;
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


//    @CrossOrigin(origins = "*")
//    @PutMapping(value = "/{uid}/book/{partySize}", consumes = "application/json")
//    public ReturnData bookTable(@PathVariable("id") Long id, @PathVariable("uid") Long uid, @PathVariable("partySize") Integer partySize, @RequestBody TimeSlot timeSlot) {
//        ReturnData returnData = new ReturnData();
//
//        Restaurant restaurant = restaurantRepository.findById(id).get();
//        List<RestaurantTable> restaurantTables = restaurant.getTables();
//        Integer minTableCap = 2;
//        List<SeatingArrangement> holdedBookingList = new ArrayList<>();
//
//        List<SeatingArrangement> bookingsList = seatingArrangementRepository.findByTimeSlot(timeSlot);
//
//        if (bookingsList.isEmpty()) {
//            // No reservations for the time slot selected is made
//            List<RestaurantTable> tableGreaterThanParty = getTablesGreaterThenPartySize(restaurantTables, partySize);
//
//            if (tableGreaterThanParty.isEmpty()) {
//                for (RestaurantTable t : tableGreaterThanParty) {
//                    if ((t.getCapacity() - partySize) <= minTableCap) {
//                        SeatingArrangement holdedBooking = makeNewBooking(timeSlot, t, partySize);
//                        holdedBookingList.add(holdedBooking);
//                    }
//                }
//                //Join the tables
//                List<SeatingArrangement> assignedTables = joinTables(
//                        restaurantTables, timeSlot, partySize
//                );
//                if (assignedTables.size() == 0) {
//                    returnData.setMessage("No available tables");
//                } else {
//                    returnData.setObject(makeFinalReservation(assignedTables, userRepository.findById(uid).get()));
//                }
//
//            } else {
//                // Join the tables
//                List<SeatingArrangement> assignedTables = joinTables(
//                        restaurantTables, timeSlot, partySize
//                );
//                returnData.setObject(makeFinalReservation(assignedTables, userRepository.findById(uid).get()));
//            }
//
//        } else {
//            // There is at least one reservation made for the selected time slot
//            List<RestaurantTable> availableTables = restaurantTables;
//            for (SeatingArrangement s : bookingsList) {
//                availableTables = availableTables.stream().filter(t -> t.getId() != s.getTable().getId()).
//                        collect(Collectors.toList());
//            }
//
//            List<SeatingArrangement> assignedTables = joinTables(
//                    availableTables, timeSlot, partySize
//            );
//            returnData.setObject(makeFinalReservation(assignedTables, userRepository.findById(uid).get()));
//
//        }
//
//        return returnData;
//    }

    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{uid}/book/{partySize}", consumes = "application/json")
    public ReturnData bookTable(@PathVariable("id") Long id, @PathVariable("uid") Long uid, @PathVariable("partySize") Integer partySize, @RequestBody TimeSlot timeSlot) {
        ReturnData returnData = new ReturnData();

        Restaurant restaurant = restaurantRepository.findById(id).get();
        List<RestaurantTable> restaurantTables = restaurant.getTables();
        List<SeatingArrangement> bookingsList = seatingArrangementRepository.findByTimeSlot(timeSlot);


        List<RestaurantTable> availableTables = restaurantTables;
        for (SeatingArrangement s : bookingsList) {
            availableTables = availableTables.stream().filter(t -> t.getId() != s.getTable().getId()).
                    collect(Collectors.toList());
        }

        if(partySize>calculateMaxAvailableCapacity(availableTables)){
            returnData.setMessage("No table available for the time slot");
            return returnData;
        }
        List<SeatingArrangement> assignedTables = joinTables(
                availableTables, timeSlot, partySize
        );
        returnData.setObject(makeFinalReservation(assignedTables, userRepository.findById(uid).get()));
        return returnData;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/check/{partySize}",consumes = "application/json")
    public ReturnData checkTableAvailability(@PathVariable("id") Long id,@PathVariable("partySize") Integer partySize, @RequestBody TimeSlot timeSlot){
        ReturnData returnData = new ReturnData();
        Restaurant restaurant = restaurantRepository.findById(id).get();
        List<RestaurantTable> restaurantTables = restaurant.getTables();
        List<RestaurantTable> availableTables = restaurantTables;
        List<SeatingArrangement> bookingsList = seatingArrangementRepository.findByTimeSlot(timeSlot);
        for (SeatingArrangement s : bookingsList) {
            availableTables = availableTables.stream().filter(t -> t.getId() != s.getTable().getId()).
                    collect(Collectors.toList());
        }
        if(partySize>calculateMaxAvailableCapacity(availableTables)){
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



    private List<SeatingArrangement> joinTables(List<RestaurantTable> availableTables, TimeSlot timeSlot, Integer partySize) {

        // Sorted list of available tables for the particular time slot
        availableTables = availableTables.stream().sorted(Comparator.comparing(RestaurantTable::getCapacity)).collect(Collectors.toList());

        // Seating arrangement that are made
        List<SeatingArrangement> reservedSeatingArrangement = new ArrayList<>();


        while (partySize > 0) {
            int i = 0;
            HashMap<Integer, Integer> difference = new HashMap<>();
            for (RestaurantTable table : availableTables) {
                difference.put(Math.abs(table.getCapacity() - partySize), i);
                i++;
            }
            List<Integer> differenceList = new ArrayList<>(difference.keySet());
            Collections.sort(differenceList);
            int minimumDifference = differenceList.get(0);
            int minimumIndex = difference.get(minimumDifference);
            RestaurantTable foundTable = availableTables.get(minimumIndex);
            int partySizeForTheTable = partySize;
            if(partySize>=foundTable.getCapacity())
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



//    private List<SeatingArrangement> joinTables(List<RestaurantTable> availableTables, TimeSlot timeSlot, Integer partySize) {
//        List<SeatingArrangement> holdSeating = new ArrayList<>();
//        if(partySize > calculateMaxAvailableCapacity(availableTables))
//            return holdSeating;
//        List<RestaurantTable> tablesLessThanPartySize = availableTables.stream().sorted(Comparator.comparing(RestaurantTable::getCapacity)).collect(Collectors.toList());
////                getTablesLessThenPartySize(availableTables, partySize);
////        if(tablesLessThanPartySize.size() == 0){
////            tablesLessThanPartySize = getTablesGreaterThenPartySize(availableTables,partySize);
////
////        }
//
//        List<RestaurantTable> holdTable = new ArrayList<>();
//
//
//        SeatingArrangement holdBooking = makeNewBooking(timeSlot, tablesLessThanPartySize.get(0), tablesLessThanPartySize.get(0).getCapacity());
//        holdTable.add(tablesLessThanPartySize.get(0));
//        holdSeating.add(holdBooking);
//        partySize = partySize - tablesLessThanPartySize.get(0).getCapacity();
//
//
//        while (partySize > 0) {
//            System.out.println("PARTY SIZE IS : "+partySize);
//            RestaurantTable leaseDifference = tablesLessThanPartySize.get(1);
//            for (int i = 1; i < tablesLessThanPartySize.size(); i++) {
//                RestaurantTable currentTable = tablesLessThanPartySize.get(i);
//                if ((currentTable.getCapacity() - partySize) < leaseDifference.getCapacity()) {
//                    if (!holdTable.contains(currentTable)) {
//                        leaseDifference = currentTable;
//                    }
//                }
//            }
//            int seatingPartySize = partySize;
//            if(partySize>=leaseDifference.getCapacity())
//                seatingPartySize = leaseDifference.getCapacity();
//            holdBooking = makeNewBooking(timeSlot, leaseDifference, seatingPartySize);
//
//            holdTable.add(leaseDifference);
//            holdSeating.add(holdBooking);
//            partySize = partySize - leaseDifference.getCapacity();
//        }
//        return holdSeating;
//    }


//    private List<RestaurantTable> getTablesGreaterThenPartySize(List<RestaurantTable> tableList, Integer partySize) {
//        return tableList.stream().filter(t -> t.getCapacity() >= partySize)
//                .sorted(Comparator.comparing(RestaurantTable::getCapacity)).collect(Collectors.toList());
//    }
//
//    private List<RestaurantTable> getTablesLessThenPartySize(List<RestaurantTable> tableList, Integer partySize) {
//        return tableList.stream().filter(t -> t.getCapacity() < partySize)
//                .sorted(Comparator.comparing(RestaurantTable::getCapacity))
//                .collect(Collectors.toList());
//    }
