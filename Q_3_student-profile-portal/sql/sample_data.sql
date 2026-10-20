-- =====================================================================
--  Student Profile Portal - sample data
--  Run SECOND.  mysql -u root -p < sql/sample_data.sql
--  Uses INSERT IGNORE, so re-running will not create duplicates.
--
--  Sample logins (CHANGE THESE before any real use):
--      admin   / Admin@123
--      teacher / Teacher@123
-- =====================================================================
USE student_portal;

INSERT IGNORE INTO users (username, password_hash, full_name) VALUES
  ('admin',   'pbkdf2$65536$LYzUVa8SALa9q9S+eXgyEg==$8mehY/Q9ClKvgzAyaelN/C72+slrUBM9JxgO1WmqO4w=', 'Portal Administrator'),
  ('teacher', 'pbkdf2$65536$VR50ukicBxVpKG+jMh1fVw==$fJjDcAp7SJwV+j5ULgRE3Mm2UZzXW+aRaERe/0B630M=', 'Tina Teacher');

INSERT IGNORE INTO students (name, email, department, image_path, document_path) VALUES
  ('Alice Johnson', 'alice.johnson@example.edu', 'Computer Science',      'alice_johnson.jpg', 'alice_johnson_record.pdf'),
  ('Bob Smith',     'bob.smith@example.edu',     'Mechanical Engineering','bob_smith.jpg',     'bob_smith_record.pdf'),
  ('Carla Gomez',   'carla.gomez@example.edu',   'Electronics',           'carla_gomez.jpg',   'carla_gomez_record.pdf'),
  ('Deepak Kumar',  'deepak.kumar@example.edu',  'Computer Science',      'deepak_kumar.jpg',  'deepak_kumar_record.pdf'),
  ('Emily Chen',    'emily.chen@example.edu',    'Civil Engineering',     'emily_chen.jpg',    'emily_chen_record.pdf'),
  -- Demo: the files below do NOT exist, to show graceful handling of missing files.
  ('Frank Miller',  'frank.miller@example.edu',  'Mathematics',           'frank_missing.jpg', 'frank_missing_record.pdf'),
  -- Demo: a student with no image and no document at all (NULL values).
  ('Grace Lee',     'grace.lee@example.edu',     'Physics',               NULL,                NULL),
  -- Demo: a plain-text document that exists.
  ('Henry Adams',   'henry.adams@example.edu',   'Mathematics',           NULL,                'henry_adams_notes.txt');
