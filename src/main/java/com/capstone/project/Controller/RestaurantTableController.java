package com.capstone.project.Controller;

import com.capstone.project.Bean.Holders.ReturnData;
import com.capstone.project.Bean.Restaurant;
import com.capstone.project.Bean.RestaurantTable;
import com.capstone.project.Repo.RestaurantRepository;
import com.capstone.project.Repo.RestaurantTableRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/restaurant/{id}/table")
@AllArgsConstructor
public class RestaurantTableController {
    private RestaurantRepository restaurantRepository;
    private RestaurantTableRepository tableRepository;

    // Get all tables of restaurant
    @GetMapping("/")
    public ReturnData getTable(@PathVariable("id") Long id){
        ReturnData returnData = new ReturnData();
        Restaurant foundRestaurant = restaurantRepository.findById(id).get();
        returnData.setCode(0);
        returnData.setMessage("Restaurant tables fetched");
        returnData.setObject(foundRestaurant.getTables());
        return returnData;
    }


    // Add new table to restaurant
    @PutMapping("/")
    public ReturnData addNewTable(@PathVariable("id") Long id, @RequestBody RestaurantTable table){
        ReturnData returnData = new ReturnData();
        RestaurantTable foundTable = tableRepository.findByNumber(table.getNumber());

        if(foundTable == null){
            RestaurantTable savedTable = tableRepository.save(table);
            returnData.setCode(0);
            returnData.setMessage("New table saved successfully");
            returnData.setObject(savedTable);
        }else{
            returnData.setCode(1);
            returnData.setMessage("Table with the same number already exists");
        }

        return returnData;
    }


    // Delete saved table
    @DeleteMapping("/{tid}")
    public ReturnData deleteTable(@PathVariable("id") Long id,@PathVariable("tid") Long tableId){
        ReturnData returnData = new ReturnData();

        Restaurant foundRestaurant = restaurantRepository.findById(id).get();
        List<RestaurantTable> tables = foundRestaurant.getTables();

        for(int i=0 ; i<tables.size();i++){
            if(tables.get(i).getId() == tableId){
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

}
