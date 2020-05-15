package com.capstone.project.Controller;

import com.capstone.project.Bean.*;
import com.capstone.project.Bean.Holders.Restaurant_User_Holder;
import com.capstone.project.Bean.Holders.ReturnData;
import com.capstone.project.Repo.*;
import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice;
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
    private DishRepository dishRepository;


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
    @PutMapping(value = "/{id}/menu/category", consumes = "application/json")
    public ReturnData addNewMenuCategory(@RequestBody Category category, @PathVariable("id") Long id) {
        ReturnData returnData = new ReturnData();

        Category savedCategory = categoryRepository.save(category);
        returnData.setObject(savedCategory);
        Restaurant foundRestaurant = restaurantRepo.findById(id).get();
        Menu foundMenu = foundRestaurant.getMenu();
        foundMenu.getCategories().add(savedCategory);
        menuRepository.save(foundMenu);

        returnData.setCode(0);
        returnData.setMessage("Category created successfully");
        return returnData;
    }


    //Edit category name
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/{id}/menu/category")
    public ReturnData editMenuCategory(@RequestParam String categoryName, @PathVariable("id") Long id) {
        Category foundCategory = categoryRepository.findById(id).get();
        foundCategory.setName(categoryName);
        foundCategory = categoryRepository.save(foundCategory);

        ReturnData returnData = new ReturnData();
        returnData.setMessage("Category renamed successfully");
        returnData.setObject(foundCategory);
        returnData.setCode(0);
        return returnData;
    }


    //Delete menu category
    @CrossOrigin(origins = "*")
    @DeleteMapping(value = "/{id}/menu/category")
    public ReturnData deleteMenuCategory(@RequestParam Long categoryId, @PathVariable("id") Long id) {

        Restaurant foundRestaurant = restaurantRepo.findById(id).get();
        Menu foundMenu = foundRestaurant.getMenu();
        List<Category> categoriesList = foundMenu.getCategories();

        for (int i = 0; i < categoriesList.size(); i++) {
            if (categoriesList.get(i).getId() == categoryId) {
                categoriesList.remove(i);
                break;
            }
        }

        foundMenu.setCategories(categoriesList);
        menuRepository.save(foundMenu);
        categoryRepository.deleteById(categoryId);

        ReturnData returnData = new ReturnData();
        returnData.setMessage("Category deleted successfully");
        returnData.setCode(0);
        return returnData;
    }


    //Add new menu item
    @CrossOrigin("*")
    @PutMapping(value = "/menu/item")
    public ReturnData addMenuItem(@RequestParam Long restaurantId,@RequestParam Long categoryId,@RequestBody Dish dish){

        Dish addedDish = dishRepository.save(dish);
        Category foundCategory = categoryRepository.findById(categoryId).get();
        foundCategory.getDishes().add(addedDish);
        categoryRepository.save(foundCategory);

        Restaurant foundRestaurant = restaurantRepo.findById(restaurantId).get();
        foundCategory.getDishes().add(addedDish);
        restaurantRepo.save(foundRestaurant);


        ReturnData returnData = new ReturnData();
        returnData.setCode(0);
        returnData.setMessage("Item added successfully");
        returnData.setObject(addedDish);
        return returnData;
    }

    //Return list of all restaurant, to be removed in production
    @GetMapping("/displayAll")
    public List<Restaurant> dislayAllRestaurant() {
        return restaurantRepo.findAll();
    }


}
