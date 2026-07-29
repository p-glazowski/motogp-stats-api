-- Ducati Lenovo Team
INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Francesco',
    'Bagnaia',
    'Francesco Bagnaia',
    'Italy',
    29,
    63,
    t.id,
    25,
    45,
    18,
    2,
    'Two-time MotoGP World Champion. Known for his incredible racing intelligence and consistency.',
    'https://www.motogp.com/images/riders/bagnaia.jpg'
FROM teams t WHERE t.name = 'Ducati Lenovo Team';

INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Marc',
    'Marquez',
    'Marc Marquez',
    'Spain',
    33,
    93,
    t.id,
    64,
    101,
    64,
    6,
    'Six-time MotoGP World Champion. One of the greatest riders in MotoGP history.',
    'https://www.motogp.com/images/riders/marquez.jpg'
FROM teams t WHERE t.name = 'Ducati Lenovo Team';

-- Aprilia Racing
INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Jorge',
    'Martin',
    'Jorge Martin',
    'Spain',
    27,
    89,
    t.id,
    3,
    9,
    5,
    1,
    'Known for his incredible qualifying pace and race craft.',
    'https://www.motogp.com/images/riders/martin.jpg'
FROM teams t WHERE t.name = 'Aprilia Racing';

INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Marco',
    'Bezzecchi',
    'Marco Bezzecchi',
    'Italy',
    27,
    72,
    t.id,
    1,
    5,
    1,
    0,
    'A rising star known for his smooth riding style and race-winning potential.',
    'https://www.motogp.com/images/riders/bezzecchi.jpg'
FROM teams t WHERE t.name = 'Aprilia Racing';

-- Monster Energy Yamaha MotoGP
INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Fabio',
    'Quartararo',
    'Fabio Quartararo',
    'France',
    26,
    20,
    t.id,
    11,
    23,
    16,
    1,
    '2021 MotoGP World Champion. Known for his aggressive riding style.',
    'https://www.motogp.com/images/riders/quartararo.jpg'
FROM teams t WHERE t.name = 'Monster Energy Yamaha MotoGP';

INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Alex',
    'Rins',
    'Alex Rins',
    'Spain',
    30,
    42,
    t.id,
    5,
    12,
    4,
    0,
    'Known for his smooth riding style and ability to perform in wet conditions.',
    'https://www.motogp.com/images/riders/rins.jpg'
FROM teams t WHERE t.name = 'Monster Energy Yamaha MotoGP';

-- Red Bull KTM Factory Racing
INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Brad',
    'Binder',
    'Brad Binder',
    'South Africa',
    29,
    33,
    t.id,
    5,
    14,
    3,
    0,
    'Known for his aggressive racing style and incredible overtaking abilities.',
    'https://www.motogp.com/images/riders/binder.jpg'
FROM teams t WHERE t.name = 'Red Bull KTM Factory Racing';

INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Pedro',
    'Acosta',
    'Pedro Acosta',
    'Spain',
    22,
    37,
    t.id,
    0,
    0,
    0,
    0,
    'One of the most exciting young talents. Moto2 World Champion.',
    'https://www.motogp.com/images/riders/acosta.jpg'
FROM teams t WHERE t.name = 'Red Bull KTM Factory Racing';

-- Honda HRC Castrol
INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Joan',
    'Mir',
    'Joan Mir',
    'Spain',
    27,
    36,
    t.id,
    2,
    8,
    0,
    1,
    '2020 MotoGP World Champion. Consistent rider known for his smooth and calculated approach.',
    'https://www.motogp.com/images/riders/mir.jpg'
FROM teams t WHERE t.name = 'Honda HRC Castrol';

INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Luca',
    'Marini',
    'Luca Marini',
    'Italy',
    27,
    10,
    t.id,
    1,
    3,
    1,
    0,
    'Half-brother of Valentino Rossi. Known for his consistent performances and race craft.',
    'https://www.motogp.com/images/riders/marini.jpg'
FROM teams t WHERE t.name = 'Honda HRC Castrol';

-- Prima Pramac Yamaha MotoGP
INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Jack',
    'Miller',
    'Jack Miller',
    'Australia',
    31,
    43,
    t.id,
    4,
    12,
    2,
    0,
    'Known for his fun personality and aggressive riding style. Multiple race winner.',
    'https://www.motogp.com/images/riders/miller.jpg'
FROM teams t WHERE t.name = 'Prima Pramac Yamaha MotoGP';

INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Toprak',
    'Razgatlioglu',
    'Toprak Razgatlioglu',
    'Turkey',
    29,
    7,
    t.id,
    0,
    0,
    0,
    0,
    'Two-time WorldSBK Champion making his MotoGP debut. Known for spectacular overtakes.',
    'https://www.motogp.com/images/riders/razgatlioglu.jpg'
FROM teams t WHERE t.name = 'Prima Pramac Yamaha MotoGP';

-- Pertamina Enduro VR46 Racing Team
INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Fabio',
    'Di Giannantonio',
    'Fabio Di Giannantonio',
    'Italy',
    27,
    49,
    t.id,
    0,
    2,
    0,
    0,
    'Showing great promise with consistent top-10 finishes.',
    'https://www.motogp.com/images/riders/digiannantonio.jpg'
FROM teams t WHERE t.name = 'Pertamina Enduro VR46 Racing Team';

INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Franco',
    'Morbidelli',
    'Franco Morbidelli',
    'Italy',
    31,
    21,
    t.id,
    3,
    7,
    1,
    0,
    'MotoGP race winner and a consistent performer. Known for his smooth style.',
    'https://www.motogp.com/images/riders/morbidelli.jpg'
FROM teams t WHERE t.name = 'Pertamina Enduro VR46 Racing Team';

-- BK8 Gresini Racing MotoGP
INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Alex',
    'Marquez',
    'Alex Marquez',
    'Spain',
    30,
    73,
    t.id,
    1,
    4,
    0,
    0,
    'Moto2 and Moto3 World Champion. Consistent podium finisher in MotoGP.',
    'https://www.motogp.com/images/riders/alexmarquez.jpg'
FROM teams t WHERE t.name = 'BK8 Gresini Racing MotoGP';

INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Fermin',
    'Aldeguer',
    'Fermin Aldeguer',
    'Spain',
    21,
    54,
    t.id,
    0,
    0,
    0,
    0,
    'Young Spanish talent making his way in MotoGP.',
    'https://www.motogp.com/images/riders/aldeguer.jpg'
FROM teams t WHERE t.name = 'BK8 Gresini Racing MotoGP';

-- Trackhouse Racing
INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Raul',
    'Fernandez',
    'Raul Fernandez',
    'Spain',
    25,
    25,
    t.id,
    0,
    0,
    0,
    0,
    'Young Spanish rider with impressive Moto2 pedigree.',
    'https://www.motogp.com/images/riders/raulfernandez.jpg'
FROM teams t WHERE t.name = 'Trackhouse Racing';

INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Ai',
    'Ogura',
    'Ai Ogura',
    'Japan',
    25,
    79,
    t.id,
    0,
    0,
    0,
    0,
    'Japanese talent making his debut in MotoGP.',
    'https://www.motogp.com/images/riders/ogura.jpg'
FROM teams t WHERE t.name = 'Trackhouse Racing';

-- LCR Honda
INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Johann',
    'Zarco',
    'Johann Zarco',
    'France',
    36,
    5,
    t.id,
    1,
    5,
    2,
    0,
    'Experienced French rider with incredible front-row qualifying pace.',
    'https://www.motogp.com/images/riders/zarco.jpg'
FROM teams t WHERE t.name = 'LCR Honda';

INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Diogo',
    'Moreira',
    'Diogo Moreira',
    'Brazil',
    22,
    11,
    t.id,
    0,
    0,
    0,
    0,
    'Moto2 World Champion making his MotoGP debut for LCR Honda.',
    'https://www.motogp.com/images/riders/moreira.jpg'
FROM teams t WHERE t.name = 'LCR Honda';

-- Red Bull KTM Tech3
INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Maverick',
    'Vinales',
    'Maverick Vinales',
    'Spain',
    31,
    12,
    t.id,
    9,
    19,
    7,
    0,
    'Multiple race winner. Known for his incredible speed on his day.',
    'https://www.motogp.com/images/riders/vinales.jpg'
FROM teams t WHERE t.name = 'Red Bull KTM Tech3';

INSERT INTO riders (first_name, last_name, full_name, nationality, age, race_number, team_id, wins, podiums, pole_positions, world_titles, biography, image_url)
SELECT
    'Enea',
    'Bastianini',
    'Enea Bastianini',
    'Italy',
    28,
    23,
    t.id,
    3,
    8,
    2,
    0,
    'Known as "The Beast" for his aggressive riding style and incredible last-lap performances.',
    'https://www.motogp.com/images/riders/bastianini.jpg'
FROM teams t WHERE t.name = 'Red Bull KTM Tech3';