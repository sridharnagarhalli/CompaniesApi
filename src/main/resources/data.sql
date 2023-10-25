INSERT INTO ADDRESSES(address_id, premises, address_line1, locality, country, postal_code)
VALUES (101, '5', 'Cranford Close', 'London', 'England', 'SW20 0DP');

INSERT INTO COMPANIES(ID, REGISTRATION_NUMBER, TITLE, COMPANY_TYPE, DATE_OF_CREATION, COMPANY_STATUS, ADDRESS_ID)
VALUES(201, '201', 'BBC LIMITED', 'LTD', '2008-02-11', 'active', 101);

INSERT INTO OFFICERS (officer_id, name, officer_role, appointed_on, resigned_on, address_id, company_id)
VALUES(301, 'RE SECRETARIES LIMITED', 'corporate-secretary', '2008-02-12', null, 101, 201);


