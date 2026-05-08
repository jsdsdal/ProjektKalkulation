-- ============================================================
-- AlphaScope Test Data
-- ============================================================

-- Competences (skills, not job titles)
INSERT INTO competence (competence_id, title) VALUES
    (1,  'Project Management'),
    (2,  'C#'),
    (3,  'JavaScript'),
    (4,  'SQL'),
    (5,  'SalesForce'),
    (6,  'Shopify'),
    (7,  'Optimizely'),
    (8,  'UCommerce'),
    (9,  'Sitecore'),
    (10, 'Cloud/DevOps'),
    (11, 'UX/UI Design'),
    (12, 'API Integration'),
    (13, 'E-commerce Platform'),
    (14, 'Test/QA');

-- Statuses
INSERT INTO status (status_id, title) VALUES
    (1, 'Not Started'),
    (2, 'Estimate in Progress'),
    (3, 'Estimate Done'),
    (4, 'Actual Hours Done'),
    (5, 'Rejected');

-- ============================================================
-- Employees (20 total, 5 with Project Management)
-- ============================================================
-- Project Managers (can own projects, higher hourly wage)
INSERT INTO employee (employee_id, name, email, password, hourly_wage) VALUES
    (1,  'Lars Nielsen',      'lars@alphasolutions.com',      'lars1234',      1200.00),
    (2,  'Maria Jensen',      'maria@alphasolutions.com',     'maria1234',     1100.00),
    (3,  'Peter Hansen',      'peter@alphasolutions.com',     'peter1234',     1300.00),
    (4,  'Anne Sørensen',     'anne@alphasolutions.com',      'anne1234',      1150.00),
(5,  'Klaus Andersen',    'klaus@alphasolutions.com',     'klaus1234',     1400.00);

-- Consultants / Specialists
INSERT INTO employee (employee_id, name, email, password, hourly_wage) VALUES
    (6,  'Mikkel Larsen',     'mikkel@alphasolutions.com',    'mikkel1234',     850.00),
    (7,  'Sofie Berg',        'sofie@alphasolutions.com',     'sofie1234',      900.00),
    (8,  'Jonas Holm',        'jonas@alphasolutions.com',     'jonas1234',      800.00),
    (9,  'Emma Kristensen',   'emma@alphasolutions.com',      'emma1234',       950.00),
    (10, 'Frederik Møller',   'frederik@alphasolutions.com',  'frederik1234',   750.00),
    (11, 'Ida Petersen',      'ida@alphasolutions.com',       'ida1234',        850.00),
    (12, 'Oliver Madsen',     'oliver@alphasolutions.com',    'oliver1234',     900.00),
    (13, 'Mathilde Olsen',    'mathilde@alphasolutions.com',  'mathilde1234',   800.00),
    (14, 'Victor Rasmussen',  'victor@alphasolutions.com',    'victor1234',     950.00),
    (15, 'Nanna Thomsen',     'nanna@alphasolutions.com',     'nanna1234',      850.00),
    (16, 'Sebastian Juul',    'sebastian@alphasolutions.com', 'sebastian1234',  750.00),
    (17, 'Astrid Dam',        'astrid@alphasolutions.com',    'astrid1234',     900.00),
    (18, 'Emil Winther',      'emil@alphasolutions.com',      'emil1234',       800.00),
    (19, 'Laura Kjær',        'laura@alphasolutions.com',     'laura1234',      850.00),
    (20, 'Rasmus Lund',       'rasmus@alphasolutions.com',    'rasmus1234',     700.00);

