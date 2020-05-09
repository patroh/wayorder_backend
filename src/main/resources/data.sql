INSERT INTO USER (fullname,email,password) VALUES ( 'Rohan Patel','rohan@gmail.com','MTIzNA==' );
INSERT INTO MENU (Id) VALUES (1);
INSERT INTO RESTAURANT (name, unit, street, city, postal, phone,menu_id)
values ('La Mexicana', 55, 'Navigator Dr', 'Mississauga', 'L5W1P5', '6665656565',1);


INSERT INTO DISH (name, image, star, reviews, likes, price)
values ('Chicken Tandoori',
        'https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F9%2F2014%2F04%2Foriginal-201309-HD-chicken-dishes-nomad-new-york-city.jpg',
        3.5,
        257,
        412,
        19.24);

INSERT INTO DISH (name, image, star, reviews, likes, price)
values ('Paneer Pizza',
        'https://i2.wp.com/www.vegrecipesofindia.com/wp-content/uploads/2018/05/paneer-pizza-recipe-1-500x375.jpg',
        4.5,
        757,
        12,
        10.00);

INSERT INTO DISH (name, image, star, reviews, likes, price)
values ('Paneer Tikka', 'https://myfancypantry.files.wordpress.com/2013/01/restraunt-style-paneer-tikka-masala.jpg',
        4.5,
        757,
        12,
        15.45);

INSERT INTO DISH (name, image, star, reviews, likes, price)
values ('Mix Veg', 'https://sweetspotnutrition.ca/wp-content/uploads/2019/01/eat-variety-healthy-foods-image-copy.jpg',
        4.5,
        757,
        12,
        9.99);

INSERT INTO DISH (name, image, star, reviews, likes, price)
values ('Seekh Kebab', 'https://i.pinimg.com/originals/87/5c/fd/875cfd391f8dfc4d20fc243b884330b6.jpg',
        4.5,
        757,
        12,
        12.00);

INSERT INTO DISH (name, image, star, reviews, likes, price)
values ('Dal Tadka',
        'https://previews.123rf.com/images/timolina/timolina1902/timolina190200229/116755008-indian-dal-traditional-indian-soup-lentils-indian-dhal-spicy-curry-in-bowl-spices-herbs-rustic-black.jpg',
        4.5,
        757,
        12,
        14.22);

INSERT INTO DISH (name, image, star, reviews, likes, price)
values ('Lasagnia',
        'https://www.connoisseurusveg.com/wp-content/uploads/2019/06/vegan-lasagna-6-of-7.jpg',
        4.5,
        757,
        12,
        10.5);

INSERT INTO DISH (name, image, star, reviews, likes, price)
values ('Alfredo Pasta',
        'https://d3hne3c382ip58.cloudfront.net/files/uploads/bookmundi/resized/cmsfeatured/pasta-1509527885-785X440.jpg',
        4.5,
        757,
        12,
        8.5);


INSERT INTO RESTAURANT_DISHES
values (1, 1);
INSERT INTO RESTAURANT_DISHES
values (1, 2);
INSERT INTO RESTAURANT_DISHES
values (1, 3);
INSERT INTO RESTAURANT_DISHES
values (1, 4);
INSERT INTO RESTAURANT_DISHES
values (1, 5);
INSERT INTO RESTAURANT_DISHES
values (1, 6);
INSERT INTO RESTAURANT_DISHES
values (1, 7);
INSERT INTO RESTAURANT_DISHES
values (1, 8);
