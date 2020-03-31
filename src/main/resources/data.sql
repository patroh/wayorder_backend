INSERT INTO RESTAURANT (name,unit,street,city,postal,phone) values ('Baba Dhaba',55,'Navigator Dr','Mississauga','L5W1P5','6665656565');

INSERT INTO DISH (name,price,star,reviews,likes,image) values ( 'Dal Tadka',14.22,3.5,120,200,'' );
INSERT INTO DISH (name,image,star,reviews,likes,price) values ('Chicken Tandoori','https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F9%2F2014%2F04%2Foriginal-201309-HD-chicken-dishes-nomad-new-york-city.jpg',
                                                               3.5,
                                                               257,
                                                               412,
                                                               19.24);
INSERT INTO RESTAURANT_DISHES values (1,1);
INSERT INTO RESTAURANT_DISHES values (1,2);