-- ============================================================
-- Employee Competences
-- ============================================================
INSERT INTO employee_competence (employee_id, competence_id) VALUES
    -- PMs: Project Management (1) + technical skills
    (1,  1), (1,  2), (1,  4),          -- Lars: PM, C#, SQL
    (2,  1), (2,  13),(2,  5),          -- Maria: PM, E-commerce, SalesForce
    (3,  1), (3,  10),(3,  12),         -- Peter: PM, Cloud/DevOps, API Integration
    (4,  1), (4,  11),(4,  6),          -- Anne: PM, UX/UI, Shopify
    (5,  1), (5,  9), (5,  7),          -- Klaus: PM, Sitecore, Optimizely

    -- Consultants: 2-4 skills each, overlapping (flat structure)
    (6,  2), (6,  3), (6,  8),          -- Mikkel: C#, JS, UCommerce
    (7,  3), (7,  11),(7,  6),          -- Sofie: JS, UX/UI, Shopify
    (8,  2), (8,  4), (8,  12),         -- Jonas: C#, SQL, API Integration
    (9,  5), (9,  12),(9,  13),         -- Emma: SalesForce, API, E-commerce
    (10, 3), (10, 10),(10, 14),         -- Frederik: JS, Cloud/DevOps, Test/QA
    (11, 2), (11, 9), (11, 4),          -- Ida: C#, Sitecore, SQL
    (12, 6), (12, 13),(12, 3),          -- Oliver: Shopify, E-commerce, JS
    (13, 11),(13, 3),                    -- Mathilde: UX/UI, JS
    (14, 10),(14, 4), (14, 12),         -- Victor: Cloud/DevOps, SQL, API
    (15, 2), (15, 7), (15, 14),         -- Nanna: C#, Optimizely, Test/QA
    (16, 4), (16, 14),                   -- Sebastian: SQL, Test/QA
    (17, 5), (17, 13),                   -- Astrid: SalesForce, E-commerce
    (18, 3), (18, 6), (18, 11),         -- Emil: JS, Shopify, UX/UI
    (19, 2), (19, 12),(19, 9),          -- Laura: C#, API, Sitecore
    (20, 10),(20, 14);                   -- Rasmus: Cloud/DevOps, Test/QA

-- ============================================================
-- Projects (15 total)
-- ============================================================

-- SIMPLE PROJECTS (3) — no subprojects, flat tasks only
INSERT INTO project (project_id, title, description, start_date, end_date, budget, employee_id, parent_project_id, status_id) VALUES
    (1,  'Sanistål Webshop Redesign',
    'Komplet redesign af Sanistål eksisterende B2B webshop med fokus på selvbetjening og brugeroplevelse.',
    '2025-09-01', '2026-01-15', 450000.00, 1, NULL, 4),

    (2,  'Novo Nordisk PIM Opsætning',
    'Implementering af PIM-system til håndtering af produktdata på tværs af Novo Nordisks digitale kanaler.',
    '2026-07-01', '2026-12-01', 680000.00, 2, NULL, 1),

    (3,  'CPH Airport Landing Page',
    'Kampagne-landingsside til CPH Airports sommerpromotion med integration til booking-system.',
    '2026-06-01', '2026-08-15', 120000.00, 3, NULL, 3);

-- COMPLEX PROJECT A: Husqvarna (parent + 2 subprojects)
INSERT INTO project (project_id, title, description, start_date, end_date, budget, employee_id, parent_project_id, status_id) VALUES
    (4,  'Husqvarna E-commerce Platform',
    'Fuld e-commerce platform til Husqvarnas nordiske B2B-kunder inkl. produktkatalog, bestilling og selvbetjening.',
    '2026-03-01', '2026-09-30', 1200000.00, 1, NULL, 2),

    (5,  'Husqvarna Frontend',
    'Komponentbibliotek og brugergrænseflader til Husqvarna-platformen.',
    '2026-04-01', '2026-07-31', NULL, 1, 4, 3),

    (6,  'Husqvarna Backend & API',
    'REST API-lag og backend-services til produktdata, ordrer og kundekonti.',
    '2026-03-15', '2026-08-31', NULL, 1, 4, 2);

-- COMPLEX PROJECT B: DLG (parent + 2 subprojects) — behind schedule
INSERT INTO project (project_id, title, description, start_date, end_date, budget, employee_id, parent_project_id, status_id) VALUES
    (7,  'DLG B2B Portal',
    'Selvbetjeningsportal til DLGs landbrugskunder med produktkatalog og ordrestyring.',
    '2026-01-15', '2026-06-30', 850000.00, 2, NULL, 2),

    (8,  'DLG Produktkatalog',
    'Søgbar produktkatalog med filtrering, kategorisering og PIM-integration.',
    '2026-02-01', '2026-05-15', NULL, 2, 7, 3),

    (9,  'DLG Ordrestyring',
    'Ordreflow inkl. godkendelsesproces, leveringsstatus og fakturaintegration.',
    '2026-03-01', '2026-06-30', NULL, 2, 7, 2);

