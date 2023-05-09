INSERT INTO maryttslocale (name, name_for_site)
VALUES ('ru', 'Русский'),
       ('en', 'Английский');

INSERT INTO maryttsvoice (name, name_for_site, locale_id)
VALUES ('dfki-poppy-hsmm', 'Poppy', 'en'),
       ('cmu-slt-hsmm', 'Standart', 'en');

INSERT INTO hmm_model (name, name_for_site)
VALUES ('RUSSIAN', 'Русский (Фонетический алфавит)'),
       ('ENGLISH', 'Английский (IPA)');