DO
$$
DECLARE
	auth_id text;
    ap_ids text[];
    ap_id text;
BEGIN

    SELECT array_agg(id)
    FROM access_point
    WHERE id NOT IN (
        SELECT access_point_fk
        FROM authorisation
    )
    INTO ap_ids;

    FOREACH ap_id IN ARRAY ap_ids
    LOOP
        SELECT gen_random_uuid() INTO auth_id;

        RAISE NOTICE 'access_point_id: % id: %', ap_id, auth_id;

        INSERT INTO public.authorisation(id, created_at, created_by, access_point_fk)
        VALUES(auth_id, now(), 'liquibase', ap_id);

        INSERT INTO public.authorisation_roles( authorisation_id, roles)
        VALUES(auth_id, 'DEVELOPER');
    END LOOP;
END;
$$  LANGUAGE PLPGSQL;