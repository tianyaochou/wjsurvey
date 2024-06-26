create table survey(
    id bigserial,
    name varchar,
    content jsonb
);
CREATE SEQUENCE survey_seq START WITH 1 INCREMENT BY 1;