-- COMPLEX PROJECT C: Georg Jensen (parent + 2 subprojects)
INSERT INTO project (project_id, title, description, start_date, end_date, budget, employee_id, parent_project_id, status_id) VALUES
    (10, 'Georg Jensen Omnichannel',
    'Omnichannel-strategi for Georg Jensen med Shopify som kerne og integration til PIM og Marketing Automation.',
    '2026-04-01', '2026-10-31', 950000.00, 4, NULL, 2),

    (11, 'Georg Jensen Shopify Migration',
    'Migration fra legacy-platform til Shopify Plus inkl. tema-udvikling og datamigrering.',
    '2026-04-15', '2026-08-31', NULL, 4, 10, 2),

    (12, 'Georg Jensen PIM Integration',
    'Integration mellem PIM-system og Shopify til automatisk produktsynkronisering.',
    '2026-07-01', '2026-10-31', NULL, 4, 10, 1);

-- MIXED PROJECTS (3)
INSERT INTO project (project_id, title, description, start_date, end_date, budget, employee_id, parent_project_id, status_id) VALUES
    (13, 'Fritz Hansen Website Relaunch',
    'Nyt website på Sitecore med fokus på brand storytelling og produktpræsentation.',
    '2026-02-01', '2026-05-31', 520000.00, 5, NULL, 3),

    (14, 'Neye CRM Integration',
    'Integration af Salesforce CRM med Neyes eksisterende e-commerce platform til personaliseret kundekommunikation.',
    '2026-03-01', '2026-07-31', 380000.00, 5, NULL, 2),

    (15, 'Drømmeland Webshop',
    'Ny Shopify-baseret webshop til Drømmeland med fokus på mobil brugeroplevelse.',
    '2026-01-01', '2026-03-31', 200000.00, 3, NULL, 5);

-- ============================================================
-- Tasks (43 total)
-- ============================================================

-- ---- PROJECT 1: Sanistål (simple, done — all have actual_hours) ----
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (1,  'UX Research & Mockups',    'Brugerinterviews og wireframes til ny B2B-shop',              '2025-09-01', '2025-09-30', 24.00, 28.00, 1, NULL),
    (2,  'Frontend Implementering',  'Implementering af godkendte designs i HTML/CSS/JS',           '2025-10-01', '2025-11-30', 40.00, 45.00, 1, NULL),
    (3,  'Backend API',              'REST API til produktdata og ordrehåndtering',                  '2025-10-15', '2025-12-15', 32.00, 30.00, 1, NULL),
    (4,  'QA & Go-live',             'Testforløb og produktionsudrulning',                          '2025-12-16', '2026-01-15', 16.00, 20.00, 1, NULL);

-- ---- PROJECT 2: Novo Nordisk (not started — no actual_hours) ----
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (5,  'Kravindsamling',           'Workshops med Novo Nordisks produktteam',                     '2026-07-01', '2026-07-31', 16.00, NULL, 2, NULL),
    (6,  'PIM Konfiguration',        'Opsætning af produkthierarki og attributter i PIM',           '2026-08-01', '2026-10-31', 40.00, NULL, 2, NULL),
    (7,  'Kanalintegration',         'Synkronisering til web, app og print',                        '2026-11-01', '2026-11-30', 24.00, NULL, 2, NULL);

-- ---- PROJECT 3: CPH Airport (estimate done) ----
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (8,  'UX Research',              'Analyse af brugerflow og kampagnebehov',                      '2026-06-01', '2026-06-15', 12.00, NULL, 3, NULL),
    (9,  'Landing Page Design',      'Visuelt design af kampagneside',                              '2026-06-16', '2026-07-07', 20.00, NULL, 3, NULL),
    (10, 'Udvikling & Integration',  'Frontend-build og booking-API integration',                   '2026-07-08', '2026-08-01', 32.00, NULL, 3, NULL),
    (11, 'Test & Lancering',         'Cross-browser test og go-live',                               '2026-08-01', '2026-08-15',  8.00, NULL, 3, NULL);

-- ---- PROJECT 5: Husqvarna Frontend (subproject of 4) ----
-- Parent task 12: no hours — aggregeres fra subtasks
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (12, 'Komponentbibliotek',       'Design og udvikling af genbrugelige UI-komponenter',          '2026-04-01', '2026-06-30', NULL,  NULL, 5, NULL);

