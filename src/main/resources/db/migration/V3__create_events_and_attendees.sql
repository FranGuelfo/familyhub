CREATE TABLE IF NOT EXISTS events (
                                      id UUID PRIMARY KEY,
                                      title VARCHAR(255) NOT NULL,
    description TEXT,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
                           family_id UUID NOT NULL
                           );

CREATE TABLE IF NOT EXISTS event_attendees (
                                               event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    member_id UUID NOT NULL REFERENCES members(id) ON DELETE CASCADE,
    PRIMARY KEY (event_id, member_id)
    );