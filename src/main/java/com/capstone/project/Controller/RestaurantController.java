package com.capstone.project.Controller;

import com.capstone.project.Bean.*;
import com.capstone.project.Bean.Holders.Restaurant_User_Holder;
import com.capstone.project.Bean.Holders.ReturnData;
import com.capstone.project.Repo.*;
import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/restaurant")
@AllArgsConstructor
public class RestaurantController {

    private final RestaurantRepository restaurantRepo;
    private final RestaurantUserRepository restaurantUserRepository;
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotForDayRepository timeSlotForDayRepository;


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
                resObj.setBusinessHours(generateDefaultBusinessHours());
                resObj.setBookingTimeSlots(generateDefaultTimeSlots());
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
    public ReturnData addMenuItem(@PathVariable("id") Long restaurantId, @RequestParam Long categoryId, @RequestBody Dish dish) {

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
    @PostMapping(value = "/{id}/menu/item", consumes = "application/json")
    public ReturnData editMenuItem(@PathVariable("id") Long restaurantId, @RequestParam Long fromCategoryId, @RequestParam Long categoryId, @RequestBody Dish dish) {

        Dish foundDish = dishRepository.findById(dish.getId()).get();
        foundDish.setName(dish.getName());
        foundDish.setDescription(dish.getDescription());
        foundDish.setPrice(dish.getPrice());
        foundDish.setTax(dish.getTax());
        foundDish.setImage(dish.getImage());

        Dish editedDish = dishRepository.save(foundDish);
        if (fromCategoryId != categoryId) {
            // Edit the item properties and also change the category

            // Delete the item reference from the current category
            Category foundCategory = categoryRepository.findById(fromCategoryId).get();
            List<Dish> foundDishesInCategory = foundCategory.getDishes();

            for (int i = 0; i < foundDishesInCategory.size(); i++) {
                if (foundDishesInCategory.get(i).getId() == editedDish.getId()) {
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
    public ReturnData deleteMenuItem(@PathVariable("id") Long restaurantId, @RequestParam Long categoryId, @RequestParam Long dishId) {

        Category foundCategory = categoryRepository.findById(categoryId).get();
        List<Dish> foundDishesInCategory = foundCategory.getDishes();

        for (int i = 0; i < foundDishesInCategory.size(); i++) {
            if (foundDishesInCategory.get(i).getId() == dishId) {
                foundDishesInCategory.remove(i);
                break;
            }
        }

        foundCategory.setDishes(foundDishesInCategory);
        categoryRepository.save(foundCategory);


        ReturnData returnData = new ReturnData();
        returnData.setCode(0);
        returnData.setMessage("Item deleted successfully");
        return returnData;
    }

    //Get menu of the restaurant
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/{id}/menu")
    public ReturnData getRestaurantMenu(@PathVariable("id") Long restaurantId) {

        ReturnData returnData = new ReturnData();
        returnData.setMessage("Menu fetched successfully");
        returnData.setCode(0);
        returnData.setObject(restaurantRepo.findById(restaurantId).get().getMenu());
        return returnData;
    }

    //Upload restaurant logo
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{id}/logo")
    public ReturnData uploadRestaurantLogo(@PathVariable("id") Long restaurantId, @RequestParam String logoUrl, @RequestParam String token) {
        logoUrl += "&token=" + token;
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
    @PutMapping(value = "/{id}/bgImg")
    public ReturnData uploadRestaurantBackImage(@PathVariable("id") Long restaurantId, @RequestParam String bgImgUrl, @RequestParam String token) {
        bgImgUrl += "&token=" + token;
        ReturnData returnData = new ReturnData();
        Restaurant foundRestaurant = restaurantRepo.findById(restaurantId).get();
        foundRestaurant.setBgImg(bgImgUrl);
        Restaurant savedRestaurant = restaurantRepo.save(foundRestaurant);
        returnData.setMessage("Back Image Uploaded Successfully");
        returnData.setCode(0);
        returnData.setObject(savedRestaurant);
        return returnData;
    }

    //Modify isDineIn paramater of restaurant
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{id}/isDineIn/{isDineIn}", consumes = "application/json")
    public ReturnData modifyIsDineIn(@PathVariable("id") Long restaurantId, @RequestParam boolean isDineIn) {
        ReturnData returnData = new ReturnData();
        Restaurant foundRestaurant = restaurantRepo.findById(restaurantId).get();
        foundRestaurant.setDineIn(isDineIn);
        restaurantRepo.save(foundRestaurant);
        returnData.setMessage("Restaurant can now accept Dine In Orders");
        returnData.setCode(0);
        return returnData;
    }

    //Modify isTakeout paramater of restaurant
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{id}/isTakeout/{isTakeout}", consumes = "application/json")
    public ReturnData modifyIsTakeout(@PathVariable("id") Long restaurantId, @RequestParam boolean isTakeout) {
        ReturnData returnData = new ReturnData();
        Restaurant foundRestaurant = restaurantRepo.findById(restaurantId).get();
        foundRestaurant.setDineIn(isTakeout);
        restaurantRepo.save(foundRestaurant);
        returnData.setMessage("Restaurant can now accept Takeout Orders");
        returnData.setCode(0);
        return returnData;
    }

    //Return list of all restaurant, to be removed in production
    @GetMapping("/displayAll")
    public List<Restaurant> dislayAllRestaurant() {
        return restaurantRepo.findAll();
    }

    //Edit Restaurant business hours
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{id}/businessHours")
    public ReturnData editBusinessHours(@PathVariable("id") Long restaurantId, @RequestBody List<TimeSlotForDay> timeSlotsForDay) {
        ReturnData returnData = new ReturnData();

        timeSlotForDayRepository.saveAll(timeSlotsForDay);

        return returnData;
    }

    // Generate default hours for restaurant NOON to MIDNIGHT
    private List<BusinessHours> generateDefaultBusinessHours() {
        List<BusinessHours> defaultHours = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            BusinessHours newTime = BusinessHours.builder().dayOfWeek(DayOfWeek.values()[i])
                    .startTime(LocalTime.NOON).endTime(LocalTime.MIDNIGHT.minusMinutes(2)).build();
            defaultHours.add(newTime);
        }
        return businessHoursRepository.saveAll(defaultHours);
    }

    ;

    // Generate default timeSlots for the restaurant
    private List<TimeSlotForDay> generateDefaultTimeSlots() {
        List<TimeSlotForDay> defaultTimeSlots = new ArrayList<>();
        final LocalTime startTime = LocalTime.NOON;
        final LocalTime endTime = LocalTime.MIDNIGHT.minusMinutes(2);

        for (int i = 0; i < 7; i++) {
            List<TimeSlot> listOfSlotForTheDay = new ArrayList<>();
            LocalTime startTimeCpy = startTime.plusMinutes(45);
            LocalTime endTimeCpy = startTimeCpy.plusMinutes(45);
            while (true) {
                if (endTimeCpy.isBefore(endTime)) {
                    TimeSlot newSlot = TimeSlot.builder().time(startTimeCpy).build();
                    listOfSlotForTheDay.add(newSlot);
                    startTimeCpy = startTimeCpy.plusMinutes(45);
                    endTimeCpy = startTimeCpy.plusMinutes(45);
                } else {
                /* Current time slot will end after the restaurant is closed so we
                 cannot considered this time slot */
                    break;
                }
            }
            TimeSlotForDay timeSlotForDay = new TimeSlotForDay().builder().timeSlots(
                    timeSlotRepository.saveAll(listOfSlotForTheDay)
            )
                    .dayOfWeek(DayOfWeek.values()[i]).build();
            defaultTimeSlots.add(timeSlotForDay);
        }

        return timeSlotForDayRepository.saveAll(defaultTimeSlots);
    }

    ;
}
