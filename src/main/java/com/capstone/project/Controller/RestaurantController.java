package com.capstone.project.Controller;

import com.capstone.project.Bean.Category;
import com.capstone.project.Bean.Holders.Restaurant_User_Holder;
import com.capstone.project.Bean.Menu;
import com.capstone.project.Bean.Restaurant;
import com.capstone.project.Bean.RestaurantUser;
import com.capstone.project.Repo.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
@AllArgsConstructor
public class RestaurantController {

    private RestaurantRepository restaurantRepo;
    private RestaurantUserRepository restaurantUserRepository;
    private MenuRepository menuRepository;
    private CategoryRepository categoryRepository;


    //Return the restaurant object
    @GetMapping()
    public Restaurant getRestaurant(@RequestParam Long id) {
        Restaurant foundRestaurant = restaurantRepo.findById(id).get();
        return foundRestaurant;
    }

    //Register a new restaurant and return the newly registered restaurant
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/register", consumes = "application/json")
    public Integer registerRestaurant(@RequestBody Restaurant_User_Holder holder) {
        Restaurant resObj = holder.getRestaurant();
        RestaurantUser resUserObj = holder.getRestaurantUser();
        Restaurant foundRestaurant = restaurantRepo.findByNameAndUnitAndStreetAndCityAndPostalAndPhone(
                resObj.getName(), resObj.getUnit(), resObj.getStreet(), resObj.getCity(), resObj.getPostal(), resObj.getPhone()
        );

        if (foundRestaurant == null) {
            // No restaurant was registered with the data provided

            RestaurantUser foundRestaurantUser = restaurantUserRepository.findByEmail(resUserObj.getEmail());

            if (foundRestaurantUser == null) {
                Menu emptyMenu = menuRepository.save(new Menu());
                resObj.setMenu(emptyMenu);
                Restaurant savedRestaurant = restaurantRepo.save(resObj);
                resUserObj.setRestaurant(savedRestaurant);
                foundRestaurantUser = restaurantUserRepository.save(resUserObj);
                return 3; //Restaurant and user saved;
            }
            return 2; // Same email user exist
        }
        return 1; // Same detail restaurant exists
    }


    //Add new menu category
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{id}/menu/category",consumes = "application/json")
    public Integer addNewMenuCategory(@RequestBody Category category,@PathVariable("id") Long id){
        Category savedCategory = categoryRepository.save(category);
        Restaurant foundRestaurant = restaurantRepo.findById(id).get();
        Menu foundMenu = foundRestaurant.getMenu();
        foundMenu.getCategories().add(savedCategory);
        menuRepository.save(foundMenu);
        return 0;
    }






    //Return list of all restaurant, to be removed in production
    @GetMapping("/displayAll")
    public List<Restaurant> dislayAllRestaurant() {
        return restaurantRepo.findAll();
    }


}