-- Subtasks of task 12
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (13, 'Header & Navigation',      'Responsiv header med søgning og mega-menu',                   '2026-04-01', '2026-04-30', 16.00, NULL, 5, 12),
    (14, 'Produktkort',              'Produktkort-komponent med varianter og priser',                '2026-04-15', '2026-05-15', 12.00, NULL, 5, 12),
    (15, 'Checkout Flow',            'Multi-step checkout med validering',                          '2026-05-01', '2026-06-30', 24.00, NULL, 5, 12);

-- Standalone task on project 5
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (16, 'Responsiv Test',           'Test på tværs af enheder og browsere',                        '2026-07-01', '2026-07-31',  8.00, NULL, 5, NULL);

-- ---- PROJECT 6: Husqvarna Backend (subproject of 4) ----
-- Parent task 17: no hours
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (17, 'API Design & Arkitektur',  'REST API design inkl. autentificering og rate limiting',      '2026-03-15', '2026-06-30', NULL,  NULL, 6, NULL);

-- Subtasks of task 17
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (18, 'Produkt-API',              'CRUD endpoints til produktdata med filtrering og paginering',  '2026-04-01', '2026-05-31', 20.00, NULL, 6, 17),
    (19, 'Ordre-API',                'Ordreprocessering, status-tracking og historik',               '2026-05-01', '2026-06-30', 24.00, NULL, 6, 17);

-- Standalone task on project 6
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (20, 'Database Design',          'Skemadesign, indeksering og migrationsscripts',               '2026-03-15', '2026-04-30', 16.00, NULL, 6, NULL);

-- ---- PROJECT 8: DLG Produktkatalog (subproject of 7) ----
-- Parent task 21: no hours, subtasks partially done
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (21, 'Datamigrering',            'Migration af produktdata fra legacy-system',                   '2026-02-01', '2026-03-31', NULL,  NULL,  8, NULL);

-- Subtasks of task 21 (have actual_hours — work done)
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (22, 'Datamapping',              'Mapping mellem legacy-felter og nyt PIM-skema',                '2026-02-01', '2026-02-28', 12.00, 14.00, 8, 21),
    (23, 'Importscripts',            'Automatiserede importscripts med validering og rollback',      '2026-03-01', '2026-03-31', 20.00, 18.00, 8, 21);

-- Standalone task on project 8
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (24, 'Søgning & Filtrering',     'Facetsøgning med Elasticsearch integration',                  '2026-04-01', '2026-05-15', 24.00, NULL,  8, NULL);

-- ---- PROJECT 9: DLG Ordrestyring (subproject of 7) ----
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (25, 'Ordreflow Design',         'Brugerstyret ordreflow med godkendelseslag',                  '2026-03-01', '2026-04-15', 16.00, NULL, 9, NULL);

-- Parent task 26: no hours
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (26, 'Betalingsintegration',     'Integration til betalings- og faktureringssystemer',          '2026-04-01', '2026-06-30', NULL,  NULL, 9, NULL);

-- Subtasks of task 26
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (27, 'Stripe Opsætning',         'Stripe payment gateway integration med webhook-håndtering',   '2026-04-01', '2026-05-15', 12.00, NULL, 9, 26),
    (28, 'Fakturamodul',             'Automatisk fakturagenerering og -forsendelse',                '2026-05-01', '2026-06-30', 20.00, NULL, 9, 26);

-- ---- PROJECT 11: Georg Jensen Shopify (subproject of 10) ----
-- Parent task 29: no hours
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (29, 'Tema-udvikling',           'Shopify Plus tema med Georg Jensen brand guidelines',         '2026-04-15', '2026-07-31', NULL,  NULL, 11, NULL);

-- Subtasks of task 29
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (30, 'Forside',                  'Hero-sektion, featured collections og storytelling-moduler',   '2026-04-15', '2026-05-31', 16.00, NULL, 11, 29),
    (31, 'Produktsider',             'PDP med varianter, zoom og cross-sell',                       '2026-05-15', '2026-06-30', 20.00, NULL, 11, 29),
    (32, 'Kollektionssider',         'Filtrerbar kollektion med quick-view',                        '2026-06-01', '2026-07-15', 12.00, NULL, 11, 29);

