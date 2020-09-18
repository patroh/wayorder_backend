package com.capstone.project.Controller;

import com.capstone.project.Bean.*;
import com.capstone.project.Bean.Holders.Restaurant_User_Holder;
import com.capstone.project.Bean.Holders.ReturnData;
import com.capstone.project.Repo.*;
import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{id}/menu/item", consumes = "application/json")
    public ReturnData addMenuItem(@PathVariable("id") Long restaurantId,@RequestParam Long categoryId,@RequestBody Dish dish){

        Dish addedDish = dishRepository.save(dish);
        Category foundCategory = categoryRepository.findById(categoryId).get();
        foundCategory.getDishes().add(addedDish);
        categoryRepository.save(foundCategory);

        Restaurant foundRestaurant = restaurantRepo.findById(restaurantId).get();
        foundRestaurant.getDishes().add(addedDish);
        restaurantRepo.save(foundRestaurant);

        ReturnData returnData = new ReturnData();
        returnData.setCode(0);
        returnData.setMessage("Item added successfully");
        returnData.setObject(addedDish);
        return returnData;
    }


    //Edit menu item
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/{id}/menu/item",consumes = "application/json")
    public ReturnData editMenuItem(@PathVariable("id") Long restaurantId,@RequestParam Long fromCategoryId,@RequestParam Long categoryId,@RequestBody Dish dish){

        Dish foundDish = dishRepository.findById(dish.getId()).get();
        foundDish.setName(dish.getName());
        foundDish.setDescription(dish.getDescription());
        foundDish.setPrice(dish.getPrice());
        foundDish.setTax(dish.getTax());
        foundDish.setImage(dish.getImage());

        Dish editedDish = dishRepository.save(foundDish);
        if(fromCategoryId != categoryId) {
            // Edit the item properties and also change the category

            // Delete the item reference from the current category
            Category foundCategory  = categoryRepository.findById(fromCategoryId).get();
            List<Dish> foundDishesInCategory = foundCategory.getDishes();

            for(int i=0 ; i<foundDishesInCategory.size() ; i++){
                if(foundDishesInCategory.get(i).getId() == editedDish.getId()){
                    foundDishesInCategory.remove(i);
                    break;
                }
            }

            foundCategory.setDishes(foundDishesInCategory);
            categoryRepository.save(foundCategory);


            // Add the reference of the item to new category
            foundCategory = categoryRepository.findById(categoryId).get();
            foundCategory.getDishes().add(editedDish);
            categoryRepository.save(foundCategory);

        }
        ReturnData returnData = new ReturnData();
        returnData.setCode(0);
        returnData.setMessage("Item edited successfully");
        returnData.setObject(editedDish);
        return returnData;
    }
    //Delete menu item
    @CrossOrigin(origins = "*")
    @DeleteMapping(value = "/{id}/menu/item")
    public ReturnData deleteMenuItem(@PathVariable("id") Long restaurantId,@RequestParam Long categoryId,@RequestParam Long dishId){

        Category foundCategory  = categoryRepository.findById(categoryId).get();
        List<Dish> foundDishesInCategory = foundCategory.getDishes();

        for(int i=0 ; i<foundDishesInCategory.size() ; i++){
            if(foundDishesInCategory.get(i).getId() == dishId){
                foundDishesInCategory.remove(i);
                break;
            }
        }

        foundCategory.setDishes(foundDishesInCategory);
        categoryRepository.save(foundCategory);


        ReturnData returnData = new ReturnData();
        returnData.setCode(0);
        returnData.setMessage("Item deleted successfully");
        return  returnData;
    }

    //Get menu of the restaurant
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/{id}/menu")
    public ReturnData getRestaurantMenu(@PathVariable("id") Long restaurantId){

        ReturnData returnData = new ReturnData();
        returnData.setMessage("Menu fetched successfully");
        returnData.setCode(0);
        returnData.setObject(restaurantRepo.findById(restaurantId).get().getMenu());
        return returnData;
    }

    //Upload restaurant logo
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{id}/logo")
    public ReturnData uploadRestaurantLogo(@PathVariable("id") Long restaurantId,@RequestParam String logoUrl,@RequestParam String token){
        logoUrl+="&token="+token;
        ReturnData returnData = new ReturnData();
        Restaurant foundRestaurant = restaurantRepo.findById(restaurantId).get();
        foundRestaurant.setLogo(logoUrl);
        Restaurant savedRestaurant = restaurantRepo.save(foundRestaurant);
        returnData.setMessage("Logo Uploaded Successfully");
        returnData.setCode(0);
        returnData.setObject(savedRestaurant);
        return returnData;
    }

    //Upload restaurant back image
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{id}/logo")
    public ReturnData uploadRestaurantBackImage(@PathVariable("id") Long restaurantId,@RequestParam String imageUrl,@RequestParam String token){
        imageUrl+="&token="+token;
        ReturnData returnData = new ReturnData();
        Restaurant foundRestaurant = restaurantRepo.findById(restaurantId).get();
        foundRestaurant.setBackImage(imageUrl);
        Restaurant savedRestaurant = restaurantRepo.save(foundRestaurant);
        returnData.setMessage("Back Image Uploaded Successfully");
        returnData.setCode(0);
        returnData.setObject(savedRestaurant);
        return returnData;
    }

    //Return list of all restaurant, to be removed in production
    @GetMapping("/displayAll")
    public List<Restaurant> dislayAllRestaurant() {
        return restaurantRepo.findAll();
    }


}
