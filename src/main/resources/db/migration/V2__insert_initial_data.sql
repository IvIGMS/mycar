-- Table Status
INSERT INTO public.status (id, status_name)
SELECT * FROM (VALUES
    (0, 'status.finish'),
    (1, 'status.inProgress')
) AS new_status(id, status_name)
WHERE NOT EXISTS (
    SELECT 1
    FROM public.status
    LIMIT 1
);


-- Table Types
INSERT INTO public.types (id, type_name) 
SELECT * FROM (VALUES
    (1, 'type.byDistance'),
    (2, 'type.byDate')
) AS new_types(id, type_name)
WHERE NOT EXISTS (
    SELECT 1
    FROM public.types
    LIMIT 1
);

-- Table Users
INSERT INTO public.users (id, username, email, "password", first_name, last_name, is_active)
VALUES
(1, 'user_test','user_test@mycar.com','$2a$10$r4ynr6IZiZiwYS05hAhdgeqeP1rXmGKmRZyBKel52HJlS6fnj26Ee','user','test', true);

-- Table cars
INSERT INTO public.cars (id, company_name, model_name, user_id, km, is_active)
VALUES
(1, 'Seat','Leon',1,20000,true);

-- Table issues
--Type 1: kms
INSERT INTO public.issues
(current_distance, name, notification_distance, type_id , car_id, status_id)
VALUES
(0, 'Cambio de aceite', 10000, 1, 1, 1),
(0, 'Cambio correa distribución', 120000, 1, 1, 0);

--Type 2: date
INSERT INTO public.issues
(name, notification_date, type_id, car_id, status_id)
VALUES
('Cambio filtro del habitáculo', NOW() + INTERVAL '365 DAYS', 2, 1, 1),
('Revisar presión de ruedas', NOW() + INTERVAL '30 DAYS', 2, 1, 1);
