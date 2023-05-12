INSERT INTO maryttslocale (name, name_for_site)
VALUES ('ru', 'Русский'),
       ('en', 'Английский');

INSERT INTO maryttsvoice (name, name_for_site, locale_id)
VALUES ('dfki-poppy-hsmm', 'Poppy', 'en_US'),
       ('cmu-slt-hsmm', 'Olivia', 'en_US'),
       ('ac-irina-hsmm', 'Ирина', 'ru'),
       ('dfki-spike-hsmm', 'Spike', 'en_US'),
       ('dfki-prudence-hsmm', 'Prudence', 'en_US');

INSERT INTO hmm_model (name, name_for_site)
VALUES ('RUSSIAN', 'Русский (Фонетический алфавит)'),
       ('ENGLISH', 'Английский (IPA)');