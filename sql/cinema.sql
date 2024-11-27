-- Crear la base de datos cinema
CREATE DATABASE IF NOT EXISTS cinema;
USE cinema;

-- Tabla movies
CREATE TABLE movies (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    duration INT NOT NULL, -- Duración en minutos
    genre VARCHAR(50),
    rating VARCHAR(10),
    description TEXT
);

-- Tabla rooms
CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_number INT NOT NULL UNIQUE,
    total_seats INT NOT NULL
);

-- Tabla functions
CREATE TABLE functions (
    function_id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT NOT NULL,
    room_id INT NOT NULL,
    function_datetime DATETIME NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

-- Tabla reservations
CREATE TABLE reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    function_id INT NOT NULL,
    seat_number INT NOT NULL,
    client_name VARCHAR(100) NOT NULL,
    client_age INT NOT NULL,
    reservation_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (function_id) REFERENCES functions(function_id),
    CONSTRAINT unique_seat_per_function UNIQUE (function_id, seat_number)
);

-- Insertar datos en la tabla movies
INSERT INTO movies (title, duration, genre, rating, description) VALUES
('The Great Adventure', 120, 'Action', 'PG-13', 'An epic action adventure.'),
('Romantic Escape', 95, 'Romance', 'PG', 'A tale of love and discovery.'),
('Mystery Manor', 110, 'Mystery', 'PG-13', 'Unravel the secrets of the manor.'),
('Comedy Nights', 100, 'Comedy', 'PG', 'Laughs guaranteed.'),
('Space Odyssey', 130, 'Sci-Fi', 'PG-13', 'Journey through the stars.'),
('Horror House', 105, 'Horror', 'R', 'Not for the faint-hearted.'),
('Animated Dreams', 90, 'Animation', 'G', 'Fun for the whole family.'),
('Historical Battles', 150, 'History', 'PG-13', 'Epic historical reenactments.'),
('Documentary World', 80, 'Documentary', 'G', 'Learn about our planet.'),
('Fantasy Realms', 115, 'Fantasy', 'PG', 'Explore magical worlds.');

-- Insertar datos en la tabla rooms
INSERT INTO rooms (room_number, total_seats) VALUES
(1, 10),
(2, 12),
(3, 15),
(4, 10),
(5, 8);

-- Insertar datos en la tabla functions
INSERT INTO functions (movie_id, room_id, function_datetime) VALUES
(1, 1, '2024-12-01 18:00:00'),
(2, 2, '2024-12-01 20:00:00'),
(3, 3, '2024-12-02 16:30:00'),
(4, 4, '2024-12-02 19:00:00'),
(5, 5, '2024-12-03 21:00:00'),
(6, 1, '2024-12-03 22:30:00'),
(7, 2, '2024-12-04 14:00:00'),
(8, 3, '2024-12-04 17:00:00'),
(9, 4, '2024-12-05 19:30:00'),
(10, 5, '2024-12-05 20:45:00');
