-- 1. Add the column as nullable
ALTER TABLE appointments
ADD COLUMN appointment_end_time TIME;

-- 2. Populate end time for existing appointments
UPDATE appointments
SET appointment_end_time = appointment_time + INTERVAL '30 minutes'
WHERE appointment_end_time IS NULL;

-- 3. Now make it NOT NULL
ALTER TABLE appointments
ALTER COLUMN appointment_end_time SET NOT NULL;