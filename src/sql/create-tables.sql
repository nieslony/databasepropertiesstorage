/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  claas
 * Created: 08.02.2017
 */

-- CREATE SCHEMA IF NOT EXISTS public;;

CREATE TABLE propertyGroups (
    id SERIAL NOT NULL PRIMARY KEY,
    name text NOT NULL UNIQUE
);;
CREATE INDEX ON propertygroups (name);;

CREATE TABLE properties (
    group_id SERIAL references propertyGroups(id),
    name varchar NOT NULL UNIQUE,
    value varchar,
    UNIQUE (group_id, name)
);;


CREATE OR REPLACE FUNCTION set_property(_group_id int, _name varchar, _value varchar) RETURNS VOID AS $$ 
    DECLARE 
    BEGIN 
        UPDATE properties SET value = _value WHERE group_id = _group_id AND name = _name;
        IF NOT FOUND THEN 
            INSERT INTO properties values(_group_id, _name, _value); 
        END IF; 
    END; 
    $$ LANGUAGE 'plpgsql';;


