--
-- PostgreSQL database dump
--

\restrict p7thb6msfwlFMJihJxaeKV7HJ9pmxN9wczvCxusMYbc3KnSbb7xtMefkpFsCLrq

-- Dumped from database version 18.6 (Debian 18.6-1.pgdg13+2)
-- Dumped by pg_dump version 18.6 (Debian 18.6-1.pgdg13+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


--
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


--
-- Name: sector; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sector (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    parent_id integer,
    sort_order integer NOT NULL
);


--
-- Name: user_profile; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_profile (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    agreed_to_terms boolean NOT NULL,
    CONSTRAINT chk_user_profile_agreed_to_terms CHECK (agreed_to_terms)
);


--
-- Name: user_profile_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.user_profile ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_profile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: user_profile_sector; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_profile_sector (
    user_profile_id bigint NOT NULL,
    sector_id integer NOT NULL
);


--
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) FROM stdin;
001-create-sector	richard	db/changelog/changes/001-create-sector.sql	2026-09-24 20:35:17.752797	1	EXECUTED	9:99525f4ff230cf2eba4f7aa01c170f9d	sql		\N	5.0.3	\N	\N	0271317169
002-seed-sectors	richard	db/changelog/changes/002-seed-sectors.sql	2026-09-24 20:35:17.764053	2	EXECUTED	9:c70479b8e4fe3328bc43fd14bb60e5aa	sql		\N	5.0.3	\N	\N	0271317169
003-create-user-profile	richard	db/changelog/changes/003-create-user-profile.sql	2026-09-24 20:56:33.190099	3	EXECUTED	9:42f6664629207d014d441b31af2bd9bc	sql		\N	5.0.3	\N	\N	0272592529
004-create-user-profile-sector	richard	db/changelog/changes/004-create-user-profile-sector.sql	2026-09-24 20:56:33.196775	4	EXECUTED	9:e5b6ad92e545f917e066b6bf8abdfb51	sql		\N	5.0.3	\N	\N	0272592529
\.


--
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.databasechangeloglock (id, locked, lockgranted, lockedby) FROM stdin;
1	f	\N	\N
\.


--
-- Data for Name: sector; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.sector (id, name, parent_id, sort_order) FROM stdin;
1	Manufacturing	\N	1
19	Construction materials	1	2
18	Electronics and Optics	1	3
6	Food and Beverage	1	4
342	Bakery & confectionery products	6	5
43	Beverages	6	6
42	Fish & fish products	6	7
40	Meat & meat products	6	8
39	Milk & dairy products	6	9
437	Other	6	10
378	Sweets & snack food	6	11
13	Furniture	1	12
389	Bathroom/sauna	13	13
385	Bedroom	13	14
390	Children’s room	13	15
98	Kitchen	13	16
101	Living room	13	17
392	Office	13	18
394	Other (Furniture)	13	19
341	Outdoor	13	20
99	Project furniture	13	21
12	Machinery	1	22
94	Machinery components	12	23
91	Machinery equipment/tools	12	24
224	Manufacture of machinery	12	25
97	Maritime	12	26
271	Aluminium and steel workboats	97	27
269	Boat/Yacht building	97	28
230	Ship repair and conversion	97	29
93	Metal structures	12	30
508	Other	12	31
227	Repair and maintenance service	12	32
11	Metalworking	1	33
67	Construction of metal structures	11	34
263	Houses and buildings	11	35
267	Metal products	11	36
542	Metal works	11	37
75	CNC-machining	542	38
62	Forgings, Fasteners	542	39
69	Gas, Plasma, Laser cutting	542	40
66	MIG, TIG, Aluminum welding	542	41
9	Plastic and Rubber	1	42
54	Packaging	9	43
556	Plastic goods	9	44
559	Plastic processing technology	9	45
55	Blowing	559	46
57	Moulding	559	47
53	Plastics welding and processing	559	48
560	Plastic profiles	9	49
5	Printing	1	50
148	Advertising	5	51
150	Book/Periodicals printing	5	52
145	Labelling and packaging printing	5	53
7	Textile and Clothing	1	54
44	Clothing	7	55
45	Textile	7	56
8	Wood	1	57
337	Other (Wood)	8	58
51	Wooden building materials	8	59
47	Wooden houses	8	60
3	Other	\N	61
37	Creative industries	3	62
29	Energy technology	3	63
33	Environment	3	64
2	Service	\N	65
25	Business services	2	66
35	Engineering	2	67
28	Information Technology and Telecommunications	2	68
581	Data processing, Web portals, E-marketing	28	69
576	Programming, Consultancy	28	70
121	Software, Hardware	28	71
122	Telecommunications	28	72
22	Tourism	2	73
141	Translation services	2	74
21	Transport and Logistics	2	75
111	Air	21	76
114	Rail	21	77
112	Road	21	78
113	Water	21	79
\.


--
-- Data for Name: user_profile; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.user_profile (id, name, agreed_to_terms) FROM stdin;
1	Richard	t
2	test	t
3	dd	t
4	test	t
5	dbuser	t
6	some1	t
\.


--
-- Data for Name: user_profile_sector; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.user_profile_sector (user_profile_id, sector_id) FROM stdin;
1	75
2	39
2	19
2	437
2	42
3	43
3	18
3	98
3	385
3	13
3	39
4	39
4	43
5	75
5	111
5	542
5	28
5	21
5	69
5	141
5	35
5	62
6	1
6	93
6	11
6	19
\.


--
-- Name: user_profile_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_profile_id_seq', 6, true);


--
-- Name: databasechangeloglock databasechangeloglock_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);


--
-- Name: user_profile_sector pk_user_profile_sector; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_profile_sector
    ADD CONSTRAINT pk_user_profile_sector PRIMARY KEY (user_profile_id, sector_id);


--
-- Name: sector sector_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sector
    ADD CONSTRAINT sector_pkey PRIMARY KEY (id);


--
-- Name: user_profile user_profile_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_profile
    ADD CONSTRAINT user_profile_pkey PRIMARY KEY (id);


--
-- Name: sector fk_sector_parent; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sector
    ADD CONSTRAINT fk_sector_parent FOREIGN KEY (parent_id) REFERENCES public.sector(id);


--
-- Name: user_profile_sector fk_user_profile_sector_profile; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_profile_sector
    ADD CONSTRAINT fk_user_profile_sector_profile FOREIGN KEY (user_profile_id) REFERENCES public.user_profile(id) ON DELETE CASCADE;


--
-- Name: user_profile_sector fk_user_profile_sector_sector; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_profile_sector
    ADD CONSTRAINT fk_user_profile_sector_sector FOREIGN KEY (sector_id) REFERENCES public.sector(id);


--
-- PostgreSQL database dump complete
--

\unrestrict p7thb6msfwlFMJihJxaeKV7HJ9pmxN9wczvCxusMYbc3KnSbb7xtMefkpFsCLrq

