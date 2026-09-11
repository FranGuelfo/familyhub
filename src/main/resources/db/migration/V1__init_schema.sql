-- 1. Familias
CREATE TABLE families (
                          id UUID PRIMARY KEY,
                          name VARCHAR(100) NOT NULL
);

-- 2. Miembros
CREATE TABLE members (
                         id UUID PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         email VARCHAR(150) NOT NULL UNIQUE,
                         role VARCHAR(20) NOT NULL
);

-- 3. Relación N:M Familias - Miembros
CREATE TABLE family_members (
                                family_id UUID NOT NULL REFERENCES families(id) ON DELETE CASCADE,
                                member_id UUID NOT NULL REFERENCES members(id) ON DELETE CASCADE,
                                PRIMARY KEY (family_id, member_id)
);

-- 4. Tareas
CREATE TABLE tasks (
                       id UUID PRIMARY KEY,
                       title VARCHAR(200) NOT NULL,
                       description TEXT,
                       status VARCHAR(20) NOT NULL,
                       priority VARCHAR(20) NOT NULL,
                       assigned_to UUID REFERENCES members(id) ON DELETE SET NULL,
                       due_date DATE
);

-- 5. Vehículos
CREATE TABLE vehicles (
                          id UUID PRIMARY KEY,
                          family_id UUID NOT NULL REFERENCES families(id) ON DELETE CASCADE,
                          brand VARCHAR(50) NOT NULL,
                          model VARCHAR(50) NOT NULL,
                          license_plate VARCHAR(20) NOT NULL UNIQUE,
                          status VARCHAR(20) NOT NULL,
                          assigned_member_id UUID REFERENCES members(id) ON DELETE SET NULL
);

-- 6. Plazas de Garaje
CREATE TABLE parking_spots (
                               id UUID PRIMARY KEY,
                               family_id UUID NOT NULL REFERENCES families(id) ON DELETE CASCADE,
                               spot_number VARCHAR(20) NOT NULL,
                               has_ev_charger BOOLEAN NOT NULL DEFAULT FALSE,
                               status_type VARCHAR(20) NOT NULL,
                               parked_vehicle_id UUID REFERENCES vehicles(id) ON DELETE SET NULL,
                               out_of_service_reason VARCHAR(255)
);

-- 7. Eventos de Calendario
CREATE TABLE events (
                        id UUID PRIMARY KEY,
                        family_id UUID NOT NULL REFERENCES families(id) ON DELETE CASCADE,
                        title VARCHAR(150) NOT NULL,
                        description TEXT,
                        start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                        end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                        category VARCHAR(30) NOT NULL,
                        location VARCHAR(200)
);

-- 8. Asistentes a Eventos
CREATE TABLE event_attendees (
                                 event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
                                 member_id UUID NOT NULL,
                                 PRIMARY KEY (event_id, member_id)
);