-- Standalone task on project 11
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (33, 'Datamigrering',            'Migration af produkter, kunder og ordrehistorik fra legacy',   '2026-07-01', '2026-08-31', 24.00, NULL, 11, NULL);

-- ---- PROJECT 13: Fritz Hansen (mixed — subtasks, some actual_hours) ----
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (34, 'Visuelt Design',           'Art direction og designsystem til nyt Fritz Hansen site',     '2026-02-01', '2026-03-15', 32.00, 35.00, 13, NULL);

-- Parent task 35: no hours
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (35, 'Sitecore Opsætning',       'Sitecore XM Cloud konfiguration og indholdsarkitektur',      '2026-03-01', '2026-04-30', NULL,  NULL, 13, NULL);

-- Subtasks of task 35 (done)
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (36, 'Indholdstyper',            'Definition af templates, rendering og datasources',            '2026-03-01', '2026-03-31', 16.00, 18.00, 13, 35),
    (37, 'Workflow Konfiguration',   'Redaktionel workflow med godkendelse og publicering',          '2026-04-01', '2026-04-30',  8.00,  7.00, 13, 35);

-- Standalone task on project 13
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (38, 'Frontend Build',           'Next.js frontend med Sitecore headless SDK',                  '2026-04-01', '2026-05-31', 40.00, NULL, 13, NULL);

-- ---- PROJECT 14: Neye CRM (mixed, in progress) ----
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (39, 'Salesforce Konfiguration', 'Tilpasning af Salesforce til Neyes kundesegmenter',           '2026-03-01', '2026-04-30', 24.00, NULL, 14, NULL);

-- Parent task 40: no hours
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (40, 'Datasynkronisering',       'Realtidssynk mellem CRM og e-commerce platform',             '2026-05-01', '2026-07-31', NULL,  NULL, 14, NULL);

-- Subtasks of task 40
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (41, 'Kundeimport',              'Bulk-import af eksisterende kundedata med dedup-logik',        '2026-05-01', '2026-06-15', 12.00, NULL, 14, 40),
    (42, 'Ordrehistorik Sync',       'Synkronisering af ordrehistorik til 360-graders kundeoverblik','2026-06-01', '2026-07-31', 16.00, NULL, 14, 40);

-- ---- PROJECT 15: Drømmeland (rejected — minimal task) ----
INSERT INTO task (task_id, title, description, start_date, end_date, estimated_hours, actual_hours, project_id, parent_task_id) VALUES
    (43, 'Indledende Vurdering',     'Feasibility-analyse og scope-estimat',                        '2026-01-01', '2026-01-15',  8.00,  8.00, 15, NULL);

-- ============================================================
-- Task Competences (which skills a task requires)
-- ============================================================
INSERT INTO task_competence (task_id, competence_id) VALUES
     -- Sanistål (project 1)
     (1,  11),                    -- UX Research: UX/UI
     (2,  3),  (2,  11),         -- Frontend: JS, UX/UI
     (3,  2),  (3,  12),         -- Backend: C#, API Integration
     (4,  14),                    -- QA: Test/QA

     -- Novo Nordisk (project 2)
     (5,  1),                     -- Kravindsamling: Project Management
     (6,  13),                    -- PIM Config: E-commerce Platform
     (7,  12), (7,  13),         -- Kanalintegration: API, E-commerce

     -- CPH Airport (project 3)
     (8,  11),                    -- UX Research: UX/UI
     (9,  11), (9,  3),          -- Design: UX/UI, JS
     (10, 3),  (10, 12),         -- Udvikling: JS, API
     (11, 14),                    -- Test: Test/QA

     -- Husqvarna Frontend (project 5)
     (13, 3),  (13, 11),         -- Header: JS, UX/UI
     (14, 3),                     -- Produktkort: JS
     (15, 3),  (15, 11),         -- Checkout: JS, UX/UI
     (16, 14),                    -- Responsiv Test: Test/QA

     -- Husqvarna Backend (project 6)
     (18, 2),  (18, 12),         -- Produkt-API: C#, API
     (19, 2),  (19, 12),         -- Ordre-API: C#, API
     (20, 4),                     -- DB Design: SQL

     -- DLG Katalog (project 8)
     (22, 4),                     -- Datamapping: SQL
     (23, 4),  (23, 2),          -- Import: SQL, C#
     (24, 3),  (24, 4),          -- Søgning: JS, SQL

     -- DLG Ordrestyring (project 9)
     (25, 11), (25, 2),          -- Ordreflow: UX/UI, C#
     (27, 12),                    -- Stripe: API Integration
     (28, 2),  (28, 12),         -- Faktura: C#, API

     -- Georg Jensen Shopify (project 11)
     (30, 6),  (30, 11),         -- Forside: Shopify, UX/UI
     (31, 6),  (31, 3),          -- Produktsider: Shopify, JS
     (32, 6),                     -- Kollektioner: Shopify
     (33, 4),  (33, 13),         -- Datamigrering: SQL, E-commerce

     -- Fritz Hansen (project 13)
     (34, 11),                    -- Visuelt Design: UX/UI
     (36, 9),                     -- Indholdstyper: Sitecore
     (37, 9),                     -- Workflow: Sitecore
     (38, 3),  (38, 9),          -- Frontend: JS, Sitecore

     -- Neye CRM (project 14)
     (39, 5),                     -- SF Config: SalesForce
     (41, 5),  (41, 4),          -- Kundeimport: SalesForce, SQL
     (42, 12), (42, 5);          -- Ordrehistorik: API, SalesForce

-- ============================================================
-- Task Employees (who is assigned to which task)
-- ============================================================
INSERT INTO task_employee (task_id, employee_id) VALUES
 -- Sanistål (project 1, done)
     (1,  13),                    -- UX Research: Mathilde (UX/UI)
     (2,  7),  (2,  6),          -- Frontend: Sofie, Mikkel
     (3,  8),  (3,  14),         -- Backend: Jonas, Victor
     (4,  10),                    -- QA: Frederik

     -- Novo Nordisk (project 2, not started — only PM assigned)
     (5,  2),                     -- Kravindsamling: Maria (PM)

     -- CPH Airport (project 3, estimate done)
     (8,  13),                    -- UX Research: Mathilde
     (9,  18), (9,  13),         -- Design: Emil, Mathilde
     (10, 12), (10, 14),         -- Udvikling: Oliver, Victor
     (11, 16),                    -- Test: Sebastian

     -- Husqvarna Frontend (project 5)
     (13, 7),  (13, 18),         -- Header: Sofie, Emil
     (14, 12),                    -- Produktkort: Oliver
     (15, 7),  (15, 13),         -- Checkout: Sofie, Mathilde
     (16, 10),                    -- Responsiv Test: Frederik

     -- Husqvarna Backend (project 6)
     (18, 8),  (18, 19),         -- Produkt-API: Jonas, Laura
     (19, 6),  (19, 8),          -- Ordre-API: Mikkel, Jonas
     (20, 16),                    -- DB Design: Sebastian

     -- DLG Katalog (project 8)
     (22, 16),                    -- Datamapping: Sebastian
     (23, 8),  (23, 16),         -- Import: Jonas, Sebastian
     (24, 12), (24, 14),         -- Søgning: Oliver, Victor

     -- DLG Ordrestyring (project 9)
     (25, 13), (25, 6),          -- Ordreflow: Mathilde, Mikkel
     (27, 9),                     -- Stripe: Emma
     (28, 6),  (28, 9),          -- Faktura: Mikkel, Emma

     -- Georg Jensen Shopify (project 11)
     (30, 18), (30, 7),          -- Forside: Emil, Sofie
     (31, 12), (31, 18),         -- Produktsider: Oliver, Emil
     (32, 12),                    -- Kollektioner: Oliver
     (33, 16), (33, 17),         -- Datamigrering: Sebastian, Astrid

     -- Fritz Hansen (project 13)
     (34, 13),                    -- Design: Mathilde
     (36, 11),                    -- Indholdstyper: Ida
     (37, 11),                    -- Workflow: Ida
     (38, 6),  (38, 19),         -- Frontend: Mikkel, Laura

     -- Neye CRM (project 14)
     (39, 9),  (39, 17),         -- SF Config: Emma, Astrid
     (41, 17), (41, 16),         -- Kundeimport: Astrid, Sebastian
     (42, 9),  (42, 14),         -- Ordrehistorik: Emma, Victor

     -- Drømmeland (project 15, rejected)
     (43, 3);                     -- Vurdering: Peter (PM)